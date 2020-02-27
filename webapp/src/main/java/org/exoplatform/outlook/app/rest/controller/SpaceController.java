package org.exoplatform.outlook.app.rest.controller;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.OutlookSpace;
import org.exoplatform.outlook.OutlookSpaceException;
import org.exoplatform.outlook.app.rest.dto.AbstractFileResource;
import org.exoplatform.outlook.app.rest.dto.FileResource;
import org.exoplatform.outlook.app.rest.dto.Space;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.jcr.RepositoryException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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
  public ResourceSupport getSpace(@PathVariable("SID") String spaceId) {

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);

    String spaceGroupId = new StringBuilder("/spaces/").append(spaceId).toString();

    OutlookSpace outlookSpace = null;
    try {
      outlookSpace = outlook.getSpace(spaceGroupId);
    } catch (OutlookSpaceException e) {
      LOG.error("Error getting space (" + spaceGroupId + ")", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting space (" + spaceGroupId + ")");
    } catch (RepositoryException e) {
      LOG.error("Error getting space (" + spaceGroupId + ")", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting space (" + spaceGroupId + ")");
    } catch (OutlookException e) {
      LOG.error("Error getting space (" + spaceGroupId + ")", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting space (" + spaceGroupId + ")");
    }

    List<Link> links = new LinkedList<>();
    links.add(linkTo(RootDiscoveryeXoServiceController.class).withRel("parent"));
    links.add(linkTo(methodOn(SpaceController.class).getSpace(spaceId)).withSelfRel());
    links.add(linkTo(methodOn(SpaceController.class).getSpaceActivities(spaceId)).withRel("activities"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceDocuments(spaceId)).withRel("documents"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceWiki(spaceId)).withRel("wiki"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceForum(spaceId)).withRel("forum"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceCalendar(spaceId)).withRel("calendar"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceMembers(spaceId)).withRel("members"));

    Space space = new Space(outlookSpace);
    space.add(links);

    return space;
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

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    SpaceService spaceService = (SpaceService) currentContainer.getComponentInstance(SpaceService.class);

    org.exoplatform.social.core.space.model.Space space = spaceService.getSpaceByPrettyName(spaceId);

    String[] spaceMembers = space.getMembers();

    List<String> membersList = Arrays.asList(spaceMembers);

    List<Link> links = new LinkedList<>();
    links.add(linkTo(methodOn(SpaceController.class).getSpace(spaceId)).withRel("parent"));
    links.add(linkTo(methodOn(SpaceController.class).getSpaceMembers(spaceId)).withSelfRel());
    links.add(linkTo(methodOn(UserController.class).getUserInfo(OutlookConstant.USER_ID)).withRel("user"));

    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(spaceMembers.length, 1, spaceMembers.length, 1);

    resource = new FileResource(metadata, membersList, links);

    return resource;
  }
}
