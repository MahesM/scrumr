<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE HTML>
<html>
<head>
    <title>Scrumr</title>
    <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/pagination.css" />
    <jsp:include page="header.jsp" />
    
	<script type="text/javascript">
	$(document).ready(function(){
		 $('.bg-pat').css({'height': ($(window).height()) + 'px'});
	     $(window).resize(function() {
	         $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
	     }); 
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
  		   <div class="social-cont">
			<table cellpadding="10" cellspacing="10" align="center">
		      <tr><td colspan="8"><h3 align="center">Login to Scrumr</h3></td></tr>
		      <tr><td colspan="8"><p align="center">Please click on any icon.</p></td></tr>
		      <tr>
		       
		        <td>
		          <a href="socialauth.do?id=google">
		            <img src="<%= request.getContextPath() %>/themes/images/google.png"" alt="Gmail" title="Gmail" border="0"/>
		          </a>
		        </td>
		        <td>
		          <a href="socialauth.do?id=facebook">
		            <img src="<%= request.getContextPath() %>/themes/images/facebook.png" alt="Facebook" title="Facebook" border="0"/>
		          </a>
		        </td>
		        <td>
		          <a href="https://pramati.staging.qontext.com/portal/external/_local_scrum">
		            <img src="<%= request.getContextPath() %>/themes/images/qontext.jpg"" alt="Qontext" title="Qontext" border="0"/>
		          </a>
		        </td>
		       
		      </tr>
		      </table>
		</div>
   </section>
</body>
</html>