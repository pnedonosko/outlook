package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.outlook.utils.PortalLocaleUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * The Outlook app controller.
 */
@Controller
@RequestMapping(value = "/app")
public class OutlookAppController {

  /** The Constant LOG. */
  private static final Log      LOG            = ExoLogger.getLogger(OutlookAppController.class);

  /**
   * The constant OUTLOOK.
   */
  protected static final String OUTLOOK        = "outlook";

  /**
   * The constant OUTLOOK_SPRING.
   */
  protected static final String OUTLOOK_SPRING = "app";

  /**
   * Gets manifest URLs response.
   *
   * @param model the model
   * @param command the command
   * @param request the request
   * @return the react base page
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public String getManifestURLsResponse(Model model,
                                        @RequestParam(value = "command") String command,
                                        HttpServletRequest request) {

    /*
     * "$BASE_URL/outlook/app" "$BASE_URL/outlook/app?command=search"
     * "$BASE_URL/outlook/app?command=userInfoRead"
     * "$BASE_URL/outlook/app?command=userInfoCompose"
     * "$BASE_URL/outlook/app?command=postStatus"
     * "$BASE_URL/outlook/app?command=startDiscussion"
     * "$BASE_URL/outlook/app?command=addAttachment"
     * "$BASE_URL/outlook/app?command=saveAttachment"
     * "$BASE_URL/outlook/app?command=convertToStatus"
     * "$BASE_URL/outlook/app?command=convertToWiki"
     * "$BASE_URL/outlook/app?command=convertToForum"
     */

    String baseName = getBaseName(request);
    model.addAttribute("baseName", baseName);
    model.addAttribute("currentRestContextName",
                       new StringBuilder(baseName).append("/").append(PortalContainer.getCurrentRestContextName()).toString());

    // User locale
    Locale userLocale = request.getLocale();
    userLocale = PortalLocaleUtils.getCurrentUserLocale(userLocale);

    String language = userLocale.getLanguage();
    String country = userLocale.getCountry();
    if (country != null && country.length() > 0) {
      language = new StringBuilder(language).append("_").append(country).toString();
    }

    model.addAttribute("language", language);
    model.addAttribute("outlook", new StringBuilder(baseName).append("/").append(OUTLOOK).toString());
    model.addAttribute("spring",
                       new StringBuilder(baseName).append("/").append(OUTLOOK).append("/").append(OUTLOOK_SPRING).toString());

    return "index.html";
  }

  private static String getBaseName(HttpServletRequest request) {
    StringBuilder baseBuilder = new StringBuilder(request.getScheme());
    baseBuilder.append("://");
    baseBuilder.append(request.getServerName());
    baseBuilder.append(":");
    baseBuilder.append(request.getServerPort());
    return baseBuilder.toString();
  }
}
