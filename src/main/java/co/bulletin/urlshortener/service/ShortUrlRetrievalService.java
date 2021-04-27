package co.bulletin.urlshortener.service;

import co.bulletin.urlshortener.entity.ShortUrl;
import co.bulletin.urlshortener.exception.model.UrlNotFoundException;
import co.bulletin.urlshortener.mapper.ShortUrlMapper;
import co.bulletin.urlshortener.model.GetShortUrlsResponse;
import co.bulletin.urlshortener.model.ShortUrlDto;
import co.bulletin.urlshortener.repository.ShortUrlRepository;
import co.bulletin.urlshortener.utility.EncodingUtility;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ShortUrlRetrievalService {

  private final ShortUrlRepository shortUrlRepository;

  private final ShortUrlMapper shortUrlMapper;

  public String findTargetUrlByShortUrlId(String shortUrlId) {
    log.info("Received request to find target URL that corresponds to the short URL with ID {}",
        shortUrlId);
    int shortUrlEntityId = EncodingUtility.base62Decode(shortUrlId);

    ShortUrl retrievedShortUrl = shortUrlRepository.findById(shortUrlEntityId)
        .orElseThrow(() -> new UrlNotFoundException(shortUrlId));
    String targetUrl = retrievedShortUrl.getTargetUrl();

    log.info("Successfully found short url with id {} with target URL: {}", shortUrlId, targetUrl);
    return targetUrl;
  }

  public GetShortUrlsResponse getShortUrls(Pageable pageable) {
    log.info("Received request to get existing shortened URLs with pageable {}", pageable);
    Page<ShortUrl> shortUrlPage = shortUrlRepository.findAll(pageable);

    List<ShortUrlDto> shortUrlDtos = shortUrlPage.getContent().stream()
        .map(shortUrlMapper::mapShortUrlEntityToDto)
        .collect(Collectors.toList());

    GetShortUrlsResponse getShortUrlsResponse = new GetShortUrlsResponse(shortUrlDtos,
        shortUrlPage.getTotalElements(), shortUrlPage.getTotalPages());
    log.info("Retrieved existing shortened URLs: {}", getShortUrlsResponse);
    return getShortUrlsResponse;
  }
}
