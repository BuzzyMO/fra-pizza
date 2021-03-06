package com.example.frapizza.dao;

import com.example.frapizza.dao.impl.PizzaDaoImpl;
import com.example.frapizza.entity.Pizza;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.pgclient.PgPool;

import java.util.List;

@VertxGen
@ProxyGen
public interface PizzaDao {
  String ADDRESS = "pizza.dao";

  static PizzaDao create(PgPool pool) {
    return new PizzaDaoImpl(pool);
  }

  static PizzaDao createProxy(Vertx vertx, String address) {
    return new PizzaDaoVertxEBProxy(vertx, address);
  }

  void save(Pizza pizza, List<Integer> ingredientIds, Handler<AsyncResult<Void>> resultHandler);

  void update(Long id, Pizza pizza, Handler<AsyncResult<Void>> resultHandler);

  void delete(Long id, Handler<AsyncResult<Void>> resultHandler);

  void readByAuthority(Integer authorityId, Handler<AsyncResult<JsonArray>> resultHandler);

  void readByUser(Long userId, Handler<AsyncResult<JsonArray>> resultHandler);

  void readAll(Handler<AsyncResult<JsonArray>> resultHandler);
}
