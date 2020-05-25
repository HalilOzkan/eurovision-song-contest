package com.pega.exercise.eurovision_song_contest.repository;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.Tuple;

/**
 * This interface describes the JDBC Repository.
 * <br>INSERT, SELECT generic RxJava2 APIs developed with Vert.x Reactive PostgreSQL Client
 */
public interface JdbcRepository {

  Single<RowSet<Row>> queryWithParams(Tuple sqlParams, String sql);

  Completable execute(Tuple sqlParams, String sql);

}
