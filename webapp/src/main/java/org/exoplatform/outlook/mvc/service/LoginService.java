package org.exoplatform.outlook.mvc.service;

import org.exoplatform.outlook.security.OutlookTokenService;
import org.exoplatform.outlook.web.RequestUtils;
import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.web.login.LoginServlet;
import org.exoplatform.web.login.LogoutControl;
import org.exoplatform.web.security.GateInToken;
import org.exoplatform.web.security.security.AbstractTokenService;
import org.exoplatform.web.security.security.CookieTokenService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class LoginService {

  private static final Log log = ExoLogger.getLogger(LoginService.class);

  public boolean login() {

    return true;
  }

  public boolean logout(HttpServletRequest req, HttpServletResponse res) {
    // XXX repeating logic of UIPortal.LogoutActionListener

    // Delete the token from JCR
    String token = RequestUtils.getCookie(req, LoginServlet.COOKIE_NAME); // getTokenCookie(req)
    if (token != null) {
      AbstractTokenService<GateInToken, String> tokenService = AbstractTokenService.getInstance(CookieTokenService.class);
      tokenService.deleteToken(token);
    }
    token = LoginServlet.getOauthRememberMeTokenCookie(req);
    if (token != null) {
      AbstractTokenService<GateInToken, String> tokenService = AbstractTokenService.getInstance(CookieTokenService.class);
      tokenService.deleteToken(token);
    }

    LogoutControl.wantLogout();
    Cookie cookie = new Cookie(LoginServlet.COOKIE_NAME, "");
    cookie.setPath(req.getContextPath());
    cookie.setMaxAge(0);
    res.addCookie(cookie);

    Cookie oauthCookie = new Cookie(LoginServlet.OAUTH_COOKIE_NAME, "");
    oauthCookie.setPath(req.getContextPath());
    oauthCookie.setMaxAge(0);
    res.addCookie(oauthCookie);

    // **********
    // Outlook add-in logout (cookies)
    String rememberMeOutlook = RequestUtils.getCookie(req, OutlookTokenService.COOKIE_NAME);
    if (rememberMeOutlook != null) {
      OutlookTokenService outlookTokens = AbstractTokenService.getInstance(OutlookTokenService.class);
      outlookTokens.deleteToken(rememberMeOutlook);
    }
    Cookie rememberMeOutlookCookie = new Cookie(OutlookTokenService.COOKIE_NAME, "");
    rememberMeOutlookCookie.setPath(req.getRequestURI());
    rememberMeOutlookCookie.setMaxAge(0);
    res.addCookie(rememberMeOutlookCookie);
    return true;
  }
}
