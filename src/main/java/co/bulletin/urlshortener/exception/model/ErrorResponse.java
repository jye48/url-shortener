package co.bulletin.urlshortener.exception.model;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {

  private HttpStatus status;
  private String errorMessage;
  private String origin;
  private String timestamp;

  public ErrorResponse(HttpStatus status, Exception ex) {
    this.status = status;
    this.errorMessage = ex.getMessage();
    origin = ex.getClass().getSimpleName();
    timestamp = LocalDateTime.now().toString();
  }
}
