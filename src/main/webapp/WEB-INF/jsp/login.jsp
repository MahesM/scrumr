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
	 $('.bg-pat').css({'height': ($(window).height()) + 'px'});
     $(window).resize(function() {
         $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
     }); 
     
 	var username = '<s:property value="loggedInUser.username"/>';
	var fullname = '<s:property value="loggedInUser.fullname"/>';
	var displayname = '<s:property value="loggedInUser.displayname"/>';
	var avatarurl = '<s:property value="loggedInUser.avatarurl"/>';
	var emailid = '<s:property value="loggedInUser.emailid"/>';
	var source = '<s:property value="source"/>';
	var qontextHostUrl = '<s:property value="qontextHostUrl"/>';
     if(username != null && username != ''){
     	$(".right-div").html('<img width="32px" height="32px" style="margin:4px;" class="float-lft"  src="'+qontextHostUrl+avatarurl+'"/><label class="float-lft loginLabel">Hi!, '+fullname+'</label><div class="index-img"><a class="index-img1"/></a></div><div class="index-img"><a class="index-img2"></a></div>');
     }
     
     function days_between(date1, date2) {
 	    var ONE_DAY = 1000 * 60 * 60 * 24;
 	    var date1_ms = date1.getTime();
 	    var date2_ms = date2.getTime();
 	    var difference_ms = Math.abs(date1_ms - date2_ms);
 	    return Math.round(difference_ms/ONE_DAY);
 	}
     
	var projAssignees = new Array();
		projAssignees.push(username);
	
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
		window.location.href = '/scrumr/sprint.action?&view=sprint&projectId='+$(this).attr("id");
	});
	
	$("#edit-proj").live('click',function(){
		$("#heading").html("Update Project");
		$(".proj_submit").val("Update");
		$(".proj_submit").attr("id","update");
		var title = $('input[name=pTitle]');
		var description = $('textarea[name=pDescription]');
		var start_date = $('input[name=pStartDate]');
		var end_date = $('input[name=pEndDate]');
		var duration = $('select[name=pSprintDuration]');
		var pno = $('input[name=pNo]');
		var id = $(this).parent().parent().find('.pno').attr("id");
		$.ajax({
    		url: '/scrumr/api/v1/projects/'+id,
    		type: 'GET',
    		async:false,
    		success: function( project ) {
    			if(project != null && project.length > 0){
    				project = project[0];
    				title.val( project.title);
    				description.val(project.description);
    				$('#datepickerFrom').datepicker("setDate", new Date(project.start_date) );
    				$('#datepickerFrom').attr("disabled",true);
    				$('#datepickerTo').attr("disabled",true);
    				duration.attr("disabled",true);
    				if(project.end_date){
	    				$('#datepickerTo').datepicker("setDate", new Date(project.end_date) );
    				}
    				duration.val(project.duration);
    				pno.val(id);
    			}
    				
    		},
    		error: function(data) { },
    		complete: function(data) { }
    	});
	});
	
	$("#newproj").live('click',function(){
		$("#heading").html("Create Project");
		$(".proj_submit").val("Create");
		$(".proj_submit").attr("id","create");
		var title = $('input[name=pTitle]');
		var description = $('textarea[name=pDescription]');
		var start_date = $('input[name=pStartDate]');
		var end_date = $('input[name=pEndDate]');
		var duration = $('select[name=pSprintDuration]');
		title.val("");
		description.val("");
		$('#datepickerFrom').attr("disabled",false);
		$('#datepickerTo').attr("disabled",false);
		duration.attr("disabled",false);
		$('#datepickerFrom').datepicker("setDate", new Date() );
		duration.val("");
	});
	
	$("#delete-proj").live('click',function(){
		var id = $(this).parent().parent().find('.pno').attr("id");
		$.ajax({
			url: '/scrumr/api/v1/projects/delete/'+id,
			type: 'GET',
			async:false,
			success: function( obj ) {
				obj = $.parseJSON(obj);
				if(obj && obj.result == "success"){
					 populateProjects();
				}
			}
		});
	});

	function populateProjects(){
		$.ajax({
			url: '/scrumr/api/v1/projects/user/'+username,
			type: 'GET',
			async:false,
			success: function( obj ) {
				var project_html = '<ul class="feed">';
				if(obj){
					if(obj.length > 0){
						for(var i=0;i < obj.length;i++){
							var project = obj[i];
							var title = project.title;
							var duration = '';
							if(project.start_date != null){
								var startdate = new Date(project.start_date);
								startdate = startdate.format("mm/dd/yyyy");
								duration += startdate;
							}else{
								duration += 'No Start Date';
							}
							if(project.end_date != null){
								var enddate = new Date(project.end_date);
								enddate = enddate.format("mm/dd/yyyy");
								duration += ' - '+ enddate;
							}else{
								duration += ' - No End Date';
							}
							var status ="Not Started";
							if(project.current_sprint > 0){
								if(project.status == "Finished"){
									status = '<label>Finished</label>';
								}else{
									status = '<label><span>Sprint '+project.current_sprint+'</span> '+project.status+'</label>';
								}
							}else{
								status = '<label>Not Started</label>';
							}
							var people="";
							var people_count = project.assignees.length > 3 ? 3:project.assignees.length;
							if(people_count > 0){
								for(var j=0; j<people_count; j++){
									people += '<img class="story-user" title="'+project.assignees[j].fullname+'" src="'+qontextHostUrl+project.assignees[j].avatarurl+'"/>';
								}
							}else{
								people = 'No Assignees';
							}
							var even = "odd";
							if(((i+1)%2) == 0){
								even = "even";
							}
							project_html += '<tr class="'+even+'"><td class="pno" id="'+project.pkey+'"><a href="/scrumr/sprint.action?&view=sprint&visit=0&projectId='+project.pkey+'">'+project.pkey+'</a></td><td class="ptitle"><a href="/scrumr/sprint.action?&view=sprint&visit=0&projectId='+project.pkey+'">'+title+'</a></td><td class="pdesc">'+project.description+'</td><td class="pstart">'+duration+'</td><td class="status">'+status+'</td><td class="users">'+people+'</td><td class="actions"><a id="edit-proj" href="#create-project"><img title="edit" style="width:16px;height:16px;margin-right:5px;cursor:pointer;" src="/scrumr/themes/images/edit.gif"/></a><img id="delete-proj" style="width:16px;height:16px;cursor:pointer;" title="delete" src="/scrumr/themes/images/delete.gif"/></td></tr>';
							
						}
						project_html += "</ul>";
						$("#project-list tbody.content").html(project_html);
					}else{
						project_html = '<p style="margin:10px 0px 10px 0px;display: inline-block;">Currently no projects available.</p>';
						$("#project-list tbody.content").html(project_html);
					}
				}else {
					project_html = '<p style="margin:10px 0px 10px 0px;display: inline-block;">Currently no projects available.</p>';
					$("#project-list tbody.content").html(project_html);
				}
			}
		});
	}
	function createProject(update){
		var id = $('input[name=pNo]');
		var title = $('input[name=pTitle]');
		var description = $('textarea[name=pDescription]');
		var start_date = $('input[name=pStartDate]');
		var end_date = $('input[name=pEndDate]');
		var duration = $('select[name=pSprintDuration]');
		if(trim(title.val()) == ""){
			$("#proj-error").html("Project title is mandatory");
			return false;
		}
			if(end_date.val() != ""){
				var days = days_between(new Date(Date.parse(start_date.val())),new Date(Date.parse(end_date.val())));
					if((days < (7*duration.val()))){
						$("#proj-error").html("Project duration conflicts with sprint duration");
						return false;
					} 
			}
			
			if(username == "" || username == null){
				return false;
			}else{
				var post_data1 = {'username':username,'displayname':'<s:property value="loggedInUser.displayname"/>','fullname':'<s:property value="loggedInUser.fullname"/>','emailid':'<s:property value="loggedInUser.emailid"/>','avatarurl':'<s:property value="loggedInUser.avatarurl"/>'};
				$.ajax({
					url: '/scrumr/api/v1/users/create',
					type: 'POST',
					data: post_data1,
					async:false,
					success: function( records ) {
						var assignees = new Array();
						assignees.push(username);
						if(update == false){
							var post_data = 'pTitle=' + title.val() + '&current_user='+username + '&pDescription=' + description.val() + '&assignees='
								+ assignees + '&pStartDate=' + start_date.val() + '&pEndDate=' + end_date.val() +'&pSprintDuration=' +duration.val();
							$.ajax({
								url: '/scrumr/api/v1/projects/create',
								type: 'POST',
								data: post_data,
								async:false,
								success: function( records ) {
									if(records[0].pkey){
										parent.$.fancybox.close();
										window.location.href = '/scrumr/sprint.action?&visit=1&projectId='+records[0].pkey;
									}
								},
								error: function(data) { },
								complete: function(data) { }
							});
						}else{
							var post_data = 'pNo='+id.val()+'&pTitle=' + title.val() + '&current_user='+username + '&pDescription=' + description.val() + '&assignees='
							+ assignees + '&pStartDate=' + start_date.val() + '&pEndDate=' + end_date.val() +'&pSprintDuration=' +duration.val();
							$.ajax({
								url: '/scrumr/api/v1/projects/update',
								type: 'POST',
								data: post_data,
								async:false,
								success: function( records ) {
									if(records[0].pkey){
										parent.$.fancybox.close();
										window.location.href = '/scrumr/sprint.action?&visit=1&projectId='+records[0].pkey;
									}
								},
								error: function(data) { },
								complete: function(data) { }
							});
						}
					},
					error: function(data) { },
					complete: function(data) { }
				});
				
				
			}
		
		return false;
	}
	
	populateProjects();
	 
	$('#create').live('click',function(){
			createProject(false);
	});
	
	$('#update').live('click',function(){
		createProject(true);
	});
	
	$("#newproj").fancybox({
		'overlayColor' : '#000',
	    'overlayOpacity' : '0.6',
	    'autoScale' : false,
	    'autoDimensions':false
	});
	$("#edit-proj").fancybox({
		'overlayColor' : '#000',
	    'overlayOpacity' : '0.6',
	    'autoScale' : false,
	    'autoDimensions':false
	});
	
}); 
</script>
</head>
<body>
	<header>
    	<a href="<%= request.getContextPath() %>/home.action" class="logo float-lft"></a>
    	<div class="right-div" style="float:right;"></div>
	</header>
   <section class="bg-pat">
   <div class="left-cont scroll">
	   	<div class="caption-cont">
	   		<h2>Projects</h2>
	   		<a id="newproj" href="#create-project"><input style="float:right;margin: 30px 0px 10px 20px;border-radius:3px;" type="submit" class="status submit" value="Create Project"/></a>
	   		<table id="project-list" class="project-list">
             	 <thead>
	             	 <tr class="header">
	             	 	<td>Project #</td>
	             	 	<td>Title</td>
	             	 	<td>Description</td>
	             	 	<td>Duration</td>
	             	 	<td>Status</td>
	             	 	<td>People</td>
	             	 	<td>Actions</td>
	             	 </tr>
	             </thead>
             	 <tbody class="content">
              	</tbody>
             </table> 
	   </div>
	   <div style="display:none;overflow:hidden !important;height:400px !important;width:500px !important;">
          	<div id="create-project" class="bg-pat">
	       <div class="bg-pat form-cont">
	           <h2 id="heading" >Create Project</h2>
	       <form class="float-lft" id="new_project_form" method="POST">
	        	<input type="hidden" name="pNo" id="pNo" value=""/>
	           <input type="text" name="pTitle" class="inp-box margin-rgt" placeholder="Give project name" required="required"/>
	           <input type="date" id="datepickerFrom" name="pStartDate" class="cal inp-box" placeholder="Start date" required="required"/>
	           <input type="date" id="datepickerTo" name="pEndDate" class="cal inp-box margin-left" placeholder="End date"/>
	           <textarea cols="80" rows="3" name="pDescription"></textarea>
	           <select class="float-lft" name="pSprintDuration" >
	               <option value="1">1 Week</option>
	               <option value="2">2 Week</option>
	               <option value="3">3 Week</option>
	               <option value="4">4 Week</option>
	               <option value="5">5 Week</option>
	           </select>
	           <input type="button" class="float-rgt submit proj_submit" value="Create Project" />
	       </form>
	       <label id="proj-error" class="error-msg"></label>
       </div>
   </div>
       </div>
   </div>
      </section>
</body>
</html>