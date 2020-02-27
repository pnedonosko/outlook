package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.outlook.app.rest.dto.AbstractFileResource;
import org.exoplatform.outlook.model.OutlookConstant;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Root discovery mail service controller.
 */
@RestController
@RequestMapping(value = "/v2/mail")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class RootDiscoveryMailServiceController {

  /**
   * Gets root discovery of outlook mail services.
   *
   * @return the root discovery of outlook mail services
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public AbstractFileResource getRootDiscoveryOfOutlookMailServices() {
    AbstractFileResource resource = null;

    return resource;
  }
}
