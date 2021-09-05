package com.example.frapizza.service.impl;

import com.example.frapizza.dao.PizzeriaDao;
import com.example.frapizza.service.PizzeriaService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class PizzeriaServiceImpl implements PizzeriaService {
  private final PizzeriaDao pizzeriaDao;

  public PizzeriaServiceImpl(Vertx vertx){
    this.pizzeriaDao = PizzeriaDao.createProxy(vertx, PizzeriaDao.ADDRESS);
  }

  @Override
  public void save(JsonObject pizzeriaJson, Handler<AsyncResult<Void>> resultHandler) {
    pizzeriaDao.save(pizzeriaJson, resultHandler);
  }

  @Override
  public void update(Integer id, JsonObject pizzeriaJson, Handler<AsyncResult<Void>> resultHandler) {
    pizzeriaDao.update(id, pizzeriaJson, resultHandler);
  }

  @Override
  public void delete(Integer id, Handler<AsyncResult<Void>> resultHandler) {
    pizzeriaDao.delete(id, resultHandler);
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    pizzeriaDao.readAll(resultHandler);
  }
}
