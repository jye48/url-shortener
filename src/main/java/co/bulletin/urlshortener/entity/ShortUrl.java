package co.bulletin.urlshortener.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "short_url")
@Data
public class ShortUrl {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "long_url", unique = true)
  @NotNull
  private String longUrl;

  @Column(name = "created_timestamp")
  @CreationTimestamp
  @NotNull
  private Instant createdTimestamp;
}
