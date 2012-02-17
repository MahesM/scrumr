package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AbstractAuthenticationFactoryImpl implements AbstractAuthenticationFactory {
	
	private AuthenticationSource sourceInstance = null;
	private ApplicationContext ctx = null;
	protected HttpServletRequest request;
	
	public AuthenticationSource getInstance(HttpServletRequest request){
		ctx = getApplicationContext(request);
		if(sourceInstance == null){
			if(request.getParameter("id") == null){
				return (AuthenticationSource) ctx.getBean("qontext");
			}else{
				return (AuthenticationSource) ctx.getBean(request.getParameter("id"));
			}
		}
		return sourceInstance;
	}
	
	public ApplicationContext getApplicationContext(HttpServletRequest request){
		if(ctx == null){
			return WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		}
		return ctx;
	}
	
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}
	
}
