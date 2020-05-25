package com.pega.exercise.eurovision_song_contest.persistence;

import com.pega.exercise.eurovision_song_contest.models.Vote;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;

/**
 * Base Interface to persist and query vote transactions
 */
public interface TransactionPersistence {

  Single<RowSet<Row>> getTopThreeListByYear(Integer year);

  Single<RowSet<Row>> getTopThreeListByCountry(Integer year, String country);

  Completable addVote(Integer year, Vote vote);

}
