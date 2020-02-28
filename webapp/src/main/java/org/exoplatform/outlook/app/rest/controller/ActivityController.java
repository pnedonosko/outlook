package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.model.ActivityInfo;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.service.LinkProvider;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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
   * Gets root.
   *
   * @return the root
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getRoot() {
    ResourceSupport resource = new ResourceSupport();

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(RootDiscoveryeXoServiceController.class).getRootDiscoveryOfOutlookExoServices()).withRel("parent"));
    links.add(linkTo(methodOn(ActivityController.class).getRoot()).withSelfRel());
    links.add(linkTo(methodOn(ActivityController.class).getActivityInfo(OutlookConstant.ACTIVITY_ID)).withRel("activity"));

    resource.add(links);

    return resource;
  }

  /**
   * Gets activity info.
   *
   * @param activityId the activity id
   * @return the activity info
   */
  @RequestMapping(value = "/{AID}", method = RequestMethod.GET, produces = OutlookConstant.HAL_AND_JSON)
  public ResourceSupport getActivityInfo(@PathVariable("AID") String activityId) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    ActivityManager activityManager = (ActivityManager) currentContainer.getComponentInstance(ActivityManager.class);

    ExoSocialActivity activity = activityManager.getActivity(activityId);

    ActivityInfo activityInfo = new ActivityInfo(activity.getTitle(),
                                                 activity.getType(),
                                                 LinkProvider.getSingleActivityUrl(activity.getId()),
                                                 activity.getPostedTime());
    org.exoplatform.outlook.app.rest.dto.ActivityInfo activityInfoDTO =
                                                                      new org.exoplatform.outlook.app.rest.dto.ActivityInfo(activityInfo);

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(ActivityController.class).getRoot()).withRel("parent"));
    links.add(linkTo(methodOn(ActivityController.class).getActivityInfo(activityId)).withSelfRel());
    activityInfoDTO.add(links);

    return activityInfoDTO;
  }
}
