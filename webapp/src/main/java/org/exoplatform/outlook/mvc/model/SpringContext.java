package org.exoplatform.outlook.mvc.model;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Allows to get access to spring beans.
 */
@Component
public class SpringContext implements ApplicationContextAware {

  private static ApplicationContext context;

  /**
   * Returns the Spring managed bean instance of the given class type if it
   * exists. Returns null otherwise.
   * 
   * @param beanClass
   * @return
   */
  public static <T extends Object> T getBean(Class<T> beanClass) {
    return context.getBean(beanClass);
  }

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    // Store ApplicationContext reference to access required beans later on
    SpringContext.context = context;
  }
}
