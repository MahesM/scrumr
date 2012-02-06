package com.imaginea.scrumr.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.UserServiceManager;
import com.imaginea.scrumr.qontextclient.EasySSLProtocolSocketFactory;
import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;
import com.imaginea.scrumr.qontextclient.Settings;
import com.imaginea.scrumr.utils.QontextUserUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings("serial")
public class GenericActionSupport extends ActionSupport implements Preparable, ServletRequestAware {

	protected HttpServletRequest request;
	protected UserServiceManager userServiceManager;
	protected User loggedInUser;



	// Default action method
	public String execute() {
		return SUCCESS;
	}

	public void prepare() throws Exception {
		System.out.println("PREPARE1");
		if( SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			System.out.println("PREPARE2");
			String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

			if (loggedInUserName.equals("anonymousUser")) {
				loggedInUser = new User();
				loggedInUser.setUsername(loggedInUserName);
			} else {
				loggedInUser = userServiceManager.readUser(loggedInUserName);
			}
			System.out.println(loggedInUserName);
			System.out.println(loggedInUser.getAuthorities());
		}
		
		initializeQontextParameters();
		
		// Override to prepare the required fields

	}
	
	public void initializeQontextParameters(){
		
		HttpSession session = request.getSession();
		 // Ignore https certificate verification errors. For staging server only. Comment for production servers
		Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);
		
		//set the consumer key and secret for localhost
		String consumerKey =  "J5UMvBgIDG2tYQBPbiJ4LtA1dOuYJEmWBOrRrT8+Bx4=";
		String consumerSecret =  "NstSq/2UYTljQR37MVzROA==";
		
		//consumer key and secret key for chennai scrumr
		//tring consumerKey = "J5UMvBgIDG2tYQBPbiJ4Ludo5KeqYHTWJwgTpcIBd2w=";
		//String consumerSecret ="HugcFh7TpCvvCwfZrMlPYw==";
		
		//modify this to Qontext Host URL
		//String qontextHostUrl = "https://www.staging.qontext.com";
		String qontextHostUrl = "https://pramati.staging.qontext.com";
		Settings mySettings = Settings.getInstance(request, qontextHostUrl, consumerKey, consumerSecret);
	    
		mySettings.saveToSession(session);
		boolean isInitialized = QontextUserUtils.initialize(request, mySettings);
		if (isInitialized == false){
			mySettings.saveToSession(session);
			return;
		}else{
			request.setAttribute("helper", QontextUserUtils.getQontextRestApiInvocationUtil());
			session.setAttribute("helper", QontextUserUtils.getQontextRestApiInvocationUtil());
			String accessToken = request.getParameter(QontextRestApiInvocationUtil.OAUTH_V2_GRANT_ACCESS_TOKEN);
			mySettings.saveOAuthAccessToken(session, accessToken);
			request.getSession().setAttribute("token", accessToken);						
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
