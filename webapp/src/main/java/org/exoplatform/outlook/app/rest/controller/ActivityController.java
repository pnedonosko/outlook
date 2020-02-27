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
 * The type Activity controller.
 */
@RestController
@RequestMapping(value = "/v2/exo/activity")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ActivityController {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(ActivityController.class);

  /**
   * Gets activity info.
   *
   * @param activityId the activity id
   * @return the activity info
   */
  @RequestMapping(value = "/{AID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getActivityInfo(@PathVariable("AID") String activityId) {
    AbstractFileResource resource = null;

    return resource;
  }
}
