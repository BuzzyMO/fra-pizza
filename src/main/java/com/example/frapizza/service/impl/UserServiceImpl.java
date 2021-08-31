package com.example.frapizza.service.impl;

import com.example.frapizza.dao.UserDao;
import com.example.frapizza.service.UserService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class.getName());
  private final UserDao userDao;

  public UserServiceImpl(Vertx vertx){
    this.userDao = UserDao.createProxy(vertx, UserDao.ADDRESS);
  }

  @Override
  public void save(JsonObject userJson, Handler<AsyncResult<Void>> resultHandler) {
    userDao.save(userJson, resultHandler);
  }

  @Override
  public void update(JsonObject userJson, Handler<AsyncResult<JsonObject>> resultHandler) {
    userDao.update(userJson, resultHandler);
  }

  @Override
  public void delete(Long id, Handler<AsyncResult<JsonObject>> resultHandler) {
    userDao.delete(id, resultHandler);
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    userDao.readAll(resultHandler);
  }
}
