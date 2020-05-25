package com.pega.exercise.eurovision_song_contest.services;

import com.pega.exercise.eurovision_song_contest.models.Vote;
import com.pega.exercise.eurovision_song_contest.persistence.TransactionPersistence;
import com.pega.exercise.eurovision_song_contest.services.impl.TransactionsManagerServiceImpl;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;

/**
 * This interface describes the Transactions Manager Service.<br>
 * <br>
 * Note that all methods has same name of corresponding operation id<br>
 * <br>
 * Vert.x Web API Service helps you routing the API requests incoming from
 * a Vert.x Web Router built with Vert.x Web API Contract Router Factory
 * to event bus. Event Bus provides itself important features like load balancing
 * and distribution of requests across different Vert.x instances.<br>
 * <br>
 * Proxy class will be generated with build using Vert.x codegen.
 */
@WebApiServiceGen
public interface TransactionsManagerService {

  static TransactionsManagerService create(TransactionPersistence persistence) {
    return new TransactionsManagerServiceImpl(persistence);
  }

  void getTopThreeListByYear(
    Integer year,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

  void getTopThreeListByCountry(
    Integer year,
    String country,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

  void addVote(
    Integer year,
    Vote body,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}
