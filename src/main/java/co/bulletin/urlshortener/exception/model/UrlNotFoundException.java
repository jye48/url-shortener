package co.bulletin.urlshortener.exception.model;

public class UrlNotFoundException extends RuntimeException {

  public UrlNotFoundException(String shortUrlId) {
    super("short url with id " + shortUrlId + " not found");
  }
}
