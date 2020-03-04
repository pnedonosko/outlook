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

/**
 * The type Rest login filter.
 */
public class RestLoginFilter extends AbstractFilter implements Filter {

  /**
   * The Constant LOG.
   */
  protected static final Logger LOG = LoggerFactory.getLogger(RestLoginFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse httpRes = (HttpServletResponse) response;

    ConversationState convo = ConversationState.getCurrent();
    if (convo != null) {
      String currentUserId = convo.getIdentity().getUserId();

      if (!IdentityConstants.ANONIM.equals(currentUserId)) {
        chain.doFilter(request, response);
      } else {
        httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        LOG.warn("Unregistered user");
      }
    } else {
      httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      LOG.warn("ConversationState is null");
    }
  }

  @Override
  public void destroy() {

  }
}
