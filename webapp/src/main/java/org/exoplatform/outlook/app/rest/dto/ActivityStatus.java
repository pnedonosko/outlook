package org.exoplatform.outlook.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * The type Activity status.
 */
public class ActivityStatus extends ResourceSupport {

  private final org.exoplatform.outlook.model.ActivityStatus activityStatus;

  /**
   * Instantiates a new Activity status.
   *
   * @param userName the user name
   * @param spaceName the space name
   * @param link the link
   */
  public ActivityStatus(String userName, String spaceName, String link) {
    this.activityStatus = new org.exoplatform.outlook.model.ActivityStatus(userName, spaceName, link);
  }

  /**
   * Instantiates a new Activity status.
   *
   * @param activityStatus the activity status
   */
  public ActivityStatus(org.exoplatform.outlook.model.ActivityStatus activityStatus) {
    this.activityStatus = activityStatus;
  }

  /**
   * Gets activity status.
   *
   * @return the activity status
   */
  @JsonProperty("activityStatus")
  public org.exoplatform.outlook.model.ActivityStatus getActivityStatus() {
    return activityStatus;
  }
}
