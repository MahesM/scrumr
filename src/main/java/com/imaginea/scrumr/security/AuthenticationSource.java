package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imaginea.scrumr.entities.User;

public interface AuthenticationSource {
	
	public User doAuthentication(HttpServletRequest request, HttpServletResponse respose);
	public String getFriends(int startIndex, int count);
	public String searchFriends(String sortType, boolean showTotalCount, int startIndex, int count);
	public String getConsumerKey();
	public void setConsumerKey(String consumerKey);
	public String getConsumerSecret();
	public void setConsumerSecret(String consumerSecret);
	public String getHostUrl();
	public void setHostUrl(String hostUrl);
	public Object getHelper();
	public void setHelper(Object helper);
	
}
