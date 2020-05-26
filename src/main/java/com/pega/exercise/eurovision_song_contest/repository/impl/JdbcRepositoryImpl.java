package com.pega.exercise.eurovision_song_contest.repository.impl;

import com.pega.exercise.eurovision_song_contest.persistence.impl.JdbcTransactionPersistenceImpl;
import com.pega.exercise.eurovision_song_contest.repository.JdbcRepository;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.*;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link JdbcTransactionPersistenceImpl} extends this JDBC Repository implementation
 */
public class JdbcRepositoryImpl implements JdbcRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(JdbcRepositoryImpl.class);

  protected final Pool client;

  public JdbcRepositoryImpl(Vertx vertx) {

    // set Postgres connection pool options
    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);

    this.client = PgPool.pool(vertx, poolOptions);

    LOGGER.info("Database connection pool created");
  }

  public Single<RowSet<Row>> query(String sql) {
    return client.preparedQuery(sql).rxExecute();
  }

  public Single<RowSet<Row>> queryWithParams(Tuple sqlParams, String sql) {
    return client.preparedQuery(sql).rxExecute(sqlParams);
  }

  protected Observable<Row> streamQueryWithParams(Tuple sqlParams, String sql) {
    return client.rxBegin() // Cursors require a transaction
      .flatMapObservable(tx -> tx
        .rxPrepare(sql)
        .flatMapObservable(preparedQuery -> {
          // Fetch 32 rows at a time
          RowStream<Row> stream = preparedQuery.createStream(32, sqlParams);
          return stream.toObservable();
        })
        .doAfterTerminate(tx::commit)); // Commit the transaction after usage
  }

  public Completable execute(Tuple sqlParams, String sql) {
    return client.rxBegin()
      .flatMapCompletable(tx -> tx
        .preparedQuery(sql)
        .rxExecute(sqlParams)
        .flatMapCompletable(result -> tx.rxCommit()));
  }

}
