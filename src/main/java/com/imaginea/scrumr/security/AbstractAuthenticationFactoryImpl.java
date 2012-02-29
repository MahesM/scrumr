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
        //quick hacks --TODO separate them into a separate spring config and deploy separately and handle in maven
        if (sourceId == null) {
            // request.getSession().setAttribute("source", "qontext");          
            return (AuthenticationSource) ctx.getBean("qontext");
        } else {
            //|| (source!=null && !source.equals("qontext") && !source.equals(sourceId))
            String sourceStr = sourceId != null ? sourceId : source;
           // if (sourceStr != null)
            //    request.getSession().setAttribute("source", sourceStr);           
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
