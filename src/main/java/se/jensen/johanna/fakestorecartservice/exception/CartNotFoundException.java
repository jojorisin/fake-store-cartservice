package se.jensen.johanna.fakestorecartservice.exception;

public class CartNotFoundException extends DomainException {

  public CartNotFoundException(String message) {
    super(message, ErrorCode.CART_NOT_FOUND);
  }
}
