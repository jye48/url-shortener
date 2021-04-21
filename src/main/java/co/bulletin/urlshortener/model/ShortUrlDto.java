package co.bulletin.urlshortener.model;

import java.time.Instant;
import lombok.Data;

@Data
public class ShortUrlDto {

  private String shortUrlId;

  private String targetUrl;

  private Instant createdTimestamp;
}
