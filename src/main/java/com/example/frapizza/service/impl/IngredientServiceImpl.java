package com.example.frapizza.service.impl;

import com.example.frapizza.dao.IngredientDao;
import com.example.frapizza.service.IngredientService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class IngredientServiceImpl implements IngredientService {
  private final IngredientDao ingredientDao;

  public IngredientServiceImpl(Vertx vertx) {
    this.ingredientDao = IngredientDao.createProxy(vertx, IngredientDao.ADDRESS);
  }


  @Override
  public void save(JsonObject ingredientJson, Handler<AsyncResult<Void>> resultHandler) {
    ingredientDao.save(ingredientJson, resultHandler);
  }

  @Override
  public void update(JsonObject ingredientJson, Handler<AsyncResult<Void>> resultHandler) {
    ingredientDao.update(ingredientJson, resultHandler);
  }

  @Override
  public void delete(Integer id, Handler<AsyncResult<Void>> resultHandler) {
    ingredientDao.delete(id, resultHandler);
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    ingredientDao.readAll(resultHandler);
  }
}
