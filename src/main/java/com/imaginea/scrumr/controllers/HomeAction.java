package com.imaginea.scrumr.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
public class HomeAction extends GenericActionSupport {

	public static final Logger LOGGER = Logger.getLogger(HomeAction.class);
	private String qontextHostUrl;
	private String redirectUrl;

    @Autowired
    private SocialAuthTemplate socialAuthTemplate;
    
	public String prepareAuthHome() throws Exception{
		if(loggedInUser != null){
			return SUCCESS;
		}else{
			return ERROR;
		}
	}
	
	public String prepareQontextHome() throws Exception {
		return SUCCESS;
	}
	
	public String prepareFaceBookHome() throws Exception {
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
		return SUCCESS;
	}

	public String getQontextHostUrl() {
		return qontextHostUrl;
	}

	public void setQontextHostUrl(String qontextHostUrl) {
		this.qontextHostUrl = qontextHostUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	

}
