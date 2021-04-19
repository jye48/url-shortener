package co.bulletin.urlshortener.utility;

public final class EncodingUtility {

  private static final char[] ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
      .toCharArray();
  private static final int R = ALLOWED_CHARS.length;

  private EncodingUtility() {
  }

  public static String convertIdToShortUrl(int id) {
    StringBuilder shortUrlBuilder = new StringBuilder();

    while (id > 0) {
      shortUrlBuilder.append(ALLOWED_CHARS[id % R]);
      id /= R;
    }

    return shortUrlBuilder.reverse().toString();
  }

  public static int shortUrlToId(String shortUrl) {
    int id = 0;

    for (char c : shortUrl.toCharArray()) {
      if ('a' <= c && c <= 'z') {
        id = id * 62 + c - 'a';
      } else if ('A' <= c && c <= 'Z') {
        id = id * 62 + c - 'A' + 26;
      } else if ('0' <= c && c <= '9') {
        id = id * 62 + c - '0' + 52;
      }
    }
    return id;
  }
}
