package com.pega.exercise.eurovision_song_contest.persistence;

import com.pega.exercise.eurovision_song_contest.persistence.impl.JdbcTransactionPersistenceImpl;
import io.vertx.reactivex.core.Vertx;

/**
 * This interface provides persisting services
 * through {@link com.pega.exercise.eurovision_song_contest.repository.impl.JdbcRepositoryImpl}
 */
public interface JdbcTransactionPersistence extends TransactionPersistence {
  /**
   * Factory method to instantiate JDBC TransactionPersistence
   * @param vertx Vert.x context
   * @return TransactionPersistence instance
   */
  static JdbcTransactionPersistence create(Vertx vertx) {
    return new JdbcTransactionPersistenceImpl(vertx);
  }

}
