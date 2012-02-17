
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<% response.sendRedirect("login.action"); %>

<!DOCTYPE HTML>
<html>
<head>
	<title>Scrumr</title>
</head>
<body>

	<table cellpadding="10" cellspacing="10" align="center">
      <tr><td colspan="8"><h3 align="center">Login to Scrumr</h3></td></tr>
      <tr><td colspan="8"><p align="center">Please click on any icon.</p></td></tr>
      <tr>
       
        <td>
          <a href="socialauth.do?id=google">
            <img src="images/gmail-icon.jpg" alt="Gmail" title="Gmail" border="0"/>
          </a>
        </td>
        <td>
          <a href="https://pramati.staging.qontext.com/portal/external/_local_scrum">
            <img src="images/qontext_icon.jpg" alt="Qontext" title="Qontext" border="0"/>
          </a>
        </td>
       
      </tr>
      </table>

</body>
</html>