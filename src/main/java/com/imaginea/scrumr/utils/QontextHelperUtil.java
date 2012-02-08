package com.imaginea.scrumr.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.qontextclient.EasySSLProtocolSocketFactory;
import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;
import com.imaginea.scrumr.qontextclient.Settings;

public class QontextHelperUtil {
	private String consumerKey;
	private String consumerSecret;
	private String qontextHostUrl;
	private QontextRestApiInvocationUtil helper;
	private HttpSession session;
	
	public User doQontextAuthentication(HttpServletRequest request, HttpServletResponse respose){
		session = request.getSession();
		
		Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);
		
		String userId = null;
		String displayName= null;
		String fullName= null;
		String emailId= null;
		String avatarUrl= null;
		User user = null;
		try{
			user = (User)session.getAttribute("loggedInUser");
			if(user == null){
				Settings mySettings = Settings.getInstance(request, qontextHostUrl, consumerKey, consumerSecret);
				mySettings.saveToSession(session);
				boolean initialized = false;
				if (helper.hasRequiredParameters(request))  {
					helper.init(request, mySettings);
					request.setAttribute("helper", helper);
					initialized = true;
				} else {
					mySettings.saveToSession(session);
				}
				session.setAttribute("helper", helper);
				JSONObject basicProfile = helper.getBasicProfile();
				JSONObject jsonObject = (JSONObject) basicProfile.get("success");
				JSONObject bodyObject = (JSONObject) jsonObject.get("body");
				JSONObject basicInfo = (JSONObject) bodyObject.get("basicInfo");
				userId= helper.getAccountId();
				displayName= basicInfo.getString("displayName");
				fullName= basicInfo.getString("fullName");
				emailId= basicInfo.getString("userId");
				JSONObject headers = (JSONObject) jsonObject.get("headers");
				String api_version = headers.getString("api-version");
				String baseUrl = mySettings.getQontextHostUrl();
				avatarUrl="/portal/st/"+api_version+"/profile/defaultUser.gif";
				if(basicInfo.has("avatarUrl")){
					avatarUrl= basicInfo.getString("avatarUrl");
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
			}
			return user;
		}catch(Exception e){
			return null;
		}
		
		
	}

	public String populateQontextUsers(int startIndex, int count){
		try {
			
			String users = helper.searchPeople(startIndex, count).toString();
			return users;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String searchUser(String sortType, boolean showTotalCount, int startIndex, int count){
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

	public String getQontextHostUrl() {
		return qontextHostUrl;
	}

	public void setQontextHostUrl(String qontextHostUrl) {
		this.qontextHostUrl = qontextHostUrl;
	}

	public QontextRestApiInvocationUtil getHelper() {
		return helper;
	}

	public void setHelper(QontextRestApiInvocationUtil helper) {
		this.helper = helper;
	}
	
	
	
}
