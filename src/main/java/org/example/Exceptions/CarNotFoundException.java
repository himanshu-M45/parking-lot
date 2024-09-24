package org.example.Exceptions;

public class CarNotFoundException extends NullPointerException {
  public CarNotFoundException(String message) {
    super(message);
  }
}
