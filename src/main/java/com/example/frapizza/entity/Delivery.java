package com.example.frapizza.entity;

import java.util.Objects;

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
