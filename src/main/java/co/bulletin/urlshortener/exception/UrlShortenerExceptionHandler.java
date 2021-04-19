package co.bulletin.urlshortener.exception;

import co.bulletin.urlshortener.exception.model.CustomMethodArgumentNotValidException;
import co.bulletin.urlshortener.exception.model.ErrorResponse;
import co.bulletin.urlshortener.exception.model.UrlNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class UrlShortenerExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {

    List<String> errors = new ArrayList<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.add(fieldName + ": " + errorMessage);
    });

    String errorString = String.join(" ", errors);

    CustomMethodArgumentNotValidException customEx = new CustomMethodArgumentNotValidException(
        errorString);

    return ExceptionHandlerUtility.createErrorResponseEntity(HttpStatus.BAD_REQUEST, customEx);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    return ExceptionHandlerUtility.createErrorResponseEntity(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(UrlNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUrlNotFoundException(UrlNotFoundException ex) {
    return ExceptionHandlerUtility.createErrorResponseEntity(HttpStatus.NOT_FOUND, ex);
  }

  @ExceptionHandler(PropertyReferenceException.class)
  public ResponseEntity<ErrorResponse> handlePropertyReferenceException(
      PropertyReferenceException ex) {
    return ExceptionHandlerUtility.createErrorResponseEntity(HttpStatus.BAD_REQUEST, ex);
  }
}
