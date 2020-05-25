package com.pega.exercise.eurovision_song_contest.persistence;

import com.pega.exercise.eurovision_song_contest.persistence.impl.InMemoryTransactionPersistenceImpl;

public interface InMemoryTransactionPersistence extends TransactionPersistence {
  /**
   * Factory method to instantiate In-memory TransactionPersistence
   *
   * @return TransactionPersistence instance
   */
  static InMemoryTransactionPersistence create() {
    return new InMemoryTransactionPersistenceImpl();
  }

}
