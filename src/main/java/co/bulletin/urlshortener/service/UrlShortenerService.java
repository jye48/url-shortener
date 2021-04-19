package co.bulletin.urlshortener.service;

import co.bulletin.urlshortener.entity.Url;
import co.bulletin.urlshortener.exception.model.UrlNotFoundException;
import co.bulletin.urlshortener.mapper.ShortUrlMapper;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.GetShortUrlsResponse;
import co.bulletin.urlshortener.model.UrlDto;
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
    return createUrlDtoFromEntity(url);
  }

  public String findTargetUrlByShortUrl(String shortUrl) {
    int shortUrlId = EncodingUtility.shortUrlToId(shortUrl);
    Url retrievedUrl = shortUrlRepository.findById(shortUrlId)
        .orElseThrow(() -> new UrlNotFoundException(shortUrl));
    return retrievedUrl.getTargetUrl();
  }

  public GetShortUrlsResponse getUrls(Pageable pageable) {
    Page<Url> urlPage = shortUrlRepository.findAll(pageable);
    List<UrlDto> urlDtos = urlPage.getContent().stream()
        .map(this::createUrlDtoFromEntity)
        .collect(Collectors.toList());
    return new GetShortUrlsResponse(urlDtos, urlPage.getTotalElements(), urlPage.getTotalPages());
  }

  private UrlDto createUrlDtoFromEntity(Url url) {
    UrlDto urlDto = shortUrlMapper.mapShortUrlEntityToDto(url);
    String shortUrl = EncodingUtility.convertIdToShortUrl(url.getId());
    urlDto.setShortUrl(shortUrl);
    return urlDto;
  }
}
