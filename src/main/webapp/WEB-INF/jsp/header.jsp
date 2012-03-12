<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>

<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery-1.7.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/mwheelIntent.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jscrollpane.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/mousewheel.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/common-functions.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/jquery.fancybox-1.3.4.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/ejs_production.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/commons.js"></script>
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery.fancybox-1.3.4.css" />
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery-ui-1.8.16.custom.css" />
<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jscrollpane.css" />
<link rel="shortcut icon" type="image/x-icon" href="http://www.qontext.com/wp-content/themes/qontext-v1.5/qontext.ico">
<link href="<%= request.getContextPath() %>/themes/style.css" rel="stylesheet"/>

<script type="text/javascript">
var userLogged = '<s:property value="loggedInUser.username"/>';
var fullname = '<s:property value="loggedInUser.fullname"/>';
var displayname = '<s:property value="loggedInUser.displayname"/>';
var avatar = '<s:property value="loggedInUser.avatarurl"/>';
var emailid = '<s:property value="loggedInUser.emailid"/>';
var source = '<s:property value="source"/>';
var qontextHostUrl = '<s:property value="qontextHostUrl"/>';
$(document).ready(function(){
	
	
});
</script>

<header>
    <div class="project-head">
    	<div class="projects">
    		<div class="proj_list"><a href="<%= request.getContextPath() %>/login.action"></a></div>
    		<div class="proj_title"><label class="project-title"></label></div>
    	</div>
    	<div class="project_tab">
    		<div class="tab projectview">
    			<label>Project Plan</label>
    			<img class="project_menu_arrow" src="themes/images/menu_arrow.png"></img>
    		</div>
    		<div class="tab sprintview">
    			<label>Sprint Plan</label>
    			<img class="project_menu_arrow" src="themes/images/menu_arrow.png"></img>
    		</div>
    		<div class="tab projectstatview">
    			<label>Sprint Tasks</label>
    			<img class="project_menu_arrow" src="themes/images/menu_arrow.png"></img>
    		</div>
    		<div class="tab project_chart">
    			<a href="<%= request.getContextPath() %>/login.action"></a>
    			<img class="project_menu_arrow" src="themes/images/menu_arrow.png"></img>
    		</div>
    		<div class="tab project_customize">
    			<a href="<%= request.getContextPath() %>/login.action"></a>
    			<img class="project_menu_arrow" src="themes/images/menu_arrow.png"></img>
    		</div>
    	</div>
    </div>
</header>

