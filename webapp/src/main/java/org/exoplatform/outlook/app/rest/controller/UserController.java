package org.exoplatform.outlook.app.rest.controller;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.OutlookSpace;
import org.exoplatform.outlook.OutlookSpaceException;
import org.exoplatform.outlook.app.rest.dto.AbstractFileResource;
import org.exoplatform.outlook.app.rest.dto.FileResource;
import org.exoplatform.outlook.app.rest.dto.Folder;
import org.exoplatform.outlook.app.rest.dto.Space;
import org.exoplatform.outlook.model.ActivityInfo;
import org.exoplatform.outlook.model.IdentityInfo;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.outlook.model.UserInfo;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.manager.RelationshipManager;
import org.exoplatform.social.core.relationship.model.Relationship;
import org.exoplatform.social.core.service.LinkProvider;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.ContentHandler;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.exoplatform.social.core.relationship.model.Relationship.Type.CONFIRMED;
import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The type User controller.
 */
@RestController
@RequestMapping(value = "/v2/exo/user")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class UserController {

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
    links.add(linkTo(methodOn(RootDiscoveryeXoServiceController.class).getRootDiscoveryOfOutlookMailServices()).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).getRoot()).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getUserInfo(OutlookConstant.USER_ID, null)).withRel("user"));
    links.add(linkTo(methodOn(UserController.class).getConnections(OutlookConstant.USER_ID)).withRel("connections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(OutlookConstant.USER_ID)).withRel("spaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(OutlookConstant.USER_ID)).withRel("documents"));
    links.add(linkTo(methodOn(UserController.class).getActivities(OutlookConstant.USER_ID, null)).withRel("activities"));
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
    OrganizationService organization = (OrganizationService) currentContainer.getComponentInstance(OrganizationService.class);

    try {
      final Charset clientCs = loadEncoding(request.getCharacterEncoding());

      Set<String> currentUserGroupIds = organization.getMembershipHandler()
                                                    .findMembershipsByUser(userId)
                                                    .stream()
                                                    .map(m -> m.getGroupId())
                                                    .collect(Collectors.toSet());

      Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, userId, true);
      List<IdentityInfo> connectionList = getConnectionsList(userId);

      List<ExoSocialActivity> top20 = activityManager.getActivitiesByPoster(userIdentity).loadAsList(0, 20);

      // Use Apache Tika to parse activity title w/o HTML
      HtmlParser htmlParser = new HtmlParser();

      List<ActivityInfo> activities = top20.stream().filter(a -> {
        String streamId = a.getStreamOwner();
        return streamId != null
            && (userIdentity.getRemoteId().equals(streamId) || currentUserGroupIds.contains(findSpaceGroupId(streamId)));
      }).map(a -> {
        // We want activity title in text (not HTML)
        ParseContext pcontext = new ParseContext();
        ContentHandler contentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        InputStream content = new ByteArrayInputStream(a.getTitle().getBytes(clientCs));
        String titleText;
        try {
          htmlParser.parse(content, contentHandler, metadata, pcontext);
          titleText = cutText(contentHandler.toString(), 100);
        } catch (Exception e) {
          String rawTitle = cutText(a.getTitle(), 100);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Cannot parse activity title: '{}...'", rawTitle, e);
          }
          titleText = rawTitle;
        }
        return new ActivityInfo(titleText, a.getType(), LinkProvider.getSingleActivityUrl(a.getId()), a.getPostedTime());
      }).collect(Collectors.toList());
      UserInfo userInfo = new UserInfo(userIdentity, activities, connectionList);

      org.exoplatform.outlook.app.rest.dto.UserInfo userInfoDTO = new org.exoplatform.outlook.app.rest.dto.UserInfo(userInfo);

      List<Link> links = new LinkedList<>();
      links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("parent"));
      links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, request)).withSelfRel());
      links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withRel("connections"));
      links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withRel("spaces"));
      links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withRel("documents"));
      links.add(linkTo(methodOn(UserController.class).getActivities(userId, null)).withRel("activities"));
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
  public AbstractFileResource getSpaces(@PathVariable("UID") String userId) {
    AbstractFileResource resource = null;

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);

    List<OutlookSpace> userSpaces = null;
    try {
      userSpaces = outlook.getUserSpaces();
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
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withSelfRel());
    links.add(linkTo(methodOn(SpaceController.class).getSpace(OutlookConstant.SPACE_ID)).withRel("space"));

    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(spaces.size(), 1, spaces.size(), 1);

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
  public AbstractFileResource getActivities(@PathVariable("UID") String userId, HttpServletRequest request) {
    AbstractFileResource resource = null;

    final Charset clientCs = loadEncoding(request.getCharacterEncoding());

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    ActivityManager activityManager = (ActivityManager) currentContainer.getComponentInstance(ActivityManager.class);
    IdentityManager identityManager = (IdentityManager) currentContainer.getComponentInstance(IdentityManager.class);
    OrganizationService organization = (OrganizationService) currentContainer.getComponentInstance(OrganizationService.class);

    Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, userId, true);
    List<ExoSocialActivity> top100 = activityManager.getActivitiesByPoster(userIdentity).loadAsList(0, 100);

    Set<String> currentUserGroupIds = null;
    try {
      currentUserGroupIds = organization.getMembershipHandler()
                                        .findMembershipsByUser(userId)
                                        .stream()
                                        .map(m -> m.getGroupId())
                                        .collect(Collectors.toSet());
    } catch (Exception e) {
      LOG.error("Error getting current user (" + userId + ") group ids", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting current user (" + userId + ") group ids");
    }

    // Use Apache Tika to parse activity title w/o HTML
    HtmlParser htmlParser = new HtmlParser();

    Set<String> finalCurrentUserGroupIds = currentUserGroupIds;
    List<ActivityInfo> activities = top100.stream().filter(a -> {
      String streamId = a.getStreamOwner();
      return streamId != null
          && (userIdentity.getRemoteId().equals(streamId) || finalCurrentUserGroupIds.contains(findSpaceGroupId(streamId)));
    }).map(a -> {
      // We want activity title in text (not HTML)
      ParseContext pcontext = new ParseContext();
      ContentHandler contentHandler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      InputStream content = new ByteArrayInputStream(a.getTitle().getBytes(clientCs));
      String titleText;
      try {
        htmlParser.parse(content, contentHandler, metadata, pcontext);
        titleText = cutText(contentHandler.toString(), 100);
      } catch (Exception e) {
        String rawTitle = cutText(a.getTitle(), 100);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Cannot parse activity title: '{}...'", rawTitle, e);
        }
        titleText = rawTitle;
      }
      return new ActivityInfo(titleText, a.getType(), LinkProvider.getSingleActivityUrl(a.getId()), a.getPostedTime());
    }).collect(Collectors.toList());

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId, null)).withRel("parent"));
    links.add(linkTo(methodOn(UserController.class).getActivities(userId, null)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, OutlookConstant.ACTIVITY_ID)).withRel("activity"));

    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(activities.size(), 1, activities.size(), 1);

    resource = new FileResource(metadata, activities, links);

    return resource;
  }

  /**
   * Post activity abstract file resource.
   *
   * @param userId the user id
   * @return the abstract file resource
   */
  @RequestMapping(value = "/{UID}/activity", method = RequestMethod.POST, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource postActivity(@PathVariable("UID") String userId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets activity.
   *
   * @param userId the user id
   * @param activityId the activity id
   * @return the activity
   */
  @RequestMapping(value = "/{UID}/activity/{AID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getActivity(@PathVariable("UID") String userId, @PathVariable("AID") String activityId) {
    AbstractFileResource resource = null;

    return resource;
  }

  private String findSpaceGroupId(String prettyName) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    SpaceService spaceService = (SpaceService) currentContainer.getComponentInstance(SpaceService.class);
    org.exoplatform.social.core.space.model.Space space = spaceService.getSpaceByPrettyName(prettyName);
    return space != null ? space.getGroupId() : "".intern();
  }

  private Charset loadEncoding(String name) {
    if (name == null || name.length() == 0) {
      name = "UTF8";
    }
    try {
      return Charset.forName(name);
    } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
      LOG.warn("Error loading client encoding charset {}: {}", name, e.toString());
      return Charset.defaultCharset();
    }
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

  private String cutText(String text, int limit) {
    return text.length() > limit ? text.substring(0, limit) : text;
  }
}
