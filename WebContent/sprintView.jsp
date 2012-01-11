<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Scrumr</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="javascript/jquery-1.7.js"></script>
	<script type="text/javascript" src="javascript/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src="javascript/mwheelIntent.js"></script>
	<script type="text/javascript" src="javascript/jscrollpane.min.js"></script>
	<script type="text/javascript" src="javascript/mousewheel.js"></script>
	<script type="text/javascript" src="javascript/jquery.fancybox-1.3.4.js"></script>
	<link type="text/css" rel="stylesheet" href="javascript/jquery.fancybox-1.3.4.css" />
	<link type="text/css" rel="stylesheet" href="javascript/jquery-ui-1.8.16.custom.css" />
	<link type="text/css" rel="stylesheet" href="javascript/jscrollpane.css" />
	<link rel="shortcut icon" type="image/x-icon" href="http://www.qontext.com/wp-content/themes/qontext-v1.5/qontext.ico">
    <link href="style.css" rel="stylesheet"/>
<%
String actualSession = null;
String currentSession = null;
String projectId = null;
HttpSession sessionVar = request.getSession(false);
currentSession = (String) sessionVar.getId();
actualSession = (String) sessionVar.getAttribute("session");

String username = "default";
if(currentSession != null && actualSession != null && actualSession.equals(currentSession)){
	username = (String) sessionVar.getAttribute("username");
	projectId = request.getParameter("projectId");
}else{
	String destination = "/index.jsp";
	request.setAttribute("error", "Please login to continue");
	RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
	rd.forward(request, response);
}
%>
  <script type='text/javascript'>
        $(function() {
        	
        	var current_sprint = 0;
        	var sprintinview = 0;
        	var totalsprints = 0;
        	var creator;
        	var users = null;
        	var userObject = new Object();
        	var userLogged = "<%=username%>";
        	function days_between(date1, date2) {

        	    var ONE_DAY = 1000 * 60 * 60 * 24;

        	    // Convert both dates to milliseconds
        	    var date1_ms = date1.getTime();
        	    var date2_ms = date2.getTime();

        	    // Calculate the difference in milliseconds
        	    var difference_ms = Math.abs(date1_ms - date2_ms);
        	    
        	    // Convert back to days and return
        	    return Math.round(difference_ms/ONE_DAY);

        	}
        	
        	function populateProjectDetails(){
        		$.ajax({
            		url: '/scrumr/restapi/project/<%= projectId%>',
            		type: 'GET',
            		async:false,
            		success: function( project ) {
            			if(project != null){
            				current_sprint = project.current_sprint;
            				sprintinview = current_sprint;
            				creator = project.createdby;
            				var title = project.title;
            				if(title.length > 30){
            					title = title.substring(0,30)+" ...";
            				}
            				$('#projectName').html(title.toUpperCase());
            				users = project.assignees;
            				totalsprints = project.no_of_sprints;
            				$("#people").html('');
            				if(users != null && users.length > 0){
            					for(var i=0;i< users.length;i++){
            						$("#people").append('<li><img title="'+users[i].name+'" src="'+users[i].profile_picture+'" width="36px" height="36px"/></li>');
            						userObject[users[i].id] = users[i];
            					}
            				}else{
            					$("#people").html('No assignees for the project');
            				}
            			}
            		},
            		error: function(data) { },
            		complete: function(data) { }
            	});
        	}
			function populateUnassignedStories(name){
				 var post_data1 = 'sprintId=0&nameString='+name+'&projectId=<%= projectId%>';
	        	 $.ajax({
	        		url: '/scrumr/restapi/stories/search',
	        		type: 'POST',
	        		data: post_data1,
	        		async:false,
	        		success: function( stories ) {
        				var story_unassigned = '';
	        			if(stories != null && stories.length){
	        				for(var i=0;i<stories.length;i++){
	        					var story = stories[i];
	        					var userObj = userObject[story.createdby];
	        					story_unassigned += '<li id="st'+story.id+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label><div class="currentSprint float-rgt enable"></div></div></div><a href="javascript:void(0);" class="strRmv remove"></a></li>';
	        				}
	        			}else{
	        				story_unassigned += '<b>No pending stories for the project</b>';
	        			}
        				$("#storyList ul").html(story_unassigned);
        				$("#storyList").jScrollPane({
        					autoReinitialise: true
        				});
	        		},
	        		error: function(data) { },
	        		complete: function(data) { }
	        	});
	        	
			}
			 
			function populateSprints(){
       		 var post_data2 = 'projectId=<%= projectId%>';
		        	$.ajax({
		        		url: '/scrumr/restapi/sprints/search',
		        		type: 'POST',
		        		data: post_data2,
		        		async:false,
		        		success: function( sprints ) {
		        			console.log(sprints);
			        			$("#project-view").html('<tr><td colspan="1"  class="stages"></td><td colspan="1"  class="stages" ></td><td colspan="1"  class="stages"></td><td colspan="1" class="stages"></td></tr>');
		        				
			        			$('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
		        		},
		        		error: function(data) { },
		        		complete: function(data) { }
		        	});
			}
			
			function populateSprintStories(sprint){
	        		 if(sprint == 0){
		        		 var string = '<label>No Sprints available for this project.</label>';
		        		 $(".sprints").html(string);
		        	 }else{
		        		 var sprintList = "";
			        	 for(var i=1; i<=totalsprints;i++){
			        		 if(i == sprint){
				        		 sprintList += '<li class="sprintHead current">Sprint '+i+' &#187; </li>';
			        		 }else{
			        			 sprintList += '<li class="sprintHead">Sprint '+i+' &#187; </li>';
			        		 }
			        	 }
			        	 $(".sprints").html(sprintList);
		        	 }
					
	        		 var post_data2 = 'sprintId='+sprint+'&projectId=<%= projectId%>';
			        	$.ajax({
			        		url: '/scrumr/restapi/stories/search',
			        		type: 'POST',
			        		data: post_data2,
			        		async:false,
			        		success: function( stories ) {
				        			$("#sprint-view").html('<tr><td colspan="1"  class="green stages"></td><td colspan="1"  class="yellow stages" ></td><td colspan="1"  class="blue stages"></td><td colspan="1" class="pink stages"></td></tr>');
			        				var story_unassigned = '<div class="header" ><span></span>Sprint Stories</div><ul id="notstarted" class="story">';
			        				var story_dev = '<div class="header" ><span></span>Development</div><ul id="dev" class="story">';
			        				var story_review = '<div class="header" ><span></span>Review &amp; QA</div><ul id="review" class="story">';
			        				var story_finished = '<div class="header" ><span></span>Finished</div><ul id="finished" class="story">';
				        			if(stories != null && stories.length > 0){
			        					var str = '';	
			        					for(var i=0;i<stories.length;i++){
				        					var story = stories[i];
				        					var userObj = userObject[story.createdby];
				        					str = '<li id="st'+story.id+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label><a class="viewStory" href="#story-cont" src="images/view.png" width="26" height="26" class=""></a></div></div><a href="javascript:void(0);" class="sptRmv remove"></a></li>';
				        					if(story.status == "Not Started"){
				        						story_unassigned += str;
				        					}else if(story.status == "Development"){
				        						story_dev += str;
				        					}else if(story.status == "Review"){
				        						story_review += str;
				        					}else if(story.status == "Finished"){
				        						story_finished += str;
				        					}
				        				}
				        				
				        				
				        				story_unassigned += '</ul>';
				       					story_dev += '</ul>';
				       					story_review += '</ul>';
				       					story_finished += '</ul>';
				        				$(".green").html(story_unassigned);
				        				$(".yellow").html(story_dev);
				        				$(".blue").html(story_review);
				        				$(".pink").html(story_finished);

				        				$( ".stages ul" ).sortable({
				        	        		connectWith: ".story",
				        	        		appendTo: 'body',
				        	        		forcePlaceholderSize: true,
				        	        		placeholder: 'ui-state-highlight',
				        	        		update: function( event, ui ) {
				        	   				 	var id = ui.item.attr("id");
				        	   				 	var status = "Not Started";
				        		   				var stat = $(this).attr("id");
				        		   				if(stat == "dev"){
				        		       				status = "Development";
				        		       			}else if(stat == "review"){
				        		       				status = "Review";
				        		       			}else if(stat == "notstarted"){
				        		       				status = "Not Started";
				        		       			}else if(stat == "finished"){
				        		       				status = "Finished";
				        		       			}
				        		   				var success = updateStoryStatus(id.split("st")[1],status);
				        		   				if(success == false){
				        		   					$(this).sortable('cancel');
				        		   				}
				        		   			}
				        	    		}).disableSelection();
				        				
				        				$(".viewStory").fancybox({
				        	        		'overlayColor' : '#000',
				        	                'overlayOpacity' : '0.6',
				        	                'autoScale' : false,
				        	                'onComplete' : (function(){
				        	                    scrollpane =$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
				        	                       }),
				        	                'onStart' : (function(){
				        	                        }),
				        	                'onClosed' : (function() {
				        	                	$("#stPeople").hide();
				        	                     scrollpane.destroy();
				        	                       })

				        	        	});
				        			}else{
				        				story_unassigned += '</ul>';
				       					story_dev += '</ul>';
				       					story_review += '</ul>';
				       					story_finished += '</ul>';
				        				$(".green").html(story_unassigned);
				        				$(".yellow").html(story_dev);
				        				$(".blue").html(story_review);
				        				$(".pink").html(story_finished);
				        				
				        				$("#notstarted").html('<label style="margin:5px;float:left">No Stories assigned to this sprint.</label>');
				        			}
				        			$('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
			        		},
			        		error: function(data) { },
			        		complete: function(data) { }
			        	});
			}
			
			function addtoCurrentSprint(storyList,sprint){
				var projectId= <%= projectId%>;
    			var post_data = 'projectId='+projectId+'&stories='+storyList+'&status=Not Started&sprint='+sprint;
    			
    			$.ajax({
    				url: '/scrumr/restapi/stories/addtosprint',
    				type: 'POST',
    				data: post_data,
    				async:false,
    				success: function( result ) {
    					
    				},
    				error: function(data) { },
    				complete: function(data) { }
    			});
			}
			
			function populateCurrentSprintStatus(){
				$.ajax({
	        		url: '/scrumr/restapi/stories/search/<%= projectId%>',
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
	        			if(stories != null && stories.length){
	        				var story_unassigned = '<div class="header " ><span></span>Sprint Stories</div><ul id="notstarted" class="story">';
	        				var story_dev = '<div class="header " ><span></span>Development</div><ul id="dev" class="story">';
	        				var story_review = '<div class="header " ><span></span>Review &amp; QA</div><ul id="review" class="story">';
	        				var story_finished = '<div class="header " ><span></span>Finished</div><ul id="finished" class="story">';
	        				for(var i=0;i<stories.length;i++){
	        					var story = stories[i];
	        					var userObj = userObject[story.createdby];
	        					if(story.status == "Not Started"){
	        						story_unassigned += '<li id="st'+story.id+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a></li>';
	        					}else if(story.status == "Development"){
	        						story_dev += '<li id="st'+story.id+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a></li>';
	        					}else if(story.status == "Review"){
	        						story_review += '<li id="st'+story.id+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="i'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a></li>';
	        					}else if(story.status == "Finished"){
	        						story_finished += '<li id="st'+story.id+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a></li>';
	        					}
	        				}
	       					story_unassigned += '</ul>';
	       					story_dev += '</ul>';
	       					story_review += '</ul>';
	       					story_finished += '</ul>';
	       					$(".green").html(story_unassigned);
	        				$(".yellow").html(story_dev);
	        				$(".blue").html(story_review);
	        				$(".pink").html(story_finished);
	        			}
	        			$('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
	        		},
	        		error: function(data) { },
	        		complete: function(data) { }
	        	});
			}
			function updateStoryStatus(id,status){
				var stat = false;
				var post_data = 'storyid=' + id + '&status='+status;
					$.ajax({
						url: '/scrumr/restapi/stories/updatestatus',
						type: 'POST',
						data: post_data,
						async:false,
						success: function( rec ) {
							var records = eval("("+rec+")");
							if(records != null && records.result == true){
								stat = true;
							}
	    				},
						error: function(data) { return false; },
						complete: function(data) { }
					}); 
					
					return stat;
        	}
			
			function deleteStory(id){
				var stat = false;
				var post_data = 'storyid=' + id;
					$.ajax({
						url: '/scrumr/restapi/stories/deletestory',
						type: 'POST',
						data: post_data,
						async:false,
						success: function( rec ) {
							var records = eval("("+rec+")");
							if(records != null && records.result == true){
								stat = true;
							}
	    				},
						error: function(data) { return false; },
						complete: function(data) { }
					}); 
					
					return stat;
        	}
			
			function addUser(id){
				var stat = false;
				 var post_data = 'userid='+id+'&projectId=<%= projectId%>';
				$.ajax({
					url: '/scrumr/restapi/project/adduser',
					type: 'POST',
					data: post_data,
					async:false,
					success: function( rec ) {
						var records = eval("("+rec+")");
						if(records != null && records.result == true){
							stat = true;
						}
					},
					error: function(data) { },
					complete: function(data) { }
				});
				
				return stat;
			}
			
			function removeUser(id){
				var stat = false;
				 var post_data = 'userid='+id+'&projectId=<%= projectId%>';
				$.ajax({
					url: '/scrumr/restapi/project/removeuser',
					type: 'POST',
					data: post_data,
					async:false,
					success: function( rec ) {
						var records = eval("("+rec+")");
						if(records != null && records.result == true){
							stat = true;
						}
					},
					error: function(data) { },
					complete: function(data) { }
				});
				
				return stat;
			}
			
			function addUserToStory(id,storyid){
				var stat = false;
				 var post_data = 'userid='+id+'&storyId='+storyid;
				$.ajax({
					url: '/scrumr/restapi/stories/adduser',
					type: 'POST',
					data: post_data,
					async:false,
					success: function( rec ) {
						var records = eval("("+rec+")");
						if(records != null && records.result == true){
							stat = true;
						}
					},
					error: function(data) { },
					complete: function(data) { }
				});
				
				return stat;
			}
			
			function removeUserFromStory(id,storyid){
				var stat = false;
				 var post_data = 'userid='+id+'&storyId='+storyid;
				$.ajax({
					url: '/scrumr/restapi/stories/removeuser',
					type: 'POST',
					data: post_data,
					async:false,
					success: function( rec ) {
						var records = eval("("+rec+")");
						if(records != null && records.result == true){
							stat = true;
						}
					},
					error: function(data) { },
					complete: function(data) { }
				});
				
				return stat;
			}
			
			function populateStoryAssignees(id){
				$.ajax({
	        		url: '/scrumr/restapi/stories/'+id,
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
	        			if(stories != null){
	        				$("#st-title").html(stories.title);
	        				var userObj = userObject[stories.createdby];
	        				$("#st-creator").html('<img title="'+userObj.name+'" src="'+userObj.profile_picture+'"/>');
	        				var assign = '';
	        				if(stories.assignees && stories.assignees.length > 0){
	        					for(var i=0;i < stories.assignees.length; i++){
	        						userObj = userObject[stories.assignees[i].id];
	        						assign += '<div class="user"><img class="remove-user" alt="'+userObj.id+'" title="'+userObj.name+'" src="'+userObj.profile_picture+'"/><span class="remvUser" >Remove</span></div>';
	        					}
	        				}else{
	        					assign = "<label style=\"margin:5px;\">No user assgined</labal>";
	        				}
	        				
	        				$("#st-assignees").html(assign);
	        				
	        				var people = '';
	        				if(users && users.length > 0){
	        					var user;
	        					for(var i=0;i < users.length; i++){
	        						user = userObject[users[i].id];
	        						people += '<div class="user"><img class="add-user" alt="'+user.id+'" title="'+user.name+'" src="'+user.profile_picture+'"/></div>';
	        					}
	        				}else{
	        					people = "<label style=\"margin:5px;\">No people in the project</labal>";
	        				}
	        				$("#st-users").html(people);
	        				
	        			}
	        			
	        		},
	        		error: function(data) { },
	        		complete: function(data) { }
	        	});
			}
			
			$("#addPeople").live('click', function(){
				$.ajax({
					url: '/scrumr/restapi/users/search',
					type: 'GET',
					async:false,
					success: function( records ) {
					var users_html = "<ul>";
					for(var i=0;i<records.length;i++){
						users_html += '<li><img src="'+records[i].profile_picture+'"/></div><div class="details"><label class="name">'+records[i].name+'</label><a class="email">'+records[i].email+'</a><label class="desig">'+records[i].designation+'</label></div><div class="businessUnit"><label class="unit">'+records[i].business_unit+'</label><label class="location">'+records[i].location+'</label></div><div style="float:left;" class="adduser float-rgt enable" id="'+records[i].id+'"></div></li>';
					}
					users_html += "</ul>";
					$("#userList-cont").html(users_html);
					
					},
					error: function(data) { },
					complete: function(data) { }
				});
				
				$(".adduser").live('click',function(){
					var id = ($(this).attr("id"));
					var success = addUser(id);
					if(success == true){
						populateProjectDetails();
						$(this).parent().hide();
					}
					
				});
				
			});
			
			$("#removePeople").live('click', function(){
					var records = users;
					var users_html = "<ul>";
					for(var i=0;i<records.length;i++){
						users_html += '<li><img src="'+records[i].profile_picture+'"/></div><div class="details"><label class="name">'+records[i].name+'</label><a class="email">'+records[i].email+'</a><label class="desig">'+records[i].designation+'</label></div><div class="businessUnit"><label class="unit">'+records[i].business_unit+'</label><label class="location">'+records[i].location+'</label></div><div style="float:left;" class="removeUser float-rgt disable" id="'+records[i].id+'"></div></li>';
					}
					users_html += "</ul>";
					$("#userList-cont").html(users_html);
				
					$(".removeUser").live('click',function(){
						var id = ($(this).attr("id"));
						if(id != creator){
						var success = removeUser(id);
							if(success == true){
								populateProjectDetails();
								$(this).parent().hide();
							}
						}
					
				});
				
			});
			
        	
            $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
            $(window).resize(function() {
                $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
            });

            $('.right').css({'width': (($(window).width()) - 300) + 'px'});
            $(window).resize(function() {
                $('.right').css({'width': (($(window).width()) - 300) + 'px'});
            });
            
            populateProjectDetails();
            populateUnassignedStories('');
	       	populateSprintStories(current_sprint);
	       	populateSprints();
        	$(".currentSprint").live('click', function(){
        		var storyList = $(this).parent().parent().parent().attr("id").split("st")[1];			
       			addtoCurrentSprint(storyList,current_sprint);
       			populateUnassignedStories('');
       		    populateSprintStories(current_sprint);
        	});
        	
        	$(".sprintHead").live('click', function(){
        		var sp = $(".sprints li").index($(this)) + 1;
        		populateSprintStories(sp);
        		sprintinview = sp;
        		current_sprint = sp;
        	});
        	
        	$('#story_form').submit(function(){
        		var title = $('input[name=stTitle]');
        		var description = "No Description";
        		var priority = $('select[name=stPriority]');
        		var user = "<%= username %>";
        		var projectId= <%= projectId%>;
        		var post_data = 'stTitle=' +title.val() + '&projectId='+projectId+'&stDescription=' + description + '&stPriority=' + priority.val() + '&user=' +user;
        		$.ajax({
        			url: '/scrumr/restapi/stories/create',
        			type: 'POST',
        			data: post_data,
        			async:false,
        			success: function( result ) {
        				console.log(result);
        				populateUnassignedStories('');
        		   	 	populateSprintStories(current_sprint);
        		   	 	title.val('');
        			},
        			error: function(data) { },
        			complete: function(data) { }
        		});
        		
        		return false;
        	});
        	
        	$(".sptRmv").live('click', function(){
        		var id = $(this).parent().attr("id");
        		id = id.replace("st","");
        		addtoCurrentSprint(id,0);
        		 populateUnassignedStories('');
     	       	populateSprintStories(sprintinview);
        	});
        	
        	$(".strRmv").live('click', function(){
        		var id = $(this).parent().attr("id");
        		id = id.replace("st","");
        		deleteStory(id);
        		populateUnassignedStories('');
     	       	populateSprintStories(sprintinview);
        	});
        	
        	$("#searchStory").keyup(function(event) {
        		  if ( event.which == 13 ) {
        			     event.preventDefault();
        			   }
        			   populateUnassignedStories($("#searchStory").val());
      			});
        	
        	$("#addPeople").fancybox({
        		'overlayColor' : '#000',
                'overlayOpacity' : '0.6',
                'autoScale' : false,
                'onComplete' : (function(){
                    scrollpane =$("#userList-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
                       }),
                'onStart' : (function(){
                        }),
                'onClosed' : (function() {

                     scrollpane.destroy();
                       })

        	});
        	
			$("#removePeople").fancybox({
        		'overlayColor' : '#000',
                'overlayOpacity' : '0.6',
                'autoScale' : false,
                'onComplete' : (function(){
                    scrollpane =$("#userList-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
                       }),
                'onStart' : (function(){
                        }),
                'onClosed' : (function() {

                     scrollpane.destroy();
                       })

        	});
			
			$(".viewStory").fancybox({
        		'overlayColor' : '#000',
                'overlayOpacity' : '0.6',
                'autoScale' : false,
                'onComplete' : (function(){
                    scrollpane =$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
                       }),
                'onStart' : (function(){
                        }),
                'onClosed' : (function() {
                	$("#stPeople").hide();
                     scrollpane.destroy();
                       })

        	});

			$(".stAddmore").live('click', function(){
				 if($(".stAddmore").html() == "Add More"){
					$(".stAddmore").html("Hide");
					$("#stPeople").show();
				}else{
					$("#stPeople").hide();
					$(".stAddmore").html("Add More");
				} 
				$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
			});
			
			$(".viewStory").live('click', function(){
				var id = $(this).parent().parent().parent().attr("id");
        		id = id.replace("st","");
        		populateStoryAssignees(id);
        		
				$(".add-user").live('click', function(){
					var eid = $(this).attr("alt");
					addUserToStory(eid, id);
					populateStoryAssignees(id);	
				});
				
				$(".remvUser").live('click', function(){
					var eid = $(this).parent().find('img').attr("alt");
					removeUserFromStory(eid, id);
					populateStoryAssignees(id);
				});
			});
			
			$('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
            $(window).resize(function() {
                $('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
            });
            
            if(userLogged != "default"){
            	$(".right-div").html('<img width="32px" height="32px" style="margin:4px;" class="float-lft"  src="'+userObject[userLogged].profile_picture+'"/><label class="float-lft loginLabel">Hi! '+userLogged+',</label><a href="/scrumr/projects?m=logout" class="logout">Logout</a><div class="index-img"><a class="index-img1"/></a></div><div class="index-img"><a class="index-img2"></a></div>');
            }else{
            	$(".right-div").html('<a href="#sign-in" class="signin">Sign In</a><div class="index-img"><a class="index-img1"/></a></div><div class="index-img"><a class="index-img2"></a></div>');
            }
			
        });
    </script>
</head>
<body>
<header>
    <a href="/scrumr/" class="logo float-lft"></a>
    <div class="tabs dashboard-tab"><a class="dashboard" href="">Dashboard</a></div>
    <div class="tabs project-tab"><a class="projects" href="/scrumr/home.jsp">Projects</a></div>
    <div class="right-div" style="float:right;"></div>
</header>
<div class="content">
    <section class="left bg-pat">
        <div class="cont float-lft">
            <label id="projectName" class="title"></label>
            <ul id="people" class="img-team">
            </ul>
            <a id="addPeople" href="#people-cont">Add</a> /
            <a id="removePeople" href="#people-cont">Remove</a> &nbsp;&nbsp;People
            <div style="display:none;overflow:hidden !important;">
            	<div id="people-cont">
            		<div class="title" class="float-lft">
                       <label>Users</label>
	                </div>
	                <div id="userInput" class="">
		                <input type="text" id="searchUser" placeholder="Search user by name..."/>
	                </div>
	                <div id="userList-cont">
	                </div>
            	</div>
            </div>
        </div>
        <div class="cont float-lft" id="test">
            <form id="story_form" method="POST">
				<div class="sTitle">
					<input type="text" id="storyTitle" name="stTitle" placeholder="Enter Story Title" min="1" max="5" required/>
				</div>
				<select name="stPriority" id="storyPriority">
				<option selected="selected" value="1">Priority 1</option>
				<option value="2">Priority 2</option>
				<option value="3">Priority 3</option>
				</select>
				<input type="submit" class="createStory submit" value="Add Story"/>
			</form>
        </div>
        <div class="scroll-cont float-lft">
            <div class="stories-cont">
                <div id="storyLabel" class="float-lft">
                       <label>User stories</label>
                </div>
                <div id="storyInput" class="float-lft">
	                <input type="text" id="searchStory" placeholder="Search story by keyword..."/>
                </div>
                <div class="cont" >
                	<div id="storyList">
                	<ul class="story">
                	</ul>
                	</div>
                </div>
            </div>
        </div>
    </section>
    <section class="right">
        <div class="content float-lft">
            
           
            <div class="sprint-cont float-lft" style="border:0;">
            <div class="float-lft" style="width:100%;">
                <ul class="sprints float-lft">
                </ul>
                <a href="" class="customize float-rgt">Customize</a>
                </div>
               <!--  <table id="sprint-view" class="sprint-detail">
                    <tbody>
                    
                    </tbody>
                </table> -->
                <table id="project-view" class="sprint-detail">
                    <tbody>
                    	 <tr>
                        <td class="">
                            <div class="header "><span></span>Sprint 1</div>
                            <ul class="story">
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                            </ul>
                        </td>
                        <td class="">
                            <div class="header "><span></span>Sprint 2</div>
                            <ul class="story">
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                            </ul>
                        </td>
                        <td class="">
                            <div class="header "><span></span>Sprint 3</div>
                            <ul class="story">
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                            </ul>
                        </td>
                        <td class="">
                            <div class="header "><span></span>Sprint 4</div>
                            <ul class="story">
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                            </ul>
                        </td>
                        <td class="">
                            <div class="header "><span></span>Sprint 5</div>
                            <ul class="story">
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                            </ul>
                        </td>
                        <td class="">
                            <div class="header "><span></span>Sprint 6</div>
                            <ul class="story">
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                            </ul>
                        </td>
                        <td class="">
                            <div class="header "><span></span>Sprint 7</div>
                            <ul class="story">
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                                <li>
                                    <p class="p3">Hong Kong Phooey, number one super guy. Hong Kong Phooey...</p>

                                    <div class="meta ">
                                        <div class="img-cont float-lft"><img src="images/img.png" width="26" height="26"
                                                                             class=""/><label class="">Created by
                                            Samir</label></div>

                                    </div>
                                    <a href="#" class="remove"></a>
                                </li>
                            </ul>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            
            <div style="display:none;overflow:hidden !important;">
            	<div id="story-cont">
            		<div class="storyTitle" class="float-lft">
                       <label>Story:</label><span id="st-priority" class="p1"></span>
                       <p id="st-title">The story content is here</p>
	                </div>
	               <div class="storyCreated" class="float-lft">
                       <label>Created by:</label>
                        <div id="st-creator" class="user"><img src="themes/1.jpg"/></div>
	                </div>
	                 <div class="storyAssignees" class="float-lft">
	                    <label>Assigned to: <span class="stAddmore" style="float:right;font-size:10px;color:#00475C;cursor:pointer;">Add More</span></label>
                       <div id="st-assignees">
                       		<div class="user"><img class="remove-user"  title="aomkaram" src="themes/1.jpg"/><span class="remvUser" >Remove</span></div>
                       		<div class="user"><img class="remove-user"  title="aomkaram" src="themes/2.jpg"/><span class="remvUser" >Remove</span></div>
                       		<div class="user"><img class="remove-user"  title="aomkaram" src="themes/3.jpg"/><span class="remvUser" >Remove</span></div>
                       </div>
                       
                       <div style="display:none;clear:both;" id="stPeople">
                       <label>People available:</label>
                       <div id="st-users">
	                       <img class="add-user" title="aomkaram" src="themes/1.jpg"/>
	                       <img class="story-user" title="aomkaram" src="themes/2.jpg"/>
	                       <img class="story-user" title="aomkaram" src="themes/3.jpg"/>
	                       <img class="story-user" title="aomkaram" src="themes/1.jpg"/>
	                       <img class="story-user" title="aomkaram" src="themes/2.jpg"/>
	                       <img class="story-user" title="aomkaram" src="themes/3.jpg"/>
	                       <img class="story-user" title="aomkaram" src="themes/1.jpg"/>
	                       <img class="story-user" title="aomkaram" src="themes/2.jpg"/>
	                       <img class="story-user" title="aomkaram" src="themes/3.jpg"/>
	                   </div>
	                </div>
	                </div>
	                <div class="storyComments" class="float-lft">
	                 <label>Comments:</label>
	                 <div class="comment-cont">
		                 <ul>
		                	 <li>
		                	 	<a href="javascript:void(0);" class="cmtRmv remove"></a>
		                	 	<img src="themes/1.jpg"/>
		                	 	<div>
			                	 	<span class="name">Arun Krishna Omkaram: </span>
			                	 	<span>This is my comment to test the total size of the text spans in how many lines</span>
			                	 	<div class="actions">January 26, 2012 9:09 am</div>
			                	 	<span></span>
		                	 	</div>
		                	 </li>
		                 </ul>
		                 <div class="comment-box">
		                 	<img src="themes/1.jpg"/>
		                 	<textarea class="comment-text" placeholder="Write a comment..." name="commment"></textarea>
		                 </div>
	                 </div>
	                </div>
            	</div>
            </div>

        </div>
    </section>
</div>
</body>
</html>