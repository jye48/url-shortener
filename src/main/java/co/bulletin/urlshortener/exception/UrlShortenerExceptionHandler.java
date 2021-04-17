package co.bulletin.urlshortener.exception;

import co.bulletin.urlshortener.exception.model.UrlNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UrlShortenerExceptionHandler {

  @ExceptionHandler(UrlNotFoundException.class)
  public ResponseEntity<Object> handleUrlNotFoundException(UrlNotFoundException ex) {
    return ExceptionHandlerUtility.createErrorResponseEntity(HttpStatus.NOT_FOUND, ex);
  }
}
