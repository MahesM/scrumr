package com.imaginea.scrumr.controllers;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class SprintAction extends GenericActionSupport {

    public JSONObject successResponse = null;

    private String qontextHostUrl;

    private String storyDescLimit;

    private String storyTitleLimit;

    public String source;

    private static final Logger logger = LoggerFactory.getLogger(SprintAction.class);

    @Override
    public void prepare() throws Exception {
        super.prepare();
    }

    public String prepareSprintHome() throws Exception {
        if (loggedInUser != null) {
            source = (String) request.getSession().getAttribute("source");
            logger.info("Source:" + source);
            if (source != null) {
                if (source.equalsIgnoreCase("facebook")) {
                    qontextHostUrl = "http://graph.facebook.com";
                    logger.info("Source:" + qontextHostUrl);
                } else if (source.equalsIgnoreCase("google")) {
                    qontextHostUrl = "";
                    logger.info("Source:" + qontextHostUrl);
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
