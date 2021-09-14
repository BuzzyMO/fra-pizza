package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.AuthDao;
import com.example.frapizza.exception.IncorrectCredentialsException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.sqlclient.SqlAuthentication;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthDaoImpl implements AuthDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthDaoImpl.class.getName());
  private final PgPool pool;

  public AuthDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void authentication(JsonObject credentials, Handler<AsyncResult<JsonObject>> resultHandler) {
    String query = "SELECT id, first_name, last_name, email, password, password_salt FROM users WHERE email=$1";

    pool.withTransaction(client -> client
        .preparedQuery(query)
        .execute(Tuple.of(credentials.getString("username")))
        .map(rs -> rs.iterator().next().toJson())
        .compose(rs -> validation(rs, credentials))
        .map(this::removeCredentialsInfo)
        .compose(resultJson -> setRoles(client, resultJson)))
      .onSuccess(json -> {
        LOGGER.info("Transaction succeeded: auth complete, id - " + json.getLong("id"));
        resultHandler.handle(Future.succeededFuture(json));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  private Future<JsonObject> validation(JsonObject resultJson, JsonObject credentials) {
    Promise<JsonObject> promise = Promise.promise();
    if (!resultJson.isEmpty() && isValidPassword(resultJson, credentials)) {
      promise.complete(resultJson);
    } else {
      promise.fail(new IncorrectCredentialsException("Password or email is incorrect"));
    }
    return promise.future();
  }

  private boolean isValidPassword(JsonObject resultJson, JsonObject credentials) {
    SqlAuthentication sqlAuth = SqlAuthentication.create(pool);
    String algorithm = "pbkdf2";
    String salt = resultJson.getString("password_salt");
    String password = resultJson.getString("password");
    String currentHash = sqlAuth.hash(algorithm, salt, credentials.getString("password"));

    return currentHash.equals(password);
  }

  private JsonObject removeCredentialsInfo(JsonObject resultJson) {
    resultJson.remove("password");
    resultJson.remove("password_salt");
    return resultJson;
  }

  private Future<JsonObject> setRoles(SqlClient client, JsonObject resultJson) {
    Promise<JsonObject> promise = Promise.promise();
    Long userId = resultJson.getLong("id");
    String selectRolesQuery = "SELECT value FROM user_authorities INNER JOIN authorities a on a.id = user_authorities.authority_id WHERE user_id=$1";
    client
      .preparedQuery(selectRolesQuery)
      .execute(Tuple.of(userId))
      .onSuccess(rs -> {
        JsonArray roles = new JsonArray();
        for (Row row : rs) {
          roles.add(row.toJson());
        }
        resultJson.put("roles", roles);
        promise.complete(resultJson);
      })
      .onFailure(promise::fail);
    return promise.future();
  }
}
