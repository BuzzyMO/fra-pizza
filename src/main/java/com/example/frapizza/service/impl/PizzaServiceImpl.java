package com.example.frapizza.service.impl;

import com.example.frapizza.dao.PizzaDao;
import com.example.frapizza.service.PizzaService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class PizzaServiceImpl implements PizzaService {
  private final PizzaDao pizzaDao;

  public PizzaServiceImpl(Vertx vertx) {
    this.pizzaDao = PizzaDao.createProxy(vertx, PizzaDao.ADDRESS);
  }

  @Override
  public void save(JsonObject completePizza, Handler<AsyncResult<Void>> resultHandler) {
    pizzaDao.save(completePizza, resultHandler);
  }

  @Override
  public void update(JsonObject pizzaJson, Handler<AsyncResult<Void>> resultHandler) {
    pizzaDao.update(pizzaJson, resultHandler);
  }

  @Override
  public void delete(Long id, Handler<AsyncResult<Void>> resultHandler) {
    pizzaDao.delete(id, resultHandler);
  }

  @Override
  public void readByAuthority(Integer authorityId, Handler<AsyncResult<JsonArray>> resultHandler) {
    pizzaDao.readByAuthority(authorityId, resultHandler);
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    pizzaDao.readAll(resultHandler);
  }
}
