<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
	 
	 $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
     $(window).resize(function() {
         $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
     });
     
     var username = '<s:property value="loggedInUser.username"/>';
     if(username != null && username != ''){
      	$(".right-div").html('<img width="32px" height="32px" style="margin:4px;" class="float-lft"  src="themes/1.jpg"/><label class="float-lft loginLabel">Hi! '+username+',</label><a href="<%= request.getContextPath() %>/j_spring_security_logout" class="logout">Logout</a><div class="index-img"><a class="index-img1"/></a></div><div class="index-img"><a class="index-img2"></a></div>');
      }else{
      	$(".right-div").html('<a href="#sign-in" class="signin">Sign In</a><div class="index-img"><a class="index-img1"/></a></div><div class="index-img"><a class="index-img2"></a></div>');
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
							var startdate = new Date(Date.parse(project.start_date));
							startdate = startdate.format("mm/dd/yyyy");
							duration += startdate;
						}else{
							duration += 'No Start Date';
						}
						if(project.end_date != null){
							var enddate = new Date(Date.parse(project.end_date));
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
								people += '<img class="story-user" title="'+project.assignees[j].name+'" src="'+project.assignees[j].profile_picture+'"/>';
							}
						}else{
							people = 'No Assignees';
						}
						project_html += '<li id="'+project.pkey+'"class="feed-item"><div class="title"><label>'+title+'</label></div><div class="duration"><label>'+duration+'</label></div><div class="current-status">'+status+'</div><div class="people">'+people+'</div></li>';
					}
					project_html += "</ul>";
					$(".feed-cont").html(project_html);
				}
			}else {
				project_html += '<h2 style="margin:20px;display: inline-block;">Currently no projects available.</h2>';
				project_html += "</ul>";
				$(".feed-cont").html(project_html);
			}
		}
	});
	
	$.ajax({
		url: '/scrumr/api/v1/users/all',
		type: 'GET',
		async:false,
		success: function( records ) {
		var users_html = "";
		for(var i=0;i<records.length;i++){
			users_html += '<div class="emp"><div class="pic"><img src="'+records[i].profile_picture+'"/></div><div class="details"><label class="name">'+records[i].name+'</label><a class="email">'+records[i].email+'</a><label class="desig">'+records[i].designation+'</label></div><div class="businessUnit"><label class="unit">'+records[i].business_unit+'</label><label class="location">'+records[i].location+'</label></div><input style="margin-top:25px" id="'+records[i].id+'" class="userBtn btn clearBtn" value="Assign" type="button"/></div>';
		}
		$(".empResults").html(users_html);
		},
		error: function(data) { },
		complete: function(data) { }
	});
		
	$(".userBtn").live('click',function(){
			$(this).attr("value","Release");
			projAssignees.push($(this).attr("id"));
	});
		
	$('#new_project_form').submit(function(){
			var title = $('input[name=pTitle]');
			var description = $('textarea[name=pDescription]');
			var assignees = projAssignees;
			var start_date = $('input[name=pStartDate]');
			var end_date = $('input[name=pEndDate]');
			var duration = $('select[name=pSprintDuration]');
			var user = username;
			var days = days_between(new Date(Date.parse(start_date.val())),new Date(Date.parse(end_date.val())));
			if(days % 7 != 0){
				$(".error-msg").html("Project duration should be in weeks");
				return false;
			}else{
				if((days % (7*duration.val())) != 0){
					$(".error-msg").html("Project duration conflicts with sprint duration");
					return false;
				} 
			}
			var post_data = 'pTitle=' + title.val() + '&current_user='+user + '&pDescription=' + description.val() + '&assignees='
				+ assignees + '&pStartDate=' + start_date.val() + '&pEndDate=' + end_date.val() +'&pSprintDuration=' +duration.val();
			
			$.ajax({
				url: '/scrumr/restapi/project/create',
				type: 'POST',
				data: post_data,
				async:false,
				success: function( records ) {
					if(records.id){
						parent.$.fancybox.close();
						window.location.href = '/scrumr/sprintView.jsp?projectId='+records.id;
					}
				},
				error: function(data) { },
				complete: function(data) { }
			});
			
			return false;
	});
	
	$("#newproj").fancybox({
		'overlayColor' : '#000',
        'overlayOpacity' : '0.6',
        'autoScale' : false
	});
	
}); 
</script>
</head>
<body>
	<header>
    	<a href="<%= request.getContextPath() %>/" class="logo float-lft"></a>
    	<div class="tabs dashboard-tab"><a class="dashboard" href="">Dashboard</a></div>
    	<div class="tabs project-tab cur"><a class="projects" href="<%= request.getContextPath() %>/project.action">Projects</a></div>
    	<div class="right-div" style="float:right;"></div>
	</header>
   <section class="bg-pat">
   <div class="left-cont">
	   	<div class="caption-cont">
	   		<h2>Projects</h2>
	   		<a id="newproj" href="#create-project"><input style="float:right;margin: 30px 30px 10px 20px;border-radius:3px;" type="submit" class="status submit" value="Create Project"/></a>
	   </div>
	   <div style="display:none;overflow:hidden !important;height:400px !importent;">
          	<div id="create-project" class="bg-pat">
	       <div class="bg-pat form-cont">
	           <h2>Create New Project</h2>
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
	       <label class="error-msg"></label>
       </div>
   </div>
       </div>
	   <div class="feed-cont">
	   	 
	   </div>	
   </div>
      </section>
</body>
</html>