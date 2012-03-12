package com.imaginea.scrumr.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.json.JSONException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.UserServiceManager;

public class FacebookInvocationHelper {

    private String consumerKey;

    private String consumerSecret;

    private String hostUrl;

    private String access_token;

    private UserServiceManager userServiceManager;

    private RestTemplate restTemplate = new RestTemplate();

    public static final Logger logger = LoggerFactory.getLogger(FacebookInvocationHelper.class);

    public void initialize(HttpServletRequest request, String consumerKey, String consumerSecret,
                                    String hostUrl, String callback) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.hostUrl = hostUrl;

        logger.info("Code: " + request.getParameter("code"));
        String url = hostUrl + "/oauth/access_token?client_id=" + consumerKey + "&client_secret="
                                        + consumerSecret + "&code=" + request.getParameter("code")
                                        + "&redirect_uri=" + callback;
        String obj = restTemplate.getForObject(url, String.class);
        this.access_token = obj.split("&")[0].split("=")[1];
    }

    public boolean hasRequiredParameters(HttpServletRequest request) {
        if (request.getParameter("code") == null) {
            return false;
        }
        return true;
    }

    public JSONObject getBasicProfile() throws JSONException {
        try {
            String my_url = hostUrl + "/me?access_token=" + access_token;
            String res = restTemplate.getForObject(my_url, String.class);
            JSONObject resp = new JSONObject(res);
            logger.info("My Details: " + resp.toString());
            return resp;
        } catch (Throwable ex) {
            return null;
        }
    }

    public String searchPeople(Integer startIndex, Integer count) {
        /*
         * String my_url = hostUrl+"/me?access_token=" + access_token; String res =
         * restTemplate.getForObject(my_url, String.class); System.out.println("My Details: "+res);
         * return res;
         */
        List<User> userList = userServiceManager.fetchAllUsers(startIndex, count);
        ObjectMapper mapper = new ObjectMapper();
        String res = null;
        try {
            res = mapper.writeValueAsString(userList);
            logger.info(res);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return res;
    }

    public String searchBasicProfile(String sortType, Integer startIndex, Integer count) {
        String my_url = hostUrl + "/me?access_token=" + access_token;
        String res = restTemplate.getForObject(my_url, String.class);
        return res;
    }

    public UserServiceManager getUserServiceManager() {
        return userServiceManager;
    }

    public void setUserServiceManager(UserServiceManager userServiceManager) {
        this.userServiceManager = userServiceManager;
    }

}
