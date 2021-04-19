package co.bulletin.urlshortener.repository;

import co.bulletin.urlshortener.entity.Url;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<Url, Integer> {

  Optional<Url> findByTargetUrl(String targetUrl);
}
