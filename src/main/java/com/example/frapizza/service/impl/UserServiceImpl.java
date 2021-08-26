package com.example.frapizza.service.impl;

import com.example.frapizza.dao.UserDao;
import com.example.frapizza.service.UserService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
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

  }

  @Override
  public void delete(String id, Handler<AsyncResult<JsonObject>> resultHandler) {

  }

  @Override
  public void readById(String id, Handler<AsyncResult<JsonObject>> resultHandler) {

  }

  @Override
  public void readAll(Handler<AsyncResult<JsonObject>> resultHandler) {

  }
}
