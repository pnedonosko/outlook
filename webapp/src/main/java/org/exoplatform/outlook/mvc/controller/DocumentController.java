package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.BadParameterException;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.OutlookSpace;
import org.exoplatform.outlook.OutlookUser;
import org.exoplatform.outlook.jcr.File;
import org.exoplatform.outlook.jcr.Folder;
import org.exoplatform.outlook.mvc.hateoas.ressupport.ParametersListResourceSupportWrapper;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

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
  private static final Log    LOG             = ExoLogger.getLogger(DocumentController.class);

  /** The Constant HAL_AND_JSON. */
  private static final String HAL_AND_JSON    = "application/hal+json";

  /** The Constant SAVE_ATTACHMENT. */
  private static final String SAVE_ATTACHMENT = "saveAttachment";

  /** The Constant ADD_FOLDER. */
  private static final String ADD_FOLDER      = "addFolder";

  /**
   * Post document parameters list.
   *
   * @param DOC_NAME the doc name
   * @param comment the comment
   * @param ewsUrl the ews url
   * @param userEmail the user email
   * @param userName the user name
   * @param messageId the message id
   * @param attachmentToken the attachment token
   * @param formParam the form param
   * @param request the request
   * @return the parameters list resource support wrapper
   */
  @RequestMapping(value = "/**/{DOC_NAME}", method = RequestMethod.POST, produces = HAL_AND_JSON)
  public PagedResources<? extends GeneralInfoBox> postDocument(@PathVariable("DOC_NAME") String DOC_NAME,
                                                               @RequestParam(value = "comment", required = false) String comment,
                                                               @RequestParam(value = "ewsUrl", required = false) String ewsUrl,
                                                               @RequestParam(value = "userEmail", required = false) String userEmail,
                                                               @RequestParam(value = "userName", required = false) String userName,
                                                               @RequestParam(value = "messageId", required = false) String messageId,
                                                               @RequestParam(value = "attachmentToken", required = false) String attachmentToken,
                                                               @RequestParam(value = "formParam") String formParam,
                                                               HttpServletRequest request) {

    PathMatcher pathMatcher = new AntPathMatcher();

    String mvcPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
    String mvcPath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
    String DPARENT_PATH = pathMatcher.extractPathWithinPattern(mvcPattern, mvcPath);

    DPARENT_PATH = new StringBuilder("/").append(DPARENT_PATH, 0, DPARENT_PATH.lastIndexOf("/")).toString();

    String groupId = "";

    Pattern pattern = Pattern.compile("(?:.)[/]{1}[\\w]*[/]{1}[\\w]*");
    Matcher matcher = pattern.matcher(DPARENT_PATH);

    if (matcher.find()) {
      groupId = matcher.group(0).substring(1);
    }

    PagedResources<? extends GeneralInfoBox> resources = null;

    switch (formParam) {
    case ADD_FOLDER:
      resources = addFolder(DPARENT_PATH, DOC_NAME, groupId);
      break;
    case SAVE_ATTACHMENT:
      resources =
                saveAttachment(DPARENT_PATH, DOC_NAME, groupId, comment, ewsUrl, userEmail, userName, messageId, attachmentToken);

      break;
    }

    return resources;
  }

  private PagedResources<FileInfo> saveAttachment(String DPARENT_PATH,
                                                  String ATTACHMENT_IDS,
                                                  String groupId,
                                                  String comment,
                                                  String ewsUrl,
                                                  String userEmail,
                                                  String userName,
                                                  String messageId,
                                                  String attachmentToken) {
    if (groupId != null && DPARENT_PATH != null && ewsUrl != null && userEmail != null && messageId != null
        && attachmentToken != null && ATTACHMENT_IDS != null) {
      try {
        ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
        OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);

        OutlookSpace space = outlook.getSpace(groupId);
        if (space != null) {
          // Remove empty attachments in the array
          List<String> attachments = new ArrayList<String>();
          for (String aid : ATTACHMENT_IDS.split(",")) {
            aid = aid.trim();
            if (aid.length() > 0) {
              attachments.add(aid);
            }
          }
          if (attachments.size() > 0) {
            Folder folder = space.getFolder(DPARENT_PATH);
            OutlookUser user = outlook.getUser(userEmail, userName, ewsUrl);
            List<File> files = outlook.saveAttachment(space,
                                                      folder,
                                                      user,
                                                      comment,
                                                      messageId,
                                                      attachmentToken,
                                                      attachments.toArray(new String[attachments.size()]));

            List<FileInfo> fileInfos = new LinkedList<>();
            files.forEach((f) -> {
              fileInfos.add(new FileInfo(f));
            });

            ParametersListResourceSupportWrapper parametersListResourceSupportWrapper =
                                                                                      new ParametersListResourceSupportWrapper();
            parametersListResourceSupportWrapper.setName("saveAttachment");
            parametersListResourceSupportWrapper.addParameter("files", fileInfos);

            Map<String, String> paramsMap = new HashMap<>(7);
            paramsMap.put("comment", comment);
            paramsMap.put("ewsUrl", ewsUrl);
            paramsMap.put("userEmail", userEmail);
            paramsMap.put("userName", userName);
            paramsMap.put("messageId", messageId);
            paramsMap.put("attachmentToken", attachmentToken);
            paramsMap.put("formParam", "addFolder");

            String params = generateParameters(paramsMap);

            parametersListResourceSupportWrapper.add(linkTo(SaveAttachmentController.class).slash(DPARENT_PATH.substring(1))
                                                                                           .slash(new StringBuilder(ATTACHMENT_IDS).append(params))
                                                                                           .withSelfRel());

            List<Link> links = new LinkedList<>();
            links.add(linkTo(SaveAttachmentController.class).slash(DPARENT_PATH.substring(1))
                                                            .slash(ATTACHMENT_IDS)
                                                            .withSelfRel());

            List<FolderInfo> folderInfos = new LinkedList<>();
            folderInfos.add(new FolderInfo(folder));

            PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(folderInfos.size(), 1, folderInfos.size(), 1);

            PagedResources<FileInfo> fileInfoPagedResources = new PagedResources<>(fileInfos, metadata, links);

            return fileInfoPagedResources;
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
        LOG.debug("Error in saving attachment request: spaceName='" + groupId + "' path=" + DPARENT_PATH + " ewsUrl='" + ewsUrl
            + "' userEmail='" + userEmail + "' messageId='" + messageId + "' attachmentToken(size)='"
            + (attachmentToken != null ? attachmentToken.length() : "null") + "' attachmentIds='" + ATTACHMENT_IDS + "'");
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in saving attachment request. Please reload the page.");
    }
  }

  private PagedResources<FolderInfo> addFolder(String DPARENT_PATH, String NAME, String groupId) {
    if (NAME != null && NAME.length() > 0) {
      ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
      OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
      try {
        Folder folder = outlook.getSpace(groupId).getFolder(DPARENT_PATH);
        folder.addSubfolder(NAME);
        ParametersListResourceSupportWrapper parametersListResourceSupportWrapper = new ParametersListResourceSupportWrapper();
        parametersListResourceSupportWrapper.setName("addFolder");
        parametersListResourceSupportWrapper.addParameter("folder", new FolderInfo(folder));

        Map<String, String> paramsMap = new HashMap<>(1);
        paramsMap.put("formParam", "addFolder");
        String params = generateParameters(paramsMap);

        parametersListResourceSupportWrapper.add(linkTo(SaveAttachmentController.class).slash(DPARENT_PATH.substring(1))
                                                                                       .slash(new StringBuilder(NAME).append(params))
                                                                                       .withSelfRel());

        List<Link> links = new LinkedList<>();
        links.add(linkTo(SaveAttachmentController.class).slash(DPARENT_PATH.substring(1)).slash(NAME).withSelfRel());

        List<FolderInfo> folderInfos = new LinkedList<>();
        folderInfos.add(new FolderInfo(folder));

        PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(folderInfos.size(), 1, folderInfos.size(), 1);

        PagedResources<FolderInfo> folders = new PagedResources<>(folderInfos, metadata, links);

        return folders;
      } catch (BadParameterException e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Error adding folder " + DPARENT_PATH + "/" + NAME + ". " + e.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding folder " + DPARENT_PATH + "/" + NAME);
      } catch (Throwable e) {
        LOG.error("Error adding folder " + DPARENT_PATH + "/" + NAME, e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding folder " + DPARENT_PATH + "/" + NAME);
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Null or zero-length folder name to add in " + DPARENT_PATH);
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folder name required");
    }

  }

  private String generateParameters(Map<String, String> paramsMap) {
    StringBuilder parameters = new StringBuilder("?");
    if (paramsMap != null && paramsMap.size() > 0) {
      paramsMap.forEach((k, v) -> {
        parameters.append(k);
        parameters.append("=");
        parameters.append(v);
        parameters.append("&");
      });
    }
    return parameters.substring(0, parameters.length());
  }
}
