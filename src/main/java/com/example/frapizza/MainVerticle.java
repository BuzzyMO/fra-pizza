package com.example.frapizza;

import com.example.frapizza.verticle.HttpVerticle;
import com.example.frapizza.verticle.DataVerticle;
import com.example.frapizza.verticle.ServiceVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class.getName());

  @Override
  public void start(Promise<Void> promise) {
    doConfig(vertx)
      .map(configJson -> new DeploymentOptions().setConfig(configJson))
      .compose(this::deployOtherVerticles)
      .onSuccess(ok -> {
        LOGGER.info("Verticles deployed is successful");
        promise.complete();
      })
      .onFailure(ex -> {
        LOGGER.error("Verticles isn't deployed: " + ex.getMessage());
        promise.fail(ex);
      });
  }

  private Future<JsonObject> doConfig (Vertx vertx) {
    ConfigStoreOptions envStore = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject().put("raw-data", true));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(envStore);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    return retriever.getConfig();
  }

  private Future<Void> deployOtherVerticles(DeploymentOptions options){
    Future<String> httpVerticle = vertx.deployVerticle(new HttpVerticle(), options);
    Future<String> serviceVerticle = vertx.deployVerticle(new ServiceVerticle());
    Future<String> databaseVerticle = vertx.deployVerticle(new DataVerticle(), options);

    return CompositeFuture.all(httpVerticle, serviceVerticle, databaseVerticle).mapEmpty();
  }
}
