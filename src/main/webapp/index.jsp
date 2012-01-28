<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.io.*" %>
<%@ page import="org.codehaus.jettison.json.JSONObject" %>
<%@ page import="net.oauth.*" %>
<%@ page import="org.codehaus.jettison.json.*" %>
<%@ page import="org.apache.commons.httpclient.protocol.Protocol" %>
<%@ page import="org.apache.commons.httpclient.protocol.ProtocolSocketFactory" %>
<%@ page import="com.imaginea.scrumr.qontextclient.*" %>


<!DOCTYPE html>


 
	
<html>
<%
        String url = "home.action?oauth_token=" + request.getParameter(OAuth.OAUTH_TOKEN);
        System.out.println (url);
        String token = request.getParameter(OAuth.OAUTH_TOKEN);
        if(token != null)
        	session.setAttribute(OAuth.OAUTH_TOKEN, token);
        
        // Ignore https certificate verification errors. For staging server only. Comment for production servers
    Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
    Protocol.registerProtocol("https", easyhttps);

	//set the consumer key and secret
	String consumerKey =  "chennaiScrum";
	String consumerSecret =  "chennaiScrum";
	
    //modify this to Qontext Host URL
    //String qontextHostUrl = "https://www.staging.qontext.com";
	String qontextHostUrl = "https://pramati.staging.qontext.com";
	token = (String)session.getAttribute(OAuth.OAUTH_TOKEN);
	System.out.println("token is: " +token);
	request.setAttribute(OAuth.OAUTH_TOKEN, token);
    QontextRestApiInvocationUtil helper = new QontextRestApiInvocationUtil();
    helper.init(request, qontextHostUrl, consumerKey, consumerSecret);
	request.setAttribute("helper", helper);
	
	
        response.sendRedirect(url);
%>
<head>
</head>
</html>