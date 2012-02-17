package com.imaginea.scrumr.security;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.qontextclient.EasySSLProtocolSocketFactory;
import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;
import com.imaginea.scrumr.qontextclient.Settings;

public class FacebookAuthenticationInstance implements AuthenticationSource{


	private String consumerKey;
	private String consumerSecret;
	private String hostUrl;
	private String graphUrl;
	private FacebookInvocationHelper helper;
	private String SCOPE;
	private String CALLBACK_URL; 
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
				if(request.getParameter("code") == null){
					String url = hostUrl+"?client_id="+consumerKey+"&redirect_uri="+CALLBACK_URL+"&scope="+SCOPE;
					respose.sendRedirect(url);
					return null;
				}else{
					if(helper.hasRequiredParameters(request)){
						helper.initialize(request, consumerKey, consumerSecret, graphUrl, CALLBACK_URL);
					}
					JSONObject userDetails = helper.getBasicProfile();
					user = new User();
					user.setDisplayname(userDetails.getString("first_name"));
					user.setUsername(userDetails.getString("username"));
					user.setFullname(userDetails.getString("name"));
					user.setEmailid(userDetails.getString("email"));
					user.setAvatarurl("/"+userDetails.getString("username")+"/picture");
					request.getSession().setAttribute("source", "facebook");
				}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String searchFriends(String sortType, boolean showTotalCount, int startIndex, int count){
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
