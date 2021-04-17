package co.bulletin.urlshortener.exception;

import co.bulletin.urlshortener.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public final class ExceptionHandlerUtility {

  private ExceptionHandlerUtility() {
  }

  public static ResponseEntity<Object> createErrorResponseEntity(HttpStatus httpStatus,
      Exception ex) {
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorResponse errorResponse = new ErrorResponse(httpStatus, ex);
    return new ResponseEntity<>(errorResponse, httpStatus);
  }
}
