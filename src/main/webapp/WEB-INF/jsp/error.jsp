
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html>

<head>
	
	<meta>
	<title> Oops !!! RP Error </title>
	
	<script type="text/javascript">
	$(document).ready( function() {
	
		
	});
	</script>
	
</head>

<body>

	<!-- Body Content -->
	
	<section class="float-left clear full-width" id="body-content">
		<div class="in">
			 
			ERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
		
			<div class="links-cont">
				<label class="page-title">Error</label>
	          	<input type="button" class="gray-btn gray-btn-small" value="<s:text name="errors.back"/>" onClick="history.go(-1)">
			</div>

		</div>
	</section>
	
</body>
		