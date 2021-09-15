package com.example.frapizza;

import com.example.frapizza.verticle.*;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

  @SuppressWarnings("rawtypes")
  private Future<Void> deployOtherVerticles(DeploymentOptions options){
    List<Future> futures = new ArrayList<>();
    futures.add(vertx.deployVerticle(new HttpVerticle(), options));
    futures.add(vertx.deployVerticle(new AuthServiceVerticle(), options));
    futures.add(vertx.deployVerticle(new IngredientServiceVerticle(), options));
    futures.add(vertx.deployVerticle(new OrderServiceVerticle(), options));
    futures.add(vertx.deployVerticle(new PizzaServiceVerticle(), options));
    futures.add(vertx.deployVerticle(new PizzeriaServiceVerticle(), options));
    futures.add(vertx.deployVerticle(new UserServiceVerticle(), options));
    futures.add(vertx.deployVerticle(new DatabaseVerticle(), options));

    return CompositeFuture.all(futures).mapEmpty();
  }
}
