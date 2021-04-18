package co.bulletin.urlshortener.exception.model;

public class CustomMethodArgumentNotValidException extends RuntimeException {

  public CustomMethodArgumentNotValidException(String message) {
    super(message);
  }
}
