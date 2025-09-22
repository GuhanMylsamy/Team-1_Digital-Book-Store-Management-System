package com.libraryManagement.project.exception;

public class InvalidPaymentTypePinException extends RuntimeException {
  public InvalidPaymentTypePinException(String message) {
    super(message);
  }
}
