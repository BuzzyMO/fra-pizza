package com.example.frapizza.connection;

import io.vertx.pgclient.PgPool;

public interface ConnectionProvider {
  PgPool getPool();
}
