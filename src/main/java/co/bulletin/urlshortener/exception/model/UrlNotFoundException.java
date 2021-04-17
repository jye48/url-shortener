package co.bulletin.urlshortener.exception.model;

public class UrlNotFoundException extends RuntimeException {

  public UrlNotFoundException(Integer id) {
    super("url with id " + id + " not found");
  }
}
