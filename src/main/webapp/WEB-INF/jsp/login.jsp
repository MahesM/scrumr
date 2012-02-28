<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
<title>Scrumr</title>
<!-- Default Includes .. -->	
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<jsp:include page="header.jsp" />

<script type="text/javascript">
$(document).ready(function(){
	 $('.bg-pat').css({'height': ($(window).height()) + 'px'});
	 $('#projectList').css({'height': ($(window).height() - 120) + 'px'});
     $(window).resize(function() {
         $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
     }); 
     
	 var projListScroll = null;
    
    
	var projAssignees = new Array();
		projAssignees.push(userLogged);
	
	 var dates = $( "#datepickerFrom, #datepickerTo" ).datepicker({
		defaultDate: null,
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
		window.location.href = 'sprint.action?&view=sprint&projectId='+$(this).attr("id");
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
    		url: 'api/v1/projects/'+id,
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
	//	$('#datepickerFrom').datepicker("setDate", new Date() );
		duration.val("");
	});
	
	$("#delete-proj").live('click',function(){
		var id = $(this).parent().parent().find('.pno').attr("id");
		$.ajax({
			url: 'api/v1/projects/delete/'+id,
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
		var months = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
		               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ];
		$.ajax({
			url: 'api/v1/projects/user/'+userLogged,
			type: 'GET',
			async:false,
			success: function( obj ) {
				var project_html = '';
				if(obj){
					if(obj.length > 0){
						for(var i=0;i < obj.length;i++){
							var project = obj[i];
							var title = project.title;
							var duration = '';
							if(project.start_date != null){
								var startdateString = "";
								var startdate = new Date(project.start_date);
								if(startdate.getYear() == new Date().getYear()){
									startdateString = months[startdate.getMonth()]+" "+ startdate.getDate();
								}else{
									startdateString = months[startdate.getMonth()]+" "+ startdate.getDate()+", "+startdate.getYear();
								}
								duration += startdateString;
							}else{
								duration += 'No Start Date';
							}
							if(project.end_date != null){
								var enddateString = "";
								var enddate = new Date(project.end_date);
								if(enddate.getYear() == new Date().getYear()){
									enddateString = months[enddate.getMonth()]+" "+ enddate.getDate();
								}else{
									enddateString = months[enddate.getMonth()]+" "+ enddate.getDate()+", "+enddate.getYear();
								}
								duration += ' - '+ enddateString;
							}else{
								duration += ' - No End Date';
							}
							var status ="Not Started";
							if(project.current_sprint > 0){
								if(project.status == "Finished"){
									status = '<label>Finished</label>';
								}else{
									status = '<a href="sprint.action?&view=sprint&projectId='+project.pkey+'"><span>Sprint '+project.current_sprint+'</span> '+project.status+'</a>';
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
							project_html += '<tr class="'+even+'"><td class="pno" id="'+project.pkey+'"><label>'+project.pkey+'</label></td><td class="ptitle"><a href="sprint.action?view=project&visit=0&projectId='+project.pkey+'">'+title+'</a></td><td class="pdesc">'+project.description+'</td><td class="pstart">'+duration+'</td><td class="status">'+status+'</td><td class="users">'+people+'</td><td class="actions"><a id="edit-proj" href="#create-project"><img title="edit" style="width:16px;height:16px;margin-right:5px;cursor:pointer;" src="/scrumr/themes/images/edit.gif"/></a><img id="delete-proj" style="width:16px;height:16px;cursor:pointer;" title="delete" src="/scrumr/themes/images/delete.gif"/></td></tr>';
							
						}
						$("#project-list tbody.content").html(project_html);
						projListScroll = $('#projectList').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
						
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
			
			if(userLogged == "" || userLogged == null){
				return false;
			}else{
				var post_data1 = {'username':userLogged,'displayname':'<s:property value="loggedInUser.displayname"/>','fullname':'<s:property value="loggedInUser.fullname"/>','emailid':'<s:property value="loggedInUser.emailid"/>','avatarurl':'<s:property value="loggedInUser.avatarurl"/>'};
				$.ajax({
					url: 'api/v1/users/create',
					type: 'POST',
					data: post_data1,
					async:false,
					success: function( records ) {
						var assignees = new Array();
						assignees.push(userLogged);
						if(update == false){
							var post_data = 'pTitle=' + title.val() + '&current_user='+userLogged + '&pDescription=' + description.val() + '&assignees='
								+ assignees + '&pStartDate=' + start_date.val() + '&pEndDate=' + end_date.val() +'&pSprintDuration=' +duration.val();
							$.ajax({
								url: 'api/v1/projects/create',
								type: 'POST',
								data: post_data,
								async:false,
								success: function( records ) {
									if(records[0].pkey){
										parent.$.fancybox.close();
										window.location.href = 'sprint.action?&visit=1&projectId='+records[0].pkey;
									}
								},
								error: function(data) { },
								complete: function(data) { }
							});
						}else{
							var post_data = 'pNo='+id.val()+'&pTitle=' + title.val() + '&current_user='+userLogged + '&pDescription=' + description.val() + '&assignees='
							+ assignees + '&pStartDate=' + start_date.val() + '&pEndDate=' + end_date.val() +'&pSprintDuration=' +duration.val();
							$.ajax({
								url: 'api/v1/projects/update',
								type: 'POST',
								data: post_data,
								async:false,
								success: function( records ) {
									if(records[0].pkey){
										parent.$.fancybox.close();
										window.location.href = 'sprint.action?&visit=1&projectId='+records[0].pkey;
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
	   		<h2>All Projects</h2>
	   		<a id="newproj" href="#create-project"><input style="float:right;margin: 30px 0px 10px 20px;border-radius:3px;" type="submit" class="status submit" value="Create Project"/></a>
	   	</div>
	   	<div id="projectList">
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