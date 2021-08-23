package com.example.frapizza.entity;

import java.util.Objects;

public class Order {
  private Long id;
  private Delivery delivery;
  private Pizza pizza;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Delivery getDelivery() {
    return delivery;
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
  }

  public Pizza getPizza() {
    return pizza;
  }

  public void setPizza(Pizza pizza) {
    this.pizza = pizza;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return id.equals(order.id) && delivery.equals(order.delivery) && pizza.equals(order.pizza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, delivery, pizza);
  }
}
