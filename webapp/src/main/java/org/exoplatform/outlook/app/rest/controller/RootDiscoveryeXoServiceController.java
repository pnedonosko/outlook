package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.outlook.model.OutlookConstant;
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
 * The type Root discovery eXo service controller.
 */
@RestController
@RequestMapping(value = "/v2/exo")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class RootDiscoveryeXoServiceController extends AbstractController {

  /**
   * Gets root discovery of outlook mail services.
   *
   * @return the root discovery of outlook mail services
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getRootDiscoveryOfOutlookExoServices() {
    ResourceSupport resource = new ResourceSupport();

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(RootDiscoveryeXoServiceController.class).getRootDiscoveryOfOutlookExoServices()).withSelfRel());
    links.add(linkTo(methodOn(RootDiscoveryMailServiceController.class).getRootDiscoveryOfOutlookMailServices()).withRel("mailServices"));
    links.add(linkTo(methodOn(UserController.class).getRoot()).withRel("userServices"));
    links.add(linkTo(methodOn(SpaceController.class).getRoot()).withRel("spaceServices"));
    links.add(linkTo(methodOn(ActivityController.class).getRoot()).withRel("activityServices"));
    links.add(linkTo(methodOn(DocumentController.class).getRoot()).withRel("documentServices"));
    resource.add(links);

    return resource;
  }
}
