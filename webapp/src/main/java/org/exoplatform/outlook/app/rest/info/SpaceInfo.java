package org.exoplatform.outlook.app.rest.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.OutlookSpace;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.jcr.RepositoryException;

/**
 * The Outlook space json.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceInfo {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(SpaceInfo.class);

  /**
   * The Group id.
   */
  @JsonProperty("groupId")
  String                   groupId;

  /**
   * The Title.
   */
  @JsonProperty("title")
  String                   title;

  /**
   * The Root folder path.
   */
  @JsonProperty("rootFolderPath")
  String                   rootFolderPath; // TODO rootPath

  /**
   * The Root folder default subfolder path.
   */
  @JsonProperty("rootFolderDefaultSubfolderPath")
  String                   rootFolderDefaultSubfolderPath; // TODO defaultFolderPath

  /**
   * Instantiates a new Outlook space json.
   *
   * @param outlookSpace the outlook space
   */
  public SpaceInfo(OutlookSpace outlookSpace) {
    this.groupId = outlookSpace.getGroupId();
    this.title = outlookSpace.getTitle();
    try {
      this.rootFolderPath = outlookSpace.getRootFolder().getPath();
    } catch (OutlookException e) {
      LOG.error(e);
    } catch (RepositoryException e) {
      LOG.error(e);
    }

    try {
      this.rootFolderDefaultSubfolderPath = outlookSpace.getRootFolder().getDefaultSubfolder().getPath();
    } catch (OutlookException e) {
      LOG.error(e);
    } catch (RepositoryException e) {
      LOG.error(e);
    }
  }
}
