package org.exoplatform.outlook.app.rest.dto;

import org.springframework.hateoas.ResourceSupport;

/**
 * The type User info.
 */
public class UserInfo extends ResourceSupport {

  /**
   * The User info.
   */
  protected final org.exoplatform.outlook.model.UserInfo userInfo;

  /**
   * Instantiates a new User info.
   *
   * @param userInfo the user info
   */
  public UserInfo(org.exoplatform.outlook.model.UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  /**
   * Gets user info.
   *
   * @return the user info
   */
  public org.exoplatform.outlook.model.UserInfo getUserInfo() {
    return userInfo;
  }
}
