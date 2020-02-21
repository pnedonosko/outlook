package org.exoplatform.outlook.rest.interceptor;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.RequestLifeCycle;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ExoContainerRequestLifeCycleInterceptor extends HandlerInterceptorAdapter {

  /** The Constant LOG. */
  protected static final Logger LOG = LoggerFactory.getLogger(ExoContainerRequestLifeCycleInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
    RequestLifeCycle.begin(currentContainer);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request,
                         HttpServletResponse response,
                         Object handler,
                         @Nullable ModelAndView modelAndView) throws Exception {
    finishRequestLifeCycle();
  }

  private void finishRequestLifeCycle() {
    Map<Object, Throwable> results = RequestLifeCycle.end();
    for (Map.Entry<Object, Throwable> entry : results.entrySet()) {
      if (entry.getValue() != null) {
        LOG.error("An error occurred while calling the method endRequest on " + entry.getKey(), entry.getValue());
      }
    }
  }
}
