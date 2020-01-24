package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.outlook.mvc.service.LoginService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.web.login.LogoutControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/")
public class LoginController {

  private static final Log LOG = ExoLogger.getLogger(LoginController.class);

  private LoginService     loginService;

  @Autowired
  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {

    LOG.warn("[LoginController logout()]: start ");

    loginService.logout(request, response);

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("unregistered-user");

    LOG.warn("[LoginController logout()]: finish ");

    return modelAndView;
  }
}
