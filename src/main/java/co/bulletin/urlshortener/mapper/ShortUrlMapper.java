package co.bulletin.urlshortener.mapper;

import co.bulletin.urlshortener.entity.ShortUrl;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.ShortUrlDto;
import co.bulletin.urlshortener.utility.EncodingUtility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShortUrlMapper {

  private final ModelMapper modelMapper;

  public ShortUrl mapCreateShortUrlRequestToEntity(CreateShortUrlRequest createShortUrlRequest) {
    return modelMapper.map(createShortUrlRequest, ShortUrl.class);
  }

  public ShortUrlDto mapShortUrlEntityToDto(ShortUrl shortUrl) {
    ShortUrlDto shortUrlDto = modelMapper.map(shortUrl, ShortUrlDto.class);

    String shortUrlId = EncodingUtility.base62Encode(shortUrl.getId());
    shortUrlDto.setShortUrlId(shortUrlId);

    return shortUrlDto;
  }
}
