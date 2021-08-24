package com.example.frapizza.connection;

import com.example.frapizza.util.ConfigLoader;
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
    JsonObject props = ConfigLoader.loadEnv(vertx);

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
}
