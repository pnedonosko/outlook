package org.exoplatform.outlook.app.config.service;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.OutlookService;
import org.exoplatform.outlook.OutlookServiceImpl;
import org.exoplatform.outlook.model.OutlookConstant;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.beans.factory.FactoryBean;

/**
 * The type Outlook service factory bean.
 */
public class OutlookServiceFactory implements FactoryBean<OutlookService> {

  /** The Constant LOG. */
  private static final Log LOG = ExoLogger.getLogger(OutlookServiceFactory.class);

  @Override
  public OutlookService getObject() throws Exception {
    try {
      ExoContainer currentContainer = ExoContainerContext.getContainerByName(OutlookConstant.PORTAL);
      if (currentContainer != null) {
        Object service = currentContainer.getComponentInstance(OutlookService.class);
        if (service instanceof OutlookService) {
          return (OutlookService) service;
        }
      }
    } catch (Exception e) {
      LOG.error("OutlookService is not initialized", e);
    }
    return null;
  }

  @Override
  public Class<OutlookService> getObjectType() {
    return OutlookService.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
}
