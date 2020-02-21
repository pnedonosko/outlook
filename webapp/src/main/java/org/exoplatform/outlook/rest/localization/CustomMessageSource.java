package org.exoplatform.outlook.rest.localization;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.utils.PortalLocaleUtils;
import org.exoplatform.services.resources.ResourceBundleService;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.*;

/**
 * The wrapper for ResourceBundleService.
 */
public class CustomMessageSource implements MessageSource {

  private static final String[] RESOURCE_BUNDLE_NAMES = { "locale.outlook.Login", "locale.outlook.Outlook" };

  @Override
  public String getMessage(String code, Object[] objects, String defaultMessage, Locale locale) {
    String message = getMessage(code, locale);

    if (message == null) {
      message = defaultMessage;
    }
    return message;
  }

  @Override
  public String getMessage(String code, Object[] objects, Locale locale) throws NoSuchMessageException {
    String message = getMessage(code, locale);

    if (message == null) {
      throw new NoSuchMessageException(code, locale);
    }
    return message;
  }

  @Override
  public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
    String message = null;

    for (String code : messageSourceResolvable.getCodes()) {
      message = getMessage(code, locale);
      if (message != null) {
        break;
      }
    }

    if (message == null) {
      if (messageSourceResolvable.getDefaultMessage() != null) {
        message = messageSourceResolvable.getDefaultMessage();
      } else {
        throw new NoSuchMessageException(messageSourceResolvable.getCodes()[0], locale);
      }
    }
    return message;
  }

  private String getMessage(String code, Locale locale) {
    String message = null;

    locale = PortalLocaleUtils.getCurrentUserLocale(locale);

    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    ResourceBundleService resourceBundleService =
                                                (ResourceBundleService) currentContainer.getComponentInstance(ResourceBundleService.class);

    ResourceBundle resourceBundle;
    for (String resourceBundleName : RESOURCE_BUNDLE_NAMES) {
      resourceBundle = resourceBundleService.getResourceBundle(resourceBundleName, locale);
      if (resourceBundle.containsKey(code)) {
        message = resourceBundle.getString(code);
        break;
      }
    }

    return message;
  }
}
