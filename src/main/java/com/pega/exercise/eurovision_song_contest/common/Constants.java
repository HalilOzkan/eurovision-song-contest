package com.pega.exercise.eurovision_song_contest.common;

/**
 * This class groups constants
 */
public final class Constants {
  private Constants() {
  }

  public static final String HTTPSERVER = "0.0.0.0";
  public static final String HTTPSERVERPORT = "HTTPSERVERPORT";
  public static final String METRICSSERVERPORT = "METRICSSERVERPORT";

  public static final String EVENTBUSADDR_TRX = "voting.transactions";
  public static final String EVENTBUSADDR_METRICS = "voting.metrics";
  public static final Long METRIC_REPORT_PERIOD = 5000L;

}
