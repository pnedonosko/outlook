package org.exoplatform.outlook.app.rest.service;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.OutlookSpace;
import org.exoplatform.outlook.OutlookSpaceException;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.jcr.RepositoryException;

/**
 * The type Space service.
 */
@Service
public class SpaceService {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(SpaceService.class);

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

  public String getGroupId(String spaceId) {
    return new StringBuilder("/spaces/").append(spaceId).toString();
  }
}
