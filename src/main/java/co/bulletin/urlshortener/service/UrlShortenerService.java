package co.bulletin.urlshortener.service;

import co.bulletin.urlshortener.entity.ShortUrl;
import co.bulletin.urlshortener.exception.model.UrlNotFoundException;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.ShortUrlDto;
import co.bulletin.urlshortener.repository.ShortUrlRepository;
import co.bulletin.urlshortener.utility.TimeUtility;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

  private final ShortUrlRepository shortUrlRepository;

  private final ModelMapper modelMapper;

  public ShortUrlDto createShortUrl(CreateShortUrlRequest createShortUrlRequest) {
    String longUrl = createShortUrlRequest.getLongUrl();

    Optional<ShortUrl> existingShortUrlOptional = shortUrlRepository.findByLongUrl(longUrl);
    if (existingShortUrlOptional.isPresent()) {
      ShortUrl existingShortUrl = existingShortUrlOptional.get();
      return createShortUrlDtoFromShortUrlEntity(existingShortUrl, Instant.now());
    } else {
      ShortUrl newShortUrl = createShortUrlEntityFromRequest(createShortUrlRequest);
      newShortUrl = shortUrlRepository.save(newShortUrl);
      return createShortUrlDtoFromShortUrlEntity(newShortUrl, Instant.now());
    }
  }

  public String findByLongUrlByShortUrlId(Integer shortUrlId) {
    ShortUrl retrievedShortUrl = shortUrlRepository.findById(shortUrlId)
        .orElseThrow(() -> new UrlNotFoundException(shortUrlId));
    return retrievedShortUrl.getLongUrl();
  }

  private ShortUrl createShortUrlEntityFromRequest(CreateShortUrlRequest createShortUrlRequest) {
    return modelMapper.map(createShortUrlRequest, ShortUrl.class);
  }

  private ShortUrlDto createShortUrlDtoFromShortUrlEntity(ShortUrl shortUrl,
      Instant currentInstant) {
    ShortUrlDto shortUrlDto = modelMapper.map(shortUrl, ShortUrlDto.class);
    long millisSinceCreation = TimeUtility
        .getTimeElapsedMillis(shortUrl.getCreatedTimestamp(), currentInstant);
    shortUrlDto.setMillisSinceCreation(millisSinceCreation);
    return shortUrlDto;
  }
}
