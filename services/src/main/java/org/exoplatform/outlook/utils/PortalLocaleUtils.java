/*
 * Copyright (C) 2003-2020 eXo Platform SAS.
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
package org.exoplatform.outlook.utils;

import org.apache.commons.lang3.LocaleUtils;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.portal.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.security.ConversationState;

import java.util.Locale;

/**
 * The Class PortalLocaleUtils.
 */
public class PortalLocaleUtils {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(PortalLocaleUtils.class);

  /**
   * Get user locale settings (may overwrite given locale).
   *
   * @param locale the locale to use by default
   * @return the current user locale to use
   */
  public static Locale getCurrentUserLocale(Locale locale) {
    ConversationState convo = ConversationState.getCurrent();
    if (convo != null) {
      String currentUserId = convo.getIdentity().getUserId();
      Locale localeSetting = PortalLocaleUtils.getUserLocale(currentUserId);
      if (localeSetting != null) {
        locale = localeSetting;
      }
    }
    return locale;
  }

  /**
   * Helper method to retrieve user locale from UserProfile.
   *
   * @param userId the user id
   * @return user locale
   */
  private static Locale getUserLocale(String userId) {
    if (userId != null) {
      UserProfile profile = null;
      OrganizationService organizationService = ExoContainerContext.getCurrentContainer()
                                                                   .getComponentInstanceOfType(OrganizationService.class);
      // get user profile
      beginContext(organizationService);
      try {
        profile = organizationService.getUserProfileHandler().findUserProfileByName(userId);
      } catch (Exception e) {
        LOG.debug(userId + " profile not found ", e);
      } finally {
        endContext(organizationService);
      }
      // fetch profile lang
      String lang = null;
      if (profile != null) {
        lang = profile.getAttribute(Constants.USER_LANGUAGE);
      }
      if (lang != null && lang.trim().length() > 0) {
        return LocaleUtils.toLocale(lang); // TODO don't use third party, just JVM here
      }
    }
    return null;
  }

  /**
   * Begin request life cycle for OrganizationService.
   *
   * @param orgService the org service
   */
  private static void beginContext(OrganizationService orgService) {
    if (orgService instanceof ComponentRequestLifecycle) {
      RequestLifeCycle.begin((ComponentRequestLifecycle) orgService);
    }
  }

  /**
   * End RequestLifeCycle for OrganizationService.
   *
   * @param orgService the org service
   */
  private static void endContext(OrganizationService orgService) {
    if (orgService instanceof ComponentRequestLifecycle) {
      RequestLifeCycle.end();
    }
  }
}
