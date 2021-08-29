package com.example.frapizza.entity;

import java.util.Objects;

public class Pizzeria {
  private Integer id;
  private String city;
  private String street;
  private String building;
  private Float latitude;
  private Float longitude;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pizzeria pizzeria = (Pizzeria) o;
    return Objects.equals(city, pizzeria.city) && Objects.equals(street, pizzeria.street) && Objects.equals(building, pizzeria.building) && latitude.equals(pizzeria.latitude) && longitude.equals(pizzeria.longitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, street, building, latitude, longitude);
  }
}
