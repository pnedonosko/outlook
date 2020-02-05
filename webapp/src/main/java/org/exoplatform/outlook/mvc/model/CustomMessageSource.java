package org.exoplatform.outlook.mvc.model;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps links to resource bundles and uses them as message source.
 */
public class CustomMessageSource implements MessageSource {

  Map<Locale, List<ResourceBundle>> concurrentLocales = new ConcurrentHashMap<>();

  public boolean containsLocale(Locale locale) {
    return concurrentLocales.containsKey(locale);
  }

  public void addLocale(Locale locale, List<ResourceBundle> resourceBundles) {
    this.concurrentLocales.put(locale, resourceBundles);
  }

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
    for (ResourceBundle resourceBundle : concurrentLocales.get(locale)) {
      if (resourceBundle.containsKey(code)) {
        message = resourceBundle.getString(code);
        break;
      }
    }
    return message;
  }
}
