package co.bulletin.urlshortener.model;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShortUrlRequest {

  @NotBlank
  private String longUrl;
}
