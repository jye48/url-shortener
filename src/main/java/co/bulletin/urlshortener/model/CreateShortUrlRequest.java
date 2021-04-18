package co.bulletin.urlshortener.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShortUrlRequest {

  @NotBlank
  @URL
  @Size(max = 2048)
  private String targetUrl;
}
