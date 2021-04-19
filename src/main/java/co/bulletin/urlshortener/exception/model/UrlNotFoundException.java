package co.bulletin.urlshortener.exception.model;

public class UrlNotFoundException extends RuntimeException {

  public UrlNotFoundException(String shortUrl) {
    super("short url " + shortUrl + " not found");
  }
}
