<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
<head>
<title>Scrumr</title>

	<%
	
	     // Ignore https certificate verification errors. For staging server only. Comment for production servers
    Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
    Protocol.registerProtocol("https", easyhttps);

	//set the consumer key and secret
	String consumerKey =  "chennaiScrum";
	String consumerSecret =  "chennaiScrum";
	
    //modify this to Qontext Host URL
    //String qontextHostUrl = "https://www.staging.qontext.com";
	String qontextHostUrl = "https://pramati.staging.qontext.com";
	String token = (String)session.getAttribute(OAuth.OAUTH_TOKEN);
	System.out.println("token is: " +token);
	request.setAttribute(OAuth.OAUTH_TOKEN, token);
    QontextRestApiInvocationUtil helper = new QontextRestApiInvocationUtil();
    helper.init(request, qontextHostUrl, consumerKey, consumerSecret);
	String accountId = helper.getAccountId();
	String content;
	String successResponse = null;
	String errorResponse = "";
	if (helper.lastOperationSuccess) {
	
		content = helper.invoke("/search/services/search/people?sort=Alphabetical&showTotalCount=true&start=0&startIndex=0&count=5");
		if (helper.lastOperationSuccess) {
			successResponse = JSONObject.quote(content);
			out.println(successResponse);
		} else {
			errorResponse = content;
			out.println(errorResponse);
		}
	} else {
		content = accountId;
		errorResponse = content;
		out.println(errorResponse);
	}
	
%>