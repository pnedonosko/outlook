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
  public ResourceSupport getRootDiscoveryOfOutlookMailServices() {
    ResourceSupport resource = new ResourceSupport();

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(RootDiscoveryMailServiceController.class).getRootDiscoveryOfOutlookMailServices()).withSelfRel());
    links.add(linkTo(methodOn(RootDiscoveryeXoServiceController.class).getRootDiscoveryOfOutlookExoServices()).withRel("exoServices"));
    links.add(linkTo(methodOn(MessageController.class).getRoot()).withRel("messageServices"));
    resource.add(links);

    return resource;
  }
}
