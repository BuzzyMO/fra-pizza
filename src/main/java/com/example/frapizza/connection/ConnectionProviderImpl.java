package com.example.frapizza.connection;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionProviderImpl implements ConnectionProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionProviderImpl.class.getName());
  private final Vertx vertx;

  public ConnectionProviderImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public PgPool getPool() {
    JsonObject props = loadEnv()
      .onSuccess(json -> LOGGER.info("Env is loaded"))
      .onFailure(ex -> {
        LOGGER.error("Env loaded is fail: " + ex.getMessage());
        throw new RuntimeException(ex);
      })
      .result();

    int port = Integer.parseInt(props.getString("DATASOURCE_PORT"));
    String host = props.getString("DATASOURCE_HOST");
    String dbName = props.getString("DATASOURCE_DBNAME");
    String username = props.getString("DATASOURCE_USERNAME");
    String password = props.getString("DATASOURCE_PASSWORD");

    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(port)
      .setHost(host)
      .setDatabase(dbName)
      .setUser(username)
      .setPassword(password);

    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);

    return PgPool.pool(vertx, connectOptions, poolOptions);
  }

  private Future<JsonObject> loadEnv() {
    ConfigStoreOptions envStore = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject().put("raw-data", true));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(envStore);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    return retriever.getConfig();
  }
}
