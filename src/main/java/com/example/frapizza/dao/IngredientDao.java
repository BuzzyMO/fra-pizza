package com.example.frapizza.dao;

import com.example.frapizza.dao.impl.IngredientDaoImpl;
import com.example.frapizza.entity.Ingredient;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.pgclient.PgPool;

@VertxGen
@ProxyGen
public interface IngredientDao {
    String ADDRESS = "ingredient.dao";

    static IngredientDao create(PgPool pool) {
        return new IngredientDaoImpl(pool);
    }

    static IngredientDao createProxy(Vertx vertx, String address) {
        return new IngredientDaoVertxEBProxy(vertx, address);
    }

    void save(Ingredient ingredient, Handler<AsyncResult<Void>> resultHandler);

    void update(Integer id, Ingredient ingredient, Handler<AsyncResult<Void>> resultHandler);

    void delete(Integer id, Handler<AsyncResult<Void>> resultHandler);

    void readAll(Handler<AsyncResult<JsonArray>> resultHandler);
}
