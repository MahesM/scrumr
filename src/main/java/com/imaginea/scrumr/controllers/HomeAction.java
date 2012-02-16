package com.imaginea.scrumr.controllers;

@SuppressWarnings("serial")
public class HomeAction extends GenericActionSupport {

    private String qontextHostUrl;

    private String redirectUrl;

    public String prepareAuthHome() throws Exception {
        /*
         * super.prepare(); if(loggedInUser != null){ super.source = "DATABASE"; return SUCCESS;
         * }else{ return ERROR; }
         */
        System.out.println("Redirecting: " + qontextHostUrl + "" + redirectUrl);
        response.sendRedirect(qontextHostUrl + "" + redirectUrl);
        return SUCCESS;
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

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
