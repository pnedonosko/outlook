package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.ResourceSupport;

/**
 * The type Activity info.
 */
public class ActivityInfo extends ResourceSupport {

  /** The Constant LOG. */
  private static final Log                                 LOG = ExoLogger.getLogger(ActivityInfo.class);

  private final org.exoplatform.outlook.model.ActivityInfo activityInfo;

  /**
   * Instantiates a new Activity info.
   *
   * @param activityInfo the activity info
   */
  public ActivityInfo(org.exoplatform.outlook.model.ActivityInfo activityInfo) {
    this.activityInfo = activityInfo;
  }

  /**
   * Gets activity info.
   *
   * @return the activity info
   */
  @JsonProperty("activityInfo")
  public org.exoplatform.outlook.model.ActivityInfo getActivityInfo() {
    return activityInfo;
  }
}
