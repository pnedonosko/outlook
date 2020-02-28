package org.exoplatform.outlook.app.rest.service;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;

import org.exoplatform.outlook.model.ActivityInfo;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.service.LinkProvider;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.ContentHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Activity service.
 */
@Service
public class ActivityService {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(ActivityService.class);

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
      InputStream content = new ByteArrayInputStream(a.getTitle().getBytes(clientCs));
      String titleText;
      try {
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

}
