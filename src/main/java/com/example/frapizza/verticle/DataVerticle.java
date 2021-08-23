package com.example.frapizza.verticle;

import com.example.frapizza.connection.ConnectionProvider;
import com.example.frapizza.connection.ConnectionProviderImpl;
import com.example.frapizza.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;

public class DataVerticle extends AbstractVerticle {
  @Override
  public void start(){
    ConnectionProvider connectionProvider = new ConnectionProviderImpl(vertx);
    PgPool connectionPool = connectionProvider.getPool();

    ServiceBinder serviceBinder = new ServiceBinder(vertx);
    serviceBinder
      .setAddress(UserService.ADDRESS)
      .register(UserService.class, UserService.create(vertx, connectionPool));
  }
}
