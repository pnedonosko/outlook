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
 * The type Space controller.
 */
@RestController
@RequestMapping(value = "/v2/exo/space")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class SpaceController {

  /** The Constant LOG. */
  private static final Log    LOG          = ExoLogger.getLogger(SpaceController.class);

  /** The Constant HAL_AND_JSON. */
  private static final String HAL_AND_JSON = "application/hal+json";

  /**
   * Gets space.
   *
   * @param spaceId the space id
   * @return the space
   */
  @RequestMapping(value = "/{SID}", method = RequestMethod.GET, produces = HAL_AND_JSON)
  public AbstractFileResource getSpace(@PathVariable("SID") String spaceId) {
    AbstractFileResource resource = null;

    return resource;
  }
}
