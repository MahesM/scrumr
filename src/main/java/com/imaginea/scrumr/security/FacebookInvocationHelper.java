package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.json.JSONException;
import org.brickred.socialauth.util.HttpUtil;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.imaginea.scrumr.utils.Utils;

public class FacebookInvocationHelper {

	private String consumerKey;
	private String consumerSecret;
	private String hostUrl;
	private String access_token;
	private RestTemplate restTemplate = new RestTemplate();

	public void initialize(HttpServletRequest request, String consumerKey,String consumerSecret, String hostUrl){
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.hostUrl = hostUrl;

		System.out.println("Code: "+request.getParameter("code"));
		//String url = hostUrl+"?client_id="+consumerKey+"&redirect_uri="+Utils.getAbsoluteURI(request)+"?id=facebook&client_secret="+consumerSecret+"&code="+request.getParameter("code");
		String url = "https://graph.facebook.com/oauth/access_token?client_id="+consumerKey+"&client_secret="+consumerSecret+"&code="+request.getParameter("code")+"&redirect_uri=http://127.0.0.1:8181/scrumr/login.action?id=facebook";
		String obj = restTemplate.getForObject(url, String.class);
		this.access_token = obj.split("&")[0].split("=")[1];
	}

	public boolean hasRequiredParameters(HttpServletRequest request){
		if(request.getParameter("code") == null){
			return false;
		}
		return true;
	}

	public JSONObject getBasicProfile() throws JSONException{
		try {
			String my_url = "https://graph.facebook.com/me?access_token=" + access_token;
			String res = restTemplate.getForObject(my_url, String.class);
			JSONObject resp = new JSONObject(res);
			System.out.println("My Details: "+resp.toString());
			return resp;
		} catch (Throwable ex) {
			return null;
		}
	}

	public String searchPeople(Integer startIndex, Integer count){
		String my_url = "https://graph.facebook.com/me?access_token=" + access_token;
		String res = restTemplate.getForObject(my_url, String.class);
		System.out.println("My Details: "+res);
		return res;
	}

	public String searchBasicProfile(String sortType, Integer startIndex, Integer count){
		String my_url = "https://graph.facebook.com/me?access_token=" + access_token;
		String res = restTemplate.getForObject(my_url, String.class);
		return res;
	}

}
