package com.imaginea.scrumr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.UserServiceManager;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings("serial")
public class GenericActionSupport extends ActionSupport implements Preparable, ServletRequestAware {

	protected HttpServletRequest request;
	protected UserServiceManager userServiceManager;
	protected User loggedInUser = null;
	public String source = "DATABASE";

	// Default action method
	public String execute() {
		return SUCCESS;
	}

	public void prepare() throws Exception {
		System.out.println("PREPARE1");
		if( SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
	}


	public void prepareLoggedinUserDetails() {
		System.out.println("prepareLoggedinUserDetails");
		String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(loggedInUserName);
		System.out.println(SecurityContextHolder.getContext().getAuthentication());

		if (loggedInUserName.equals("anonymousUser")) {
			loggedInUser = new User();
			loggedInUser.setUsername(loggedInUserName);
		} else {

			loggedInUser = userServiceManager.readUser(loggedInUserName);

		}

		System.out.println(loggedInUser.getUsername());

		System.out.println(loggedInUser.getAuthorities());


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

}
