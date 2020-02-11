package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.outlook.mvc.service.TestService;
import org.exoplatform.outlook.portlet.IdentityInfo;
import org.exoplatform.outlook.utils.PortalLocaleUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/app")
public class OutlookAppController {

  private static final Log    log            = ExoLogger.getLogger(OutlookAppController.class);

  private TestService         testService;

  private static final String OUTLOOK        = "outlook";

  private static final String OUTLOOK_SPRING = "app";

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

    model.addAttribute("baseName", getBaseName(request));
    model.addAttribute("currentRestContextName", PortalContainer.getCurrentRestContextName());

    // User locale
    Locale userLocale = request.getLocale();
    userLocale = PortalLocaleUtils.getCurrentUserLocale(userLocale);

    String language = userLocale.getLanguage();
    String country = userLocale.getCountry();
    if (country != null && country.length() > 0) {
      language += "_" + country; // TODO use StringBuilder it's quicker
    }

    model.addAttribute("language", language);
    model.addAttribute("outlook", OUTLOOK);
    model.addAttribute("spring", OUTLOOK_SPRING);

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
