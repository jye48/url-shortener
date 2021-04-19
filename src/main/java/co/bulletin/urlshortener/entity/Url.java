package co.bulletin.urlshortener.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "short_url", indexes = @Index(columnList = "target_url"))
@EntityListeners(AuditingEntityListener.class)
@Data
public class Url {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "target_url", unique = true)
  @NotNull
  private String targetUrl;

  @Column(name = "created_timestamp")
  @CreatedDate
  @NotNull
  private Instant createdTimestamp;
}
