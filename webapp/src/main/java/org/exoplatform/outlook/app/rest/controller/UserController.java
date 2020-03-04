package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.*;
import org.exoplatform.outlook.app.rest.dto.*;
import org.exoplatform.outlook.model.ActivityInfo;
import org.exoplatform.outlook.model.IdentityInfo;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.outlook.model.UserInfo;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.manager.RelationshipManager;
import org.exoplatform.social.core.relationship.model.Relationship;
import org.exoplatform.social.core.service.LinkProvider;
import org.exoplatform.social.core.space.spi.SpaceService;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.exoplatform.social.core.relationship.model.Relationship.Type.CONFIRMED;
import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The type User controller.
 */
@RestController
@RequestMapping(value = "/v2/exo/user")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class UserController extends AbstractController {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(UserController.class);

  /**
   * Gets root.
   *
   * @return the root
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getRoot() {
    ResourceSupport resource = new ResourceSupport();

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(RootDiscoveryeXoServiceController.class).getRootDiscoveryOfOutlookExoServices()).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).getRoot()).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getUserInfo(OutlookConstant.USER_ID, null)).withRel("user"));
    links.add(linkTo(methodOn(UserController.class).getConnections(OutlookConstant.USER_ID)).withRel("connections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(OutlookConstant.USER_ID, null, null)).withRel("spaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(OutlookConstant.USER_ID)).withRel("documents"));
    links.add(linkTo(methodOn(UserController.class).getActivities(OutlookConstant.USER_ID,
                                                                  null,
                                                                  null,
                                                                  null)).withRel("activities"));
    links.add(linkTo(methodOn(UserController.class).getActivity(OutlookConstant.USER_ID,
                                                                OutlookConstant.ACTIVITY_ID)).withRel("activity"));

    resource.add(links);

    return resource;
  }

  /**
   * Gets user info.
   *
   * @param userId the user id
   * @return the user info
   */
  @RequestMapping(value = "/{UID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getUserInfo(@PathVariable("UID") String userId, HttpServletRequest request) {

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    ActivityManager activityManager = (ActivityManager) currentContainer.getComponentInstance(ActivityManager.class);
    IdentityManager identityManager = (IdentityManager) currentContainer.getComponentInstance(IdentityManager.class);

    try {
      Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, userId, true);
      List<IdentityInfo> connectionList = getConnectionsList(userId);

      List<ExoSocialActivity> top20 = activityManager.getActivitiesByPoster(userIdentity).loadAsList(0, 20);

      List<ActivityInfo> activities = convertToActivityInfos(top20, userId, request);
      UserInfo userInfo = new UserInfo(userIdentity, activities, connectionList);

      org.exoplatform.outlook.app.rest.dto.UserInfo userInfoDTO = new org.exoplatform.outlook.app.rest.dto.UserInfo(userInfo);

      List<Link> links = new LinkedList<>();
      links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("parent"));
      links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, request)).withSelfRel());
      links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withRel("connections"));
      links.add(linkTo(methodOn(UserController.class).getSpaces(userId, null, null)).withRel("spaces"));
      links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withRel("documents"));
      links.add(linkTo(methodOn(UserController.class).getActivities(userId, null, null, null)).withRel("activities"));
      userInfoDTO.add(links);

      return userInfoDTO;
    } catch (Exception e) {
      LOG.error("Cannot find details of user: {}", userId, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot find details of user: " + userId);
    }
  }

  /**
   * Gets connections.
   *
   * @param userId the user id
   * @return the connections
   */
  @RequestMapping(value = "/{UID}/connections", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getConnections(@PathVariable("UID") String userId) {
    AbstractFileResource resource = null;

    List<IdentityInfo> userConnections = null;
    try {
      userConnections = getConnectionsList(userId);
    } catch (Exception e) {
      LOG.error("Cannot get user connections {}", userId, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot get user (" + userId + ") connections");
    }

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, null)).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getUserInfo(OutlookConstant.USER_ID, null)).withRel("user"));

    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(userConnections.size(), 1, userConnections.size(), 1);

    resource = new FileResource(metadata, userConnections, links);

    return resource;
  }

  /**
   * Gets spaces.
   *
   * @param userId the user id
   * @return the spaces
   */
  @RequestMapping(value = "/{UID}/spaces", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaces(@PathVariable("UID") String userId,
                                        @RequestParam Integer offset,
                                        @RequestParam Integer limit) {
    AbstractFileResource resource = null;

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
    SpaceService spaceService = (SpaceService) currentContainer.getComponentInstance(SpaceService.class);

    List<OutlookSpace> userSpaces = null;
    try {
      userSpaces = outlook.getUserSpaces(offset, limit);
    } catch (OutlookSpaceException e) {
      LOG.error("Error getting user (" + userId + ") spaces", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting user (" + userId + ") spaces");
    } catch (RepositoryException e) {
      LOG.error("Error getting user (" + userId + ") spaces", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting user (" + userId + ") spaces");
    } catch (OutlookException e) {
      LOG.error("Error getting user (" + userId + ") spaces", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting user (" + userId + ") spaces");
    }

    List<Space> spaces = new LinkedList<>();
    userSpaces.forEach((v) -> {
      spaces.add(new Space(v));
    });

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, null)).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId, null, null)).withSelfRel());
    links.add(linkTo(methodOn(SpaceController.class).getSpace(OutlookConstant.SPACE_ID)).withRel("space"));

    int availableSpaceNumber = userSpaces.size();
    try {
      availableSpaceNumber = spaceService.getMemberSpaces(userId).getSize();
    } catch (Exception e) {
      LOG.error("Error defining number of available user (" + userId + ") spaces", e);
    }
    PagedResources.PageMetadata metadata = getPaginationMetadata(offset, limit, availableSpaceNumber);

    resource = new FileResource(metadata, spaces, links);

    return resource;
  }

  /**
   * Gets documents.
   *
   * @param userId the user id
   * @return the documents
   */
  @RequestMapping(value = "/{UID}/documents", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getDocuments(@PathVariable("UID") String userId) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);

    org.exoplatform.outlook.jcr.Folder rootUserFolder = null;

    try {
      rootUserFolder = outlook.getUserDocuments().getRootFolder();
    } catch (OutlookException e) {
      LOG.error("Error getting a user (" + userId + ") root folder", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a user (" + userId + ") root folder");
    } catch (RepositoryException e) {
      LOG.error("Error getting a user (" + userId + ") root folder", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a user (" + userId + ") root folder");
    }

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, null)).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withSelfRel());
    links.add(linkTo(DocumentController.class).slash(OutlookConstant.DOCUMENT_PATH).withRel("document"));

    int folderFilesAndSubfolders = 0;
    try {
      folderFilesAndSubfolders = rootUserFolder.getSubfolders().size() + rootUserFolder.getFiles().size();
    } catch (RepositoryException e) {
      LOG.error("Error getting a user (" + userId + ") root folder subfolders or files", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting a user (" + userId + ") root folder subfolders or files");
    } catch (OutlookException e) {
      LOG.error("Error getting a user (" + userId + ") root folder subfolders or files", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting a user (" + userId + ") root folder subfolders or files");
    }
    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(folderFilesAndSubfolders,
                                                                           1,
                                                                           folderFilesAndSubfolders,
                                                                           1);

    Folder rootFolder = new Folder(rootUserFolder, metadata, links);

    return rootFolder;
  }

  /**
   * Gets activities.
   *
   * @param userId the user id
   * @return the activities
   */
  @RequestMapping(value = "/{UID}/activities", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getActivities(@PathVariable("UID") String userId,
                                            @RequestParam Integer offset,
                                            @RequestParam Integer limit,
                                            HttpServletRequest request) {
    AbstractFileResource resource = null;

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    ActivityManager activityManager = (ActivityManager) currentContainer.getComponentInstance(ActivityManager.class);
    IdentityManager identityManager = (IdentityManager) currentContainer.getComponentInstance(IdentityManager.class);

    Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, userId, true);
    List<ExoSocialActivity> userActivities = activityManager.getActivitiesByPoster(userIdentity).loadAsList(offset, limit);

    List<ActivityInfo> activities = convertToActivityInfos(userActivities, userId, request);

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, null)).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).getActivities(userId, null, null, null)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, OutlookConstant.ACTIVITY_ID)).withRel("activity"));

    int activitiesNumber = activityManager.getActivitiesByPoster(userIdentity).getSize();
    PagedResources.PageMetadata metadata = getPaginationMetadata(offset, limit, activitiesNumber);

    resource = new FileResource(metadata, activities, links);

    return resource;
  }

  /**
   * Post activity.
   *
   * @param userId the user id
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
  @RequestMapping(value = "/{UID}/activity", method = RequestMethod.POST, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport postActivity(@PathVariable("UID") String userId,
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

    OutlookUser user = null;
    ExoSocialActivity activity = null;

    try {
      user = outlook.getUser(userEmail, userName, null);
      OutlookMessage message = buildMessage(user, messageId, fromEmail, fromName, created, modified, title, subject, body);
      activity = user.postActivity(message);
    } catch (Throwable e) {
      LOG.error("Error converting message to activity status for " + userEmail, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error converting message to activity status for " + userEmail);
    }

    ActivityStatus activityStatus = new ActivityStatus(user.getLocalUser(), null, activity.getPermaLink());

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, null)).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).postActivity(userId,
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
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, activity.getId())).withRel("activity"));
    activityStatus.add(links);

    return activityStatus;
  }

  /**
   * Gets activity.
   *
   * @param userId the user id
   * @param activityId the activity id
   * @return the activity
   */
  @RequestMapping(value = "/{UID}/activity/{AID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getActivity(@PathVariable("UID") String userId, @PathVariable("AID") String activityId) {

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
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, null)).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, activityId)).withSelfRel());
    activityInfoDTO.add(links);

    return activityInfoDTO;
  }

  private List<IdentityInfo> getConnectionsList(String name) throws Exception {
    List<IdentityInfo> connectionList = new ArrayList<>();
    // TODO get rid of deprecated
    for (Relationship relationship : getRelationships(name)) {
      if (!relationship.getSender().getRemoteId().equals(name)) {
        connectionList.add(new IdentityInfo(relationship.getSender()));
      } else {
        connectionList.add(new IdentityInfo(relationship.getReceiver()));
      }
    }
    return connectionList;
  }

  @Deprecated
  private List<Relationship> getRelationships(String name) throws Exception {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    IdentityManager identityManager = (IdentityManager) currentContainer.getComponentInstance(IdentityManager.class);
    RelationshipManager relationshipManager =
                                            (RelationshipManager) currentContainer.getComponentInstance(RelationshipManager.class);

    Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, name, true);
    List<Relationship> relationships = relationshipManager.getRelationshipsByStatus(userIdentity, CONFIRMED, 0, 0);
    return relationships;
  }
}
