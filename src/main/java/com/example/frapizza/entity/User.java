package com.example.frapizza.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

@DataObject(generateConverter = true)
public class User {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String passwordSalt;
  private String phoneNumber;
  private OffsetDateTime createdAt;

  public User(JsonObject json) {
    this.id = json.getLong("id");
    this.firstName = json.getString("firstName");
    this.lastName = json.getString("lastName");
    this.email = json.getString("email");
    this.password = json.getString("password");
    this.passwordSalt = json.getString("passwordSalt");
    this.phoneNumber = json.getString("phoneNumber");
    if (json.getInstant("createdAt") != null) {
      this.createdAt = OffsetDateTime.ofInstant(json.getInstant("createdAt"), ZoneId.of("UTC"));
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordSalt() {
    return passwordSalt;
  }

  public void setPasswordSalt(String passwordSalt) {
    this.passwordSalt = passwordSalt;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
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
    User user = (User) o;
    return email.equals(user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}
