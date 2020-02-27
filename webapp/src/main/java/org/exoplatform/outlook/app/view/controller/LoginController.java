package org.exoplatform.outlook.app.view.controller;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

  private static final Log   LOG           = ExoLogger.getLogger(LoginController.class);

  @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public String login(HttpServletRequest request, HttpServletResponse response) {
    return "login.html";
  }
}