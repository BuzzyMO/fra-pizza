package com.example.frapizza.verticle;

import com.example.frapizza.service.IngredientService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class IngredientServiceVerticle extends AbstractVerticle {
  private MessageConsumer<JsonObject> consumer;
  private ServiceBinder serviceBinder;

  @Override
  public void start() {
    serviceBinder = new ServiceBinder(vertx);

    this.consumer = serviceBinder
      .setAddress(IngredientService.ADDRESS)
      .register(IngredientService.class, IngredientService.create(vertx));
  }

  @Override
  public void stop() {
    serviceBinder.unregister(consumer);
  }
}
