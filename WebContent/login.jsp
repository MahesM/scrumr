<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html><head>
<meta content="text/html; charset=ISO-8859-1" http-equiv="Content-Type">
<script src="javascript/jquery-1.7.js" type="text/javascript"></script>
<link href="javascript/jquery-ui-1.8.14.custom.css" rel="stylesheet" type="text/css">
<script src="javascript/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="style.css">
<link href="http://www.qontext.com/wp-content/themes/qontext-v1.5/qontext.ico" type="image/x-icon" rel="shortcut icon">
<title>Scrum Board - Have fun in scrumming</title>
<%
String error = null;
HttpSession sessionVar = request.getSession(true);
if(sessionVar != null){
	sessionVar.invalidate();
	error = (String) request.getAttribute("error");
}

%>
<script type="text/javascript">
$(document).ready(function(){
	
     $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
     $(window).resize(function() {
         $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
     });
	$("#username").val('');
	$("#password").val('');
	
});
</script>
</head>
<body>
	<header>
            <a href="" class="logo float-lft"></a>
            <div class="float-rgt"></div>
        </header>
   <section class="bg-pat">
       <section class="form-cont">
           <h1>Login</h1>
           <div class="bor"></div>
       <form id="login-form" method="post" action="/scrumr/projects" accept-charset="utf-8">
           <input type="text" class="inp-box margin-rgt" placeholder="Enter Username" required id="username" name="username" maxlength="30" class="username">
           <input type="password" class="inp-box margin-rgt" placeholder="Enter Password" required name="password" id="password" class="password">
           <input type="submit" class="float-left submit" value="Login"/>
           <label class="error"><% if (error != null) {%><%= error %> <%}%></label> 
       </form>


       </section>
   </section>
    </body>
    </html>