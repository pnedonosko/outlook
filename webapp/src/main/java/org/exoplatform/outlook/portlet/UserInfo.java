/*
 * Copyright (C) 2003-2019 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.outlook.portlet;

import org.exoplatform.services.organization.User;
import org.exoplatform.social.common.RealtimeListAccess;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.manager.ActivityManager;

import java.util.List;

/**
 * User info acts as a facade on top of eXo organization user and its data in
 * Social.<br>
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:pnedonosko@exoplatform.com">Peter Nedonosko</a>
 * @version $Id: UserInfo.java 00000 Jul 16, 2019 pnedonosko $
 */
public class UserInfo {

  private final User     orgUser;

  private final Identity identity;

  private final ActivityManager socialActivityManager;

  /**
   * Instantiates a new user info.
   * @param orgUser the org user
   * @param identity the identity
   * @param socialActivityManager the user social activity
   */
  public UserInfo(User orgUser, Identity identity, ActivityManager socialActivityManager) {
    this.orgUser = orgUser;
    this.identity = identity;
    this.socialActivityManager = socialActivityManager;
  }

  public String getFirstName() {
    return orgUser.getFirstName();
  }

  public String getLastName() {
    return orgUser.getLastName();
  }

  public String getDisplayName() {
    return orgUser.getDisplayName();
  }

  public String getPosition() {
    return identity.getProfile().getPosition();
  }

  //???????????????????????????????????????????????????????????
  public List<ExoSocialActivity> getUserSocialActyvityes(){
    RealtimeListAccess<ExoSocialActivity> activity = (RealtimeListAccess<ExoSocialActivity>) socialActivityManager
            .getActivity(identity.getId());
    List<ExoSocialActivity> exoSocialActivityList = activity.loadAsList(0, 20);
    return exoSocialActivityList;
  }

  @Deprecated // TODO remove it, everything required should be exposed via this class methods
  public Profile getProfile() {
    return identity.getProfile();
  }

//  public String getTitle(){
//    ResourceBundle resourceBundle = ResourceBundle.getBundle("title");
//    return resourceBundle.getString("title");
//  }

}
