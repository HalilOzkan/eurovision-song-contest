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

public class Launcher extends VertxCommandLauncher implements VertxLifecycleHooks {

  private Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

  public static void main(String[] args) {

    //enforce SLF4J logging set
    if (null == System.getProperty("vertx.logger-delegate-factory-class-name")) {
      System.setProperty("vertx.logger-delegate-factory-class-name", io.vertx.core.logging.SLF4JLogDelegateFactory.class.getCanonicalName());
    }

    new Launcher()
      .dispatch(args);
  }

  @Override
  public void afterConfigParsed(JsonObject config) {
    //no any implementation need right now
  }

  @Override
  public void beforeStartingVertx(VertxOptions options) {
    // check if required parameters set in environment variables
    if (Boolean.TRUE.equals(dbParametersMissing())) {
      LOGGER.error("Please set database paramater environment variables properly");
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

  private Boolean dbParametersMissing() {
    return ((getenv("PGHOST") == null)
      || (getenv("PGPORT") == null)
      || (getenv("PGUSER") == null)
      || (getenv("PGPASSWORD") == null)
      || (getenv("PGDATABASE") == null));
  }

}
