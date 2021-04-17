package co.bulletin.urlshortener.utility;

import java.time.Duration;
import java.time.Instant;

public final class TimeUtility {

  private TimeUtility() {
  }

  public static long getTimeElapsedMillis(Instant start, Instant end) {
    return Duration.between(start, end).toMillis();
  }
}
