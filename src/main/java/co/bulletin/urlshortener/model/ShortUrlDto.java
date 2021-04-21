package co.bulletin.urlshortener.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlDto {

  private String shortUrlId;

  private String targetUrl;

  private Instant createdTimestamp;
}
