package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.outlook.mvc.service.TestService;
import org.exoplatform.outlook.portlet.IdentityInfo;
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

@Controller
@RequestMapping(value = "/app")
public class OutlookAppController {

  private static final Log log = ExoLogger.getLogger(OutlookAppController.class);

  private TestService      testService;

  @Autowired
  public void setTestService(TestService testService) {
    this.testService = testService;
  }

  @RequestMapping(value = "/test-thymeleaf", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public String getTestThymeleaf(Model model, @RequestParam(value = "email") String email) {

    log.warn("[TestController]: start ");

    ConversationState convo = ConversationState.getCurrent();
    if (convo != null) {
      String currentUserId = convo.getIdentity().getUserId();
      model.addAttribute("user", currentUserId);
      log.warn("currentUserId - " + currentUserId);
    } else {
      model.addAttribute("user", null);
      log.warn("ConversationState is null");
    }

    List<IdentityInfo> list = null;
    try {
      list = testService.findUsersByEmail(email, null);
    } catch (Exception e) {
      log.error("findUsersByEmail Exception", e);
    }

    if (list != null) {
      log.warn("[TestController]: list length: " + list.size());

      try {
        log.warn("[TestController]: getFullName " + list.toArray(new IdentityInfo[0])[0].getFullName());
        model.addAttribute("fullName", list.toArray(new IdentityInfo[0])[0].getFullName());
      } catch (Exception e) {
        log.warn("[TestController]: getFullName error ", e);
      }
    } else {
      log.warn("[TestController]: list is null ");
    }

    return "test.html";
  }

  @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public String getManifestURLsResponse(Model model, @RequestParam(value = "command") String command) {

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

    return "react/build/index.html";
  }

  @RequestMapping(value = "/js-properties", method = RequestMethod.GET)
  public String getJSProperties(Model model, HttpServletRequest request) {
    model.addAttribute("baseName", getBaseName(request));
    model.addAttribute("currentRestContextName", PortalContainer.getCurrentRestContextName());

    String language = request.getLocale().getLanguage();
    String country = request.getLocale().getCountry();
    if (country != null && country.length() > 0) {
      language += "_" + country;
    }

    model.addAttribute("language", language);
    return "properties.js";
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
