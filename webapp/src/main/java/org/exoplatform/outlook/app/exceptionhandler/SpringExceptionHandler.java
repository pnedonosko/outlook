package org.exoplatform.outlook.app.exceptionhandler;

import org.exoplatform.outlook.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * The type Spring exception handler.
 */
@RestControllerAdvice
public class SpringExceptionHandler {

  /**
   * Handle exception.
   *
   * @param ex the exception
   * @return the message string
   */
  @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionResponse handleException(Exception ex) {
    return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }
}
