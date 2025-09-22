package com.libraryManagement.project.exception;

public class InvalidPaymentTypeException extends RuntimeException {
  public InvalidPaymentTypeException(String message) {
    super(message);
  }
}
