package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.AuthDao;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthDaoImpl implements AuthDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthDaoImpl.class.getName());
  private final PgPool pool;

  public AuthDaoImpl(PgPool pool){
    this.pool = pool;
  }

  @Override
  public void authentication(JsonObject credentials, Handler<AsyncResult<JsonObject>> resultHandler) {
    String query = "SELECT id, first_name, last_name, email FROM users WHERE email=$1 AND password=$2";

    pool.withTransaction(client -> client
        .preparedQuery(query)
        .execute(Tuple.of(credentials.getString("username"), credentials.getString("password")))
        .onFailure(ex -> LOGGER.error("User authentication failed: " + ex.getMessage())))
      .onSuccess(rs -> {
        if(rs.rowCount()<1){
          String errMsg ="Password or email incorrect";
          LOGGER.error(errMsg);
          resultHandler.handle(Future.failedFuture(errMsg));
          return;
        }
        Row row = rs.iterator().next();
        JsonObject json = row.toJson();
        LOGGER.info("Transaction succeeded");
        resultHandler.handle(Future.succeededFuture(json));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed");
        resultHandler.handle(Future.failedFuture(ex));
      });
  }
}
