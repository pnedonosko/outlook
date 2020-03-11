package org.exoplatform.outlook.app.exceptionhandler;

import org.exoplatform.outlook.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

/**
 * The type Missing servlet request parameter exception handler.
 */
@RestControllerAdvice
public class MissingServletRequestParameterExceptionHandler {

  /**
   * Handle exception.
   *
   * @param ex the exception
   * @return the message string
   */
  @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleException(MissingServletRequestParameterException ex) {
    return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
  }
}
