package co.bulletin.urlshortener.service;

import co.bulletin.urlshortener.entity.ShortUrl;
import co.bulletin.urlshortener.exception.model.UrlNotFoundException;
import co.bulletin.urlshortener.mapper.ShortUrlMapper;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.ShortUrlDto;
import co.bulletin.urlshortener.repository.ShortUrlRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

  private final ShortUrlRepository shortUrlRepository;

  private final ShortUrlMapper shortUrlMapper;

  public ShortUrlDto createShortUrl(CreateShortUrlRequest createShortUrlRequest) {
    String targetUrl = createShortUrlRequest.getTargetUrl();

    Optional<ShortUrl> existingShortUrlOptional = shortUrlRepository.findByTargetUrl(targetUrl);
    if (existingShortUrlOptional.isPresent()) {
      ShortUrl existingShortUrl = existingShortUrlOptional.get();
      return shortUrlMapper.mapShortUrlEntityToDto(existingShortUrl);
    } else {
      ShortUrl newShortUrl = shortUrlMapper.mapCreateShortUrlRequestToEntity(createShortUrlRequest);
      newShortUrl = shortUrlRepository.save(newShortUrl);
      return shortUrlMapper.mapShortUrlEntityToDto(newShortUrl);
    }
  }

  public String findByTargetUrlByShortUrlId(Integer shortUrlId) {
    ShortUrl retrievedShortUrl = shortUrlRepository.findById(shortUrlId)
        .orElseThrow(() -> new UrlNotFoundException(shortUrlId));
    return retrievedShortUrl.getTargetUrl();
  }
}
