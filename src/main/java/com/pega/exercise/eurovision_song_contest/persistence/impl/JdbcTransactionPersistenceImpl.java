package com.pega.exercise.eurovision_song_contest.persistence.impl;

import com.pega.exercise.eurovision_song_contest.models.Vote;
import com.pega.exercise.eurovision_song_contest.persistence.JdbcTransactionPersistence;
import com.pega.exercise.eurovision_song_contest.repository.impl.JdbcRepositoryImpl;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.Tuple;

import static java.lang.System.getenv;

public class JdbcTransactionPersistenceImpl extends JdbcRepositoryImpl implements JdbcTransactionPersistence {

  public JdbcTransactionPersistenceImpl(Vertx vertx) {
    // create Postgres connection pool and client via super JDBC Repository constructor
    super(vertx);
  }

  @Override
  public Single<RowSet<Row>> getTopThreeListByYear(Integer year) {
    return this.queryWithParams(Tuple.of(year), GET_TOP_THREE_LIST_BY_YEAR);
  }

  @Override
  public Single<RowSet<Row>> getTopThreeListByCountry(Integer year, String country) {
    return this.queryWithParams(Tuple.of(year, country), GET_TOP_THREE_LIST_BY_COUNTRY);
  }

  @Override
  public Completable addVote(Integer year, Vote vote) {
    return this.execute(Tuple.of(vote.getCountryFrom(), vote.getVotedFor(), year), INSERT_STATEMENT);
  }

  public Single<RowSet<Row>> getMetrics() {
    return this.query(GET_METRICS);
  }

  //SQL statements

  public static final String TBL_SCHEMA = getenv("PGDATABASE")
    + "."
    + getenv("PGSCHEMA");

  public static final String GET_TOP_THREE_LIST_BY_YEAR = "select votedfor\n" +
    "from " + TBL_SCHEMA + ".votes\n" +
    "where year = $1\n" +
    "group by votedfor\n" +
    "order by count(votedfor) desc\n" +
    "limit 3";

  public static final String GET_TOP_THREE_LIST_BY_COUNTRY = "select countryfrom\n" +
    "from " + TBL_SCHEMA + ".votes\n" +
    "where year = $1\n" +
    "and votedfor = initcap($2)\n" +
    "group by countryfrom\n" +
    "order by count(countryfrom) desc\n" +
    "limit 3";

  public static final String INSERT_STATEMENT = "INSERT INTO\n" +
    TBL_SCHEMA + ".votes (countryfrom, votedfor, year)\n" +
    "VALUES (initcap($1), initcap($2), $3)";

  public static final String GET_METRICS = "select year, countryfrom, count(countryfrom)\n" +
    "from " + TBL_SCHEMA + ".votes\n" +
    "group by year, countryfrom\n" +
    "order by year desc, count(countryfrom) desc";
}
