package com.pega.exercise.eurovision_song_contest;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.launcher.VertxCommandLauncher;
import io.vertx.core.impl.launcher.VertxLifecycleHooks;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.getenv;

/**
 * The entry point of the application.
 * The fatjar has a manifest with <b>Main-Class</b> set to the this class as launcher,
 * also <b>Main-Verticle</b> is set within this manifest
 */
public class Launcher extends VertxCommandLauncher implements VertxLifecycleHooks {

  private Logger logger = LoggerFactory.getLogger(Launcher.class);

  /**
   * It enforces SLF4J logging, please check logback.xml for logging settings.
   * Next, dispatches the command option to Vert.x context to start the application
   * @param args Vert.x command options. This application starts with "run" option, set by Gradle Application run task
   */
  public static void main(String[] args) {

    //enforce SLF4J logging set
    if (null == System.getProperty("vertx.logger-delegate-factory-class-name")) {
      System.setProperty("vertx.logger-delegate-factory-class-name", io.vertx.core.logging.SLF4JLogDelegateFactory.class.getCanonicalName());
    }

    //TODO: sanitize command line arguments before using them, check Apache CLI CommandLineParser
    new Launcher()
      .dispatch(args);
  }

  @Override
  public void afterConfigParsed(JsonObject config) {
    //no any implementation need right now
  }

  /**
   * if the database parameters set then allow starting Vertx, otherwise terminate jvm
   * @param options Vertx options, this application has no requirement to manipulate or access options yet
   */
  @Override
  public void beforeStartingVertx(VertxOptions options) {
    // check if required parameters set in environment variables
    if (Boolean.TRUE.equals(dbParametersMissing())) {
      logger.error("Please set database parameter environment variables properly");
      System.exit(1);
    }
  }

  @Override
  public void afterStartingVertx(Vertx vertx) {
    //no any implementation need right now
  }

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    //no any implementation need right now
  }

  @Override
  public void beforeStoppingVertx(Vertx vertx) {
    //no any implementation need right now
  }

  @Override
  public void afterStoppingVertx() {
    //no any implementation need right now
  }

  @Override
  public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions, Throwable cause) {
    //no any implementation need right now
  }

  /**
   * check if all database parameters set
   * @return true if all parameters set, otherwise return false
   */
  private Boolean dbParametersMissing() {
    return ((getenv("PGHOST") == null)
      || (getenv("PGPORT") == null)
      || (getenv("PGUSER") == null)
      || (getenv("PGPASSWORD") == null)
      || (getenv("PGDATABASE") == null));
  }

}
