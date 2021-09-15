package com.example.frapizza.service.impl;

import com.example.frapizza.dao.PizzaDao;
import com.example.frapizza.entity.Pizza;
import com.example.frapizza.service.PizzaService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class PizzaServiceImpl implements PizzaService {
  private final PizzaDao pizzaDao;

  public PizzaServiceImpl(Vertx vertx) {
    this.pizzaDao = PizzaDao.createProxy(vertx, PizzaDao.ADDRESS);
  }

  @Override
  public void save(JsonObject pizzaIngredients, Handler<AsyncResult<Void>> resultHandler) {
    Pizza pizza = new Pizza(pizzaIngredients.getJsonObject("pizza"));
    List<Integer> ingredientIds = pizzaIngredients.getJsonArray("ingredients")
      .stream()
      .map(e -> (Integer) e)
      .collect(Collectors.toList());

    pizzaDao.save(pizza, ingredientIds, resultHandler);
  }

  @Override
  public void update(Long id, JsonObject pizzaJson, Handler<AsyncResult<Void>> resultHandler) {
    Pizza pizza = new Pizza(pizzaJson);
    pizzaDao.update(id, pizza, resultHandler);
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
  public void readByUser(Long userId, Handler<AsyncResult<JsonArray>> resultHandler) {
    pizzaDao.readByUser(userId, resultHandler);
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    pizzaDao.readAll(resultHandler);
  }
}
