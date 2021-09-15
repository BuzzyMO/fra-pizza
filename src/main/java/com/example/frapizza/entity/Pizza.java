package com.example.frapizza.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

@DataObject(generateConverter = true)
public class Pizza {
  private Long id;
  private String name;
  private String description;
  private Long createdBy;
  private OffsetDateTime createdAt;

  public Pizza(JsonObject json) {
    this.id = json.getLong("id");
    this.name = json.getString("name");
    this.description = json.getString("description");
    this.createdBy = json.getLong("createdBy");
    this.createdAt = OffsetDateTime.ofInstant(json.getInstant("createdAt"), ZoneId.of("UTC"));
  }

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public JsonObject toJson() {
    return JsonObject.mapFrom(this);
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
