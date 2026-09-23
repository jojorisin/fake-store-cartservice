package se.jensen.johanna.fakestorecartservice.exception;

public abstract class InfrastructureException extends RuntimeException {

  private final ErrorCode errorCode;

  public InfrastructureException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public InfrastructureException(String message, ErrorCode errorCode,
      Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

}
