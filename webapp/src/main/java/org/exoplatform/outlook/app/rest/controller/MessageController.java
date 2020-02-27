package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.outlook.app.rest.dto.AbstractFileResource;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Message controller.
 */
@RestController
@RequestMapping(value = "/v2/mail/message")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class MessageController {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(MessageController.class);

  /**
   * Gets message.
   *
   * @param messageId the message id
   * @return the message
   */
  @RequestMapping(value = "/{MID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getMessage(@PathVariable("MID") String messageId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets message attachments.
   *
   * @param messageId the message id
   * @return the message attachments
   */
  @RequestMapping(value = "/{MID}/attachment", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getMessageAttachments(@PathVariable("MID") String messageId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Post message attachments.
   *
   * @param messageId the message id
   * @return the abstract file resource
   */
  @RequestMapping(value = "/{MID}/attachment", method = RequestMethod.POST, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource postMessageAttachments(@PathVariable("MID") String messageId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets attachment binary content.
   *
   * @param messageId the message id
   * @param attachmentId the attachment id
   * @return the attachment binary content
   */
  @RequestMapping(value = "/{MID}/attachment/{AID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getAttachmentBinaryContent(@PathVariable("MID") String messageId,
                                                         @PathVariable("AID") String attachmentId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Post attachment binary content.
   *
   * @param messageId the message id
   * @param attachmentId the attachment id
   * @return the abstract file resource
   */
  @RequestMapping(value = "/{MID}/attachment/{AID}", method = RequestMethod.POST, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource postAttachmentBinaryContent(@PathVariable("MID") String messageId,
                                                          @PathVariable("AID") String attachmentId) {
    AbstractFileResource resource = null;

    return resource;
  }
}
