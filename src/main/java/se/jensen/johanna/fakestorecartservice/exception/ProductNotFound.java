package se.jensen.johanna.fakestorecartservice.exception;

public class ProductNotFound extends DomainException {

  public ProductNotFound(String message) {
    super(message, ErrorCode.PRODUCT_NOT_FOUND);
  }
}
