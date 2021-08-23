package com.example.frapizza.service.impl;

import com.example.frapizza.dao.impl.UserDaoImpl;
import com.example.frapizza.entity.User;
import com.example.frapizza.service.UserService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class.getName());
  private final UserDaoImpl userDao;

  public UserServiceImpl(Vertx vertx, PgPool pool){
    this.userDao = new UserDaoImpl(pool);
  }

  @Override
  public void save(JsonObject userJson, Handler<AsyncResult<Void>> resultHandler) {
    User user = userJson.mapTo(User.class);
    resultHandler.handle(userDao.save(user)
      .onSuccess(r -> LOGGER.info("User is created: " + user.getEmail()))
      .onFailure(ex -> LOGGER.error("User creation failed: " + user.getId())));
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
