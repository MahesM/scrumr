package com.imaginea.scrumr.controllers;

import java.util.ArrayList;
import java.util.List;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
public class HomeAction extends GenericActionSupport {

	private String qontextHostUrl;
	private String redirectUrl;
	public String source;
	
	public String prepareHome() throws Exception{
		if(loggedInUser != null){
			source = (String) request.getSession().getAttribute("source");
			System.out.println("Source:"+source);
			if(source.equalsIgnoreCase("facebook")){
				qontextHostUrl = "http://graph.facebook.com";
				System.out.println("Source:"+qontextHostUrl);
			}else if(source.equalsIgnoreCase("google")){
				qontextHostUrl = "http://graph.facebook.com";
				System.out.println("Source:"+qontextHostUrl);
			}
			return SUCCESS;
		}else{
			return ERROR;
		}
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
