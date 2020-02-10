package org.exoplatform.outlook.utils;

import org.apache.commons.lang3.LocaleUtils;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.portal.Constants;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.security.ConversationState;

import java.util.Locale;

public class PortalLocaleUtils {

  private static final Log LOG = ExoLogger.getLogger(PortalLocaleUtils.class);

  /**
   * Get user locale settings (overwrite request locale)
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
   * Helper method to retrieve user locale from UserProfile
   * 
   * @param userId
   * @return user locale
   */
  private static Locale getUserLocale(String userId) {
    String lang = "";
    UserProfile profile = null;
    //
    if (userId != null) {
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
      if (profile != null) {
        lang = profile.getAttribute(Constants.USER_LANGUAGE);
      }
      if (lang != null && lang.trim().length() > 0) {
        return LocaleUtils.toLocale(lang);
      }
    }
    return null;
  }

  /**
   * Begin request life cycle for OrganizationService
   * 
   * @param orgService
   */
  private static void beginContext(OrganizationService orgService) {
    if (orgService instanceof ComponentRequestLifecycle) {
      RequestLifeCycle.begin((ComponentRequestLifecycle) orgService);
    }
  }

  /**
   * End RequestLifeCycle for OrganizationService
   * 
   * @param orgService
   */
  private static void endContext(OrganizationService orgService) {
    if (orgService instanceof ComponentRequestLifecycle) {
      RequestLifeCycle.end();
    }
  }
}
