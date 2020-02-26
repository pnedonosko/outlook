package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.app.rest.dto.AbstractFileResource;
import org.exoplatform.outlook.app.rest.dto.Folder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.jcr.RepositoryException;
import java.util.LinkedList;
import java.util.List;

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
  private static final Log    LOG          = ExoLogger.getLogger(UserController.class);

  /** The Constant HAL_AND_JSON. */
  private static final String HAL_AND_JSON = "application/hal+json";

  /** The Constant USER_ID. */
  private static final String USER_ID      = "{UID}";

  /** The Constant ACTIVITY_ID. */
  private static final String ACTIVITY_ID  = "{AID}";

  /**
   * Gets root.
   *
   * @return the root
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = HAL_AND_JSON)
  public AbstractFileResource getRoot() {
    AbstractFileResource resource = null;

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getRoot()).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getUserInfo(USER_ID)).withRel("getUserInfo"));
    links.add(linkTo(methodOn(UserController.class).getConnections(USER_ID)).withRel("getConnections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(USER_ID)).withRel("getSpaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(USER_ID)).withRel("getDocuments"));
    links.add(linkTo(methodOn(UserController.class).getActivities(USER_ID)).withRel("getActivities"));
    links.add(linkTo(methodOn(UserController.class).postActivity(USER_ID)).withRel("postActivity"));
    links.add(linkTo(methodOn(UserController.class).getActivity(USER_ID, ACTIVITY_ID)).withRel("getActivity"));

    return resource;
  }

  /**
   * Gets user info.
   *
   * @param userId the user id
   * @return the user info
   */
  @RequestMapping(value = "/{UID}", method = RequestMethod.GET, produces = HAL_AND_JSON)
  public AbstractFileResource getUserInfo(@PathVariable("UID") String userId) {
    AbstractFileResource resource = null;

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("getRoot"));
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withRel("getConnections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withRel("getSpaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withRel("getDocuments"));
    links.add(linkTo(methodOn(UserController.class).getActivities(userId)).withRel("getActivities"));
    links.add(linkTo(methodOn(UserController.class).postActivity(userId)).withRel("postActivity"));
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, ACTIVITY_ID)).withRel("getActivity"));

    return resource;
  }

  /**
   * Gets connections.
   *
   * @param userId the user id
   * @return the connections
   */
  @RequestMapping(value = "/{UID}/connections", method = RequestMethod.GET, produces = HAL_AND_JSON)
  public AbstractFileResource getConnections(@PathVariable("UID") String userId) {
    AbstractFileResource resource = null;

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("getRoot"));
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId)).withRel("getUserInfo"));
    links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withRel("getSpaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withRel("getDocuments"));
    links.add(linkTo(methodOn(UserController.class).getActivities(userId)).withRel("getActivities"));
    links.add(linkTo(methodOn(UserController.class).postActivity(userId)).withRel("postActivity"));
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, ACTIVITY_ID)).withRel("getActivity"));

    return resource;
  }

  /**
   * Gets spaces.
   *
   * @param userId the user id
   * @return the spaces
   */
  @RequestMapping(value = "/{UID}/spaces", method = RequestMethod.GET, produces = HAL_AND_JSON)
  public AbstractFileResource getSpaces(@PathVariable("UID") String userId) {
    AbstractFileResource resource = null;

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("getRoot"));
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId)).withRel("getUserInfo"));
    links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withRel("getConnections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withRel("getDocuments"));
    links.add(linkTo(methodOn(UserController.class).getActivities(userId)).withRel("getActivities"));
    links.add(linkTo(methodOn(UserController.class).postActivity(userId)).withRel("postActivity"));
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, ACTIVITY_ID)).withRel("getActivity"));

    return resource;
  }

  /**
   * Gets documents.
   *
   * @param userId the user id
   * @return the documents
   */
  @RequestMapping(value = "/{UID}/documents", method = RequestMethod.GET, produces = HAL_AND_JSON)
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
    links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("getRoot"));
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId)).withRel("getUserInfo"));
    links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withRel("getConnections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withRel("getSpaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getActivities(userId)).withRel("getActivities"));
    links.add(linkTo(methodOn(UserController.class).postActivity(userId)).withRel("postActivity"));
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, ACTIVITY_ID)).withRel("getActivity"));

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
  @RequestMapping(value = "/{UID}/activities", method = RequestMethod.GET, produces = HAL_AND_JSON)
  public AbstractFileResource getActivities(@PathVariable("UID") String userId) {
    AbstractFileResource resource = null;

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("getRoot"));
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId)).withRel("getUserInfo"));
    links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withRel("getConnections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withRel("getSpaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withRel("getDocuments"));
    links.add(linkTo(methodOn(UserController.class).getActivities(userId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).postActivity(userId)).withRel("postActivity"));
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, ACTIVITY_ID)).withRel("getActivity"));

    return resource;
  }

  /**
   * Post activity abstract file resource.
   *
   * @param userId the user id
   * @return the abstract file resource
   */
  @RequestMapping(value = "/{UID}/activity", method = RequestMethod.POST, produces = HAL_AND_JSON)
  public AbstractFileResource postActivity(@PathVariable("UID") String userId) {
    AbstractFileResource resource = null;

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("getRoot"));
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId)).withRel("getUserInfo"));
    links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withRel("getConnections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withRel("getSpaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withRel("getDocuments"));
    links.add(linkTo(methodOn(UserController.class).getActivities(userId)).withRel("getActivities"));
    links.add(linkTo(methodOn(UserController.class).postActivity(userId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, ACTIVITY_ID)).withRel("getActivity"));

    return resource;
  }

  /**
   * Gets activity.
   *
   * @param userId the user id
   * @param activityId the activity id
   * @return the activity
   */
  @RequestMapping(value = "/{UID}/activity/{AID}", method = RequestMethod.GET, produces = HAL_AND_JSON)
  public AbstractFileResource getActivity(@PathVariable("UID") String userId, @PathVariable("AID") String activityId) {
    AbstractFileResource resource = null;

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("getRoot"));
    links.add(linkTo(methodOn(UserController.class).getUserInfo(userId)).withRel("getUserInfo"));
    links.add(linkTo(methodOn(UserController.class).getConnections(userId)).withRel("getConnections"));
    links.add(linkTo(methodOn(UserController.class).getSpaces(userId)).withRel("getSpaces"));
    links.add(linkTo(methodOn(UserController.class).getDocuments(userId)).withRel("getDocuments"));
    links.add(linkTo(methodOn(UserController.class).getActivities(userId)).withRel("getActivities"));
    links.add(linkTo(methodOn(UserController.class).postActivity(userId)).withRel("postActivity"));
    links.add(linkTo(methodOn(UserController.class).getActivity(userId, activityId)).withSelfRel());

    return resource;
  }

}
