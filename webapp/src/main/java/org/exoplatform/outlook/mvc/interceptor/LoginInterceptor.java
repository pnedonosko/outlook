package org.exoplatform.outlook.mvc.interceptor;

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.IdentityConstants;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

  /** The Constant LOG. */
  protected static final Logger LOG                    = LoggerFactory.getLogger(LoginInterceptor.class);

  /** The Constant OUTLOOK_LOGIN. */
  public static final String    OUTLOOK_LOGIN          = "/outlook/login";

  /** The Constant OUTLOOK_LOGIN_TEMPLATE. */
  public static final String    OUTLOOK_LOGIN_TEMPLATE = "/outlook/login?target=_url_";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    ConversationState convo = ConversationState.getCurrent();
    if (convo != null) {
      String currentUserId = convo.getIdentity().getUserId();

      if (!IdentityConstants.ANONIM.equals(currentUserId) || OUTLOOK_LOGIN.equals(request.getRequestURI())) {
        return true;
      } else {
        response.sendRedirect(OUTLOOK_LOGIN_TEMPLATE.replace("_url_", getRequestString(request)));
        LOG.warn("Unregistered user");
      }
    } else {
      response.sendRedirect(OUTLOOK_LOGIN_TEMPLATE.replace("_url_", getRequestString(request)));
      LOG.warn("ConversationState is null");
    }
    return false;
  }

  private String getRequestString(HttpServletRequest request) {
    StringBuilder requestStringBuilder = new StringBuilder(request.getRequestURI());
    if (request.getQueryString() != null) {
      requestStringBuilder.append("?").append(request.getQueryString());
    }
    return requestStringBuilder.toString();
  }
}
