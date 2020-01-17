/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.outlook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.RolesAllowed;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.container.web.AbstractHttpServlet;
import org.exoplatform.outlook.portlet.IdentityInfo;
import org.exoplatform.outlook.security.OutlookTokenService;
import org.exoplatform.outlook.web.RequestUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.rest.Connector;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.StateKey;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.web.login.LoginServlet;
import org.exoplatform.web.login.LogoutControl;
import org.exoplatform.web.security.GateInToken;
import org.exoplatform.web.security.security.AbstractTokenService;
import org.exoplatform.web.security.security.CookieTokenService;


/**
 * The servlet.
 */

public class TestServlet extends AbstractHttpServlet implements ResourceContainer {

    /**
     *
     */
    private static final long serialVersionUID = -1330051083735349589L;

    /** . */
    private static final Log log = ExoLogger.getLogger(TestServlet.class);


    public TestServlet(){
    }




    @GET
    @RolesAllowed("users")
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.warn("[SampleServlet]: Current portal " + getContainer());
        log.warn("[SampleServlet]: Servlet Context " + getServletContext());

        /*try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


        ConversationState convo = ConversationState.getCurrent();
        if (convo != null) {
            String currentUserId = convo.getIdentity().getUserId();
            resp.getWriter().print("currentUserId - " + currentUserId);
            log.warn("currentUserId - " + currentUserId);
        } else {
            resp.getWriter().print("ConversationState is null");
            log.warn("ConversationState is null");
        }

        List<IdentityInfo> list = null;
        try {
            list = findUsersByEmail("nik.riabovol@gmail.com", null);
        } catch (Exception e) {
            log.error("findUsersByEmail Exception", e);
        }

        if(list!=null){
            log.warn("[SampleServlet]: list length: " + list.size());

            try {
                log.warn("[SampleServlet]: getFullName " + list.toArray(new IdentityInfo[0])[0].getFullName());
                resp.getWriter().println(list.toArray(new IdentityInfo[0])[0].getFullName());
            } catch (Exception e) {
                log.warn("[SampleServlet]: getFullName error ",e);
            }
        } else {
            log.warn("[SampleServlet]: list is null ");
        }



        /*try {
            // We set the character encoding now to UTF-8 before obtaining parameters
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Encoding not supported", e);
        }

        log.warn("Test servlet doGet");

        try {
            log.warn("fullLogout start");
            fullLogout(req,resp);
            log.warn("fullLogout finish");
        } catch (Exception e) {
            log.error("fullLogout Exception", e);
        }*/


    }

    @POST
    @RolesAllowed("users")
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private List<IdentityInfo> findUsersByEmail(String emails, String excludeOne) throws Exception {
        List<IdentityInfo> list = new ArrayList<>();
        ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
        //RequestLifeCycle.begin(currentContainer);
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
        } /*finally {
            RequestLifeCycle.end();
        }*/

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

    void fullLogout(HttpServletRequest req, HttpServletResponse res) {
        // XXX repeating logic of UIPortal.LogoutActionListener
    /*PortalRequestContext prContext = Util.getPortalRequestContext();
    HttpServletRequest req = prContext.getRequest();
    HttpServletResponse res = prContext.getResponse();*/

        // Delete the token from JCR
        String token = RequestUtils.getCookie(req, LoginServlet.COOKIE_NAME); // getTokenCookie(req)
        if (token != null) {
            AbstractTokenService<GateInToken, String> tokenService = AbstractTokenService.getInstance(CookieTokenService.class);
            tokenService.deleteToken(token);
        }
        token = LoginServlet.getOauthRememberMeTokenCookie(req);
        if (token != null) {
            AbstractTokenService<GateInToken, String> tokenService = AbstractTokenService.getInstance(CookieTokenService.class);
            tokenService.deleteToken(token);
        }

        LogoutControl.wantLogout();
        Cookie cookie = new Cookie(LoginServlet.COOKIE_NAME, "");
        cookie.setPath(req.getContextPath());
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        Cookie oauthCookie = new Cookie(LoginServlet.OAUTH_COOKIE_NAME, "");
        oauthCookie.setPath(req.getContextPath());
        oauthCookie.setMaxAge(0);
        res.addCookie(oauthCookie);

        // **********
        // Outlook add-in logout (cookies)
        String rememberMeOutlook = RequestUtils.getCookie(req, OutlookTokenService.COOKIE_NAME);
        if (rememberMeOutlook != null) {
            OutlookTokenService outlookTokens = AbstractTokenService.getInstance(OutlookTokenService.class);
            outlookTokens.deleteToken(rememberMeOutlook);
        }
        Cookie rememberMeOutlookCookie = new Cookie(OutlookTokenService.COOKIE_NAME, "");
        rememberMeOutlookCookie.setPath(req.getRequestURI());
        rememberMeOutlookCookie.setMaxAge(0);
        res.addCookie(rememberMeOutlookCookie);


      /*  ConversationRegistry conversationRegistry = (ConversationRegistry) getContainer().getComponentInstanceOfType(ConversationRegistry.class);

        HttpSession session = req.getSession();

        ConversationState convo = ConversationState.getCurrent();

        String currentUserId = convo.getIdentity().getUserId();
        //String sessionId = session.();

        List<StateKey>a stateKeys =  conversationRegistry.getStateKeys(currentUserId);

        StateKey stateKey =
        ConversationState conversationState = conversationRegistry.unregister(sessionId);

        if (conversationState != null) {
            log.info("Remove conversation state " + sessionId);
            if (conversationState.getAttribute(ConversationState.SUBJECT) != null) {
                Subject subject = (Subject) conversationState.getAttribute(ConversationState.SUBJECT);
                LoginContext ctx = null;
                try {
                    ctx = new LoginContext("exo-domain",  subject);
                } catch (LoginException e) {
                    e.printStackTrace();
                }
                try {
                    ctx.logout();
                } catch (LoginException e) {
                    e.printStackTrace();
                }
            } else {
                log.warn("Subject was not found in ConversationState attributes.");
            }*/
    }
}