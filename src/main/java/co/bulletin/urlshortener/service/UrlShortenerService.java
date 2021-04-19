package co.bulletin.urlshortener.service;

import co.bulletin.urlshortener.entity.Url;
import co.bulletin.urlshortener.exception.model.UrlNotFoundException;
import co.bulletin.urlshortener.mapper.ShortUrlMapper;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.UrlDto;
import co.bulletin.urlshortener.repository.ShortUrlRepository;
import co.bulletin.urlshortener.utility.EncodingUtility;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

  private final ShortUrlRepository shortUrlRepository;

  private final ShortUrlMapper shortUrlMapper;

  public UrlDto createShortUrl(CreateShortUrlRequest createShortUrlRequest) {
    String targetUrl = createShortUrlRequest.getTargetUrl();
    Optional<Url> existingShortUrlOptional = shortUrlRepository.findByTargetUrl(targetUrl);
    Url url;

    if (existingShortUrlOptional.isPresent()) {
      url = existingShortUrlOptional.get();
    } else {
      Url newUrl = shortUrlMapper.mapCreateShortUrlRequestToEntity(createShortUrlRequest);
      url = shortUrlRepository.save(newUrl);
    }
    return createShortUrlDtoFromEntity(url);
  }

  public String findTargetUrlByShortUrl(String shortUrl) {
    int shortUrlId = EncodingUtility.shortUrlToId(shortUrl);
    Url retrievedUrl = shortUrlRepository.findById(shortUrlId)
        .orElseThrow(() -> new UrlNotFoundException(shortUrl));
    return retrievedUrl.getTargetUrl();
  }

  private UrlDto createShortUrlDtoFromEntity(Url url) {
    UrlDto urlDto = shortUrlMapper.mapShortUrlEntityToDto(url);
    String shortUrl = EncodingUtility.convertIdToShortUrl(url.getId());
    urlDto.setShortUrl(shortUrl);
    return urlDto;
  }
}
