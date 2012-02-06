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
 	<%
			/* String token = request.getParameter(OAuth.OAUTH_TOKEN);
		    if(token != null)
		    	session.setAttribute(OAuth.OAUTH_TOKEN, token); */
	
			    // Ignore https certificate verification errors. For staging server only. Comment for production servers
		/*	Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", easyhttps);
			
			//set the consumer key and secret
			String consumerKey =  "J5UMvBgIDG2tYQBPbiJ4LtA1dOuYJEmWBOrRrT8+Bx4=";
			String consumerSecret =  "NstSq/2UYTljQR37MVzROA==";
			
			//modify this to Qontext Host URL
			//String qontextHostUrl = "https://www.staging.qontext.com";
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
		    }*/
		    QontextRestApiInvocationUtil helper= (QontextRestApiInvocationUtil)request.getAttribute("helper");
			JSONObject basicProfile = helper.getBasicProfile();
			
			System.out.println("Basic Profile :"+basicProfile);
			String userId= helper.getAccountId();
			/*String displayName= basicProfile.getString("displayName");
			String fullName= basicProfile.getString("fullName");
			String emailId= basicProfile.getString("userId");
			String avatarUrl= basicProfile.getString("avatarUrl");*/
			
			JSONObject jsonObject = (JSONObject) basicProfile.get("success");
			JSONObject bodyObject = (JSONObject) jsonObject.get("body");
			JSONObject basicInfo = (JSONObject) bodyObject.get("basicInfo");
			
			String displayName= basicInfo.getString("displayName");
			String fullName= basicInfo.getString("fullName");
			String emailId= basicInfo.getString("userId");
			JSONObject headers = (JSONObject) jsonObject.get("headers");
			String api_version = headers.getString("api-version");
			
			Settings mySettings = (Settings)session.getAttribute("mySettings");
			
			String baseUrl = mySettings.getQontextHostUrl();
			String avatarUrl=""+baseUrl+"/portal/st/"+api_version+"/profile/defaultUser.gif";
			if(basicInfo.has("avatarUrl")){
			avatarUrl= baseUrl+""+basicInfo.getString("avatarUrl");
			}

			
			
			session.setAttribute("userLogged", userId);
			//session.setAttribute("helper", helper);
			
			//String accessToken = request.getParameter(QontextRestApiInvocationUtil.OAUTH_V2_GRANT_ACCESS_TOKEN);
			//mySettings.saveOAuthAccessToken(session, accessToken);
			//System.out.println(accessToken);
			//request.getSession().setAttribute("token", accessToken);
			request.getSession().setAttribute("userid", userId);
			%>
			var user = '<%= userId %>';
			//temp code that accesses profile.json locally
			<%-- $.getJSON("<%= request.getContextPath() %>/themes/json/profile.json",function(object){
				user = object.success.body.basicInfo.displayName;
			     if(user != null && user != ''){
			     	$(".right-div").html('<img width="32px" height="32px" style="margin:4px;" class="float-lft"  src="themes/images/1.jpg"/><label class="float-lft loginLabel">Hi! '+user+',</label><a href="<%= request.getContextPath() %>/j_spring_security_logout" class="logout">Logout</a><div class="index-img"><a class="index-img1"/></a></div><div class="index-img"><a class="index-img2"></a></div>');
			     }else{
			     	$(".right-div").html('<a href="#sign-in" class="signin">Sign In</a><div class="index-img"><a class="index-img1"/></a></div><div class="index-img"><a class="index-img2"></a></div>');
			     }
			}); --%>
	
	 
			 $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
		     $(window).resize(function() {
		         $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
		     });
		     $('input[text]').val('');
		     $('textarea').val('');
		     $('select').val('');
     function days_between(date1, date2) {
 	    var ONE_DAY = 1000 * 60 * 60 * 24;
 	    var date1_ms = date1.getTime();
 	    var date2_ms = date2.getTime();
 	    var difference_ms = Math.abs(date1_ms - date2_ms);
 	    return Math.round(difference_ms/ONE_DAY);
 	}
     
	var dates = $( "#datepickerFrom, #datepickerTo" ).datepicker({
		defaultDate: "+1w",
		changeMonth: true,
		numberOfMonths: 1,
		onSelect: function( selectedDate ) {
		var option = this.id == "datepickerFrom" ? "minDate" : "maxDate",
		instance = $( this ).data( "datepicker" ),
		date = $.datepicker.parseDate(
		instance.settings.dateFormat ||
		$.datepicker._defaults.dateFormat,
		selectedDate, instance.settings );
		dates.not( this ).datepicker( "option", option, date );
		}
	});
	
	$(".feed-item").live('click',function(){
		window.location.href = '/scrumr/sprintView.jsp?projectId='+$(this).attr("id");
	});
	
	$('#new_project_form').submit(function(){
			var title = $('input[name=pTitle]');
			var description = $('textarea[name=pDescription]');
			var start_date = $('input[name=pStartDate]');
			var end_date = $('input[name=pEndDate]');
			var duration = $('select[name=pSprintDuration]');
			
			var days = days_between(new Date(Date.parse(start_date.val())),new Date(Date.parse(end_date.val())));
				if((days < (7*duration.val()))){
					$("#proj-error").html("Project duration conflicts with sprint duration");
					return false;
				} 
				if(user == "" || user == null){
					return false;
				}else{
					<%-- var post_data1 = 'userid='+user +'&displayname=<%=displayName%>&fullname=<%=fullName%>&emailid=<%=emailId%>&avatarurl=<%=avatarUrl%>'; --%>
					var post_data1 = {'userid':user,'displayname':'<%=displayName%>','fullname':'<%=fullName%>','emailid':'<%=emailId%>','avatarurl':'<%=avatarUrl%>'};
					$.ajax({
						url: '/scrumr/api/v1/users/create',
						type: 'POST',
						data: post_data1,
						async:false,
						success: function( records ) {
							var assignees = new Array();
							assignees.push(user);
							var post_data = 'pTitle=' + title.val() + '&current_user='+user + '&pDescription=' + description.val() + '&assignees='
								+ assignees + '&pStartDate=' + start_date.val() + '&pEndDate=' + end_date.val() +'&pSprintDuration=' +duration.val();
							$.ajax({
								url: '/scrumr/api/v1/projects/create',
								type: 'POST',
								data: post_data,
								async:false,
								success: function( records ) {
									if(records[0].pkey){
										parent.$.fancybox.close();
										alert("success");
										window.location.href = '/scrumr/sprint.action?&visit=1&projectId='+records[0].pkey;
									}
								},
								error: function(data) { },
								complete: function(data) { }
							});
						},
						error: function(data) { },
						complete: function(data) { }
					});
					
					
				}
			
			return false;
	});
	
	$("#newproj").fancybox({
		'overlayColor' : '#000',
        'overlayOpacity' : '0.6',
        'autoScale' : false
	});
	
	$(".signin").fancybox({
		'overlayColor' : '#000',
        'overlayOpacity' : '0.3',
        'autoScale' : false,
        'onComplete' : (function(){
               }),
        'onStart' : (function(){
                }),
        'onClosed' : (function() {
               })

	});
}); 
</script>
</head>
<body>
	<header>
    	<a href="<%= request.getContextPath() %>/" class="logo float-lft"></a>
    	<div class="tabs dashboard-tab"><a class="dashboard" href="">Dashboard</a></div>
    	<div class="tabs project-tab"><a class="projects" href="<%= request.getContextPath() %>/project.action">Projects</a></div>
    	<div class="right-div" style="float:right;">
    	</div>
	</header>
   <section class="bg-pat">
   <div id="create-project" class="bg-pat">
	       <div class="bg-pat form-cont">
	           <h1>Create New Project</h1>
	           <div class="bor float-lft"></div>
	       <form class="float-lft" id="new_project_form" method="POST">
	           <input type="text" name="pTitle" class="inp-box margin-rgt" placeholder="Give project name" required/>
	           <input type="date" id="datepickerFrom" name="pStartDate" class="cal inp-box" placeholder="Start date" required/>
	           <input type="date" id="datepickerTo" name="pEndDate" class="cal inp-box margin-left" placeholder="End date" required/>
	           <textarea cols="80" rows="3" name="pDescription"></textarea>
	           <select class="float-lft" name="pSprintDuration" >
	               <option value="1">Choose Sprint duration</option>
	               <option value="1">1 Week</option>
	               <option value="2">2 Week</option>
	               <option value="3">3 Week</option>
	               <option value="4">4 Week</option>
	               <option value="5">5 Week</option>
	           </select>
	           <input type="submit" class="float-rgt submit proj_submit" value="Create Project" />
	       </form>
	       <label id="proj-error" class="error-msg"></label>
       </div>
   </div>
   <div style="display:none;overflow:hidden !important;height:400px !importent;">
          	<div id="sign-in" class="">
		           <div class="loginHead float-lft">Sign In</div>
				       <form id="login-form" method="post" action="<%= request.getContextPath() %>/j_spring_security_check" accept-charset="utf-8">
				       <label>Username / Email:</label>
			           <input type="text" class="inp-box margin-rgt" placeholder="Enter Username" required id="username" name="j_username" maxlength="30" class="username">
			           <label>Password:</label>
			           <input type="password" class="inp-box margin-rgt" placeholder="Enter Password" required name="j_password" id="password" class="password">
			           <input type="submit" class="float-left submit" value="Login"/>
			           <label id="user-error" class="error-msg"></label> 
	      		 </form>
	      	  </div>
   			</div>
   </section>
</body>
</html>