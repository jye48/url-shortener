package co.bulletin.urlshortener.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlDtoWrapper {

  private ShortUrlDto shortUrlDto;

  private HttpStatus httpStatus;
}
