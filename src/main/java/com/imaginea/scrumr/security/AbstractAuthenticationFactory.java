package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;

public interface AbstractAuthenticationFactory {
	
	public AuthenticationSource getInstance(HttpServletRequest request);

}
