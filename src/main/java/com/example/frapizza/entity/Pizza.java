package com.example.frapizza.entity;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Pizza {
  private Long id;
  private String name;
  private User createdBy;
  private OffsetDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pizza pizza = (Pizza) o;
    return id.equals(pizza.id) && name.equals(pizza.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
