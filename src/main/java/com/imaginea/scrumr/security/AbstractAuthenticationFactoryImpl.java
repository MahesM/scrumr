package com.imaginea.scrumr.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AbstractAuthenticationFactoryImpl implements AbstractAuthenticationFactory {

    private ApplicationContext ctx = null;

    protected HttpServletRequest request;

    public AuthenticationSource getInstance(HttpServletRequest request) {
        ctx = getApplicationContext(request);
        
            String source = (String) request.getSession().getAttribute("source");
            String sourceId = request.getParameter("id");
            if (sourceId == null && source == null) {
                
                request.getSession().setAttribute("source", "qontext");
                System.out.println("test");
                return (AuthenticationSource) ctx.getBean("qontext");
            } else {
                String sourceStr = sourceId != null ? sourceId : source;
                if(sourceStr != null)
                request.getSession().setAttribute("source", sourceStr);
                System.out.println("test");
                return (AuthenticationSource) ctx.getBean(sourceStr);
            }               
    }

    public ApplicationContext getApplicationContext(HttpServletRequest request) {
        if (ctx == null) {
            return WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        }
        return ctx;
    }

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

}
