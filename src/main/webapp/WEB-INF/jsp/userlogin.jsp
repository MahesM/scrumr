<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.codehaus.jettison.json.JSONObject" %>
<%@ page import="net.oauth.*" %>
<%@ page import="org.codehaus.jettison.json.*" %>
<%@ page import="org.apache.commons.httpclient.protocol.Protocol" %>
<%@ page import="org.apache.commons.httpclient.protocol.ProtocolSocketFactory" %>
<%@ page import="com.imaginea.scrumr.qontextclient.*" %> 

<!DOCTYPE html>
<html>
<head>
<title>User Authentication</title>
<!-- Default Includes .. -->	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery-1.7.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/mwheelIntent.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jscrollpane.min.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/mousewheel.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/common-functions.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery.fancybox-1.3.4.js"></script>
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery.fancybox-1.3.4.css" />
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery-ui-1.8.16.custom.css" />
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jscrollpane.css" />
	<link rel="shortcut icon" type="image/x-icon" href="http://www.qontext.com/wp-content/themes/qontext-v1.5/qontext.ico">
   <link href="<%= request.getContextPath() %>/themes/style.css" rel="stylesheet"/>
<script type="text/javascript">
$(document).ready(function(){
 	
	 
	 $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
     $(window).resize(function() {
         $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
     });
     $('input[text]').val('');
     $('textarea').val('');
     $('select').val('');
    
}); 
</script>
</head>
<body>
	<header>
    	<a href="<%= request.getContextPath() %>/" class="logo float-lft"></a>
    	<div class="right-div" style="float:right;">
    	</div>
	</header>
   <section class="bg-pat">
  		 <div id="sign-in" class="">
	          <form id="login-form" method="post" action="<%= request.getContextPath() %>/j_spring_security_check" accept-charset="utf-8">
		    	<label>Username / Email:</label>
	           <input type="text" class="inp-box margin-rgt" placeholder="Enter Username" required id="username" name="j_username" maxlength="30" class="username">
	           <label>Password:</label>
	           <input type="password" class="inp-box margin-rgt" placeholder="Enter Password" required name="j_password" id="password" class="password">
	           <input type="submit" class="float-left submit" value="Login"/>
	           <label id="user-error" class="error-msg"></label>    
	    	</form>
	     </div>
   </section>
</body>
</html>