package co.bulletin.urlshortener.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlDtoWrapper {

  private ShortUrlDto shortUrlDto;

  private boolean isNewShortUrl;
}
