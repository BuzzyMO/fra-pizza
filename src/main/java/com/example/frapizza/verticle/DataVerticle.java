package com.example.frapizza.verticle;

import com.example.frapizza.connection.ConnectionProvider;
import com.example.frapizza.connection.ConnectionProviderImpl;
import com.example.frapizza.dao.AuthDao;
import com.example.frapizza.dao.UserDao;
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
      .setAddress(UserDao.ADDRESS)
      .register(UserDao.class, UserDao.create(connectionPool));

    serviceBinder
      .setAddress(AuthDao.ADDRESS)
      .register(AuthDao.class, AuthDao.create(connectionPool));
  }
}
