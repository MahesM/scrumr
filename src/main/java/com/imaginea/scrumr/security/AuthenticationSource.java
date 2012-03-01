package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.imaginea.scrumr.entities.User;

public interface AuthenticationSource {
	
	public User doAuthentication(HttpServletRequest request, HttpServletResponse respose) throws UsernameNotFoundException;
	public String getFriends(int startIndex, int count) throws Exception;
	public String searchFriends(String sortType, boolean showTotalCount, int startIndex, int count) throws Exception;
	public String getConsumerKey();
	public void setConsumerKey(String consumerKey);
	public String getConsumerSecret();
	public void setConsumerSecret(String consumerSecret);
	public String getHostUrl();
	public void setHostUrl(String hostUrl);
	public Object getHelper();
	public void setHelper(Object helper);
	
}
