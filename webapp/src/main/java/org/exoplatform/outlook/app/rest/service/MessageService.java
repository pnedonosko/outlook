package org.exoplatform.outlook.app.rest.service;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.*;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Calendar;

/**
 * The type Message service.
 */
@Service
public class MessageService {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(MessageService.class);

  /**
   * Build message.
   *
   * @param user the user
   * @param messageId the message id
   * @param fromEmail the from email
   * @param fromName the from name
   * @param created the created
   * @param modified the modified
   * @param title the title
   * @param subject the subject
   * @param body the body
   * @return the outlook message
   * @throws OutlookException the outlook exception
   * @throws ParseException the parse exception
   */
  public OutlookMessage buildMessage(OutlookUser user,
                                     String messageId,
                                     String fromEmail,
                                     String fromName,
                                     String created,
                                     String modified,
                                     String title,
                                     String subject,
                                     String body) throws OutlookException, ParseException {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
    OutlookEmail from = outlook.getAddress(fromEmail, fromName);
    Calendar createdDate = Calendar.getInstance();
    createdDate.setTime(OutlookMessage.DATE_FORMAT.parse(created));
    Calendar modifiedDate = Calendar.getInstance();
    modifiedDate.setTime(OutlookMessage.DATE_FORMAT.parse(modified));
    OutlookMessage message = outlook.buildMessage(messageId, user, from, null, createdDate, modifiedDate, title, subject, body);
    return message;
  }
}
