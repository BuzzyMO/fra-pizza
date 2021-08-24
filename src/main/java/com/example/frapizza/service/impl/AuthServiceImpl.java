package com.example.frapizza.service.impl;

import com.example.frapizza.dao.AuthDao;
import com.example.frapizza.dao.impl.AuthDaoImpl;
import com.example.frapizza.service.AuthService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthServiceImpl implements AuthService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class.getName());
  private AuthDao authDao;

  public AuthServiceImpl(Vertx vertx, PgPool pool){
    this.authDao = new AuthDaoImpl(pool);
  }

  @Override
  public void authentication(JsonObject credentials, Handler<AsyncResult<JsonObject>> resultHandler) {
    authDao.authentication(credentials)
      .onSuccess(ar -> resultHandler.handle(Future.succeededFuture(ar)))
      .onFailure(ex -> resultHandler.handle(Future.failedFuture(ex)));
  }
}
