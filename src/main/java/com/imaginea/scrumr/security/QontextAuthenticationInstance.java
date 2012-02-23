package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.qontextclient.EasySSLProtocolSocketFactory;
import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;
import com.imaginea.scrumr.qontextclient.Settings;

public class QontextAuthenticationInstance implements AuthenticationSource {

    private String consumerKey;

    private String consumerSecret;

    private String hostUrl;

    transient private QontextRestApiInvocationUtil helper;

    private HttpSession session;

    public static final Logger logger = LoggerFactory.getLogger(QontextAuthenticationInstance.class);

    public User doAuthentication(HttpServletRequest request, HttpServletResponse respose) {

        Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyhttps);

        String userId = null;
        String displayName = null;
        String fullName = null;
        String emailId = null;
        String avatarUrl = null;
        User user = null;
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                session = request.getSession(true);
                Settings mySettings = Settings.getInstance(request, hostUrl, consumerKey, consumerSecret);
                mySettings.saveToSession(session);
                boolean initialized = false;
                if (helper.hasRequiredParameters(request)) {
                    helper.init(request, mySettings);
                    request.setAttribute("helper", helper);
                    initialized = true;
                } else {
                    mySettings.saveToSession(session);
                }
                session.setAttribute("helper", helper);
                JSONObject basicProfile = helper.getBasicProfile();
                JSONObject headers = null;
                JSONObject basicInfo = null;
                if (basicProfile.has("success")) {
                    JSONObject jsonObject = (JSONObject) basicProfile.get("success");
                    headers = (JSONObject) jsonObject.get("headers");
                    JSONObject bodyObject = (JSONObject) jsonObject.get("body");
                    if (bodyObject.has("basicInfo"))
                        basicInfo = (JSONObject) bodyObject.get("basicInfo");
                }
                userId = helper.getAccountId();
                if (basicInfo != null && headers != null) {
                    displayName = basicInfo.getString("displayName");
                    fullName = basicInfo.getString("fullName");
                    emailId = basicInfo.getString("userId");

                    String api_version = headers.getString("api-version");
                    String baseUrl = mySettings.getQontextHostUrl();
                    avatarUrl = "/portal/st/" + api_version + "/profile/defaultUser.gif";
                    if (basicInfo.has("avatarUrl")) {
                        avatarUrl = basicInfo.getString("avatarUrl");
                    }
                }
                user = new User();
                user.setDisplayname(displayName);
                user.setUsername(userId);
                user.setFullname(fullName);
                user.setEmailid(emailId);
                user.setAvatarurl(avatarUrl);
                session.setAttribute("loggedInUser", user);
                String accessToken = mySettings.getOAuthAccessToken(session);
                mySettings.saveOAuthAccessToken(session, accessToken);
                session.setAttribute("token", accessToken);

                request.getSession().setAttribute("source", "qontext");

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String searchFriends(String sortType, boolean showTotalCount, int startIndex, int count) {
        try {
            String users = helper.searchBasicProfile(sortType, startIndex, count).toString();
            return users;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        this.helper = (QontextRestApiInvocationUtil) helper;
    }
}
