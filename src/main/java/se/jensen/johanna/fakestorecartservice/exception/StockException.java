package se.jensen.johanna.fakestorecartservice.exception;

public class StockException extends DomainException {

  public StockException(String message) {
    super(message, ErrorCode.LOW_STOCK);
  }
}
