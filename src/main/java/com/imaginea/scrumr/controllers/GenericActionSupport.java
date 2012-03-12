package com.imaginea.scrumr.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.UserServiceManager;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings("serial")
public class GenericActionSupport extends ActionSupport implements Preparable, ServletRequestAware, ServletResponseAware {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected UserServiceManager userServiceManager;
	protected User loggedInUser = null;
	private static final Logger logger = LoggerFactory.getLogger(GenericActionSupport.class);
	// Default action method
	public String execute() {
		return SUCCESS;
	}

	public void prepare() throws Exception {
	    logger.debug("PREPARE1");
		if( SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
	}


	public void prepareLoggedinUserDetails() {
	    logger.debug("prepareLoggedinUserDetails");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info(loggedInUserName);
		logger.info("SecurityContext",SecurityContextHolder.getContext().getAuthentication());

		if (loggedInUserName.equals("anonymousUser")) {
			loggedInUser = new User();
			loggedInUser.setUsername(loggedInUserName);
		} else {

			loggedInUser = userServiceManager.readUser(loggedInUserName);

		}

		logger.info(loggedInUser.getUsername());

		logger.info("Authorities",loggedInUser.getAuthorities());


	}



	/* Getters and Setters */

	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}

	public UserServiceManager getUserServiceManager() {
		return userServiceManager;
	}

	public void setUserServiceManager(UserServiceManager userServiceManager) {
		this.userServiceManager = userServiceManager;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public void setServletResponse(HttpServletResponse httpServletResponse) {
		this.response = httpServletResponse;
	}

	
}
