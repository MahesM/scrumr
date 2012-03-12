package com.imaginea.scrumr.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class HomeAction extends GenericActionSupport {

    private String qontextHostUrl;

    private String redirectUrl;

    public String source;
    
    private static final Logger logger = LoggerFactory.getLogger(HomeAction.class);

    public String prepareHome() throws Exception {
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
                return SUCCESS;
            }
            return ERROR;
        } else {
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
