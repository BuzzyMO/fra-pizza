package com.example.frapizza.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

@DataObject(generateConverter = true)
public class Delivery {
  private Long id;
  private Long createdBy;
  private Integer pizzeriaFrom;
  private String city;
  private String street;
  private String building;
  private String apartment;
  private Integer distanceM;
  private Integer expTime;

  public Delivery(JsonObject json) {
    this.id = json.getLong("id");
    this.createdBy = json.getLong("createdBy");
    this.pizzeriaFrom = json.getInteger("pizzeriaFrom");
    this.city = json.getString("city");
    this.street = json.getString("street");
    this.building = json.getString("building");
    this.apartment = json.getString("apartment");
    this.distanceM = json.getInteger("distanceM");
    this.expTime = json.getInteger("expTime");
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public Integer getPizzeriaFrom() {
    return pizzeriaFrom;
  }

  public void setPizzeriaFrom(Integer pizzeriaFrom) {
    this.pizzeriaFrom = pizzeriaFrom;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public String getApartment() {
    return apartment;
  }

  public void setApartment(String apartment) {
    this.apartment = apartment;
  }

  public Integer getDistanceM() {
    return distanceM;
  }

  public void setDistanceM(Integer distanceM) {
    this.distanceM = distanceM;
  }

  public Integer getExpTime() {
    return expTime;
  }

  public void setExpTime(Integer expTime) {
    this.expTime = expTime;
  }

  public JsonObject toJson() {
    return JsonObject.mapFrom(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Delivery delivery = (Delivery) o;
    return createdBy.equals(delivery.createdBy) && city.equals(delivery.city) && street.equals(delivery.street) && building.equals(delivery.building) && Objects.equals(apartment, delivery.apartment) && expTime.equals(delivery.expTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdBy, city, street, building, apartment, expTime);
  }
}
