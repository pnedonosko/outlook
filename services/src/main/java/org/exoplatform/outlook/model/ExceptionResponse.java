package org.exoplatform.outlook.model;

/**
 * The type Exception response.
 */
public class ExceptionResponse {

  /**
   * The Code.
   */
  protected int    code;

  /**
   * The Message.
   */
  protected String message;

  /**
   * Instantiates a new Exception response.
   *
   * @param code the code
   * @param message the message
   */
  public ExceptionResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * Gets code.
   *
   * @return the code
   */
  public int getCode() {
    return code;
  }

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }
}
