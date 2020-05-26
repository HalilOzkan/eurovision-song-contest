package com.pega.exercise.eurovision_song_contest

import com.pega.exercise.eurovision_song_contest.common.Constants
import com.pega.exercise.eurovision_song_contest.persistence.JdbcTransactionPersistence
import com.pega.exercise.eurovision_song_contest.persistence.TransactionPersistence
import io.reactivex.functions.Consumer
import io.vertx.core.Promise
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler
import io.vertx.reactivex.sqlclient.Row
import org.slf4j.LoggerFactory


class MetricsReporterVerticle : AbstractVerticle() {

  private val logger = LoggerFactory.getLogger("MetricsReporterVerticle")
  var timerID = 0L

  // Called when verticle is deployed
  override fun start(startPromise: Promise<Void>) {

    val persistence: TransactionPersistence = JdbcTransactionPersistence.create(vertx)

    timerID = vertx.setPeriodic(Constants.METRIC_REPORT_PERIOD) { _ ->
      persistence.metrics
        .doOnError(Consumer { logger.error(it.localizedMessage) })
        .subscribe(Consumer {
          val response: JsonArray = JsonArray()
          it.forEach { row: Row? ->
            response
              .add(JsonObject().put("year", row?.getValue(0))
                .put("country", row?.getValue(1))
                .put("count", row?.getValue(2)))
          }
          vertx.eventBus().send(Constants.EVENTBUSADDR_METRICS, response)
        })
    }

    val router = Router.router(vertx)

    val sockJSHandler: SockJSHandler = SockJSHandler.create(vertx)
    val options = SockJSBridgeOptions()
      .addOutboundPermitted(PermittedOptions().setAddress(Constants.EVENTBUSADDR_METRICS));

    // mount the bridge on the router
    router.mountSubRouter("/eventbus", sockJSHandler.bridge(options))
    // The web server webroot static content handler
    router.delegate.route().handler(StaticHandler.create().setCachingEnabled(false));

    vertx.createHttpServer(HttpServerOptions()
      .setHost(Constants.HTTPSERVER)
      .setPort(System.getenv(Constants.METRICSSERVERPORT).toInt()))
      .requestHandler(router)
      .listen { event ->
        if (event.succeeded()) startPromise.complete()
        else
          startPromise.fail(event.cause())
      }
  }

  // Optional - called when verticle is undeployed
  override fun stop(stopPromise: Promise<Void>) {
    vertx.cancelTimer(timerID)
  }
}
