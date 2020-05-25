package com.pega.exercise.eurovision_song_contest.persistence.impl;

import com.pega.exercise.eurovision_song_contest.models.Vote;
import com.pega.exercise.eurovision_song_contest.persistence.InMemoryTransactionPersistence;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated For testing purpose<br>
 * A test implementation using  Java Streams and storing data in a map<br>
 * Please use {@link JdbcTransactionPersistenceImpl} instead
 */
@Deprecated
public class InMemoryTransactionPersistenceImpl implements InMemoryTransactionPersistence {

  private final Map<Integer, Map<String, String>> votes;

  public InMemoryTransactionPersistenceImpl() {
    votes = new HashMap<>();
  }

  /*
  @Override
  public List<String> getTopThreeListByYear(Integer year) {

//    Map<String, Long> finalMap = new LinkedHashMap<>();
    List<String> result = new ArrayList<>();

    votes.entrySet().stream()
      .filter(integerMapEntry -> integerMapEntry.getKey() == year)
      .flatMap(y -> y.getValue().entrySet().stream())
      .collect(groupingBy(Map.Entry::getValue, counting()))
      .entrySet().stream()
      .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
      .limit(3)
      .forEachOrdered(e -> result.add(e.getKey()));
//      .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

    return result;
//    return finalMap;

  }

*/
  @Override
  public Single<RowSet<Row>> getTopThreeListByYear(Integer year) {
    return null;
  }

  @Override
  public Single<RowSet<Row>> getTopThreeListByCountry(Integer year, String country) {
    return null;
  }

  @Override
  public Completable addVote(Integer year, Vote vote) {
    return null;
  }

  /*
  @Override
  public List<String> getTopThreeListByCountry(Integer year, String country) {
    return null;
  }
*/

  /*
  @Override
  public Vote addVote(Integer year, Vote vote) {

    ObjectMapper oMapper = new ObjectMapper();
    Map<String, String> map = oMapper.convertValue(vote, Map.class);
    votes.put(year, map);
    return vote;

  }
   */

}
