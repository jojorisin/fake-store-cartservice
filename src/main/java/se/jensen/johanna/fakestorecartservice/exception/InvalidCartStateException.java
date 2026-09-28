package se.jensen.johanna.fakestorecartservice.exception;

public class InvalidCartStateException extends DomainException {

  public InvalidCartStateException(String message) {
    super(message, ErrorCode.INVALID_CART_STATE);
  }
}
