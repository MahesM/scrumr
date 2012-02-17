package com.imaginea.scrumr.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.json.JSONException;
import org.brickred.socialauth.util.HttpUtil;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.utils.Utils;

public class GoogleInvocationHelper {

	private String consumerKey;
	private String consumerSecret;
	private String hostUrl;
	private String access_token;
	
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private RestTemplate restTemplate = new RestTemplate();

	public void initialize(HttpServletRequest request, String consumerKey,String consumerSecret, String hostUrl, String callback){
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.hostUrl = hostUrl;

		try {
			String authorizationCode = request.getParameter("code");
			GoogleAuthorizationCodeGrant authRequest = new GoogleAuthorizationCodeGrant(TRANSPORT,
					JSON_FACTORY, consumerKey, consumerSecret, authorizationCode, callback);
			authRequest.useBasicAuthorization = false;
			AccessTokenResponse authResponse = authRequest.execute();
			String accessToken = authResponse.accessToken;
			GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(accessToken,
					TRANSPORT, JSON_FACTORY, consumerKey, consumerSecret, authResponse.refreshToken);
			HttpRequestFactory rf = TRANSPORT.createRequestFactory(access);
			System.out.println("Access token: " + authResponse.accessToken);
			
			this.access_token = authResponse.accessToken;
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean hasRequiredParameters(HttpServletRequest request){
		if(request.getParameter("code") == null){
			return false;
		}
		return true;
	}

	public JSONObject getBasicProfile() throws JSONException{
		try {
			String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token="+access_token;
			String obj = restTemplate.getForObject(url, String.class);
			JSONObject resp = new JSONObject(obj);
			System.out.println("My Details: "+resp.toString());
			return resp;
		} catch (Throwable ex) {
			return null;
		}
	}

	public String searchPeople(Integer startIndex, Integer count){
		String my_url = "https://www.googleapis.com/oauth2/v1/userinfo=" + access_token;
		String res = restTemplate.getForObject(my_url, String.class);
		System.out.println("My Details: "+res);
		return res;
	}

	public String searchBasicProfile(String sortType, Integer startIndex, Integer count){
		String my_url = "https://www.googleapis.com/oauth2/v1/userinfo=" + access_token;
		String res = restTemplate.getForObject(my_url, String.class);
		return res;
	}

}
