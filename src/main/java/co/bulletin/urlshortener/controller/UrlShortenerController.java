package co.bulletin.urlshortener.controller;

import co.bulletin.urlshortener.exception.model.ErrorResponse;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.GetShortUrlsResponse;
import co.bulletin.urlshortener.model.ShortUrlDto;
import co.bulletin.urlshortener.model.ShortUrlDtoWrapper;
import co.bulletin.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
    name = "Url Shortener Controller",
    description = "Contains endpoints to create and retrieve shortened URLs")
@Slf4j
@RequiredArgsConstructor
public class UrlShortenerController {

  private final UrlShortenerService urlShortenerService;

  @Operation(summary = "Create a new short URL for a given target URL")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved the existing short URL for the given target URL",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ShortUrlDto.class))),
          @ApiResponse(
              responseCode = "201",
              description = "Successfully created a new short URL",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ShortUrlDto.class))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid request",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/short-urls")
  public ResponseEntity<ShortUrlDto> createShortUrl(
      @Valid @RequestBody CreateShortUrlRequest createShortUrlRequest) {
    log.info("Received request to create a short url: {}", createShortUrlRequest);

    ShortUrlDtoWrapper shortUrlDtoWrapper = urlShortenerService
        .createShortUrl(createShortUrlRequest);
    ShortUrlDto shortUrlDto = shortUrlDtoWrapper.getShortUrlDto();
    boolean isNew = shortUrlDtoWrapper.isNewShortUrl();

    if (isNew) {
      log.info("Successfully created a new short url: {}", shortUrlDto);
      return new ResponseEntity<>(shortUrlDto, HttpStatus.CREATED);
    }
    log.info("Successfully retrieved existing short url: {}", shortUrlDto);
    return new ResponseEntity<>(shortUrlDto, HttpStatus.OK);
  }

  @Operation(summary = "This route WILL NOT redirect to the target URL if called through Swagger "
      + "UI. Redirects the requester to the corresponding target URL for the given short URL ID")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "301",
              description = "Successfully found the target URL that corresponds to the given short "
                  + "URL ID and set the target URL as the Location header value in the response"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid request",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Short URL was not found",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)))
      })
  @GetMapping("/short-urls/{shortUrlId}")
  public ResponseEntity<Void> redirectToTargetUrl(@PathVariable String shortUrlId) {
    log.info("Received request to find and redirect the requester to the target URL that "
        + "corresponds to the short URL with ID {}", shortUrlId);
    String targetUrl = urlShortenerService.findTargetUrlByShortUrlId(shortUrlId);
    log.info("Successfully found short url with id {} with target URL: {}", shortUrlId, targetUrl);
    return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
        .location(URI.create(targetUrl))
        .build();
  }

  @Operation(summary = "Returns a paged list of all existing shortened URLs")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved page of existing short URLs",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = GetShortUrlsResponse.class)))
      })
  @GetMapping("/short-urls")
  public GetShortUrlsResponse getExistingShortenedUrls(@ParameterObject Pageable pageable) {
    log.info("Received request to get existing shortened URLs with pageable {}", pageable);
    GetShortUrlsResponse getShortUrlsResponse = urlShortenerService.getShortUrls(pageable);
    log.info("Successfully retrieved existing shortened URLs: {}", getShortUrlsResponse);
    return getShortUrlsResponse;
  }
}
