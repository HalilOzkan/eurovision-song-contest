package com.pega.exercise.eurovision_song_contest.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

/**
 * Data object that represents a vote<br>
 * <br>
 * To trigger the code generation, we must also add in the same package of the interface
 * a <a href="file://package-info.java">package-info.java</a> with @ModuleGen annotation<br>
 * <br>
 * We must define both a constructor with only a JsonObject parameter and
 * a toJson() method that returns a JsonObject<br>
 * <br>
 * Converter class will be generated with build using Vert.x codegen.
 */
@DataObject(generateConverter = true, publicConverter = false)
public class Vote {

  private String countryFrom;
  private String votedFor;

  public Vote(String countryFrom, String votedFor) {
    this.countryFrom = countryFrom;
    this.votedFor = votedFor;
  }

  public Vote(Vote other) {
    this.countryFrom = other.countryFrom;
    this.votedFor = other.votedFor;
  }

  public Vote(JsonObject json) {
    VoteConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    VoteConverter.toJson(this, json);
    return json;
  }

  @Fluent
  public Vote setCountryFrom(String countryFrom) {
    this.countryFrom = countryFrom;
    return this;
  }

  @Fluent
  public Vote setVotedFor(String votedFor) {
    this.votedFor = votedFor;
    return this;
  }

  public String getCountryFrom() {
    return countryFrom;
  }

  public String getVotedFor() {
    return votedFor;
  }

}
