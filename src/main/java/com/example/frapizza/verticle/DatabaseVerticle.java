package com.example.frapizza.verticle;

import com.example.frapizza.dao.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.sqlclient.PoolOptions;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

public class DatabaseVerticle extends AbstractVerticle {
  private JsonObject props;

  @Override
  public void start(Promise<Void> promise) {
    props = config();
    doDatabaseMigration()
      .onSuccess(promise::complete)
      .onFailure(promise::fail);

    PgPool connectionPool = getPool();
    ServiceBinder serviceBinder = new ServiceBinder(vertx);

    serviceBinder
      .setAddress(UserDao.ADDRESS)
      .register(UserDao.class, UserDao.create(connectionPool));

    serviceBinder
      .setAddress(AuthDao.ADDRESS)
      .register(AuthDao.class, AuthDao.create(connectionPool));

    serviceBinder
      .setAddress(PizzaDao.ADDRESS)
      .register(PizzaDao.class, PizzaDao.create(connectionPool));

    serviceBinder
      .setAddress(PizzeriaDao.ADDRESS)
      .register(PizzeriaDao.class, PizzeriaDao.create(connectionPool));

    serviceBinder
      .setAddress(OrderDao.ADDRESS)
      .register(OrderDao.class, OrderDao.create(connectionPool));

    serviceBinder
      .setAddress(IngredientDao.ADDRESS)
      .register(IngredientDao.class, IngredientDao.create(connectionPool));
  }

  private Future<Void> doDatabaseMigration() {
    Promise<Void> promise = Promise.promise();
    Flyway flyway = Flyway.configure().dataSource(
        props.getString("DATASOURCE_URL"),
        props.getString("DATASOURCE_USERNAME"),
        props.getString("DATASOURCE_PASSWORD"))
      .load();
    try {
      flyway.migrate();
      promise.complete();
    } catch (FlywayException ex) {
      promise.fail(ex);
    }
    return promise.future();
  }

  private PgPool getPool() {
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
      .setMaxSize(16);

    return PgPool.pool(vertx, connectOptions, poolOptions);
  }
}
