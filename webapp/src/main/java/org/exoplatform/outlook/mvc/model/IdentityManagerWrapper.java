package org.exoplatform.outlook.mvc.model;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.social.core.manager.IdentityManager;
import org.springframework.stereotype.Component;

@Component
public class IdentityManagerWrapper {

  private IdentityManager identityManager;

  public IdentityManager getIdentityManager() {
    if (identityManager == null) {
      ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
      this.identityManager = (IdentityManager) currentContainer.getComponentInstance(IdentityManager.class);
    }
    return identityManager;
  }
}
