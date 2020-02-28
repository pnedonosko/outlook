package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.*;
import org.exoplatform.outlook.app.rest.dto.*;
import org.exoplatform.outlook.app.rest.service.ActivityService;
import org.exoplatform.outlook.app.rest.service.MessageService;
import org.exoplatform.outlook.model.ActivityInfo;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.service.LinkProvider;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.core.storage.api.ActivityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The type Space controller.
 */
@RestController
@RequestMapping(value = "/v2/exo/space")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class SpaceController {

  /** The Constant LOG. */
  private static final Log                                      LOG = ExoLogger.getLogger(SpaceController.class);

  /** The Activity service. */
  @Autowired
  private ActivityService                                       activityService;

  /** The Space service. */
  @Autowired
  private org.exoplatform.outlook.app.rest.service.SpaceService spaceService;

  /** The Message service. */
  @Autowired
  private MessageService                                        messageService;

  @RequestMapping(value = "", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getRoot() {
    ResourceSupport resource = new ResourceSupport();

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(RootDiscoveryeXoServiceController.class).getRootDiscoveryOfOutlookExoServices()).withRel("parent"));
    links.add(linkTo(methodOn(SpaceController.class).getRoot()).withSelfRel());
    links.add(linkTo(methodOn(SpaceController.class).getSpace(OutlookConstant.SPACE_ID)).withRel("space"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceActivities(OutlookConstant.SPACE_ID, null)).withRel("activities"));
    links.add(linkTo(methodOn(SpaceController.class).getActivityInfo(OutlookConstant.SPACE_ID, null)).withRel("activity"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceDocuments(OutlookConstant.SPACE_ID)).withRel("documents"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceWiki(OutlookConstant.SPACE_ID)).withRel("wiki"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceForum(OutlookConstant.SPACE_ID)).withRel("forum"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceCalendar(OutlookConstant.SPACE_ID)).withRel("calendar"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceMembers(OutlookConstant.SPACE_ID)).withRel("members"));

    resource.add(links);

    return resource;
  }

  /**
   * Gets space.
   *
   * @param spaceId the space id
   * @return the space
   */
  @RequestMapping(value = "/{SID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getSpace(@PathVariable("SID") String spaceId) {

    OutlookSpace outlookSpace = spaceService.getOutlookSpace(spaceId);

    List<Link> links = new LinkedList<>();
    links.add(linkTo(RootDiscoveryeXoServiceController.class).withRel("parent"));
    links.add(linkTo(methodOn(SpaceController.class).getSpace(spaceId)).withSelfRel());
    links.add(linkTo(methodOn(SpaceController.class).getSpaceActivities(spaceId, null)).withRel("activities"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceDocuments(spaceId)).withRel("documents"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceWiki(spaceId)).withRel("wiki"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceForum(spaceId)).withRel("forum"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceCalendar(spaceId)).withRel("calendar"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceMembers(spaceId)).withRel("members"));

    Space space = new Space(outlookSpace);
    space.add(links);

    return space;
  }

  /**
   * Gets space activities.
   *
   * @param spaceId the space id
   * @return the space activities
   */
  @RequestMapping(value = "/{SID}/activities", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaceActivities(@PathVariable("SID") String spaceId, HttpServletRequest request) {
    AbstractFileResource resource = null;

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    IdentityManager identityManager = (IdentityManager) currentContainer.getComponentInstance(IdentityManager.class);
    ActivityStorage activityStorage = (ActivityStorage) currentContainer.getComponentInstance(ActivityStorage.class);
    SpaceService spaceService = (SpaceService) currentContainer.getComponentInstance(SpaceService.class);

    String spaceGroupId = new StringBuilder("/spaces/").append(spaceId).toString();

    org.exoplatform.social.core.space.model.Space space = null;
    try {
      space = spaceService.getSpaceByGroupId(spaceGroupId);
    } catch (Exception e) {
      LOG.error("Error getting space (" + spaceGroupId + ")", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting space (" + spaceGroupId + ")");
    }

    Identity spaceIdentity = null;
    try {
      spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName());
    } catch (Exception e) {
      LOG.error("Error getting space (" + spaceGroupId + ") identity", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting space (" + spaceGroupId + ") identity");
    }

    // the limit is 100
    List<ExoSocialActivity> spaceActivities = null;
    try {
      spaceActivities = activityStorage.getSpaceActivities(spaceIdentity, 0, 100);
    } catch (Exception e) {
      LOG.error("Error getting space (" + spaceGroupId + ") activities", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting space (" + spaceGroupId + ") activities");
    }

    ConversationState convo = ConversationState.getCurrent();
    String currentUserId = null;
    if (convo != null) {
      currentUserId = convo.getIdentity().getUserId();
    }

    List<ActivityInfo> activityInfos = activityService.convertToActivityInfos(spaceActivities, currentUserId, request);

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(SpaceController.class).getSpace(spaceId)).withRel("parent"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceActivities(spaceId, request)).withSelfRel());
    links.add(linkTo(methodOn(SpaceController.class).getActivityInfo(spaceId, OutlookConstant.ACTIVITY_ID)).withRel("activity"));

    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(activityInfos.size(), 1, activityInfos.size(), 1);

    resource = new FileResource(metadata, activityInfos, links);

    return resource;
  }

  /**
   * Post space activity.
   *
   * @param spaceId the space id
   * @param messageId the message id
   * @param title the title
   * @param subject the subject
   * @param body the body
   * @param created the created
   * @param modified the modified
   * @param userName the user name
   * @param userEmail the user email
   * @param fromName the from name
   * @param fromEmail the from email
   * @return the activity status
   */
  @RequestMapping(value = "/{SID}/activity", method = RequestMethod.POST, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport postSpaceActivity(@PathVariable("SID") String spaceId,
                                           @RequestParam String messageId,
                                           @RequestParam String title,
                                           @RequestParam String subject,
                                           @RequestParam String body,
                                           @RequestParam String created,
                                           @RequestParam String modified,
                                           @RequestParam String userName,
                                           @RequestParam String userEmail,
                                           @RequestParam String fromName,
                                           @RequestParam String fromEmail) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);

    String groupId = spaceService.getGroupId(spaceId);

    OutlookSpace space = null;
    ExoSocialActivity activity = null;

    try {
      OutlookUser user = outlook.getUser(userEmail, userName, null);
      OutlookMessage message = messageService.buildMessage(user,
                                                           messageId,
                                                           fromEmail,
                                                           fromName,
                                                           created,
                                                           modified,
                                                           title,
                                                           subject,
                                                           body);
      space = outlook.getSpace(groupId);

      if (space != null) {
        activity = space.postActivity(message);
      } else {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Error converting message to activity status : space not found " + groupId + ". OutlookUser " + userEmail);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                          "Error converting message to activity status : space not found " + groupId);
      }
    } catch (Throwable e) {
      LOG.error("Error converting message to activity status for " + userEmail, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error converting message to activity status for " + userEmail);
    }

    ActivityStatus activityStatus = new ActivityStatus(null, space.getTitle(), activity.getPermaLink());

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(SpaceController.class).getSpace(spaceId)).withRel("parent"));
    links.add(linkTo(methodOn(SpaceController.class).postSpaceActivity(spaceId,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null)).withSelfRel());
    links.add(linkTo(methodOn(SpaceController.class).getActivityInfo(spaceId, OutlookConstant.ACTIVITY_ID)).withRel("activity"));
    activityStatus.add(links);

    return activityStatus;
  }

  /**
   * Gets activity info.
   *
   * @param spaceId the space id
   * @param activityId the activity id
   * @return the activity info
   */
  @RequestMapping(value = "/{SID}/activity/{AID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getActivityInfo(@PathVariable("SID") String spaceId, @PathVariable("AID") String activityId) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    ActivityManager activityManager = (ActivityManager) currentContainer.getComponentInstance(ActivityManager.class);

    ExoSocialActivity activity = activityManager.getActivity(activityId);

    ActivityInfo activityInfo = new ActivityInfo(activity.getTitle(),
                                                 activity.getType(),
                                                 LinkProvider.getSingleActivityUrl(activity.getId()),
                                                 activity.getPostedTime());
    org.exoplatform.outlook.app.rest.dto.ActivityInfo activityInfoDTO =
                                                                      new org.exoplatform.outlook.app.rest.dto.ActivityInfo(activityInfo);

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(SpaceController.class).getSpace(spaceId)).withRel("parent"));
    links.add(linkTo(methodOn(SpaceController.class).getActivityInfo(spaceId, activityId)).withSelfRel());
    activityInfoDTO.add(links);

    return activityInfoDTO;
  }

  /**
   * Gets space documents.
   *
   * @param spaceId the space id
   * @return the space documents
   */
  @RequestMapping(value = "/{SID}/documents", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaceDocuments(@PathVariable("SID") String spaceId) {
    OutlookSpace outlookSpace = spaceService.getOutlookSpace(spaceId);

    // path to documents
    String pathToRootFolder = "";
    try {
      pathToRootFolder = outlookSpace.getRootFolder().getPath();
    } catch (OutlookException e) {
      LOG.error("Error getting a space root folder in order to get the root path (rootPath)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting a space root folder in order to get the root path (rootPath)");
    } catch (RepositoryException e) {
      LOG.error("Error getting a space root folder in order to get the root path (rootPath)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting a space root folder in order to get the root path (rootPath)");
    }

    org.exoplatform.outlook.jcr.Folder folder = null;
    int numberOfFolderFilesAndSubfolders = 0;
    try {
      folder = outlookSpace.getFolder(pathToRootFolder);
      numberOfFolderFilesAndSubfolders = folder.getSubfolders().size() + folder.getFiles().size();
    } catch (BadParameterException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Error reading folder " + pathToRootFolder + ". " + e.getMessage());
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error reading folder " + pathToRootFolder);
    } catch (Throwable e) {
      LOG.error("Error reading folder " + pathToRootFolder, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading folder " + pathToRootFolder);
    }

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(SpaceController.class).getSpace(spaceId)).withRel("parent"));
    links.add(linkTo(DocumentController.class).slash(pathToRootFolder.substring(1)).withSelfRel());

    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(numberOfFolderFilesAndSubfolders,
                                                                           1,
                                                                           numberOfFolderFilesAndSubfolders,
                                                                           1);

    return new Folder(folder, metadata, links);
  }

  /**
   * Gets space wiki.
   *
   * @param spaceId the space id
   * @return the space wiki
   */
  @RequestMapping(value = "/{SID}/wiki", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaceWiki(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets space forum.
   *
   * @param spaceId the space id
   * @return the space forum
   */
  @RequestMapping(value = "/{SID}/forum", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaceForum(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets space calendar.
   *
   * @param spaceId the space id
   * @return the space calendar
   */
  @RequestMapping(value = "/{SID}/calendar", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaceCalendar(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets space members.
   *
   * @param spaceId the space id
   * @return the space members
   */
  @RequestMapping(value = "/{SID}/members", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaceMembers(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    SpaceService spaceService = (SpaceService) currentContainer.getComponentInstance(SpaceService.class);

    org.exoplatform.social.core.space.model.Space space = spaceService.getSpaceByPrettyName(spaceId);

    String[] spaceMembers = space.getMembers();

    List<String> membersList = Arrays.asList(spaceMembers);

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(SpaceController.class).getSpace(spaceId)).withRel("parent"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceMembers(spaceId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getUserInfo(OutlookConstant.USER_ID, null)).withRel("user"));

    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(spaceMembers.length, 1, spaceMembers.length, 1);

    resource = new FileResource(metadata, membersList, links);

    return resource;
  }
}
