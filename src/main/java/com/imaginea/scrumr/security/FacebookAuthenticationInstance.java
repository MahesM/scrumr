package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.qontextclient.EasySSLProtocolSocketFactory;

public class FacebookAuthenticationInstance implements AuthenticationSource {

    private String consumerKey;

    private String consumerSecret;

    private String hostUrl;

    private String graphUrl;

    private FacebookInvocationHelper helper;

    private String SCOPE;

    private String CALLBACK_URL;

    public static final Logger logger = LoggerFactory.getLogger(FacebookAuthenticationInstance.class);

    public User doAuthentication(HttpServletRequest request, HttpServletResponse respose) {

        Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyhttps);

        String userId = null;
        String displayName = null;
        String fullName = null;
        String emailId = null;
        String avatarUrl = null;
        User user = null;
        int port = request.getServerPort();

        StringBuilder callbackUrl = new StringBuilder(request.getScheme()).append("://").append(request.getServerName());
        if (port != 80) {
            callbackUrl.append(":").append(port);
        }
        callbackUrl.append(request.getContextPath()).append(CALLBACK_URL);
        logger.info("callback url:" + callbackUrl);
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                if (request.getParameter("error_reason") != null) {
                    logger.error("Error: " + request.getParameter("error_description"));
                } else {
                    if (request.getParameter("code") == null) {

                        String url = hostUrl + "?client_id=" + consumerKey + "&redirect_uri="
                                                        + callbackUrl + "&scope=" + SCOPE;
                        respose.sendRedirect(url);
                        return null;
                    } else {
                        if (helper.hasRequiredParameters(request)) {
                            helper.initialize(request, consumerKey, consumerSecret, graphUrl, callbackUrl.toString());
                        }
                        JSONObject userDetails = helper.getBasicProfile();
                        user = new User();
                        user.setDisplayname(userDetails.getString("first_name"));
                        user.setUsername(userDetails.getString("id"));
                        user.setFullname(userDetails.getString("name"));
                        user.setEmailid(userDetails.getString("email"));
                        user.setAvatarurl("/" + userDetails.getString("id") + "/picture");
                        request.getSession().setAttribute("source", "facebook");
                    }
                }
            } else {
                user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    public String getFriends(int startIndex, int count) {
        try {

            String users = helper.searchPeople(startIndex, count).toString();
            return users;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String searchFriends(String sortType, boolean showTotalCount, int startIndex, int count) {
        try {
            String users = helper.searchBasicProfile(sortType, startIndex, count).toString();
            return users;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public Object getHelper() {
        return helper;
    }

    public void setHelper(Object helper) {
        this.helper = (FacebookInvocationHelper) helper;
    }

    public String getSCOPE() {
        return SCOPE;
    }

    public void setSCOPE(String sCOPE) {
        SCOPE = sCOPE;
    }

    public String getCALLBACK_URL() {
        return CALLBACK_URL;
    }

    public void setCALLBACK_URL(String cALLBACK_URL) {
        CALLBACK_URL = cALLBACK_URL;
    }

    public String getGraphUrl() {
        return graphUrl;
    }

    public void setGraphUrl(String graphUrl) {
        this.graphUrl = graphUrl;
    }
}
