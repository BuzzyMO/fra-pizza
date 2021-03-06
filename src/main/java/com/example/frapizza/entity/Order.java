package com.example.frapizza.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

@DataObject(generateConverter = true)
public class Order {
  private Long id;
  private Long deliveryId;
  private Long pizzaId;
  private OffsetDateTime orderedAt;

  public Order(JsonObject json) {
    this.id = json.getLong("id");
    this.deliveryId = json.getLong("deliveryId");
    this.pizzaId = json.getLong("pizzaId");
    if(json.getInstant("orderedAt") != null){
      this.orderedAt = OffsetDateTime.ofInstant(json.getInstant("orderedAt"), ZoneId.of("UTC"));
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDeliveryId() {
    return deliveryId;
  }

  public void setDeliveryId(Long deliveryId) {
    this.deliveryId = deliveryId;
  }

  public Long getPizzaId() {
    return pizzaId;
  }

  public void setPizzaId(Long pizzaId) {
    this.pizzaId = pizzaId;
  }

  public OffsetDateTime getOrderedAt() {
    return orderedAt;
  }

  public void setOrderedAt(OffsetDateTime orderedAt) {
    this.orderedAt = orderedAt;
  }

  public JsonObject toJson() {
    return JsonObject.mapFrom(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(id, order.id) && Objects.equals(deliveryId, order.deliveryId) && Objects.equals(pizzaId, order.pizzaId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, deliveryId, pizzaId);
  }
}
