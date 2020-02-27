package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.BadParameterException;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.OutlookSpace;
import org.exoplatform.outlook.OutlookUser;
import org.exoplatform.outlook.app.rest.dto.Attachment;
import org.exoplatform.outlook.jcr.File;
import org.exoplatform.outlook.app.rest.dto.Folder;
import org.exoplatform.outlook.app.rest.dto.AbstractFileResource;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The type Document controller.
 */
@RestController
@RequestMapping(value = "/v2/exo/document")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class DocumentController {

  /** The Constant LOG. */
  private static final Log    LOG        = ExoLogger.getLogger(DocumentController.class);

  /** The Constant ATTACHMENT. */
  private static final String ATTACHMENT = "attachment";

  /** The Constant FOLDER. */
  private static final String FOLDER     = "folder";

  /** The Outlook service. */
  /*
   * private final OutlookService outlookService;
   * @Autowired public DocumentController(OutlookService outlookService) {
   * this.outlookService = outlookService; }
   */

  /**
   * Post document parameters list.
   *
   * @param docName the doc name
   * @param comment the comment
   * @param ewsUrl the ews url
   * @param userEmail the user email
   * @param userName the user name
   * @param messageId the message id
   * @param attachmentToken the attachment token
   * @param resourceType the resource type
   * @param request the request
   * @return the parameters list resource support wrapper
   */
  @RequestMapping(value = "/**/{DOC_NAME}", method = RequestMethod.POST, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource postDocument(@PathVariable("DOC_NAME") String docName,
                                           @RequestParam(value = "comment", required = false) String comment,
                                           @RequestParam(value = "ewsUrl", required = false) String ewsUrl,
                                           @RequestParam(value = "userEmail", required = false) String userEmail,
                                           @RequestParam(value = "userName", required = false) String userName,
                                           @RequestParam(value = "messageId", required = false) String messageId,
                                           @RequestParam(value = "attachmentToken", required = false) String attachmentToken,
                                           @RequestParam(value = "resourceType") String resourceType,
                                           HttpServletRequest request) {

    String dParentPath = getDocumentParentPath(request, this);

    String groupId = getGroupId(dParentPath);

    AbstractFileResource resource = null;

    switch (resourceType) {
    case FOLDER:
      resource = addFolder(dParentPath, docName, groupId);
      break;
    case ATTACHMENT:
      resource = saveAttachment(dParentPath, docName, groupId, comment, ewsUrl, userEmail, userName, messageId, attachmentToken);
      break;
    }

    return resource;
  }

  private Attachment saveAttachment(String dParentPath,
                                    String attachmentIds,
                                    String groupId,
                                    String comment,
                                    String ewsUrl,
                                    String userEmail,
                                    String userName,
                                    String messageId,
                                    String attachmentToken) {
    if (groupId != null && dParentPath != null && ewsUrl != null && userEmail != null && messageId != null
        && attachmentToken != null && attachmentIds != null) {
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
            org.exoplatform.outlook.jcr.Folder folder = space.getFolder(dParentPath);
            OutlookUser user = outlook.getUser(userEmail, userName, ewsUrl);
            List<File> files = outlook.saveAttachment(space,
                                                      folder,
                                                      user,
                                                      comment,
                                                      messageId,
                                                      attachmentToken,
                                                      attachments.toArray(new String[attachments.size()]));

            List<Link> links = new LinkedList<>();
            links.add(linkTo(DocumentController.class).slash(dParentPath.substring(1)).slash(attachmentIds).withSelfRel());

            PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(files.size(), 1, files.size(), 1);

            Attachment attachment = new Attachment(metadata,
                                                   links,
                                                   comment,
                                                   ewsUrl,
                                                   userEmail,
                                                   userName,
                                                   messageId,
                                                   attachmentToken,
                                                   files);
            return attachment;
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
        LOG.debug("Error in saving attachment request: spaceName='" + groupId + "' path=" + dParentPath + " ewsUrl='" + ewsUrl
            + "' userEmail='" + userEmail + "' messageId='" + messageId + "' attachmentToken(size)='"
            + (attachmentToken != null ? attachmentToken.length() : "null") + "' attachmentIds='" + attachmentIds + "'");
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in saving attachment request. Please reload the page.");
    }
  }

  private Folder addFolder(String dParentPath, String name, String groupId) {
    if (name != null && name.length() > 0) {
      ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
      OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
      try {
        org.exoplatform.outlook.jcr.Folder folder = outlook.getSpace(groupId).getFolder(dParentPath);
        folder.addSubfolder(name);
        org.exoplatform.outlook.jcr.Folder addedFolder = null;

        for (org.exoplatform.outlook.jcr.Folder fold : folder.getSubfolders()) {
          if (fold.getName().equals(name)) {
            addedFolder = fold;
            break;
          }
        }

        List<Link> links = new LinkedList<>();
        links.add(linkTo(DocumentController.class).slash(dParentPath.substring(1)).slash(name).withSelfRel());

        PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(addedFolder.getSubfolders().size(),
                                                                               1,
                                                                               addedFolder.getSubfolders().size(),
                                                                               1);

        return new Folder(addedFolder, metadata, links);
      } catch (BadParameterException e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Error adding folder " + dParentPath + "/" + name + ". " + e.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding folder " + dParentPath + "/" + name);
      } catch (Throwable e) {
        LOG.error("Error adding folder " + dParentPath + "/" + name, e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding folder " + dParentPath + "/" + name);
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Null or zero-length folder name to add in " + dParentPath);
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder name required");
    }

  }

  /**
   * Gets document.
   *
   * @param docName the doc name
   * @param request the request
   * @return the document
   */
  @RequestMapping(value = "/**/{DOC_NAME}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getDocument(@PathVariable("DOC_NAME") String docName, HttpServletRequest request) {

    String dParentPath = getDocumentParentPath(request, this);

    String groupId = getGroupId(dParentPath);

    AbstractFileResource resource = null;

    if (!docName.contains(".")) {
      resource = getFolder(dParentPath, docName, groupId);
    } else {
      // get file
    }

    return resource;
  }

  private Folder getFolder(String dParentPath, String name, String groupId) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
    String folderPath = null;
    try {
      folderPath = new StringBuilder(dParentPath).append("/").append(name).toString();
      org.exoplatform.outlook.jcr.Folder folder = outlook.getSpace(groupId).getFolder(folderPath);

      List<Link> links = new LinkedList<>();
      links.add(linkTo(DocumentController.class).slash(dParentPath.substring(1)).slash(name).withSelfRel());

      int folderFilesAndSubfolders = folder.getSubfolders().size() + folder.getFiles().size();
      PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(folderFilesAndSubfolders,
                                                                             1,
                                                                             folderFilesAndSubfolders,
                                                                             1);
      return new Folder(folder, metadata, links);
    } catch (BadParameterException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Error reading folder " + folderPath + ". " + e.getMessage());
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error reading folder " + folderPath);
    } catch (Throwable e) {
      LOG.error("Error reading folder " + folderPath, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading folder " + folderPath);
    }
  }

  private String getDocumentParentPath(HttpServletRequest request, Object currentController) {
    String requestURL = request.getRequestURL().toString();

    int parentPathStartPint = linkTo(currentController.getClass()).toString().length();
    int parentPathFinishPoint = requestURL.lastIndexOf("/");

    return requestURL.substring(parentPathStartPint, parentPathFinishPoint);
  }

  private String getGroupId(String dParentPath) {
    String groupId = "";

    Pattern pattern = Pattern.compile("(?:.)[/]{1}[\\w]*[/]{1}[\\w]*");
    Matcher matcher = pattern.matcher(dParentPath);

    if (matcher.find()) {
      groupId = matcher.group(0).substring(1);
    }
    return groupId;
  }

}
