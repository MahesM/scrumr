package com.imaginea.scrumr.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationDetails;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.utils.QontextHelperUtil;

public class ScrumrPreAuthenticationFilter extends
		AbstractPreAuthenticatedProcessingFilter {

	private static final Logger logger = Logger.getLogger(ScrumrPreAuthenticationFilter.class);
	private AuthenticationManager authenticationManager;
	private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private String exceptionUrlPattern;
	private String adminTokenName = "auth_tkt";
	private String adminUserName = "realuser";
	private QontextHelperUtil qontextUtil;
	public String getExceptionUrlPattern() {
		return exceptionUrlPattern;
	}

	public void setExceptionUrlPattern(String exceptionUrlPattern) {
		this.exceptionUrlPattern = exceptionUrlPattern;
	}

	public ScrumrPreAuthenticationFilter() {
		super();
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String userId = extractUsername(request);
		logger.info("Obtained principal for authentication " + userId);
		return userId;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		String password = extractPassword(request);
		return password;
	}

	private String extractPassword(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return request.getParameter("j_password");
	}

	/**
	 * Locates the user name in the request.
	 * 
	 * @param request
	 *            the submitted request which is to be authenticated
	 * @return the username value (if present), null otherwise.
	 */
	private String extractUsername(HttpServletRequest request) {
		return request.getParameter("j_username");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Authentication authResult = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Checking secure context token: "
					+ SecurityContextHolder.getContext().getAuthentication());
		}

		if (!requiresAuthentication((HttpServletRequest) request)) {
			chain.doFilter(request, response);
		} else {
			/*
			 * if(SecurityContextHolder.getContext().getAuthentication() ==
			 * null) {
			 */HttpServletRequest servletRequest = (HttpServletRequest) request;
			String adminToken = "admin";
			User user = qontextUtil.doQontextAuthentication((HttpServletRequest)request, (HttpServletResponse)response);
			/*Cookie[] c = servletRequest.getCookies();
			if (c != null)
				for (Cookie cookie : c) {
					if (cookie.getName().equals(adminTokenName)) {
						adminToken = cookie.getValue();
					}
					if (cookie.getName().equals(adminUserName))
						username = cookie.getValue();
				}*/
			if (adminToken != null && user != null) {

				/*
				 * if(SecurityContextHolder.getContext().getAuthentication()!=null
				 * ) authResult =
				 * (AuthToken)SecurityContextHolder.getContext().getAuthentication
				 * ();
				 */if (authResult == null)
					authResult = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<GrantedAuthority>());
				successfulAuthentication((HttpServletRequest) request,
						(HttpServletResponse) response, authResult);
			} else if (adminToken == null
					&& SecurityContextHolder.getContext().getAuthentication() != null) {
				chain.doFilter((HttpServletRequest)request, (HttpServletResponse)response);
			}

			
			try {
				chain.doFilter(request, response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean requiresAuthentication(HttpServletRequest request) {

		/*if (request.getRequestURI().contains(
				exceptionUrlPattern.substring(exceptionUrlPattern.length() - 4,
						exceptionUrlPattern.length()))) {
			return true;
		} else
			return false;*/
		return true;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void afterPropertiesSet() {
	}

	public String getAdminTokenName() {
		return adminTokenName;
	}

	public void setAdminTokenName(String adminTokenName) {
		this.adminTokenName = adminTokenName;
	}

	public QontextHelperUtil getQontextUtil() {
		return qontextUtil;
	}

	public void setQontextUtil(QontextHelperUtil qontextUtil) {
		this.qontextUtil = qontextUtil;
	}

	
}