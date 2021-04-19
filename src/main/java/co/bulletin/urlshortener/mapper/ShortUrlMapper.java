package co.bulletin.urlshortener.mapper;

import co.bulletin.urlshortener.entity.Url;
import co.bulletin.urlshortener.model.CreateShortUrlRequest;
import co.bulletin.urlshortener.model.UrlDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShortUrlMapper {

  private final ModelMapper modelMapper;

  public Url mapCreateShortUrlRequestToEntity(CreateShortUrlRequest createShortUrlRequest) {
    return modelMapper.map(createShortUrlRequest, Url.class);
  }

  public UrlDto mapShortUrlEntityToDto(Url url) {
    return modelMapper.map(url, UrlDto.class);
  }
}
