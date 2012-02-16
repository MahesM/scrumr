package com.imaginea.scrumr.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SuccessController {

    @Autowired
    private SocialAuthTemplate socialAuthTemplate;

    @RequestMapping(value = "/authSuccess")
    public ModelAndView getRedirectURL(final HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView();
        List<Contact> contactsList = new ArrayList<Contact>();
        SocialAuthManager manager = socialAuthTemplate.getSocialAuthManager();
        AuthProvider provider = manager.getCurrentAuthProvider();
        contactsList = provider.getContactList();
        if (contactsList != null && contactsList.size() > 0) {
            for (Contact p : contactsList) {
                if (!StringUtils.hasLength(p.getFirstName())
                                                && !StringUtils.hasLength(p.getLastName())) {
                    p.setFirstName(p.getDisplayName());
                }
            }
        }
        mv.addObject("profile", provider.getUserProfile());
        mv.addObject("contacts", contactsList);
        mv.setViewName("/jsp/login.jsp");

        return mv;
    }
}
