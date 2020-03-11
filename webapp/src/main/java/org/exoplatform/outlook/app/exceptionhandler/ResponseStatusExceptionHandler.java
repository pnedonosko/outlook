package org.exoplatform.outlook.app.exceptionhandler;

import org.exoplatform.outlook.model.ExceptionResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * The type Response status exception handler.
 */
@RestControllerAdvice
public class ResponseStatusExceptionHandler {

  /**
   * Handle exception.
   *
   * @param ex the exception
   * @return the message string
   */
  @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ExceptionResponse> handleException(ResponseStatusException ex) {
    return new ResponseEntity(new ExceptionResponse(ex.getStatus().value(), ex.getMessage()), ex.getStatus());
  }
}
