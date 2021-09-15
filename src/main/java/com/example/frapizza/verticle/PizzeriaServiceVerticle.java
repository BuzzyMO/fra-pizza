package com.example.frapizza.verticle;

import com.example.frapizza.service.PizzeriaService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class PizzeriaServiceVerticle extends AbstractVerticle {
  private MessageConsumer<JsonObject> consumer;
  private ServiceBinder serviceBinder;

  @Override
  public void start() {
    serviceBinder = new ServiceBinder(vertx);

    this.consumer = serviceBinder
      .setAddress(PizzeriaService.ADDRESS)
      .register(PizzeriaService.class, PizzeriaService.create(vertx));
  }

  @Override
  public void stop() {
    serviceBinder.unregister(consumer);
  }
}
