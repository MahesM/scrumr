package com.imaginea.scrumr.controllers;

import org.apache.log4j.Logger;

import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;

@SuppressWarnings("serial")
public class HomeAction extends GenericActionSupport {

	public static final Logger LOGGER = Logger.getLogger(HomeAction.class);
	private String qontextHostUrl;
	
	public String prepareAuthHome() throws Exception{
		super.prepare();
		if(loggedInUser != null){
			super.source = "DATABASE";
			return SUCCESS;
		}else{
			return ERROR;
		}
		
	}
	
	public String prepareQontextHome() throws Exception {
		super.prepare();
		super.source = "QONTEXT";
		return SUCCESS;
	}

	public String getQontextHostUrl() {
		return qontextHostUrl;
	}

	public void setQontextHostUrl(String qontextHostUrl) {
		this.qontextHostUrl = qontextHostUrl;
	}

}
