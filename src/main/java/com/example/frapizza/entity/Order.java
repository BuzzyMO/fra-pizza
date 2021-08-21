package com.example.frapizza.entity;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pizza_id")
  private Pizza pizza;

}
