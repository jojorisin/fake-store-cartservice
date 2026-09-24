package se.jensen.johanna.fakestorecartservice.exception;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {

  private final ErrorCode errorCode;

  public DomainException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

}
