package co.bulletin.urlshortener.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {

  private String shortUrl;

  private String targetUrl;

  private Instant createdTimestamp;
}
