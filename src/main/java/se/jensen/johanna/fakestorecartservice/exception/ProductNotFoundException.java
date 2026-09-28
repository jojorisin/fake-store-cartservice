package se.jensen.johanna.fakestorecartservice.exception;

public class ProductNotFoundException extends DomainException {

  public ProductNotFoundException(String message) {
    super(message, ErrorCode.PRODUCT_NOT_FOUND);
  }
}
