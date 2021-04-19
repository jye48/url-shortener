package co.bulletin.urlshortener.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetShortUrlsResponse {

  private List<UrlDto> urls;

  private Long totalElementCount;

  private Integer totalPageCount;
}
