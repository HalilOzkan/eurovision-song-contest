package com.pega.exercise.eurovision_song_contest;

import com.pega.exercise.eurovision_song_contest.common.Constants;
import com.pega.exercise.eurovision_song_contest.persistence.JdbcTransactionPersistence;
import com.pega.exercise.eurovision_song_contest.persistence.TransactionPersistence;
import com.pega.exercise.eurovision_song_contest.services.TransactionsManagerService;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static java.lang.System.getenv;

/**
 * This is the single and only verticle<br><br>
 * Verticles are chunks of code that get deployed and run by Vert.x.
 * A Vert.x instance maintains N event loop threads (where N by default is core*2) by default.
 * Verticles can be written in any of the languages that Vert.x supports and
 * a single application can include verticles written in multiple languages.<br><br>
 * This verticle starts transaction manager service using JDBC transaction persistence and then
 * binds this transaction manager service to event bus at a specific address.
 * Next, it constructs the router factory using openapi.yaml and mounts handler to validate message.<br><br>
 * Whenever a Json message receives via http server, the Web API Contract Router Factory validates it and
 * the mounted service on eventbus processes the message and transaction
 */
public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  HttpServer server;
  ServiceBinder serviceBinder;

  MessageConsumer<JsonObject> consumer;

  /**
   * Start Transaction service
   * <br>This service is responsible to manage voting transactions
   * <br>It should be started before http server
   */
  private CompletableFuture<String> startTransactionService() {
    CompletableFuture<String> future = new CompletableFuture<>();
    serviceBinder = new ServiceBinder(vertx.getDelegate());

    TransactionPersistence persistence = JdbcTransactionPersistence.create(vertx);

    // Create an instance of TransactionManagerService and mount to event bus
    TransactionsManagerService transactionsManagerService = TransactionsManagerService.create(persistence);
    consumer = serviceBinder
      .setAddress(Constants.EVENTBUSADDR_TRX)
      .register(TransactionsManagerService.class, transactionsManagerService);
    future.complete(consumer.address());
    return future;
  }

  /**
   * This method constructs the router factory using openapi.yaml
   * <br>then introspect the OpenAPI spec to mount handlers for all operations that specifies a x-vertx-event-bus annotation
   * <br>then mount services on eventbus for introspected operations
   * <br>starts the http server with built router
   *
   * @return Future
   */
  private CompletableFuture<String> startHttpServer() {
    LOGGER.info("HTTP server starting");
    CompletableFuture<String> future = new CompletableFuture<>();
    OpenAPI3RouterFactory.create(this.vertx.getDelegate(), "/openapi.yaml", openAPI3RouterFactoryAsyncResult -> {
      if (openAPI3RouterFactoryAsyncResult.succeeded()) {
        OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();

        // Mount services on event bus based on extensions
        routerFactory.mountServicesFromExtensions();

        // Generate the router
        Router router = routerFactory.getRouter();
        server = vertx.getDelegate()
          .createHttpServer(new HttpServerOptions()
            .setPort(Integer.parseInt(getenv(Constants.HTTPSERVERPORT)))
            .setHost(Constants.HTTPSERVER));
        server.requestHandler(router).listen(ar -> {
          if (ar.succeeded()) future.complete(router.getRoutes().toString());
          else future.completeExceptionally(ar.cause());
        });
      } else {
        // Something went wrong during router factory initialization
        future.completeExceptionally(openAPI3RouterFactoryAsyncResult.cause());
      }
    });
    return future;
  }

  private CompletableFuture<Long> startMetricCollector() {
    LOGGER.info("HTTP server starting");

    CompletableFuture<Long> future = new CompletableFuture<>();

    long timerID = vertx.setPeriodic(Constants.METRIC_REPORT_PERIOD, id -> {
      Vertx.vertx().eventBus().send(Constants.EVENTBUSADDR_METRICS, "metrics message");
    });

    future.complete(timerID);
    return future;
  }

  /**
   * When this verticle receive start signal with a promise
   * First starts transaction service and then http server
   *
   * @param startPromise completed if verticle starts successfully, otherwise returns failed
   */
  @Override
  public void start(Promise<Void> startPromise) {
    startTransactionService()
      .thenCompose(s -> startHttpServer())
      .whenComplete((s, throwable) -> {
        if (throwable == null) {
          LOGGER.info("Application started successfully");
          vertx.deployVerticle(new MetricsReporterVerticle());
          startPromise.complete();
        } else {
          LOGGER.error("Application can't start \n{}", throwable.getLocalizedMessage());
          startPromise.fail(throwable);
        }
      });
  }

  @Override
  public void stop() {
    this.server.close();
    consumer.unregister();
  }


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
    vertx.deployVerticle(new MetricsReporterVerticle());
  }

}
