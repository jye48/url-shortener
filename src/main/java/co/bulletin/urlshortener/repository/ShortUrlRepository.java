package co.bulletin.urlshortener.repository;

import co.bulletin.urlshortener.entity.ShortUrl;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Integer> {

  Optional<ShortUrl> findByLongUrl(String longUrl);
}
