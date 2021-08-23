package com.example.frapizza;

import com.example.frapizza.verticle.HttpVerticle;
import com.example.frapizza.verticle.DataVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class.getName());

  @Override
  public void start(Promise<Void> promise) {
    CompositeFuture.all(
        vertx.deployVerticle(new HttpVerticle()),
        vertx.deployVerticle(new DataVerticle()))
      .onSuccess(ok -> {
        LOGGER.info("Verticles deployed is successful");
        promise.complete();
      })
      .onFailure(ex -> {
        LOGGER.error("Verticles isn't deployed: " + ex.getMessage());
        promise.fail(ex);
      });

  }
}
