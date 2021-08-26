package com.example.frapizza.service.impl;

import com.example.frapizza.dao.AuthDao;
import com.example.frapizza.service.AuthService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthServiceImpl implements AuthService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class.getName());
  private final AuthDao authDao;

  public AuthServiceImpl(Vertx vertx){
    this.authDao = AuthDao.createProxy(vertx, AuthDao.ADDRESS);
  }

  @Override
  public void authentication(JsonObject credentials, Handler<AsyncResult<JsonObject>> resultHandler) {
    authDao.authentication(credentials, resultHandler);
  }
}
