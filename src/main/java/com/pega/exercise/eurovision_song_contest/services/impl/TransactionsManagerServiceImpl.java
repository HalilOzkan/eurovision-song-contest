package com.pega.exercise.eurovision_song_contest.services.impl;

import com.pega.exercise.eurovision_song_contest.models.Vote;
import com.pega.exercise.eurovision_song_contest.persistence.TransactionPersistence;
import com.pega.exercise.eurovision_song_contest.services.TransactionsManagerService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.reactivex.sqlclient.Row;

import java.util.Iterator;

public class TransactionsManagerServiceImpl implements TransactionsManagerService {

  public static final String ERROR = "error";
  private final TransactionPersistence persistence;

  public TransactionsManagerServiceImpl(TransactionPersistence persistence) {
    this.persistence = persistence;
  }

  @Override
  public void getTopThreeListByYear(Integer year, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
    persistence.getTopThreeListByYear(year)
      .doOnError(throwable -> resultHandler.handle(
        Future.succeededFuture(OperationResponse.completedWithJson(new JsonObject().put(ERROR, throwable.getLocalizedMessage())))
      ))
      .subscribe(rows -> {
        resultHandler.handle(
          Future.succeededFuture(OperationResponse.completedWithJson(generateResponse(rows.iterator())))
        );
      });
  }

  @Override
  public void getTopThreeListByCountry(Integer year, String country, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
    persistence.getTopThreeListByCountry(year, country)
      .doOnError(throwable -> resultHandler.handle(
        Future.succeededFuture(OperationResponse.completedWithJson(new JsonObject().put(ERROR, throwable.getLocalizedMessage())))
      ))
      .subscribe(rows -> {
        resultHandler.handle(
          Future.succeededFuture(OperationResponse.completedWithJson(generateResponse(rows.iterator())))
        );
      });
  }

  @Override
  public void addVote(Integer year, Vote body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
    persistence.addVote(year, body)
      .doOnError(throwable -> resultHandler.handle(
        Future.succeededFuture(OperationResponse.completedWithJson(new JsonObject().put(ERROR, throwable.getLocalizedMessage())))
      ))
      .subscribe(() -> resultHandler.handle(Future.succeededFuture(
        OperationResponse.completedWithJson(
          JsonObject.mapFrom(body)
        )
      )));
  }

  private JsonObject generateResponse(Iterator<Row> iterator) {
    JsonObject response = new JsonObject();
    if (iterator.hasNext())
      response.put("first", iterator.next().getValue(0));
    if (iterator.hasNext())
      response.put("second", iterator.next().getValue(0));
    if (iterator.hasNext())
      response.put("third", iterator.next().getValue(0));
    return response;
  }
}
