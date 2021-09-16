package com.example.frapizza.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.util.Objects;

@DataObject(generateConverter = true)
public class Ingredient {
  private Integer id;
  private String name;
  private BigDecimal cost;

  public Ingredient(JsonObject json) {
    this.id = json.getInteger("id");
    this.name = json.getString("name");
    if(json.getString("cost") != null){
      this.cost = new BigDecimal(json.getString("cost"));
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public JsonObject toJson() {
    return JsonObject.mapFrom(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ingredient that = (Ingredient) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
