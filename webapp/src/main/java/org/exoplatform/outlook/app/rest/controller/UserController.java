package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.outlook.app.rest.dto.AbstractFileResource;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

  /**
   * Gets root.
   *
   * @return the root
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = HAL_AND_JSON)
  public AbstractFileResource getRoot() {
    AbstractFileResource resource = null;
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
    AbstractFileResource resource = null;
    return resource;
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
    return resource;
  }

}
