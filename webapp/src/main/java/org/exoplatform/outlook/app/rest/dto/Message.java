package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.exoplatform.outlook.OutlookMessage;
import org.springframework.hateoas.ResourceSupport;

/**
 * The type Message.
 */
public class Message extends ResourceSupport {

  private final OutlookMessage message;

  /**
   * Instantiates a new Message.
   *
   * @param message the message
   */
  public Message(OutlookMessage message) {
    this.message = message;
  }

  /**
   * Gets body.
   *
   * @return the body
   */
  @JsonProperty("messageBody")
  public String getBody() {
    return message.getBody();
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  @JsonProperty("messageBodyContentType")
  public String getType() {
    return message.getType();
  }
}
