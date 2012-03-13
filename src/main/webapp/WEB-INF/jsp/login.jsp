<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE HTML>
<html>
<head>
    <title>Scrumr</title>


     
	 <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery-ui-1.8.16.custom.css" />
	   <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/skin.css" />
	
     <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/style.css" />

	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery-1.7.js"></script>

    <script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery-ui-1.8.16.custom.min.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery.jcarousel.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/ejs_production.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/common-functions.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/loginpage.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/commons.js"></script>
	<script type="text/javascript" >
		var userLogged = '<s:property value="loggedInUser.username"/>';
		var fullname = '<s:property value="loggedInUser.fullname"/>';
		var displayname = '<s:property value="loggedInUser.displayname"/>';
		var avatar = '<s:property value="loggedInUser.avatarurl"/>';
		var emailid = '<s:property value="loggedInUser.emailid"/>';
		var source = '<s:property value="source"/>';
		var qontextHostUrl = '<s:property value="qontextHostUrl"/>';
		
		
	</script>
</head>
	<body>

		<div id="tabs">
			<ul>
				<li class="tab_Recent" >
					<a href="#tabs-1">Recent</a>
					<img src="themes/images/menu_arrow.png" class="tabPointer" />
				</li>
				<li class="tab_All" id="tab_All" >
					<a href="#tabs-2">All</a>
					<img src="themes/images/menu_arrow.png" class="tabPointer" />
				</li>
				<li class="tab_New">
					<a href="#tabs-3">New</a>
					<img src="themes/images/menu_arrow.png" class="tabPointer" />
				</li>
			</ul>

			<div id="tabs-1">
				<jsp:include page="recentProject.jsp" />
			</div>
			<div id="tabs-2">
				<jsp:include page="allProjects.jsp" />
			</div>
			<div id="tabs-3">
				<jsp:include page="createProject.jsp" />
			</div>
			
		</div>

	</body>
</html>