package org.exoplatform.outlook.app.rest.controller;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.*;
import org.exoplatform.outlook.model.ActivityInfo;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.service.LinkProvider;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.ContentHandler;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractController {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(AbstractController.class);

  // Activity

  /**
   * Convert to activity infos list.
   *
   * @param spaceActivities the space activities
   * @param userId the user id
   * @param request the request
   * @return the list
   */
  public List<ActivityInfo> convertToActivityInfos(List<ExoSocialActivity> spaceActivities,
                                                   String userId,
                                                   HttpServletRequest request) {

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    IdentityManager identityManager = (IdentityManager) currentContainer.getComponentInstance(IdentityManager.class);
    OrganizationService organization = (OrganizationService) currentContainer.getComponentInstance(OrganizationService.class);

    Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, userId, true);

    final Charset clientCs = loadEncoding(request.getCharacterEncoding());

    Set<String> currentUserGroupIds = null;
    try {
      currentUserGroupIds = organization.getMembershipHandler()
                                        .findMembershipsByUser(userId)
                                        .stream()
                                        .map(m -> m.getGroupId())
                                        .collect(Collectors.toSet());
    } catch (Exception e) {
      LOG.error("Error getting current user (" + userId + ") group ids", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting current user (" + userId + ") group ids");
    }

    // Use Apache Tika to parse activity title w/o HTML
    HtmlParser htmlParser = new HtmlParser();

    Set<String> finalCurrentUserGroupIds = currentUserGroupIds;
    List<ActivityInfo> activities = spaceActivities.stream().filter(a -> {
      String streamId = a.getStreamOwner();
      return streamId != null
          && (userIdentity.getRemoteId().equals(streamId) || finalCurrentUserGroupIds.contains(findSpaceGroupId(streamId)));
    }).map(a -> {
      // We want activity title in text (not HTML)
      ParseContext pcontext = new ParseContext();
      ContentHandler contentHandler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      String titleText;
      try (InputStream content = new ByteArrayInputStream(a.getTitle().getBytes(clientCs))) {
        htmlParser.parse(content, contentHandler, metadata, pcontext);
        titleText = cutText(contentHandler.toString(), 100);
      } catch (Exception e) {
        String rawTitle = cutText(a.getTitle(), 100);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Cannot parse activity title: '{}...'", rawTitle, e);
        }
        titleText = rawTitle;
      }
      return new org.exoplatform.outlook.model.ActivityInfo(titleText,
                                                            a.getType(),
                                                            LinkProvider.getSingleActivityUrl(a.getId()),
                                                            a.getPostedTime());
    }).collect(Collectors.toList());

    return activities;
  }

  private String cutText(String text, int limit) {
    return text.length() > limit ? text.substring(0, limit) : text;
  }

  private String findSpaceGroupId(String prettyName) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    SpaceService spaceService = (SpaceService) currentContainer.getComponentInstance(SpaceService.class);
    org.exoplatform.social.core.space.model.Space space = spaceService.getSpaceByPrettyName(prettyName);
    return space != null ? space.getGroupId() : "".intern();
  }

  private Charset loadEncoding(String name) {
    if (name == null || name.length() == 0) {
      name = "UTF8";
    }
    try {
      return Charset.forName(name);
    } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
      LOG.warn("Error loading client encoding charset {}: {}", name, e.toString());
      return Charset.defaultCharset();
    }
  }

  // Message

  /**
   * Build message.
   *
   * @param user the user
   * @param messageId the message id
   * @param fromEmail the from email
   * @param fromName the from name
   * @param created the created
   * @param modified the modified
   * @param title the title
   * @param subject the subject
   * @param body the body
   * @return the outlook message
   * @throws OutlookException the outlook exception
   * @throws ParseException the parse exception
   */
  public OutlookMessage buildMessage(OutlookUser user,
                                     String messageId,
                                     String fromEmail,
                                     String fromName,
                                     String created,
                                     String modified,
                                     String title,
                                     String subject,
                                     String body) throws OutlookException, ParseException {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);
    OutlookEmail from = outlook.getAddress(fromEmail, fromName);
    Calendar createdDate = Calendar.getInstance();
    createdDate.setTime(OutlookMessage.DATE_FORMAT.parse(created));
    Calendar modifiedDate = Calendar.getInstance();
    modifiedDate.setTime(OutlookMessage.DATE_FORMAT.parse(modified));
    OutlookMessage message = outlook.buildMessage(messageId, user, from, null, createdDate, modifiedDate, title, subject, body);
    return message;
  }

  // Pagination

  /**
   * Gets pagination metadata.
   *
   * @param offset the offset
   * @param limit the limit
   * @param availableElementsNumber the available elements number
   * @return the pagination metadata
   */
  public PagedResources.PageMetadata getPaginationMetadata(Integer offset, Integer limit, int availableElementsNumber) {
    int requestedDataSize = limit - offset;
    if (requestedDataSize <= 0) {
      LOG.error("Pagination error: offset - limit <= 0");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pagination error: offset - limit <= 0");
    }

    long pages = (long) Math.ceil((double) availableElementsNumber / requestedDataSize);
    int currentPostition = offset + 1;

    int responseSize = 0;
    if (offset < availableElementsNumber && limit <= availableElementsNumber) {
      responseSize = (availableElementsNumber - offset) - (availableElementsNumber - limit);
    } else if (offset < availableElementsNumber) {
      responseSize = availableElementsNumber - offset;
    }
    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(responseSize,
                                                                           currentPostition,
                                                                           availableElementsNumber,
                                                                           pages);
    return metadata;
  }

  // Space

  /**
   * Gets outlook space.
   *
   * @param spaceId the space id
   * @return the outlook space
   */
  public OutlookSpace getOutlookSpace(String spaceId) {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    OutlookService outlook = (OutlookService) currentContainer.getComponentInstance(OutlookService.class);

    String spaceGroupId = getGroupId(spaceId);

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
    return outlookSpace;
  }

  /**
   * Gets group id.
   *
   * @param spaceId the space id
   * @return the group id
   */
  public String getGroupId(String spaceId) {
    return new StringBuilder("/spaces/").append(spaceId).toString();
  }

  /**
   * Gets the request base uri.
   *
   * @param request the request
   * @return the request base uri
   */
  public String getRequestBaseURL(HttpServletRequest request) {
    URI requestUri = null;
    try {
      requestUri = new URI(request.getScheme(), null, request.getServerName(), request.getServerPort(), null, null, null);
    } catch (URISyntaxException e) {
      LOG.error("Error getting requestUri", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting requestUri");
    }
    return requestUri.toASCIIString();
  }
}
