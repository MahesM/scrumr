<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="javascript/jquery-1.7.js"></script>
<link type="text/css" rel="stylesheet" href="javascript/jquery-ui-1.8.16.custom.css" />
<script type="text/javascript" src="javascript/jquery-ui-1.8.16.custom.min.js"></script>
<link href="styles.css" type="text/css" rel="stylesheet" />
<link rel="shortcut icon" type="image/x-icon" href="http://www.qontext.com/wp-content/themes/qontext-v1.5/qontext.ico">
<title>Scrum Board - Projects</title>
<%

String actualSession = null;
String currentSession = null;
String project = null;
String stories = null;
String projectId = null;
HttpSession sessionVar = request.getSession(false);
currentSession = (String) sessionVar.getId();
actualSession = (String) sessionVar.getAttribute("session");

String username = "";
if(currentSession != null && actualSession != null && actualSession.equals(currentSession)){
	username = (String) sessionVar.getAttribute("username");
	/* project = (String) request.getAttribute("project");
	stories = (String) request.getAttribute("stories"); */
	projectId = request.getParameter("projectId");
}else{
	String destination = "/login.jsp";
	request.setAttribute("error", "Please login to continue");
	RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
	rd.forward(request, response);
}
	
%>
<script type="text/javascript">
$(document).ready(function(){
	$( ".status-cont" ).draggable();
	$.ajax({
		url: '/scrumr/restapi/project/<%= projectId%>',
		type: 'GET',
		async:false,
		success: function( project ) {
			console.log(project);
			if(project){
				var proj_html = '<ul><li class="clearfix"><div class="userImage"><img alt="image1" src="themes/1.jpg"></div><div class="projectBrief"><div class="pjHead"><div class="r1"><a class="pjTitle" href="#">'+project.title+'</a> <div class="createdby" ><img class="icon" src="themes/user.gif">by <a href="">'+project.createdby+'</a></div></div><div class="r2">Created on '+project.creation_date+'</div></div><div class="pjCont"><p>'+project.description+'</p></div>	<div class="pFoot"><div class="modified">Last updated on '+project.last_updated+' by <a href="">'+project.last_updatedby+'</a></div><p class="pFootTools"><img class="icon" src="themes/comment.gif"><a class="comments">Comments</a><a class="status"><b>'+project.status+'</b></a></p></div></div></li></ul>';
				$("#projDetails").html(proj_html);
			}
		},
		error: function(data) { },
		complete: function(data) { }
	});
	
	$.ajax({
		url: '/scrumr/restapi/stories/search/<%= projectId%>',
		type: 'GET',
		async:false,
		success: function( stories ) {
			if(stories && stories.length){
				var story_html = "";
				for(var i=0;i<stories.length;i++){
					var story = stories[i];
					story_html += '<li class="clearfix" id="'+story.id+'"><span class="number">'+(i+1)+'.</span><div class="story projectBrief"><div class="pjHead"><div class="r1"><input type="checkbox" class="story-check" /><a class="pjTitle" href="#">'+story.title+'</a> <div class="createdby" ><img class="icon" src="themes/user.gif">by <a href="">'+story.createdby+'</a></div></div><div class="r2">Created on'+story.creation_date+'</div></div><div class="pjCont"><p>'+story.description+'</p></div><div class="pFoot"><div class="modified">Last updated on '+story.last_updated+' by <a href="">'+story.last_updatedby+'</a></div><p class="pFootTools"><img class="icon" src="themes/comment.gif"><a class="comments">Comments</a><a class="status"><b>'+story.status+'</b></a></p></div></div></li>';
				}
				$(".currentSprint").show();
				$(".userStoryContent").html(story_html);
			}
		},
		error: function(data) { },
		complete: function(data) { }
	});
	
	
	$('#story_form').submit(function(){
		var title = $('input[name=stTitle]');
		var description = $('textarea[name=stDescription]');
		var priority = $('select[name=stPriority]');
		var user = "<%= username %>";
		var projectId= <%= projectId%>;
		var post_data = 'stTitle=' +title.val() + '&projectId='+projectId+'&stDescription=' + description.val() + '&stPriority=' + priority.val() + '&user=' +user;
		alert(post_data);
		$.ajax({
			url: '/scrumr/restapi/stories/create',
			type: 'POST',
			data: post_data,
			async:false,
			success: function( result ) {
			var records = result;
				alert(records);
				window.location.reload(true);
			},
			error: function(data) { },
			complete: function(data) { }
		});
		
		return false;
	});
	
	$(".currentSprint").live('click', function(){
		alert("hi");
		var storyList = new Array();
		$('.story-check:checked').each(function(){
			storyList.push($(this).parent().parent().parent().parent().attr("id"));			
		});
		
		if(storyList.length > 0){
			var projectId= <%= projectId%>;
			var post_data = 'projectId='+projectId+'&stories='+storyList;
			
			$.ajax({
				url: '/scrumr/restapi/stories/addtosprint',
				type: 'POST',
				data: post_data,
				async:false,
				success: function( result ) {
				var records = result;
					console.log(records);
					alert(records);
					$('.story-check:checked').attr("disabled",true);
				},
				error: function(data) { },
				complete: function(data) { }
			});
			
		}
	});
	
	$(".sprintView").live('click', function(){
		window.location.href = "/scrumr/sprintView.jsp?projectId=<%= projectId%>";
	});
		
});
</script>
</head>
<body>
	<div class="container">
		<div class="header">
			<div class="logo" >
				</div><img style="margin-top:25px;margin-left:-20px;" src="themes/reg.gif"/>
		</div>
			<div class="userBar clear" >
				<label><b>Welcome, Arun Krishna Omkaram</b></label>
				<span class="settings"></span>
				<label class="right"><b>User Settings</b></label>
			</div>
		<div class="clear wrapper">
			<div class="containerL">
				<div class="contentbox">
				<div class="titlebar"><b>Project Details</b><b style="float:right;margin-right:10px;cursor:pointer;" class="sprintView">Sprint View</b></div>
				<div id="projectsDetails" class="projectContent">
					<ul id="projDetails">
						
						</ul>
						<div class="userStories">
							<div class="stories"><h2 class="title">User Stories</h2></div>
							<form id="story_form" method="POST">
								<div class="sTitle">
									<input type="text" id="storyTitle" name="stTitle" placeholder="Enter Story Title"/>
								</div>
								<select name="stPriority" id="storyPriority">
								<option selected="selected" value="1">Priority 1</option>
								<option value="2">Priority 2</option>
								<option value="3">Priority 3</option>
								<option value="4">Priority 4</option>
								</select>
								<div class="sContent">
									<textarea class="text" name="stDescription" rows="4" placeholder="Enter Story Details"></textarea>
								</div>
								<input type="submit" class="createStory" value="Create A Story"/>
							</form>
							<ul class="userStoryContent">
							</ul>
							<div class="currentSprint">Add To Current Sprint</div>
							<div class="clearFix"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="containerR">
				<div class="statusfeed">
					<div class="titlebar"><b>Status Feed</b></div>
					<div class="status-cont">
						<ul>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>Today</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>Today</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>Today</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>Today</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>1 day ago</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>2 days ago</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
						</ul>
					</div>
					<div class="chatBox"></div>
				</div>
				<div class="notifications">
				<div class="titlebar"><b>Notifications</b></div>
					<div class="status-cont not-cont">
						<ul>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>Today</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>Today</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>Today</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>Today</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>1 day ago</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
							<li class="clearfix">
								<a class="author" href="#">Abdul Muneer</a> <span>2 days ago</span>
								<p>The wireless carriers' investment in 4G networks could be the salve that the ailing U.S.</p>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>