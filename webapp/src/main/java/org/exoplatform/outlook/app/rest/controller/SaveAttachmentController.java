package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.*;
import org.exoplatform.outlook.jcr.File;
import org.exoplatform.outlook.jcr.Folder;
import org.exoplatform.outlook.app.rest.resource.ParametersListResourceSupportWrapper;
import org.exoplatform.outlook.app.rest.dto.Space;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.*;

/**
 * The Attachment controller.
 */
@Deprecated
@RestController
@RequestMapping(value = "/v1")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class SaveAttachmentController {

  /** The Constant LOG. */
  private static final Log    LOG             = ExoLogger.getLogger(SaveAttachmentController.class);

  /** The Constant HAL_AND_JSON. */
  private static final String HAL_AND_JSON    = "application/hal+json";

  /**
   * The constant SAVE_ATTACHMENT.
   */
  public static final String  SAVE_ATTACHMENT = "/save-attachment";

  /**
   * The constant FOLDERS.
   */
  public static final String  FOLDERS         = "/folders";

  /**
   * The constant ADD_FOLDER.
   */
  public static final String  ADD_FOLDER      = "/add-folder";

  private Map<String, String> saveAttachmentPassesConstants;

  public SaveAttachmentController() {
    this.saveAttachmentPassesConstants = new HashMap<>();
    this.saveAttachmentPassesConstants.put("saveAttachment", SAVE_ATTACHMENT);
    this.saveAttachmentPassesConstants.put("getSaveAttachmentFormUserSpaces", SAVE_ATTACHMENT);
    this.saveAttachmentPassesConstants.put("folders", FOLDERS);
    this.saveAttachmentPassesConstants.put("addFolder", ADD_FOLDER);
  }

  /**
   * Save attachment.
   *
   * @param groupId the group id
   * @param path the path
   * @param comment the comment
   * @param ewsUrl the ews url
   * @param userEmail the user email
   * @param userName the user name
   * @param messageId the message id
   * @param attachmentToken the attachment token
   * @param attachmentIds the attachment ids
   * @return the parameters list resource support wrapper
   */
  @RequestMapping(value = SAVE_ATTACHMENT, method = RequestMethod.POST, produces = HAL_AND_JSON)
  public ParametersListResourceSupportWrapper saveAttachment(@RequestParam("groupId") String groupId,
                                                             @RequestParam("path") String path,
                                                             @RequestParam("comment") String comment,
                                                             @RequestParam("ewsUrl") String ewsUrl,
                                                             @RequestParam("userEmail") String userEmail,
                                                             @RequestParam("userName") String userName,
                                                             @RequestParam("messageId") String messageId,
                                                             @RequestParam("attachmentToken") String attachmentToken,
                                                             @RequestParam("attachmentIds") String attachmentIds) {

    if (groupId != null && path != null && ewsUrl != null && userEmail != null && messageId != null && attachmentToken != null
        && attachmentIds != null) {
      try {
        ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
        OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);

        OutlookSpace space = outlook.getSpace(groupId);
        if (space != null) {
          // Remove empty attachments in the array
          List<String> attachments = new ArrayList<String>();
          for (String aid : attachmentIds.split(",")) {
            aid = aid.trim();
            if (aid.length() > 0) {
              attachments.add(aid);
            }
          }
          if (attachments.size() > 0) {
            Folder folder = space.getFolder(path);
            OutlookUser user = outlook.getUser(userEmail, userName, ewsUrl);
            List<File> files = outlook.saveAttachment(space,
                                                      folder,
                                                      user,
                                                      comment,
                                                      messageId,
                                                      attachmentToken,
                                                      attachments.toArray(new String[attachments.size()]));

            ParametersListResourceSupportWrapper parametersListResourceSupportWrapper =
                                                                                      new ParametersListResourceSupportWrapper();
            parametersListResourceSupportWrapper.setName("saveAttachment");
            parametersListResourceSupportWrapper.addParameter("files", files);

            parametersListResourceSupportWrapper.add(generateLinks("saveAttachment",
                                                                   SaveAttachmentController.class,
                                                                   saveAttachmentPassesConstants));

            return parametersListResourceSupportWrapper;
          } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment IDs (attachmentIds) are empty");
          }
        } else {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Error saving attachment: space not found " + groupId + ". OutlookUser " + userEmail);
          }
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error saving attachment: space not found " + groupId);
        }
      } catch (Throwable e) {
        LOG.error("Error saving attachment in the space " + groupId + ": " + e.getMessage(), e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                          "Error saving attachment in the space. Please contact your administrator.");
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Error in saving attachment request: spaceName='" + groupId + "' path=" + path + " ewsUrl='" + ewsUrl
            + "' userEmail='" + userEmail + "' messageId='" + messageId + "' attachmentToken(size)='"
            + (attachmentToken != null ? attachmentToken.length() : "null") + "' attachmentIds='" + attachmentIds + "'");
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in saving attachment request. Please reload the page.");
    }
  }

  /**
   * Gets save attachment form user spaces.
   *
   * @return the parameters list resource support wrapper
   */
  @RequestMapping(value = SAVE_ATTACHMENT, method = RequestMethod.GET, produces = HAL_AND_JSON)
  public ParametersListResourceSupportWrapper getSaveAttachmentFormUserSpaces() {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
    try {
      ParametersListResourceSupportWrapper parametersListResourceSupportWrapper = new ParametersListResourceSupportWrapper();
      parametersListResourceSupportWrapper.setName("getSaveAttachmentFormUserSpaces");

      List<Space> spaces = new LinkedList<>();
      outlook.getUserSpaces().forEach(space -> spaces.add(new Space(space)));
      parametersListResourceSupportWrapper.addParameter("spaces", spaces);

      parametersListResourceSupportWrapper.add(generateLinks("getSaveAttachmentFormUserSpaces",
                                                             SaveAttachmentController.class,
                                                             saveAttachmentPassesConstants));
      /*
       * Link selfLink = linkTo(methodOn(SaveAttachmentController.class).
       * getSaveAttachmentFormUserSpaces()).withSelfRel();
       * parametersListResourceSupportWrapper.add(selfLink); Link saveAttachmentLink =
       * linkTo(methodOn(SaveAttachmentController.class)).slash(SAVE_ATTACHMENT).
       * withRel("saveAttachment");
       * parametersListResourceSupportWrapper.add(saveAttachmentLink);
       */

      return parametersListResourceSupportWrapper;
    } catch (AccessException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    } catch (Exception e) {
      LOG.error("Error rendering save attachments form", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error rendering save attachments form");
    } catch (Throwable e) {
      LOG.error("Error showing save attachments form", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error showing save attachments form");
    }
  }

  /**
   * Folders parameters list resource support wrapper.
   *
   * @param groupId the group id
   * @param path the path
   * @return the parameters list resource support wrapper
   */
  @RequestMapping(value = FOLDERS, method = RequestMethod.GET, produces = HAL_AND_JSON)
  public ParametersListResourceSupportWrapper folders(String groupId, String path) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
    try {
      ParametersListResourceSupportWrapper parametersListResourceSupportWrapper = new ParametersListResourceSupportWrapper();
      parametersListResourceSupportWrapper.setName("folders");
      parametersListResourceSupportWrapper.addParameter("folders", outlook.getSpace(groupId).getFolder(path));

      parametersListResourceSupportWrapper.add(generateLinks("folders",
                                                             SaveAttachmentController.class,
                                                             saveAttachmentPassesConstants));

      return parametersListResourceSupportWrapper;
    } catch (BadParameterException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Error reading folder " + path + ". " + e.getMessage());
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error reading folder " + path);
    } catch (Throwable e) {
      LOG.error("Error reading folder " + path, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading folder " + path);
    }
  }

  /**
   * Add folder.
   *
   * @param groupId the group id
   * @param path the path
   * @param name the name
   * @return the parameters list resource support wrapper
   */
  @RequestMapping(value = ADD_FOLDER, method = RequestMethod.POST, produces = HAL_AND_JSON)
  public ParametersListResourceSupportWrapper addFolder(String groupId, String path, String name) {
    if (name != null && name.length() > 0) {
      ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
      OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
      try {
        Folder folder = outlook.getSpace(groupId).getFolder(path);
        folder.addSubfolder(name);
        ParametersListResourceSupportWrapper parametersListResourceSupportWrapper = new ParametersListResourceSupportWrapper();
        parametersListResourceSupportWrapper.setName("addFolder");
        parametersListResourceSupportWrapper.addParameter("folder", folder);

        parametersListResourceSupportWrapper.add(generateLinks("addFolder",
                                                               SaveAttachmentController.class,
                                                               saveAttachmentPassesConstants));

        return parametersListResourceSupportWrapper;
      } catch (BadParameterException e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Error adding folder " + path + "/" + name + ". " + e.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding folder " + path + "/" + name);
      } catch (Throwable e) {
        LOG.error("Error adding folder " + path + "/" + name, e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding folder " + path + "/" + name);
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Null or zero-length folder name to add in " + path);
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder name required");
    }
  }

  private <T> List<Link> generateLinks(String self, Class<T> controllerClass, Map<String, String> passesConstants) {
    List<Link> links = new LinkedList<>();
    for (Map.Entry<String, String> entry : passesConstants.entrySet()) {
      if (!entry.getKey().equals(self)) {
        links.add(linkTo(controllerClass).slash(entry.getValue().substring(1)).withRel(entry.getKey()));
      } else {
        links.add(linkTo(SaveAttachmentController.class).slash(entry.getValue().substring(1)).withSelfRel());
      }
    }
    return links;
  }
}
