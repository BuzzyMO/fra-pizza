package com.example.frapizza.exception;

public class IncorrectCredentialsException extends RuntimeException {

  public IncorrectCredentialsException(String errorMessage) {
    super(errorMessage);
  }
}
