package com.example.frapizza.verticle;

import com.example.frapizza.service.PizzaService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class PizzaServiceVerticle extends AbstractVerticle {
  private MessageConsumer<JsonObject> consumer;
  private ServiceBinder serviceBinder;

  @Override
  public void start() {
    serviceBinder = new ServiceBinder(vertx);

    this.consumer = serviceBinder
      .setAddress(PizzaService.ADDRESS)
      .register(PizzaService.class, PizzaService.create(vertx));
  }

  @Override
  public void stop() {
    serviceBinder.unregister(consumer);
  }
}
