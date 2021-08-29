package com.example.frapizza.verticle;

import com.example.frapizza.service.AuthService;
import com.example.frapizza.service.OrderService;
import com.example.frapizza.service.PizzaService;
import com.example.frapizza.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;

public class ServiceVerticle extends AbstractVerticle {

  @Override
  public void start() {
    ServiceBinder serviceBinder = new ServiceBinder(vertx);

    serviceBinder
      .setAddress(UserService.ADDRESS)
      .register(UserService.class, UserService.create(vertx));

    serviceBinder
      .setAddress(AuthService.ADDRESS)
      .register(AuthService.class, AuthService.create(vertx));

    serviceBinder
      .setAddress(PizzaService.ADDRESS)
      .register(PizzaService.class, PizzaService.create(vertx));

    serviceBinder
      .setAddress(OrderService.ADDRESS)
      .register(OrderService.class, OrderService.create(vertx));
  }
}
