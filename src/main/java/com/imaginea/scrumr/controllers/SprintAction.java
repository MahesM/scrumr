package com.imaginea.scrumr.controllers;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;

@SuppressWarnings("serial")
public class SprintAction extends GenericActionSupport {

	public static final Logger LOGGER = Logger.getLogger(SprintAction.class);
	public JSONObject successResponse = null;
	private String qontextHostUrl;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}

	public String prepareSprintHome() throws Exception {
		super.prepare();
		return SUCCESS;
	}

	public String prepareSprintStaticPage() {

		return SUCCESS;
	}

	public String getQontextHostUrl() {
		return qontextHostUrl;
	}

	public void setQontextHostUrl(String qontextHostUrl) {
		this.qontextHostUrl = qontextHostUrl;
	}

}
