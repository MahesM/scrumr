package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.qontextclient.EasySSLProtocolSocketFactory;

public class GoogleAuthenticationInstance implements AuthenticationSource {

    private String consumerKey;

    private String consumerSecret;

    private String hostUrl;

    private GoogleInvocationHelper helper;

    private HttpSession session;

    private String SCOPE;

    private String CALLBACK_URL;

    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private RestTemplate restTemplate = new RestTemplate();

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
                if (request.getParameter("code") == null) {
                    String authorizeUrl = new GoogleAuthorizationRequestUrl(consumerKey, CALLBACK_URL, SCOPE).build();
                    respose.sendRedirect(authorizeUrl);
                    return null;
                } else {
                    if (helper.hasRequiredParameters(request)) {
                        helper.initialize(request, consumerKey, consumerSecret, hostUrl, CALLBACK_URL);
                    }
                    JSONObject userDetails = helper.getBasicProfile();
                    user = new User();
                    user.setDisplayname(userDetails.getString("given_name"));
                    user.setUsername(userDetails.getString("id"));
                    user.setFullname(userDetails.getString("name"));
                    user.setEmailid(userDetails.getString("email"));
                    if (userDetails.has("picture")) {
                        user.setAvatarurl(userDetails.getString("picture"));
                    } else {
                        user.setAvatarurl(request.getContextPath() + "/themes/images/default.png");
                    }
                    request.getSession().setAttribute("source", "google");

                    // Refresh a token (SHOULD ONLY BE DONE WHEN ACCESS TOKEN EXPIRES)
                    /*
                     * access.refreshToken(); System.out.println("Original Token: " + accessToken +
                     * " New Token: " + access.getAccessToken());
                     */
                }
            } else {
                user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }
            return user;
        } catch (Exception e) {
            return null;
        }

    }

    public String getFriends(int startIndex, int count) {
        try {

            String users = helper.searchPeople(startIndex, count).toString();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String searchFriends(String sortType, boolean showTotalCount, int startIndex, int count) {
        try {
            String users = helper.searchBasicProfile(sortType, startIndex, count).toString();
            return users;
        } catch (Exception e) {
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
        this.helper = (GoogleInvocationHelper) helper;
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
}
