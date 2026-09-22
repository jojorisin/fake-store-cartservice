package se.jensen.johanna.fakestorecartservice.exception;

public class InternalClientException extends InfrastructureException {

  public InternalClientException(String message, Throwable cause) {
    super(message, ErrorCode.INTERNAL_CLIENT_ERROR, cause);
  }

  public InternalClientException(String message){
    super(message,ErrorCode.INTERNAL_CLIENT_ERROR);
  }
}
