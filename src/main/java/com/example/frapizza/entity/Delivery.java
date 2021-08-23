package com.example.frapizza.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Objects;

public class Delivery {
  private Long id;
  private User user;
  private String city;
  private String street;
  private String building;
  private String apartment;
  private Time expTime;
  private BigDecimal cost;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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

  public Time getExpTime() {
    return expTime;
  }

  public void setExpTime(Time expTime) {
    this.expTime = expTime;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Delivery delivery = (Delivery) o;
    return user.equals(delivery.user) && city.equals(delivery.city) && street.equals(delivery.street) && building.equals(delivery.building) && Objects.equals(apartment, delivery.apartment) && expTime.equals(delivery.expTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, city, street, building, apartment, expTime);
  }
}
