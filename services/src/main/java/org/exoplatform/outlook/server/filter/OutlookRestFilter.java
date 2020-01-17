package org.exoplatform.outlook.server.filter;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.container.web.AbstractFilter;
import org.exoplatform.services.rest.impl.EnvironmentContext;
import org.exoplatform.web.filter.Filter;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Map;

public class OutlookRestFilter extends AbstractFilter implements Filter {

  /** The Constant LOG. */
  protected static final Logger LOG = LoggerFactory.getLogger(OutlookRestFilter.class);

  /**
   * {@inheritDoc}
   */
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

  @Override
  public void destroy() {
    // nothing
  }
}
