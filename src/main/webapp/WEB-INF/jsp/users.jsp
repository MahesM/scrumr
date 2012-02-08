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

<%
// Ignore https certificate verification errors. For staging server only. Comment for production servers
    Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
    Protocol.registerProtocol("https", easyhttps);

	//set the consumer key and secret for localhost
	String consumerKey =  "J5UMvBgIDG2tYQBPbiJ4LtA1dOuYJEmWBOrRrT8+Bx4=";
	String consumerSecret =  "NstSq/2UYTljQR37MVzROA==";
	
	//Consumer Key and secret for SCRUMR CHENNAI -		J5UMvBgIDG2tYQBPbiJ4Ludo5KeqYHTWJwgTpcIBd2w=
	//Consumer Secret -     HugcFh7TpCvvCwfZrMlPYw==
	
    //modify this to Qontext Host URL
	String qontextHostUrl = "https://pramati.staging.qontext.com";

    Settings mySettings = Settings.getInstance(request, qontextHostUrl, consumerKey, consumerSecret);
    mySettings.saveToSession(session);
    QontextRestApiInvocationUtil helper = new QontextRestApiInvocationUtil();
    boolean initialized = false;
    if (helper.hasRequiredParameters(request))  {
        helper.init(request, mySettings);
        request.setAttribute("helper", helper);
        initialized = true;
    } else {
        mySettings.saveToSession(session);
        //request.getRequestDispatcher("/configure.jsp").include(request, response);
        return;
    }
out.println(helper.getBasicProfile());
String accessToken = request.getParameter(QontextRestApiInvocationUtil.OAUTH_V2_GRANT_ACCESS_TOKEN);
out.println(helper.getOAuthToken());
mySettings.saveOAuthAccessToken(session, accessToken);
%>