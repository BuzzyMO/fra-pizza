package com.example.frapizza.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface AuthDao {
  Future<JsonObject> authentication(JsonObject credentials);
}
