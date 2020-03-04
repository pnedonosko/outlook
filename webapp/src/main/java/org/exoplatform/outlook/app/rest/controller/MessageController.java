package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.BadParameterException;
import org.exoplatform.outlook.OutlookMessage;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.OutlookUser;
import org.exoplatform.outlook.app.rest.dto.AbstractFileResource;
import org.exoplatform.outlook.app.rest.dto.Message;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The type Message controller.
 */
@RestController
@RequestMapping(value = "/v2/mail/message")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class MessageController extends AbstractController {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(MessageController.class);

  /**
   * Gets root.
   *
   * @return the root
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getRoot() {
    ResourceSupport resource = new ResourceSupport();

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(RootDiscoveryMailServiceController.class).getRootDiscoveryOfOutlookMailServices()).withRel("parent"));
    links.add(linkTo(methodOn(MessageController.class).getRoot()).withSelfRel());
    links.add(linkTo(methodOn(MessageController.class).getMessage(OutlookConstant.MESSAGE_ID,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null)).withRel("message"));
    links.add(linkTo(methodOn(MessageController.class).getMessageAttachments(OutlookConstant.MESSAGE_ID)).withRel("attachments"));

    resource.add(links);

    return resource;
  }

  /**
   * Gets message.
   *
   * @param messageId the message id
   * @return the message
   */
  @RequestMapping(value = "/{MID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getMessage(@PathVariable("MID") String messageId,
                                    @RequestParam String ewsUrl,
                                    @RequestParam String userEmail,
                                    @RequestParam String userName,
                                    @RequestParam String messageToken) {
    if (ewsUrl != null && userEmail != null && messageId != null && messageToken != null) {

      ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
      OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);

      try {
        OutlookUser user = outlook.getUser(userEmail, userName, ewsUrl);
        OutlookMessage message = outlook.getMessage(user, messageId, messageToken);
        Message messageDTO = new Message(message);

        List<Link> links = new LinkedList<>();
        links.add(linkTo(methodOn(MessageController.class).getRoot()).withRel("parent"));
        links.add(linkTo(methodOn(MessageController.class).getMessage(OutlookConstant.MESSAGE_ID,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null)).withSelfRel());
        messageDTO.add(links);
        return messageDTO;
      } catch (BadParameterException e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Error reading message " + messageId + ". " + e.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error reading message " + messageId);
      } catch (Throwable e) {
        LOG.error("Error reading message " + messageId, e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading message " + messageId);
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Null or zero-length message ID or user parameters to read message");
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message ID and user parameters required");
    }
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
