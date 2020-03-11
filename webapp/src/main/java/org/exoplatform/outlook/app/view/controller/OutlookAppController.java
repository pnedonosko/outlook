package org.exoplatform.outlook.app.view.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.outlook.utils.PortalLocaleUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.IdentityConstants;

/**
 * The Outlook app controller.
 */
@Controller
@RequestMapping(value = "/view")
public class OutlookAppController {

  /** The Constant LOG. */
  private static final Log      LOG         = ExoLogger.getLogger(OutlookAppController.class);

  /**
   * The constant OUTLOOK.
   */
  protected static final String OUTLOOK     = "outlook";

  /**
   * The constant OUTLOOK_APP.
   */
  protected static final String OUTLOOK_APP = "app";

  /**
   * Gets manifest URLs response.
   *
   * @param model the model
   * @param command the command
   * @param request the request
   * @return the react base page
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public String getIndex(Model model, @RequestParam(value = "command") String command, HttpServletRequest request) {

    /*
     * "$BASE_URL/outlook/app" "$BASE_URL/outlook/app?command=search" "$BASE_URL/outlook/app?command=userInfoRead"
     * "$BASE_URL/outlook/app?command=userInfoCompose" "$BASE_URL/outlook/app?command=postStatus"
     * "$BASE_URL/outlook/app?command=startDiscussion" "$BASE_URL/outlook/app?command=addAttachment"
     * "$BASE_URL/outlook/app?command=saveAttachment" "$BASE_URL/outlook/app?command=convertToStatus"
     * "$BASE_URL/outlook/app?command=convertToWiki" "$BASE_URL/outlook/app?command=convertToForum"
     */

    String baseUrl = getBaseURL(request);
    model.addAttribute("baseName", baseUrl);

    ConversationState convo = ConversationState.getCurrent();
    if (convo != null && convo.getIdentity() != null) {
      model.addAttribute("userName", convo.getIdentity().getUserId());
    } else {
      model.addAttribute("userName", IdentityConstants.ANONIM);
    }

    model.addAttribute("command", command);

    model.addAttribute("exoRestBaseURL",
                       new StringBuilder(baseUrl).append("/").append(PortalContainer.getCurrentRestContextName()).toString());

    CharSequence contextURL = new StringBuilder(baseUrl).append("/").append(OUTLOOK);
    model.addAttribute("contextURL", contextURL.toString());
    CharSequence appBaseURL = new StringBuilder(contextURL).append("/").append(OUTLOOK_APP);
    model.addAttribute("appBaseURL", appBaseURL.toString());
    CharSequence appRestURL = new StringBuilder(contextURL).append("/v2");
    model.addAttribute("restUserURL", new StringBuilder(appRestURL).append("/exo/user").toString());
    model.addAttribute("restSpaceURL", new StringBuilder(appRestURL).append("/exo/space").toString());
    model.addAttribute("restActivityURL", new StringBuilder(appRestURL).append("/exo/activity").toString());
    model.addAttribute("restDocumentURL", new StringBuilder(appRestURL).append("/exo/document").toString());
    model.addAttribute("restMailURL", new StringBuilder(appRestURL).append("/mail/message").toString());

    // User locale
    Locale userLocale = request.getLocale();
    userLocale = PortalLocaleUtils.getCurrentUserLocale(userLocale);

    String language = userLocale.getLanguage();
    String country = userLocale.getCountry();
    if (country != null && country.length() > 0) {
      language = new StringBuilder(language).append("_").append(country).toString();
    }
    model.addAttribute("language", language);

    return "index.html";
  }

  private static String getBaseURL(HttpServletRequest request) {
    StringBuilder baseBuilder = new StringBuilder(request.getScheme());
    baseBuilder.append("://");
    baseBuilder.append(request.getServerName());
    baseBuilder.append(":");
    baseBuilder.append(request.getServerPort());
    return baseBuilder.toString();
  }
}
