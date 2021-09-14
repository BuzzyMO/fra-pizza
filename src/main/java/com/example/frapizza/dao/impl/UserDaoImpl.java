package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.UserDao;
import com.example.frapizza.entity.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.VertxContextPRNG;
import io.vertx.ext.auth.sqlclient.SqlAuthentication;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl implements UserDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class.getName());
  private final PgPool pool;

  public UserDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void save(JsonObject userJson, Handler<AsyncResult<Void>> resultHandler) {
    User user = userJson.mapTo(User.class);
    String salt = VertxContextPRNG.current().nextString(32);
    String encodedPassword = encode(salt, user.getPassword());
    String query = "INSERT INTO users(first_name, last_name, email, password, password_salt, phone_number) " +
      "VALUES ($1, $2, $3, $4, $5, $6)";
    pool.withTransaction(client -> client
        .preparedQuery(query)
        .execute(Tuple.of(user.getFirstName(), user.getLastName(), user.getEmail(), encodedPassword, salt, user.getPhoneNumber())))
      .onSuccess(rs -> {
        LOGGER.info("Transaction succeeded: user is created " + user.getEmail());
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: user not created " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void update(Long id, JsonObject userJson, Handler<AsyncResult<Void>> resultHandler) {
    User user = userJson.mapTo(User.class);
    String updateQuery = "UPDATE users SET(first_name, last_name, email, password, phone_number) " +
      "= ($1, $2, $3, $4, $5) WHERE id=$6";
    pool.withTransaction(client -> client
        .preparedQuery(updateQuery)
        .execute(Tuple.of(user.getFirstName(), user.getLastName(), user.getEmail(),
          user.getPassword(), user.getPhoneNumber(), id)))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: user is updated " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: user not updated " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void delete(Long id, Handler<AsyncResult<Void>> resultHandler) {
    String deleteQuery = "DELETE FROM users WHERE id=$1";
    pool.withTransaction(client -> client
        .preparedQuery(deleteQuery)
        .execute(Tuple.of(id)))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: user is deleted " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: user not deleted " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    String readAllQuery = "SELECT* FROM users";
    pool.withTransaction(client -> client
        .preparedQuery(readAllQuery)
        .execute())
      .onSuccess(rs -> {
        JsonArray jsonArray = new JsonArray();
        for (Row row : rs) {
          jsonArray.add(row.toJson());
        }
        LOGGER.info("Transaction succeeded: users is read");
        resultHandler.handle(Future.succeededFuture(jsonArray));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: users not read: " + ex.getMessage());
        ex.printStackTrace();
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  private String encode(String salt, String secret) {
    SqlAuthentication sqlAuth = SqlAuthentication.create(pool);
    return sqlAuth.hash("pbkdf2", salt, secret);
  }
}
