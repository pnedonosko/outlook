package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.OutlookSpace;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.jcr.RepositoryException;
import java.util.Objects;

/**
 * The type Space.
 */
public class Space {

  /** The Constant LOG. */
  private static final Log     LOG = ExoLogger.getLogger(Space.class);

  /**
   * The Outlook space.
   */
  protected final OutlookSpace outlookSpace;

  /**
   * Instantiates a new Outlook space.
   *
   * @param outlookSpace the outlook space
   */
  public Space(OutlookSpace outlookSpace) {
    this.outlookSpace = outlookSpace;
  }

  /**
   * Gets group id.
   *
   * @return the group id
   */
  @JsonProperty("groupId")
  public String getGroupId() {
    return outlookSpace.getGroupId();
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  @JsonProperty("title")
  public String getTitle() {
    return outlookSpace.getTitle();
  }

  /**
   * Gets root path.
   *
   * @return the root path
   */
  @JsonProperty("rootPath")
  public String getRootPath() {
    String rootPath = "";
    try {
      rootPath = outlookSpace.getRootFolder().getPath();
    } catch (OutlookException e) {
      LOG.error("Error getting a space root folder in order to get the root path (rootPath)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting a space root folder in order to get the root path (rootPath)");
    } catch (RepositoryException e) {
      LOG.error("Error getting a space root folder in order to get the root path (rootPath)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting a space root folder in order to get the root path (rootPath)");
    }
    return rootPath;
  }

  /**
   * Gets default folder path.
   *
   * @return the default folder path
   */
  @JsonProperty("defaultFolderPath")
  public String getDefaultFolderPath() {
    String defaultFolderPath = "";
    try {
      defaultFolderPath = outlookSpace.getRootFolder().getDefaultSubfolder().getPath();
    } catch (OutlookException e) {
      LOG.error("Error getting a space root folder in order to get the default subfolder path (defaultFolderPath)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting a space root folder in order to get the default subfolder path (defaultFolderPath)");
    } catch (RepositoryException e) {
      LOG.error("Error getting a space root folder in order to get the default subfolder path (defaultFolderPath)", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Error getting a space root folder in order to get the default subfolder path (defaultFolderPath)");
    }
    return defaultFolderPath;
  }

  @JsonProperty("fullRootPath")
  public String getFullRootParh() {
    String defaultFolderPath = "";
    try {
      defaultFolderPath = outlookSpace.getRootFolder().getFullPath();
    } catch (OutlookException e) {
      LOG.error("Error getting a full root path in the outlook space", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a full root path in the outlook space");
    } catch (RepositoryException e) {
      LOG.error("Error getting a full root path in the outlook space", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a full root path in the outlook space");
    }
    return defaultFolderPath;
  }

  @JsonProperty("rootLabel")
  public String getRootLabel() {
    String defaultFolderPath = "";
    try {
      defaultFolderPath = outlookSpace.getRootFolder().getFullPath();
    } catch (OutlookException e) {
      LOG.error("Error getting a root label in the outlook space", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a root label in the outlook space");
    } catch (RepositoryException e) {
      LOG.error("Error getting a root label in the outlook space", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting a root label in the outlook space");
    }
    return defaultFolderPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Space space = (Space) o;
    return Objects.equals(outlookSpace, space.outlookSpace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outlookSpace);
  }
}
