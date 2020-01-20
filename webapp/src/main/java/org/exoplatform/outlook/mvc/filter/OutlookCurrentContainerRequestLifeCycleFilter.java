package org.exoplatform.outlook.mvc.filter;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.RequestLifeCycle;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Map;

@WebFilter("/app/*")
public class OutlookCurrentContainerRequestLifeCycleFilter implements Filter {

  /** The Constant LOG. */
  protected static final Logger LOG = LoggerFactory.getLogger(OutlookCurrentContainerRequestLifeCycleFilter.class);

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
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();

    RequestLifeCycle.begin(currentContainer);
    try {
      chain.doFilter(request, response);
    } catch (Exception e) {
      LOG.error("An error occurred while the request handled ", e);
    } finally {
      Map<Object, Throwable> results = RequestLifeCycle.end();
      for (Map.Entry<Object, Throwable> entry : results.entrySet()) {
        if (entry.getValue() != null) {
          LOG.error("An error occurred while calling the method endRequest on " + entry.getKey(), entry.getValue());
        }
      }
    }
  }
}
