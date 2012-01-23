<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
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
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/pagination.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/highcharts.js"></script>
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/pagination.css" />
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery.fancybox-1.3.4.css" />
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery-ui-1.8.16.custom.css" />
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jscrollpane.css" />
	<link rel="shortcut icon" type="image/x-icon" href="http://www.qontext.com/wp-content/themes/qontext-v1.5/qontext.ico">
   <link href="<%= request.getContextPath() %>/themes/style.css" rel="stylesheet"/>
  <%
	String view = request.getParameter("view");
   String projectId = request.getParameter("projectId");
  %>
  <script type='text/javascript'>
        $(function() {
        	
        	var userLogged = '<s:property value="loggedInUser.username"/>';
        	
        	var current_sprint = 0;
        	var users_arr = [];
        	var sprintinview = 0;
        	var totalsprints = 0;
        	var creator;
        	var users = null;
        	var userObject = new Object();
        	var duration = '';
        	var project_view = 1;
        	<% if(view != null && view.equalsIgnoreCase("sprint")){ %>
	    		project_view = 0;
				$('.sprints').show();
				$('.sprintview').css('color',"#00475C");
				$('.sprintview').parent().css('background-color',"#F6EEE1");
		       	$(".projectview").css('color',"gray");
		       	$(".projectview").parent().css('background-color',"#FFFFFF");
    		<% } %>
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
    						/* if(project.start_date != null){
    							var startdate = new Date(Date.parse(project.start_date));
    							startdate = startdate.format("mm/dd/yyyy");
    							duration += '<span></span>'+startdate;
    						}else{
    							duration += 'No Start Date';
    						}
    						if(project.end_date != null){
    							var enddate = new Date(Date.parse(project.end_date));
    							enddate = enddate.format("mm/dd/yyyy");
    							duration += ' - <span></span>'+ enddate;
    						}else{
    							duration += ' - No End Date';
    						} */
            				$('#projectName').html(title);
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
            				
            				var user_html = '';
            				if(users.length == 0){
            					user_html += 'No users in the project';
            				}else{
	            				for(var i=0;i<users.length;i++){
	            					user_html += "<li id='"+users[i].id+"'><img src='"+users[i].profile_picture+"' title='"+users[i].name+"''/></li>";
	            				}
            				}
           					$('#user-list').html(user_html);
            			}
            		},
            		error: function(data) { },
            		complete: function(data) { }
            	});
        	}
			function populateUnassignedStories(name){
	        	 $.ajax({
	        		url: '/scrumr/api/v1/stories/backlog/<%= projectId%>',
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
        				var story_unassigned = '';
	        			if(stories != null && stories.length){
	        				for(var i=0;i<stories.length;i++){
	        					var story = stories[i];
	        					var userObj = userObject[story.createdby];
	        					story_unassigned += '<li id="st'+story.id+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label><div class="usrComment">0</div></div></div><a href="javascript:void(0);" class="strRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        				}
	        			}else{
	        				story_unassigned += '<b>No pending stories for the project</b>';
	        				$('#storyList ul').css({'height': (($(window).height()) - 180) + 'px'});
	        			}
        				$("#storyList ul").html(story_unassigned);
        				$( "#storyList").jScrollPane({});
        				$( "#storyList ul" ).sortable({
        	        		connectWith: ".story",
        	        		appendTo: 'body',
        	        		forcePlaceholderSize: true,
        	        		placeholder: 'ui-state-highlight',
        	        		update:function(){
        	        		}
        	    		}).disableSelection();
        				
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
		        			if(sprints.length > 0){
		        				var duration = '<span></span>';
								if(sprints[0].project.start_date != null){
									var startdate = new Date(Date.parse(sprints[0].project.start_date));
									startdate = startdate.format("dd mmm yyyy");
									duration += startdate;
								}else{
									duration += 'No Start Date';
								}
								if(sprints[0].project.end_date != null){
									var enddate = new Date(Date.parse(sprints[0].project.end_date));
									enddate = enddate.format("dd mmm yyyy");
									duration += '&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;<span></span>'+ enddate;
								}else{
									duration += ' - No End Date';
								}
								$('.duration-hd label').html(duration+'&nbsp;&nbsp;&nbsp;'+sprints[0].project.status);
								
				        		var sprint_html = '<ul id="holderforpage" class="col">';
			        			$("ul.col li").css("width",100/sprints.length+'%');
			        			for(var k=0; k<sprints.length;k++ ){
			        				sprint_html += '<li class="stages"><div class="header "><span></span>Sprint '+(k+1)+'</div><ul id="sp'+sprints[k].id+'"class="story">';
			        				var post_data2 = 'sprintId='+sprints[k].id+'&projectId=<%= projectId%>';
						        	$.ajax({
						        		url: '/scrumr/restapi/stories/search',
						        		type: 'POST',
						        		data: post_data2,
						        		async:false,
						        		success: function( stories ) {
					        				if(stories != null && stories.length > 0){
					        					for(var i=0;i<stories.length;i++){
						        					var story = stories[i];
						        					var userObj = userObject[story.createdby];
						        					 if(story.assignees.length > 0){
							        					var imageHTML = " ";
							        					for(var j=0;j<story.assignees.length;j++){
							        						if(j==2){
																//imageHTML+="<a class='viewStory' href='#story-cont'>.. more</a>";
																imageHTML+="<label>.....</label>";
																break;
															}
															imageHTML+="<img height='26' width='26' class='' src='"+story.assignees[j].profile_picture+"' title='"+story.assignees[j].name+"'>";
														}
							        					sprint_html +=  '<li id="st'+story.id+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont">'+imageHTML+'<div class="usrComment"></div></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
						        					}else{
						        						sprint_html += '<li id="st'+story.id+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label><div class="usrComment"></div></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
						        					} 
						        				}
					        					$(".viewStory").fancybox({
					        		        		'overlayColor' : '#000',
					        		                'overlayOpacity' : '0.6',
					        		                'autoScale' : false,
					        		                'onComplete' : (function(){
					        		                    scrollpane =$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
					        		                       }),
					        		                'onStart' : (function(el){
					        		                        }),
					        		                'onClosed' : (function() {
					        		                	$("#stPeople").hide();
					        		                     scrollpane.destroy();
					        		                       })

					        		        	});
					        				}
						        		},
						        		error: function(data) { },
						        		complete: function(data) { }
						        	});
			        				sprint_html += '</ul>';
			        				sprint_html +='</li>';
			        				
			        			}
			        			
			        			sprint_html +='</ul>';
			        			$("#project-view").html(sprint_html);
			        			$('#pageCtrls').html("");
			        			setTimeout(function(){
			        				$('#holderforpage').sweetPages({perPage:4});
				    				var controls = $('.swControls').detach();
			    					controls.appendTo('#pageCtrls'); 
			        			},1);
			        			//$( ".stages " ).jScrollPane({});
			        			$( ".stages ul" ).sortable({
		        	        		connectWith: ".story",
		        	        		//appendTo: 'body',
		        	        		forcePlaceholderSize: true,
		        	        		placeholder: 'ui-state-highlight',
		        	        		update: function( event, ui ) {
		        	        			var id = ui.item.attr("id").split("st")[1];
		        	        			console.log(ui.item.closest('section'));
		        	        			if(ui.item.closest('section').hasClass('left')){ //dropped back to unassigned list
		        	        				ui.item.find('a.remove').removeClass('sptRmv').addClass('strRmv');
		        	        				addtoCurrentSprint(id,0);
		        	        				populateUnassignedStories('');
		        	        			}else {
		        	        				ui.item.find('a.remove').removeClass('strRmv').addClass('sptRmv');
		        	        				var sprint = $(this).attr("id").split("sp")[1];
			        		   				var success = addtoCurrentSprint(id,sprint);
			        		   				//ui.item.find("a.currentSprint").removeClass().addClass('viewStory');
			        		   				if(success == false){
			        		   					$(this).sortable('cancel');
			        		   				}
		        	        			}
		        	        			
		        		   			}
		        	    		}).disableSelection();
			        			
				        		$('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
				        		/* $("#storyList").jScrollPane({
		        				}); */
		        			}
		        			
		        			
		        		},
		        		error: function(data) { },
		        		complete: function(data) { 
		        			
		        		}
		        	});
			}
			
			function refreshStoryPortlet(storyId){
			//	addUserToStory(users_arr,storyId);
				$.ajax({
					url : '/scrumr/restapi/stories/' + storyId,
					type : 'GET',
					async : false,
					success : function(stories) {
						var imageHTML = " ";
						if(stories.assignees.length > 0){
							for(var i=0;i<stories.assignees.length;i++){
								if(i==2){
								//	imageHTML+="<a class='viewStory' href='#story-cont'>.. more</a>";
									imageHTML+="<label>.....</label>";
									break;
								}
								imageHTML+="<img height='26' width='26' class='' src='"+stories.assignees[i].profile_picture+"' title='"+stories.assignees[i].name+"'>";
							}
						}else{
							imageHTML+="<img height='26' width='26' class='' src='"+stories.createdBy+"' title='"+stories.createdBy+"'><label class=''>Created by "+stories.createdBy+"</label>";
						}
						$('li#st'+storyId).find('.img-cont').html(imageHTML);
						viewStoryFancyBox();
					}
				});
						
			}
			
			function viewStoryFancyBox(){
				/* $('.viewStory').live("click",function(){
					var storyEl=$(this).parent().parent().parent();
					var animateTop = parseInt($(storyEl).css('top'))+20;
					$(storyEl).css('position','absolute').css('top', animateTop).animate({
						position:"absolute",
						width:"400px",
						height:"300px"
					},1000);
				}); */
				var bgColor = "";
				$(".viewStory").fancybox({
	        		'overlayColor' : '#000',
	                'overlayOpacity' : '0.6',
	                'autoScale' : false,
	                'onComplete' : (function(){
	                    scrollpane =$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
	                    $('#story-cont').css("background-color",bgColor);
	                       }),
	                'onStart' : (function(el){
	                	var storyEl=$(el).closest('li');
	                	bgColor = ($(storyEl).css('backgroundColor'));
	                        }),
	                'onClosed' : (function() {
	                	$("#stPeople").hide();
	                     scrollpane.destroy();
	                       })

	        	}); 
			}
			
			
			function showAddUserPopup(elOffset){
				if($('.popup-cont').find('div#pointerEl').hasClass('pointer-rgt')){
					$('.popup-cont').find('div#pointerEl').removeClass('pointer-rgt').addClass('pointer');
				}
				var elTop = elOffset.top;
				var elLeft = elOffset.left;
				var new_left = elLeft+210;
				var popupWidth =parseInt($('.popup-cont').css('width'));
				var sectionWidth = parseInt($('section.right').css('width'))+parseInt($('section.right').css('padding-left'));
				if(new_left+popupWidth > sectionWidth){
					var current_left = elLeft-240;
					$('.popup-cont').find('div#pointerEl').removeClass('pointer').addClass('pointer-rgt');
					$('.popup-cont').css('top',elTop);
					$('.popup-cont').css('left',current_left);
				}else if(new_left > sectionWidth){
					var current_left = elLeft-500;
					$('.popup-cont').css('top',elTop);
					$('.popup-cont').css('left',current_left);
				}else{
					$('.popup-cont').css('top',elTop);
					$('.popup-cont').css('left',new_left);
				}
				
				$('.popup-cont').show();
			}
			
			function populateSprintStories(sprint){
	        		 if(totalsprints == 0){
		        		 var string = '<label>No Sprints available for this project.</label>';
		        		 $(".sprints").html(string);
		        	 }else{
		        		 var sprintList = "";
		        		 sprintList += '<li class="sprintHead">Sprint '+sprint+' </li>';
			        	 /* for(var i=1; i<=totalsprints;i++){
			        		 if(i == sprint){
				        		 sprintList += '<li class="sprintHead current">Sprint '+i+' &#187; </li>';
			        		 }else{
			        			 sprintList += '<li class="sprintHead">Sprint '+i+' &#187; </li>';
			        		 }
			        	 } */
			        	 $(".sprints").html(sprintList);
		        	 }
	        		 
	        		 var post_data2 = 'sprintId='+sprint+'&projectId=<%= projectId%>';
			        	$.ajax({
			        		url: '/scrumr/restapi/sprints/lookup',
			        		type: 'POST',
			        		data: post_data2,
			        		async:false,
			        		success: function( result ) {
			        			if(result != null){
				        			var duration = '<span></span>';
		    						if(result.startdate != null){
		    							var startdate = new Date(Date.parse(result.startdate));
		    							startdate = startdate.format("dd mmm yyyy");
		    							duration += startdate;
		    						}else{
		    							duration += 'No Start Date';
		    						}
		    						if(result.enddate != null){
		    							var enddate = new Date(Date.parse(result.enddate));
		    							enddate = enddate.format("dd mmm yyyy");
		    							duration += '&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;<span></span>'+ enddate;
		    						}else{
		    							duration += ' - No End Date';
		    						}
		    						$('.duration-hd label').html(duration+'&nbsp;&nbsp;&nbsp;'+result.status);
			        			}
			        		},
			        		error: function(data) { },
			        		complete: function(data) { }
			        	});
					
	        		 var post_data2 = 'sprintId='+sprint+'&projectId=<%= projectId%>';
			        	$.ajax({
			        		url: '/scrumr/restapi/stories/search',
			        		type: 'POST',
			        		data: post_data2,
			        		async:false,
			        		success: function( stories ) {
				        			$("#sprint-view tbody").html('<tr><td colspan="1"  class="green stages"></td><td colspan="1"  class="yellow stages" ></td><td colspan="1"  class="blue stages"></td><td colspan="1" class="pink stages"></td></tr>');
			        				var story_unassigned = '<div class="header" ><span></span>Sprint Stories</div><div class="sprintCont"><ul id="notstarted" class="story">';
			        				var story_dev = '<div class="header" ><span></span>Development</div><div class="sprintCont"><ul id="dev" class="story">';
			        				var story_review = '<div class="header" ><span></span>Review &amp; QA</div><div class="sprintCont"><ul id="review" class="story">';
			        				var story_finished = '<div class="header" ><span></span>Finished</div><div class="sprintCont"><ul id="finished" class="story">';
				        			if(stories != null && stories.length > 0){
			        					var str = '';
			        					var story='';
			        					var userObj='';
			        					for(var i=0;i<stories.length;i++){
				        					story = stories[i];
				        					userObj = userObject[story.createdby];
				        					 if(story.assignees.length > 0){
						        					var imageHTML = " ";
						        					for(var j=0;j<story.assignees.length;j++){
						        						if(j==2){
															//imageHTML+="<a class='viewStory' href='#story-cont'>.. more</a>";
															imageHTML+="<a class='viewStory' href='#story-cont'>.....</a>";
															break;
														}
														imageHTML+="<img height='26' width='26' class='' src='"+story.assignees[j].profile_picture+"' title='"+story.assignees[j].name+"'>";
													}
						        					str =  '<li id="st'+story.id+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont">'+imageHTML+'<div class="usrComment">0</div></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
					        					}else{
				        							str = '<li id="st'+story.id+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label><div class="usrComment">0</div></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
					        					}
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
				        				
				        				
				        				story_unassigned += '</div></ul>';
				       					story_dev += '</div></ul>';
				       					story_review += '</div></ul>';
				       					story_finished += '</div></ul>';
				        				$(".green").html(story_unassigned);
				        				$(".yellow").html(story_dev);
				        				$(".blue").html(story_review);
				        				$(".pink").html(story_finished);
				        				
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
				        			$( ".stages ul" ).sortable({
			        	        		connectWith: ".story",
			        	        		//appendTo: 'body',
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
			        		       				$(this).find("label:first").remove();
			        		       			}else if(stat == "finished"){
			        		       				status = "Finished";
			        		       			}
			        		   				var success = updateStoryStatus(id.split("st")[1],status,sprint);
			        		   				var elOffset = $(ui.item[0]).offset();
			        		   				if($(ui.item[0]).parents('td').hasClass('green')){
			        		   					$(ui.item[0]).find('.img-cont').html("<img src='"+userObj.profile_picture+"' width='26' height='26' class=''/><label class=''>Created by "+story.createdby+"</label>");
			        		   					//call addUserToStory with empty users arr or call clear api that will remove all users from that story
			        		   				}
			        		   				if(!($(ui.item[0]).parents('td').hasClass('green')) && !($(ui.item[0]).closest('section').hasClass('left'))){
			        		   					showAddUserPopup(elOffset);
			        		   					//refreshStoryPortlet(id.split("st")[1]);
			        		   					$('#popup_done').live("click",function(){
			        		   						refreshStoryPortlet(id.split("st")[1]);
			        		   						$(this).closest('.popup-cont').hide();
			        		   					});
			        		   				}
											if(($(ui.item[0]).closest('section').hasClass('left'))) {
			        		   					var new_id = id.replace("st","");
			        		   					$(ui.item[0]).find('a.remove').removeClass('sptRmv').addClass('strRmv');
			        		   					addtoCurrentSprint(new_id, 0);
			        		   					populateUnassignedStories('');
			        		   				}else{
			        		   					$(ui.item[0]).find('a.remove').removeClass('strRmv').addClass('sptRmv');
			        		   				}
			        		   				if(success == false){
			        		   					$(this).sortable('cancel');
			        		   				}
			        		   			}
			        	    		}).disableSelection();
				        			$('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
				        			//$('.stages div.sprintCont').css({'height': (($(window).height()) - 450) + 'px'});
				        			//$( ".stages div.sprintCont" ).jScrollPane({});
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
	        					var duration = '<span></span>';
	    						if(stories[0].sprint_id.startdate != null){
	    							var startdate = new Date(Date.parse(stories[0].sprint_id.startdate));
	    							startdate = startdate.format("mm/dd/yyyy");
	    							duration += startdate;
	    						}else{
	    							duration += 'No Start Date';
	    						}
	    						if(stories[0].sprint_id.enddate != null){
	    							var enddate = new Date(Date.parse(stories[0].sprint_id.enddate));
	    							enddate = enddate.format("mm/dd/yyyy");
	    							duration += ' - <span></span>'+ enddate;
	    						}else{
	    							duration += ' - No End Date';
	    						}
	    						$('.duration-hd label').html(duration);
	        					var userObj = userObject[story.createdby];
	        					if(story.status == "Not Started"){
	        						story_unassigned += '<li id="st'+story.id+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "Development"){
	        						story_dev += '<li id="st'+story.id+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "Review"){
	        						story_review += '<li id="st'+story.id+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="i'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "Finished"){
	        						story_finished += '<li id="st'+story.id+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.profile_picture+'" width="26" height="26" class=""/><label class="">Created by '+story.createdby+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
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
			function updateStoryStatus(id,status,sprint){
				var stat = false;
				var post_data = 'stories=' + id + '&status='+status+ '&sprint='+sprint;
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
	        				// Comment Section 
	        				
	        				$('<input>').attr({
				    			type: 'hidden',
				    			id: 'current_story_id',
				    			value: id
							}).appendTo('form');	
							
							// Ends Here
	        			}
	        			
	        		},
	        		error: function(data) { },
	        		complete: function(data) { }
	        	});
			}
			
			$('ul#user-list li').live("click",function(){
				if($(this).hasClass("selected")){
					$(this).removeClass("selected");
					$(this).css('border','none');
					users_arr.splice($(this).attr('id').indexOf(),1);
				}else{
					$(this).addClass("selected");
					$(this).css('border','1px solid blue');
					users_arr.push($(this).attr('id'));
				}
			});
			
			
			$("#addPeople").live('click', function(){
				$.ajax({
					url: '/scrumr/restapi/users/search',
					type: 'GET',
					async:false,
					success: function( records ) {
						var total_users = records;
						for(var j=0;j<total_users.length;j++){
							for(var k=0;k<users.length;k++){
								if(total_users[j].id === users[k].id){
									total_users.splice(j,1);
								}else if(total_users[j].id === creator){
									total_users.splice(j,1);
								}
							}
						}
						var users_html = "<ul>";
						for(var i=0;i<total_users.length;i++){
							users_html += '<li><img src="'+total_users[i].profile_picture+'"/></div><div class="details"><label class="name">'+total_users[i].name+'</label><a class="email">'+total_users[i].email+'</a><label class="desig">'+total_users[i].designation+'</label></div><div class="businessUnit"><label class="unit">'+total_users[i].business_unit+'</label><label class="location">'+total_users[i].location+'</label></div><div style="float:left;" class="adduser float-rgt enable" id="'+total_users[i].id+'"></div></li>';
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
						if(records[i].id != creator){
							users_html += '<li><img src="'+records[i].profile_picture+'"/></div><div class="details"><label class="name">'+records[i].name+'</label><a class="email">'+records[i].email+'</a><label class="desig">'+records[i].designation+'</label></div><div class="businessUnit"><label class="unit">'+records[i].business_unit+'</label><label class="location">'+records[i].location+'</label></div><div style="float:left;" class="removeUser float-rgt disable" id="'+records[i].id+'"></div></li>';
						}
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
			
			// Comments Section
	       	populateCommentingUserDetails();
	       	populateStoryComments();
	       	populateStoryTodos();
	       	// Ends Here
			
            if(project_view ==1){
     	       	populateSprints();
				$("#sprint-view").hide();
				$("#project-view").show();
				$(".duration-hd").find('label').show();
				$(".duration-hd").find('#pageCtrls').show();
				$(".duration-hd").find('ul').hide();
			}else{
     	       	populateSprintStories(current_sprint);
				$("#sprint-view").show();
				$("#project-view").hide();
				$(".duration-hd").find('label').hide();
				$(".duration-hd").find('#pageCtrls').hide();
				$(".duration-hd").find('ul').show();
			}
        	$(".currentSprint").live('click', function(){
        		var storyList = $(this).parent().parent().parent().attr("id").split("st")[1];
       			addtoCurrentSprint(storyList,current_sprint);
       			$(this).parent().parent().parent().hide("fade","slow");
       			setTimeout(function(){
       				populateUnassignedStories('');
       				if(project_view ==1){
             	       	populateSprints();
        			}else{
             	       	populateSprintStories(current_sprint);
        			}
       			},1000);
        	});
        	
        	/* $(".sprintHead").live('click', function(){
        		var sp = $(".sprints li").index($(this)) + 1;
        		populateSprintStories(sp);
        		sprintinview = sp;
        		current_sprint = sp;
        		viewStoryFancyBox();
        	}); */
        	
        	$('#story_form').submit(function(){
        		var title = $('input[name=stTitle]');
        		var description = "No Description";
        		var priority = $('select[name=stPriority]');
        		var user = userLogged;
        		var projectId= <%= projectId%>;
        		var post_data = 'stTitle=' +title.val() + '&projectId='+projectId+'&stDescription=' + description + '&stPriority=' + priority.val() + '&user=' +user;
        		$.ajax({
        			url: '/scrumr/restapi/stories/create',
        			type: 'POST',
        			data: post_data,
        			async:false,
        			success: function( result ) {
        				populateUnassignedStories('');
        				if(project_view ==1){
        					populateSprints();
        				}else{
	        		   	 	populateSprintStories(current_sprint);
        				}
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
        		$(this).parent().hide("fade","slow");
        		setTimeout(function(){ //execute with a delay inorder to show the fade effect
        			populateUnassignedStories('');
        			//commented this out as i feel it is unnecessary to populate content. On refresh, it will populate anyways.
            		/* if(project_view ==1){
           				populateSprints();
    				}else{
   	     	       		populateSprintStories(sprintinview);
    				}  */
        		},1000);
        		
        	});
        	
        	$(".strRmv").live('click', function(){
        		 var id = $(this).parent().attr("id");
        		id = id.replace("st","");
        		$(this).parent().hide("fade","slow");
        		setTimeout(function(){
        			deleteStory(id);
        			populateUnassignedStories('');
        			//commented this out as i feel it is unnecessary to populate content. On refresh, it will populate anyways.
        			/* if(project_view ==1){
           				populateSprints();
    				}else{
   						populateSprintStories(sprintinview);
    				}  */
        		},1000);
        		
        	});
        	
        	$("#searchStory").keyup(function(event) {
    			if (event.which == 13) {
    				event.preventDefault();
    			}
    			var query=$('#searchStory').val();
    			var selector = $('#storyList .story');
    			query = query.replace(/ /gi, '|'); //add OR for regex query  

    			$(selector).children('li').each(
    					function() {
    						($(this).find('p').text()
    								.search(new RegExp(query, "i")) < 0) ? $(this)
    								.hide() : $(this).show();
    					});
    		});
    		
    		$("#searchUser").keyup(function(event) {
    			if (event.which == 13) {
    				event.preventDefault();
    			}
    			var query=$('#searchUser').val();
    			var selector = $('#userList-cont').find('ul');
    			query = query.replace(/ /gi, '|'); //add OR for regex query  
    			$(selector).children('li').each(
    					function() {
    						($(this).find('label.name').text()
    								.search(new RegExp(query, "i")) < 0) ? $(this)
    								.hide() : $(this).show();
    					});

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
			viewStoryFancyBox();
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
        		populateStoryComments();
        		populateStoryTodos();
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
			
			$(".projectview").live('click',function(){
				project_view = 1;
				$('.sprints').hide();
		       	$(this).css('color',"#00475C");
		       	$(this).parent().css('background-color',"#F6EEE1");
				$(".sprintview").css('color',"gray");
				$(".sprintview").parent().css('background-color',"#FFFFFF");
		       	populateSprints();
				$("#sprint-view").hide();
				$("#project-view").show();
				$(".duration-hd").find('label').show();
				$(".duration-hd").find('#pageCtrls').show();
				$(".duration-hd").find('ul').hide();
				viewStoryFancyBox();
			});
			
			$(".sprintview").live('click',function(){
				project_view = 0;
				$('.sprints').show();
				$(this).css('color',"#00475C");
				$(this).parent().css('background-color',"#F6EEE1");
		       	$(".projectview").css('color',"gray");
		       	$(".projectview").parent().css('background-color',"#FFFFFF");
				populateSprintStories(current_sprint);
				$("#sprint-view").show();
				$("#project-view").hide();
				$(".duration-hd").find('label').hide();
				$(".duration-hd").find('#pageCtrls').hide();
				$(".duration-hd").find('ul').show();
				viewStoryFancyBox();
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
//code for highcharts
    	var areaChart; // globally available
    	var pieChart;
    	var storyData=[];
    	var colors = Highcharts.getOptions().colors;
    	//areaChart = new Highcharts.Chart({
    		var areaChartOptions = {
	         chart: {
	            renderTo: 'lineChart',
	            defaultSeriesType: 'area',
	         },
	         title: {
	            text: '',
	            x: -20 //center
	         },
	         plotOptions:{
	        	pointInterval:20 
	         },
	         xAxis: {
	            categories: [],
	            startOnTick:true,
	            gridLineWidth:1,
	            gridLineColor:'#E6E6E6',
	            lineColor:'#E6E6E6',
	            gridLineDashStyle:'solid',
	            tickmarkPlacement:'on',
	         },
	         yAxis: {
	            title: {
	               text: 'USER STORIES',
	               style: {
	            	fontWeight:'bold',
	            	color:'#7F7F7F'
	               }
	            },
	            labels:{
					enabled:false
				},	
	            gridLineColor:'#E6E6E6',
	            lineColor:'#E6E6E6',
	            gridLineDashStyle:'longdash',
	            
	         },
	         tooltip: {
					formatter: function() {
			                return '<b>'+ this.y +' stories</b>';
					}
				},
			legend:{
				enabled:false
			},	
			credits:{
				enabled:false
			},	
			
	         series: [{
	            name: 'stories',
	            data: [15, 510, 140, 120]
	         }]
	      };
		for(var i=0;i<totalsprints;i++){
    		areaChartOptions.xAxis.categories.push("SPRINT"+(i+1)+"");
    	}
		
		chart = new Highcharts.Chart(areaChartOptions);
    	pieChart = new Highcharts.Chart({
			chart: {
				renderTo: 'pieChart',
				type: 'pie'
			},
			title: {
				text: ''
			},
			yAxis: {
				title: {
					text: 'Sprint in stages'
				}
			},
			plotOptions: {
				pie: {
					allowPointSelect: false,
		            cursor: 'pointer',
		            dataLabels: {
		               enabled: false
		            },
		            showInLegend: true,
				}
			},
			tooltip: {
				formatter: function() {
					return '<b>'+ this.point.name +'</b>: '+ this.y +' %';
				}
			},
			credits:{
				enabled:false
			},
			legend: {
		        layout: 'vertical',
		        backgroundColor: 'whiteSmoke',
		        borderWidth:'0px',
		        align: 'right',
		        verticalAlign: 'top',
		        itemStyle: {
		            paddingBottom: '10px',
		            color:'#7f7f7f'
		        }
		    },
			series: [{
				name: 'stories',
				data:  [{ 
					name:'Development in Progress',
					y: 20,
					color: colors[0]
				}, {
					name:'Finished',
					y: 10,
					color: colors[1]
				}, {
					name:'Bugs Reported',
					y: 8,
					color: colors[2]
				}, {
					name:'Unassigned',
					y: 5,
					color: colors[3]
				}, {
					name:'QA in Progress',
					y: 9,
					color: colors[4]
				}],
				innerSize: '75%',
				size:'115%'
				
			}]
		});
	// Comment Section Code
        	
        	var month_list=new Array("January","Febraury","March","April","May","June","July","August","September","October","November","December");
        	
        	function populateCommentingUserDetails(){
            	
        		var commentBoxHtml = '';
				var user = userLogged;								
				var current_user = userObject[user];											
				commentBoxHtml = '<img src="'+current_user.profile_picture+'">';							
				$(".comment-box-user").html(commentBoxHtml);
				$(".todo-box-user").html(commentBoxHtml);
				
            }
        	
			function populateStoryComments(){
				var loggedDate='';
				var id = $("#current_story_id").val();				
				var commentsHtml = '<div>';								
				$.ajax({
					url : '/scrumr/restapi/comments/search/'+id,
					type : 'GET',
					async : false,					
					success :function(comments){
						if (comments != null){							
	        				if(comments.length > 0){
		        				commentsHtml += '<ul style="list-style:none;">';
		        				for (var i=0;i<comments.length;i++){
			        				comment = comments[i];
			        				var newDate = new Date(comment.logDate);
			        				var dtString = newDate.getDate()+" "+month_list[newDate.getMonth()]+","+newDate.getFullYear();				        				    				
			        				//alert($.datepicker.parseDate('MM d,yy',new Date(parseInt(comment.logDate))));			        																						        		
			        				commentsHtml += '<li class="comment-list" style="width:100%;"><a id='+comment.commentID+' class="cmtRmvComment remove" href="javascript:void(0);"></a><img src="'+comment.user.profile_picture+'"><div><span class="name">'+comment.user.name+'</span><span><pre style="float:right;">'+comment.content+'</pre></span><div>'+dtString+'</div></div></li>';				        						        	
			        				}
		        				commentsHtml += '</ul></div>';
		        			}			        				        			        			
						}else{
								commentsHtml += '</div>';
							}
						
						$(".comment-display").html(commentsHtml);
						$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;																
					},
					error : function(data){},
					complete : function(data){}					
					});	
			}					
        	
        	$(".cmtRmvComment").live('click',function(){        		
            	var commentID = $(this).attr("id");
            	deleteStoryComment(commentID);
            	populateStoryComments();
            	});

        	function deleteStoryComment(commentID){

        		var post_data = 'commentID='+commentID;
        		$.ajax({
          			url: '/scrumr/restapi/comments/delete',
          			type: 'POST',
          			data: post_data,
          			async:false,
          			success: function( result ) {
              										                  			
          			},
          			error: function(data) { },
         			complete: function(data) { }            		
                  	 });          	
            }

        	function parsedString(str){
            	var commentText = '';
            	var i=0;
            	var nextIndex=0,index=0;

                while (i<str.length){
                    nextIndex = str.indexOf(' ',i+1);
                    index = nextIndex;
                    while (nextIndex < i+35 && nextIndex != -1){
                        index = nextIndex;                        
                        nextIndex = str.indexOf(' ',nextIndex+1);
                        }
                    if (nextIndex == -1){
                        if ((str.length-index) < 35){
                        		commentText += str.substring(i,str.length);
                            	break;
                            }else{
                            	commentText += str.substring(i,index);
                                commentText += '\n';
                                i = index+1;
                            	while (i<str.length+35){
                                    commentText += str.substring(i,i+35);
                                    commentText += '\n';
                                    i+=35;
                                    }
                                break;  
                            }                            
                        }                                           
                    commentText += str.substring(i,index);
                    commentText += '\n';
                    i = index+1;
                }
                //alert($.trim(commentText));
                commentText =$.trim(commentText);         	            
            	return commentText+'\n ';
        	}
        	
            $('.comment-text').keypress(function(e){
                 if (e.which == 13){                                                                
                	 var user = userLogged;
                	 var story_id = $("#current_story_id").val();	
                	 var commentText = $('.comment-text').val();                	 
					 commentText = parsedString(commentText);		                    	 			                   	               	        	          	                 	      
                     var post_data = '&content='+ commentText+'&storyid='+story_id+'&logdate='+$.datepicker.formatDate('yy-mm-dd', new Date())+'&user='+user;                     
                     $.ajax({
              			url: '/scrumr/restapi/comments/create',
              			type: 'POST',
              			data: post_data,
              			async:false,
              			success: function( comment ) {
                  			e.preventDefault();
              				$('.comment-text').val('');							            				             			
              				var newDate = new Date(comment.logDate);
              				var dtString = newDate.getDate()+" "+month_list[newDate.getMonth()]+","+newDate.getFullYear();              				         				              			
              				$('.comment-display ul').append('<li class="comment-list" style="width:100%";><a id='+comment.commentID+' class="cmtRmvComment remove" href="javascript:void(0);"></a><img src="'+comment.user.profile_picture+'"><div><span class="name">'+comment.user.name+'</span><span><pre style="float:right;">'+comment.content+'</pre></span><div>'+dtString+'</div></div></li>');
//              				alert('came here');              				 
              				$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
              				$('.comment-text').focus();         				              				                 
              			},
              			error: function(data) { },
             			complete: function(data) { }            		
                      	});
                 	}                     
                });
         // Ends Here

         // Todo Code starts here
                  
         
         $('#todo-form').submit(function(){
             
        	 var user = userLogged;
        	 var story_id = $("#current_story_id").val();	
        	 var todoText = $(".todo-text").val();                       	  	 
        	         	 
			 var milestonePeriod = $("#todo-milestones").val();			 
			 todoText = parsedString(todoText);
        	 		                    	 			                   	               	        	          	                 	      
             var post_data = '&content='+ todoText+'&storyid='+story_id+'&milestonePeriod='+milestonePeriod+'&user='+user;                     
             $.ajax({
      			url: '/scrumr/restapi/todo/create',
      			type: 'POST',
      			data: post_data,
      			async:false,
      			success: function( todo ) {          			
      				$('.todo-text').val('');
      				$('#todo-milestones').val('Milestones');							            				             			      				              				         				              			
      				$('.todo-display ul').prepend('<li class="todo-list" style="width:100%";><a id='+todo.todoID+' class="cmtRmvTodo remove" href="javascript:void(0);"></a><img src="'+todo.user.profile_picture+'"><div><span class="name">'+todo.user.name+'</span><span><pre style="float:right;">'+todo.content+'</pre></span><div>Milestone Period :'+todo.milestonePeriod+'</div></div></li>');
      				$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
      				$('.todo-text').focus();         				              				                 
      			},
      			error: function(data) { },
     			complete: function(data) { }            		
              	});
        
             	return false;
             });

         function deleteStoryTodo(todoID){

     		var post_data = 'todoID='+todoID;
     		$.ajax({
       			url: '/scrumr/restapi/todo/delete',
       			type: 'POST',
       			data: post_data,
       			async:false,
       			success: function( result ) {
           										                  			
       			},
       			error: function(data) { },
      			complete: function(data) { }            		
               	 });          	
         }

         $(".cmtRmvTodo").live('click',function(){        		
         	var todoID = $(this).attr("id");
         	deleteStoryTodo(todoID);
         	populateStoryTodos();
         	});

         function populateStoryTodos(){				
				var id = $("#current_story_id").val();				
				var todosHtml = '<div>';								
				$.ajax({
					url : '/scrumr/restapi/todo/search/'+id,
					type : 'GET',
					async : false,					
					success :function(todos){
						if (todos != null){							
	        				if(todos.length > 0){
		        				todosHtml += '<ul style="list-style:none;">';
		        				for (var i=0;i<todos.length;i++){
		        					todo = todos[i];			        								        				    						        							        																						        	
			        				todosHtml += '<li class="todo-list" style="width:100%;"><a id='+todo.todoID+' class="cmtRmvTodo remove" href="javascript:void(0);"></a><img src="'+todo.user.profile_picture+'"><div><span class="name">'+todo.user.name+'</span><span><pre style="float:right;">'+todo.content+'</pre></span><div>Milestone Period: '+todo.milestonePeriod+'</div></div></li>';				        						        	
			        				}
		        				todosHtml += '</ul></div>';
		        			}			        				        			        			
						}else{
								todosHtml += '</div>';
							}
						
						$(".todo-display").html(todosHtml);
						$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;																
					},
					error : function(data){},
					complete : function(data){}					
					});	
			}		
			
        });
    </script>
</head>
<body>
<header>
    <a href="<%= request.getContextPath() %>/" class="logo float-lft"></a>
    <div class="tabs dashboard-tab"><a class="dashboard" href="">Dashboard</a></div>
    <div class="tabs project-tab"><a class="projects" href="<%= request.getContextPath() %>/project.action">Projects</a></div>
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
                       <div class="priority">
							<div class="p1"></div>
							<div class="p2" ></div>
							<div class="p3" ></div>
						</div>
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
            <div class="chart-container">
					<div class="float-lft" style="width: 50%;">
						<p>Burn out charts</p>
						<select class="float-lft" name="sprintDuration">
						<option name="xxx">xxx</option>
						<option name="yyy">yyy</option>
						</select>
						<div id="lineChart"></div>
					</div>
					<div class="float-lft" style="width: 50%;">
						<p>Sprint Status</p>
						<select class="float-lft" name="sprint">
						<option name="xxx">xxx</option>
						<option name="yyy">yyy</option>
						</select>
						<div id="pieChart"></div>
					</div>
				</div>
				
				<div class=toggleChartLine>
				<a class="toggleChart" href="javascript:void(0);"></a>
				</div>
				
				
           
            <div class="sprint-cont float-lft" style="border:0;">
	            <div class="view-cont float-lft">
	                <!-- <ul class="sprints float-lft">
	                </ul> -->
	                <div class="view-hd">
	                	<div class="prj-view-hd"><label class="projectview">Project View</label></div>
		                <div class="sp-view-hd"><label class="sprintview">Sprint View</label>
		                
		                </div>
		                 <a href="" class="customize float-rgt">Customize</a>
	                </div>
	                <div class="duration-hd">
	                	<label style="display:none;float:left;"><span></span>12/12/2011 - <span></span>12/12/2011</label>
	                	<ul class="sprints float-lft">
	                	</ul> 
	                	<div id="pageCtrls" style="display:none;float:right;width:auto;height:30px;"></div>
	                </div>
	            </div>
                <div id="project-view" style="overflow:hidden;" class="float-lft clear col-cont" >
                </div>
                
                <table id="sprint-view" class="sprint-detail">
                    <tbody>
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
	                <div class="todo" class="float-lft">
	                    <label>Todo List:</label>
                       <div id="todo-box" class="comment-cont">
                       		<div class="todo-box-user float-lft">
								<img src="themes/1.jpg"/>
							</div>
  							<form id="todo-form"> 
		                 	<textarea class="todo-text" placeholder="Write a Todo..." name="todo"></textarea>
		                 	<select id="todo-milestones">
		                 		<option value="" selected="selected">Milestones</option>
		                 		<option value="1 Day">1 Day</option>
		                 		<option value="2 Days">2 Days</option>
		                 		<option value="3 Days">3 Days</option>
		                 		<option value="4 Days">4 Days</option>
		                 		<option value="5 Days">5 Days</option>
		                 		<option value="6 Days">6 Days</option>
		                 		<option value="7 Days">7 Days</option>
		                 	</select>
		                 	<input class="submit" type="submit" value="Done" />
		                 	</form>
		                 	<div class="todo-display">
		                 		<ul>
		                	 		<li>
		                	 			<a href="javascript:void(0);" class="cmtRmvTodo remove"></a>
		                	 			<img src="themes/1.jpg"/>
		                	 			<div>
			                	 			<span class="name">Arun Krishna Omkaram: </span>
			                	 			<span>This is my comment to test the total size of the text spans in how many lines</span>
			                	 			<div class="actions">January 26, 2012 9:09 am</div>
			                	 			<span></span>
		                	 			</div>
		                	 		</li>
		                 		</ul>
							</div>
<!--  		                 	<textarea class="todo-text" placeholder="Write a Todo..." name="todo"></textarea>
		                 	<input type="hidden" id="todo-milestone-picker" /> -->
                       </div>
                                              
	                </div>
	                <div class="storyComments" class="float-lft">
	                 <label>Comments:</label>
	                 <div class="comment-cont">
						<div class="comment-display">
		                 <ul>
		                	 <li>
		                	 	<a href="javascript:void(0);" class="cmtRmvComment remove"></a>
		                	 	<img src="themes/1.jpg"/>
		                	 	<div>
			                	 	<span class="name">Arun Krishna Omkaram: </span>
			                	 	<span>This is my comment to test the total size of the text spans in how many lines</span>
			                	 	<div class="actions">January 26, 2012 9:09 am</div>
			                	 	<span></span>
		                	 	</div>
		                	 </li>
		                 </ul>
						</div>
		                 <div class="comment-box">
							<div class="comment-box-user float-lft">
								<img src="themes/1.jpg"/>
							</div>
		                 	<textarea class="comment-text" placeholder="Write a comment..." name="commment"></textarea>
		                 </div>
	                 </div>
	                </div>
            	</div>
            </div>
        </div>
        
         <div class="popup-cont" style="display:none;">
           <div class="c-box">
               <div class="c-box-head">Assign to </div>
               <div class="c-box-content">
                   <ul id="user-list">
                   
                       
                   </ul>

               </div>
                 <div class="actions-cont float-rgt">

                       <input id="popup_done" type="button" class="float-rgt submit" value="Done"/>
                       <a id="popup_cancel">later</a>
                   </div>
               <div id="pointerEl" class="pointer"></div> </div>
       </div>
       <script>
                     $('#popup_cancel').live("click",function(){
                    	 $('.popup-cont').hide();
                     }); 
                     
                     //code to toggle chart container
                     $('.toggleChart').live("click",function(){
                    	 $(this).parent().prev('.chart-container').slideToggle("slow",function(){
                    		 if(!($(this).is(':visible'))){
                    			 $(this).next('.toggleChartLine').find('a.toggleChart').css('background','url("images/br_down.png") no-repeat').css('margin-top','-5px');
                    			 $(this).next('.toggleChartLine').css('margin-top','5px').css('border-bottom','none').css('border-top','1px solid #7f7f7f');
                    		 }else {
                    			 $(this).next('.toggleChartLine').find('a.toggleChart').css('background','url("images/br_up.png") no-repeat').css('margin-top','0');
                    			 $(this).next('.toggleChartLine').css('margin-top','0').css('border-top','none').css('border-bottom','1px solid #7f7f7f');
                    		 }
                    	 });
                     });
                     
                     //code to show stories based on priority
                     var priorityDisabledObj=[];
                     $('.priority div').bind("click",function(){
                    	 if($(this).hasClass('disabled')){
                    		 $(this).removeClass('disabled');
                    		 var index = priorityDisabledObj.indexOf($(this).attr('class'));
                    		 priorityDisabledObj.splice(index, 1);
                    		 showStoryOnPriority(priorityDisabledObj);
                    		 
                    	 }else{
                    		 priorityDisabledObj[priorityDisabledObj.length] = $(this).attr('class');
                    		 $(this).addClass('disabled');
                    		 showStoryOnPriority(priorityDisabledObj);
                    	 }
                     });
                     
                     function showStoryOnPriority(priorityDisabledObj){
                    	 $('#storyList ul.story').find('li').each(function(){
                    		 $(this).show();
                    		 for(var i=0;i<priorityDisabledObj.length;i++){
                    			 if($(this).find('p').attr('class') == priorityDisabledObj[i]){
                    				 $(this).hide();
                    			 }
                    		 }
                        	 
                         }); 
                     }
                     
                  /*  complete: function(data) { 
                	    $('.c-box-content').jScrollPane({
                      		showArrows : true,
                 				scrollbarWidth : '20'	   
                         }).data().jsp; 
                   } */
                   
                   
                   
                   </script>
    </section>
</div>
</body>
</html>