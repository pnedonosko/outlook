package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.OutlookSpace;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.jcr.RepositoryException;

/**
 * The type Space dto.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceDTO {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(SpaceDTO.class);

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
   * The Root path.
   */
  @JsonProperty("rootPath")
  String                   rootPath;

  /**
   * The Default folder path.
   */
  @JsonProperty("defaultFolderPath")
  String                   defaultFolderPath;

  /**
   * Instantiates a new Outlook space json.
   *
   * @param outlookSpace the outlook space
   */
  public SpaceDTO(OutlookSpace outlookSpace) {
    this.groupId = outlookSpace.getGroupId();
    this.title = outlookSpace.getTitle();
    try {
      this.rootPath = outlookSpace.getRootFolder().getPath();
    } catch (OutlookException e) {
      LOG.error(e);
    } catch (RepositoryException e) {
      LOG.error(e);
    }

    try {
      this.defaultFolderPath = outlookSpace.getRootFolder().getDefaultSubfolder().getPath();
    } catch (OutlookException e) {
      LOG.error(e);
    } catch (RepositoryException e) {
      LOG.error(e);
    }
  }
}
