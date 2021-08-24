package com.example.frapizza.util;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class.getName());

  private ConfigLoader() {
  }

  public static JsonObject loadEnv(Vertx vertx) {
    ConfigStoreOptions envStore = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject().put("raw-data", true));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(envStore);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    return retriever.getConfig()
      .onSuccess(json -> LOGGER.info("Env is loaded"))
      .onFailure(ex -> {
        LOGGER.error("Env loaded is fail: " + ex.getMessage());
        throw new RuntimeException(ex);
      })
      .result();
  }
}
