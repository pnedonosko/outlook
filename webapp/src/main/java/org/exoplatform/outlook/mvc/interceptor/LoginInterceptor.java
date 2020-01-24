package org.exoplatform.outlook.mvc.interceptor;

import org.exoplatform.services.security.ConversationState;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

  /** The Constant LOG. */
  protected static final Logger LOG                  = LoggerFactory.getLogger(LoginInterceptor.class);

  /** The Constant OUTLOOK_UNREGISTERED. */
  public static final String    OUTLOOK_UNREGISTERED = "/portal/login";

  /** The Constant OUTLOOK_COOKIE. */
  public static final String    OUTLOOK_COOKIE       = "outlook-login";

  /** The Constant UNREGISTERED_ID. */
  public static final String    UNREGISTERED_ID      = "__anonim";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    ConversationState convo = ConversationState.getCurrent();
    if (convo != null) {
      String currentUserId = convo.getIdentity().getUserId();

      if (!UNREGISTERED_ID.equals(currentUserId)) {
        return true;
      } else {
        response.sendRedirect(OUTLOOK_UNREGISTERED);
        LOG.warn("ConversationState is null");
      }
    } else {
      response.sendRedirect(OUTLOOK_UNREGISTERED);
      LOG.warn("ConversationState is null");
    }
    return false;
  }
}
