package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * The type Root app controller.
 */
@RestController
@RequestMapping(value = "/v2")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class RootAppController {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(DocumentController.class);

  /**
   * Gets root.
   *
   * @return the root
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getRoot() {
    ResourceSupport resource = new ResourceSupport();

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(RootAppController.class).getRoot()).withSelfRel());
    links.add(linkTo(methodOn(RootDiscoveryeXoServiceController.class).getRootDiscoveryOfOutlookExoServices()).withRel("exo"));
    links.add(linkTo(methodOn(RootDiscoveryMailServiceController.class).getRootDiscoveryOfOutlookMailServices()).withRel("mail"));
    resource.add(links);

    return resource;
  }
}
