package se.jensen.johanna.fakestorecartservice.exception;

public class IllegalCartStateException extends DomainException {

  public IllegalCartStateException(String message) {
    super(message, ErrorCode.ILLEGAL_CART_STATE);
  }
}
