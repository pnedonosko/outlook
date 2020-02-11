package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.outlook.mvc.service.LoginService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/")
public class LoginController {

  private static final Log   LOG             = ExoLogger.getLogger(LoginController.class);

  /** The Constant OUTLOOK_LOGIN_TEMPLATE. */
  public static final String OUTLOOK_LOGIN = "/outlook/login";

  private LoginService       loginService;

  @Autowired
  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

    LOG.warn("[LoginController logout()]: start "); // TODO why warn? is it a problem like event?

    loginService.logout(request, response);

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("unregistered-user");
    response.sendRedirect(OUTLOOK_LOGIN);
    LOG.warn("[LoginController logout()]: finish ");
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public String login(HttpServletRequest request, HttpServletResponse response) {

    LOG.warn("[LoginController login()] "); // TODO why warn? is it a problem like event?

    return "login.html";
  }
}
