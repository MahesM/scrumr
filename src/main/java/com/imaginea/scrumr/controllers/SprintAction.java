package com.imaginea.scrumr.controllers;

import org.codehaus.jettison.json.JSONObject;

@SuppressWarnings("serial")
public class SprintAction extends GenericActionSupport {

    public JSONObject successResponse = null;

    private String qontextHostUrl;

    private String storyDescLimit;

    private String storyTitleLimit;

    public String source;

    @Override
    public void prepare() throws Exception {
        super.prepare();
    }

    public String prepareSprintHome() throws Exception {
        if (loggedInUser != null) {
            source = (String) request.getSession().getAttribute("source");
            System.out.println("Source:" + source);
            if (source != null) {
                if (source.equalsIgnoreCase("facebook")) {
                    qontextHostUrl = "http://graph.facebook.com";
                    System.out.println("Source:" + qontextHostUrl);
                } else if (source.equalsIgnoreCase("google")) {
                    qontextHostUrl = "";
                    System.out.println("Source:" + qontextHostUrl);
                }
            }
            return SUCCESS;
        } else {
            return ERROR;
        }
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
