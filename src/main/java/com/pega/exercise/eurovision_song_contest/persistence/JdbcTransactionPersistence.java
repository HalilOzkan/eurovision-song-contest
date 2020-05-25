package com.pega.exercise.eurovision_song_contest.persistence;

import com.pega.exercise.eurovision_song_contest.persistence.impl.JdbcTransactionPersistenceImpl;
import io.vertx.reactivex.core.Vertx;

public interface JdbcTransactionPersistence extends TransactionPersistence {
  /**
   * Factory method to instantiate JDBC TransactionPersistence
   *
   * @return TransactionPersistence instance
   */
  static JdbcTransactionPersistence create(Vertx vertx) {
    return new JdbcTransactionPersistenceImpl(vertx);
  }

}
