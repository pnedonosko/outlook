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
 * The type Space controller.
 */
@RestController
@RequestMapping(value = "/v2/exo/space")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class SpaceController {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(SpaceController.class);

  /**
   * Gets space.
   *
   * @param spaceId the space id
   * @return the space
   */
  @RequestMapping(value = "/{SID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpace(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets space activities.
   *
   * @param spaceId the space id
   * @return the space activities
   */
  @RequestMapping(value = "/{SID}/activities", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaceActivities(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Post space activity abstract file resource.
   *
   * @param spaceId the space id
   * @return the abstract file resource
   */
  @RequestMapping(value = "/{SID}/activity", method = RequestMethod.POST, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource postSpaceActivity(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets activity info.
   *
   * @param spaceId the space id
   * @param activityId the activity id
   * @return the activity info
   */
  @RequestMapping(value = "/{SID}/activity/{AID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getActivityInfo(@PathVariable("SID") String spaceId, @PathVariable("AID") String activityId) {
    AbstractFileResource resource = null;

    return resource;
  }

  /**
   * Gets space documents.
   *
   * @param spaceId the space id
   * @return the space documents
   */
  @RequestMapping(value = "/{SID}/documents", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getSpaceDocuments(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    return resource;
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

    return resource;
  }
}
