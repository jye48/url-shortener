package co.bulletin.urlshortener.service;

import co.bulletin.urlshortener.entity.ShortUrl;
import co.bulletin.urlshortener.mapper.ShortUrlMapper;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.ShortUrlDto;
import co.bulletin.urlshortener.model.ShortUrlDtoWrapper;
import co.bulletin.urlshortener.repository.ShortUrlRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ShortUrlCreationService {

  private final ShortUrlRepository shortUrlRepository;

  private final ShortUrlMapper shortUrlMapper;

  public ShortUrlDtoWrapper createShortUrl(CreateShortUrlRequest createShortUrlRequest) {
    log.info("Received request to create a short url: {}", createShortUrlRequest);
    String targetUrl = createShortUrlRequest.getTargetUrl();
    Optional<ShortUrl> existingShortUrlOptional = shortUrlRepository.findByTargetUrl(targetUrl);
    ShortUrl shortUrl;
    boolean isNewShortUrl = false;

    if (existingShortUrlOptional.isPresent()) {
      shortUrl = existingShortUrlOptional.get();
      log.info("Found existing short URL ID for target url {}", targetUrl);
    } else {
      ShortUrl newShortUrl = shortUrlMapper.mapCreateShortUrlRequestToEntity(createShortUrlRequest);
      shortUrl = shortUrlRepository.save(newShortUrl);
      isNewShortUrl = true;
      log.info("Created a new short URL for target URL {}", targetUrl);
    }

    ShortUrlDto shortUrlDto = shortUrlMapper.mapShortUrlEntityToDto(shortUrl);

    return createShortUrlDtoWrapper(shortUrlDto, isNewShortUrl);
  }

  private ShortUrlDtoWrapper createShortUrlDtoWrapper(ShortUrlDto shortUrlDto,
      boolean isNewShortUrl) {

    if (isNewShortUrl) {
      return new ShortUrlDtoWrapper(shortUrlDto, HttpStatus.CREATED);
    }
    return new ShortUrlDtoWrapper(shortUrlDto, HttpStatus.OK);
  }
}
