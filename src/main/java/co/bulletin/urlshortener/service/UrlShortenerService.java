package co.bulletin.urlshortener.service;

import co.bulletin.urlshortener.entity.ShortUrl;
import co.bulletin.urlshortener.exception.model.UrlNotFoundException;
import co.bulletin.urlshortener.mapper.ShortUrlMapper;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.GetShortUrlsResponse;
import co.bulletin.urlshortener.model.ShortUrlDto;
import co.bulletin.urlshortener.repository.ShortUrlRepository;
import co.bulletin.urlshortener.utility.EncodingUtility;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

  private final ShortUrlRepository shortUrlRepository;

  private final ShortUrlMapper shortUrlMapper;

  public ShortUrlDto createShortUrl(CreateShortUrlRequest createShortUrlRequest) {
    String targetUrl = createShortUrlRequest.getTargetUrl();
    Optional<ShortUrl> existingShortUrlOptional = shortUrlRepository.findByTargetUrl(targetUrl);
    ShortUrl shortUrl;

    if (existingShortUrlOptional.isPresent()) {
      shortUrl = existingShortUrlOptional.get();
    } else {
      ShortUrl newShortUrl = shortUrlMapper.mapCreateShortUrlRequestToEntity(createShortUrlRequest);
      shortUrl = shortUrlRepository.save(newShortUrl);
    }

    return createShortUrlDtoFromEntity(shortUrl);
  }

  public String findTargetUrlByShortUrlId(String shortUrlId) {
    int shortUrlEntityId = EncodingUtility.base62Decode(shortUrlId);

    ShortUrl retrievedShortUrl = shortUrlRepository.findById(shortUrlEntityId)
        .orElseThrow(() -> new UrlNotFoundException(shortUrlId));

    return retrievedShortUrl.getTargetUrl();
  }

  public GetShortUrlsResponse getShortUrls(Pageable pageable) {
    Page<ShortUrl> shortUrlPage = shortUrlRepository.findAll(pageable);

    List<ShortUrlDto> shortUrlDtos = shortUrlPage.getContent().stream()
        .map(this::createShortUrlDtoFromEntity)
        .collect(Collectors.toList());

    return new GetShortUrlsResponse(shortUrlDtos, shortUrlPage.getTotalElements(),
        shortUrlPage.getTotalPages());
  }

  private ShortUrlDto createShortUrlDtoFromEntity(ShortUrl shortUrl) {
    ShortUrlDto shortUrlDto = shortUrlMapper.mapShortUrlEntityToDto(shortUrl);

    String shortUrlId = EncodingUtility.base62Encode(shortUrl.getId());
    shortUrlDto.setShortUrlId(shortUrlId);

    return shortUrlDto;
  }
}
