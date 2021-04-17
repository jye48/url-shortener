package co.bulletin.urlshortener.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlDto {

  private Integer id;

  private String longUrl;

  private Long millisSinceCreation;

  private Instant createdTimestamp;
}
