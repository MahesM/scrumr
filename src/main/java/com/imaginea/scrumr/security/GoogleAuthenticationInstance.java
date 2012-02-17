package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.qontextclient.EasySSLProtocolSocketFactory;
import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;
import com.imaginea.scrumr.qontextclient.Settings;

public class GoogleAuthenticationInstance implements AuthenticationSource{


	private String consumerKey;
	private String consumerSecret;
	private String hostUrl;
	private QontextRestApiInvocationUtil helper;
	private HttpSession session;
	
	public User doAuthentication(HttpServletRequest request, HttpServletResponse respose){
		
		Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);
		
		String userId = null;
		String displayName= null;
		String fullName= null;
		String emailId= null;
		String avatarUrl= null;
		User user = null;
		try{
			if(SecurityContextHolder.getContext().getAuthentication() == null )
			{
				System.out.println("GOOGLE AUTH");
				
			}else{
				user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			}
			return user;
		}catch(Exception e){
			return null;
		}
		
		
	}

	public String getFriends(int startIndex, int count){
		try {
			
			String users = helper.searchPeople(startIndex, count).toString();
			return users;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String searchFriends(String sortType, boolean showTotalCount, int startIndex, int count){
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
