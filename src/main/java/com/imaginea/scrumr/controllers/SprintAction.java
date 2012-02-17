package com.imaginea.scrumr.controllers;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;

@SuppressWarnings("serial")
public class SprintAction extends GenericActionSupport {


	public JSONObject successResponse = null;
	private String qontextHostUrl;
	private String storyDescLimit;
	private String storyTitleLimit;
	
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

	public String getStoryDescLimit() {
		return storyDescLimit;
	}

	public void setStoryDescLimit(String storyDescLimit) {
		this.storyDescLimit = storyDescLimit;
	}

	public String getStoryTitleLimit() {
		return storyTitleLimit;
	}

	public void setStoryTitleLimit(String storyTitleLimit) {
		this.storyTitleLimit = storyTitleLimit;
	}
	
	
	
	

}
