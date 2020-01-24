package org.exoplatform.outlook.mvc.filter;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.security.ConversationState;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter("/app/*")
public class LoginFilter implements Filter {

  /** The Constant LOG. */
  protected static final Logger LOG                  = LoggerFactory.getLogger(LoginFilter.class);

  /** The Constant OUTLOOK_UNREGISTERED. */
  public static final String    OUTLOOK_UNREGISTERED = "/outlook/app/unregistered";

  /** The Constant UNREGISTERED_ID. */
  public static final String    UNREGISTERED_ID      = "__anonim";

  @Override
  public void destroy() {
    //
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    //
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse httpRes = (HttpServletResponse) response;

    ConversationState convo = ConversationState.getCurrent();
    if (convo != null) {
      String currentUserId = convo.getIdentity().getUserId();

      if (!UNREGISTERED_ID.equals(currentUserId)) {
        chain.doFilter(request, response);
      } else {
        httpRes.sendRedirect(OUTLOOK_UNREGISTERED);
        LOG.warn("ConversationState is null");
      }
    } else {
      httpRes.sendRedirect(OUTLOOK_UNREGISTERED);
      LOG.warn("ConversationState is null");
    }
  }

}
