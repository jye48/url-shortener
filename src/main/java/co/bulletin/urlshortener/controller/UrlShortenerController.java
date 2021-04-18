package co.bulletin.urlshortener.controller;

import co.bulletin.urlshortener.exception.model.ErrorResponse;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.ShortUrlDto;
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
    description = "Contains endpoints for create and retrieve shortened URLs")
@Slf4j
@RequiredArgsConstructor
public class UrlShortenerController {

  private final UrlShortenerService urlShortenerService;

  @Operation(summary = "Create a new short URL for a given long URL")
  @ApiResponses(
      value = {
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
                  schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Parent organization id was not found",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/short-urls")
  public ResponseEntity<ShortUrlDto> createShortUrl(
      @Valid @RequestBody CreateShortUrlRequest createShortUrlRequest) {
    log.info("Received request to create a short url: {}", createShortUrlRequest);
    ShortUrlDto shortUrlDto = urlShortenerService.createShortUrl(createShortUrlRequest);
    log.info("Successfully created a new short url: {}", shortUrlDto);
    return new ResponseEntity<>(shortUrlDto, HttpStatus.CREATED);
  }

  @Operation(summary = "Redirects the requester to the corresponding long URL for the given short URL ID")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "302",
              description = "Successfully found the long URL that corresponds to the given short URL ID"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid request",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Short URL with the given ID was not found",
              content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)))
      })
  @GetMapping("/short-urls/{shortUrlId}")
  public ResponseEntity<Void> redirectToLongUrl(@PathVariable Integer shortUrlId) {
    log.info("Received request to find and redirect the requester to the long URL that corresponds"
        + "to the short URL with ID {}", shortUrlId);
    String longUrl = urlShortenerService.findByLongUrlByShortUrlId(shortUrlId);
    log.info("Successfully found the corresponding long URL {} for short url with id {}", longUrl,
        shortUrlId);
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(longUrl))
        .build();
  }
}