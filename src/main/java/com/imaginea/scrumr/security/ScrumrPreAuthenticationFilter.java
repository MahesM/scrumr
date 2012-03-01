package com.imaginea.scrumr.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.imaginea.scrumr.entities.User;

public class ScrumrPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(ScrumrPreAuthenticationFilter.class);

    private AuthenticationManager authenticationManager;

    private AbstractAuthenticationFactory abstractAuthenticationFactory;

    private AuthenticationSource authenticationSource;

    private String exceptionUrlPattern;

    public String getExceptionUrlPattern() {
        return exceptionUrlPattern;
    }

    public void setExceptionUrlPattern(String exceptionUrlPattern) {
        this.exceptionUrlPattern = exceptionUrlPattern;
    }

    public ScrumrPreAuthenticationFilter() {
        super();
        setCheckForPrincipalChanges(true);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String userId = extractUsername(request);
        /*
         * String source = (String) request.getSession().getAttribute("source"); String sourceId =
         * request.getParameter("id"); logger.info("Obtained principal for authentication " + userId
         * + "source" + source + " SourceId:" + sourceId); if ((source!= null &&
         * !source.equals(sourceId) && !source.equals("qontext")) || ( sourceId != null &&
         * !sourceId.equals(source))) return null;
         */

        return userId;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String password = extractPassword(request);
        return password;
    }

    private String extractPassword(HttpServletRequest request) {
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
        User user = (User) request.getSession().getAttribute("loggedInUser");
        if (user != null)
            return user.getUsername();
        else
            return null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                    throws IOException, ServletException {
        Authentication authResult = null;
        if (logger.isDebugEnabled()) {
            logger.info("Checking secure context token: "
                                            + SecurityContextHolder.getContext().getAuthentication());
        }

        if (!requiresAuthentication((HttpServletRequest) request, (HttpServletResponse) response)) {
            // doesn't require any authentication as token for Authenticated principal is available           
            super.doFilter(request, response, chain);
            // chain.doFilter(request, response);
        } else {

            try {
                HttpServletRequest servletRequest = (HttpServletRequest) request;
                authenticationSource = abstractAuthenticationFactory.getInstance(servletRequest);
                User user = authenticationSource.doAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
                authResult = SecurityContextHolder.getContext().getAuthentication();
                if (user != null) {
                    if (authResult == null)
                        authResult = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<GrantedAuthority>());
                    // set the Authentication instance into secure Context
                    successfulAuthentication((HttpServletRequest) request, (HttpServletResponse) response, authResult);
                } else if (user == null && authResult != null) {
                    chain.doFilter((HttpServletRequest) request, (HttpServletResponse) response);                 
                    // super.doFilter(request, response, chain);
                }
                if (user != null)
                    chain.doFilter(request, response);
                else{
                    //unsuccessfulAuthentication(servletRequest, (HttpServletResponse)response, new AuthenticationException("Provider changed") {
                        
                    //});
                    servletRequest.getSession().removeAttribute("source");
                }                
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw e;
            } catch (ServletException e) {
                logger.error(e.getMessage(), e);
                throw e;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
    }

    private boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String source = (String) request.getSession().getAttribute("source");
        String sourceId = request.getParameter("id");
        logger.info("Source changed ?" + "source" + source + " SourceId:" + sourceId);
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return true;
        }     

        if ((sourceId != null && source != null && !source.equals(sourceId) && !source.equals("qontext"))
                                        || (sourceId != null && !sourceId.equals(source))) {
            String sourceStr = sourceId != null ? sourceId : source;
            if (sourceStr != null)
               request.getSession().setAttribute("source", sourceStr);
            unsuccessfulAuthentication(request, response, new AuthenticationException("Provider changed") {
            });
            //SecurityContextHolder.getContext().setAuthentication(null);
            return true;
        }
        return false;        
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void afterPropertiesSet() {
    }

    public AbstractAuthenticationFactory getAbstractAuthenticationFactory() {
        return abstractAuthenticationFactory;
    }

    public void setAbstractAuthenticationFactory(
                                    AbstractAuthenticationFactory abstractAuthenticationFactory) {
        this.abstractAuthenticationFactory = abstractAuthenticationFactory;
    }
}