package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.outlook.mvc.service.TestService;
import org.exoplatform.outlook.portlet.IdentityInfo;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TestController {

    private static final Log log = ExoLogger.getLogger(TestController.class);

    private TestService testService;

    @Autowired
    public void setTestService(TestService testService) {
        this.testService = testService;
    }

    @RequestMapping(value = "/test",
            method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getTestPage(@RequestParam(value = "email") String email) {

        log.warn("[TestController]: start ");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test-page");

        ConversationState convo = ConversationState.getCurrent();
        if (convo != null) {
            String currentUserId = convo.getIdentity().getUserId();
            modelAndView.addObject("user", currentUserId);
            log.warn("currentUserId - " + currentUserId);
        } else {
            modelAndView.addObject("user", null);
            log.warn("ConversationState is null");
        }

        List<IdentityInfo> list = null;
        try {
            list = testService.findUsersByEmail(email, null);
        } catch (Exception e) {
            log.error("findUsersByEmail Exception", e);
        }

        if(list!=null){
            log.warn("[TestController]: list length: " + list.size());

            try {
                log.warn("[TestController]: getFullName " + list.toArray(new IdentityInfo[0])[0].getFullName());
                modelAndView.addObject("fullName", list.toArray(new IdentityInfo[0])[0].getFullName());
            } catch (Exception e) {
                log.warn("[TestController]: getFullName error ",e);
            }
        } else {
            log.warn("[TestController]: list is null ");
        }

        return modelAndView;
    }
}
