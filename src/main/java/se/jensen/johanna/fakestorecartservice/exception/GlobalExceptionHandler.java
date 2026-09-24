package se.jensen.johanna.fakestorecartservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.jensen.johanna.fakestorecartservice.dto.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> handleDomainException(DomainException e,
      HttpServletRequest request) {
    log.error("Domain exception. path: {}", request.getRequestURI(), e);
    HttpStatus status = getHttpStatus(e.getErrorCode());
    return ResponseEntity.status(status).body(
        new ErrorResponse(Instant.now(), status.value(), e.getErrorCode(), e.getMessage(), null)
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
    log.error("unexpected exception. path:{}", request.getRequestURI(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ErrorCode.INTERNAL_SERVER_ERROR, "unable to process request", null)
    );
  }

  private HttpStatus getHttpStatus(ErrorCode errorCode) {
    return switch (errorCode) {
      case ILLEGAL_CART_STATE -> HttpStatus.BAD_REQUEST;
      case PRODUCT_NOT_FOUND -> HttpStatus.NOT_FOUND;
      case INTERNAL_CLIENT_ERROR, INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }

}
