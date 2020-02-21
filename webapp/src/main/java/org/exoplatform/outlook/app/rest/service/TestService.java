package org.exoplatform.outlook.app.rest.service;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.outlook.portlet.IdentityInfo;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    private static final Log log = ExoLogger.getLogger(TestService.class);

    public List<IdentityInfo> findUsersByEmail(String emails, String excludeOne) throws Exception {
        List<IdentityInfo> list = new ArrayList<>();
        ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();

        try {
            for (String email : emails.split(",")) {
                User user = findUserByEmail(email.toLowerCase());
                if (user != null) {
                    IdentityManager identityManager =
                            (IdentityManager) currentContainer
                                    .getComponentInstance(IdentityManager.class);
                    Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, user.getUserName(), false);
                    if (userIdentity != null && (excludeOne != null ? !excludeOne.equals(userIdentity.getRemoteId()) : true)) {
                        list.add(new IdentityInfo(userIdentity));
                    }
                }
            }
        } catch (Exception exception) {
            log.warn("Error while findUsersByEmail: " + emails , exception);
        }

        return list;
    }

    private User findUserByEmail(String email) throws Exception {
        ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
        org.exoplatform.services.organization.Query query = new org.exoplatform.services.organization.Query();
        query.setEmail(email);
        OrganizationService organization =
                (OrganizationService) currentContainer
                        .getComponentInstance(OrganizationService.class);
        ListAccess<User> res = organization.getUserHandler()
                .findUsersByQuery(query, org.exoplatform.services.organization.UserStatus.ENABLED);
        if (res.getSize() > 0) {
            return res.load(0, 1)[0];
        }
        return null;
    }
}
