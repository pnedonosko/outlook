package org.exoplatform.outlook.mvc.interceptor;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.mvc.model.SpringContext;
import org.exoplatform.outlook.mvc.model.CustomMessageSource;
import org.exoplatform.services.resources.ResourceBundleService;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Handles user requests and adds locales
 * (from ResourceBundleService as List<ResourceBundle>)
 * to Spring Message Source if not added.
 */
public class LocaleInitInterceptor extends HandlerInterceptorAdapter {

  /** The Constant LOG. */
  protected static final Logger LOG = LoggerFactory.getLogger(LocaleInitInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    CustomMessageSource customMessageSource = getMessageSource();
    if (!customMessageSource.containsLocale(request.getLocale())) {
      ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
      if (currentContainer != null) {
        ResourceBundleService resourceBundleService =
                                                    (ResourceBundleService) currentContainer.getComponentInstance(ResourceBundleService.class);
        if (resourceBundleService != null) {
          List<ResourceBundle> resourceBundles = new LinkedList<>();
          resourceBundles.add(resourceBundleService.getResourceBundle("locale.outlook.Login", request.getLocale()));
          resourceBundles.add(resourceBundleService.getResourceBundle("locale.outlook.Outlook", request.getLocale()));
          customMessageSource.addLocale(request.getLocale(), resourceBundles);

          if (LOG.isDebugEnabled()) {
            LOG.debug("Added locale: " + request.getLocale().getLanguage());
          }
        }
      }
    }
    return true;
  }

  private CustomMessageSource getMessageSource() {
    return SpringContext.getBean(CustomMessageSource.class);
  }
}
