package org.exoplatform.outlook.server.filter;

import org.exoplatform.container.web.AbstractFilter;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.IdentityConstants;
import org.exoplatform.web.filter.Filter;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends AbstractFilter implements Filter {

  /** The Constant LOG. */
  protected static final Logger LOG                    = LoggerFactory.getLogger(LoginFilter.class);

  /** The Constant OUTLOOK_LOGIN. */
  public static final String    OUTLOOK_LOGIN          = "/outlook/login";

  /** The Constant OUTLOOK_LOGIN_TEMPLATE. */
  public static final String    OUTLOOK_LOGIN_TEMPLATE = "/outlook/login?target=_url_";

  /**
   * {@inheritDoc}
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpServletResponse httpRes = (HttpServletResponse) response;

    ConversationState convo = ConversationState.getCurrent();
    if (convo != null) {
      String currentUserId = convo.getIdentity().getUserId();

      if (!IdentityConstants.ANONIM.equals(currentUserId) || OUTLOOK_LOGIN.equals(httpReq.getRequestURI())) {
        chain.doFilter(request, response);
      } else {
        httpRes.sendRedirect(OUTLOOK_LOGIN_TEMPLATE.replace("_url_", getRequestString(httpReq)));
        LOG.warn("Unregistered user");
      }
    } else {
      httpRes.sendRedirect(OUTLOOK_LOGIN_TEMPLATE.replace("_url_", getRequestString(httpReq)));
      LOG.warn("ConversationState is null");
    }
  }

    @Override
    public void destroy() {

    }

    private String getRequestString(HttpServletRequest request) {
    StringBuilder requestStringBuilder = new StringBuilder(request.getRequestURI());
    if (request.getQueryString() != null) {
      requestStringBuilder.append("?").append(request.getQueryString());
    }
    return requestStringBuilder.toString();
  }
}
