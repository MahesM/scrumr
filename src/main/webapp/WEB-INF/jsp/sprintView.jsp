<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.io.*" %>
<%@ page import="org.codehaus.jettison.json.JSONObject" %>
<%@ page import="net.oauth.*" %>
<%@ page import="org.codehaus.jettison.json.*" %>
<%@ page import="org.apache.commons.httpclient.protocol.Protocol" %>
<%@ page import="org.apache.commons.httpclient.protocol.ProtocolSocketFactory" %>
<%@ page import="com.imaginea.scrumr.qontextclient.*" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>Scrumr</title>
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
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery.fancybox-1.3.4.css" />
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jquery-ui-1.8.16.custom.css" />
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/jscrollpane.css" />
	<link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/pagination.css" />
	<link rel="shortcut icon" type="image/x-icon" href="http://www.qontext.com/wp-content/themes/qontext-v1.5/qontext.ico">
    <link href="<%= request.getContextPath() %>/themes/style.css" rel="stylesheet"/>
 <%
	String view = request.getParameter("view");
   	String projectId = request.getParameter("projectId");
    String visit = request.getParameter("visit");
 %>
  <script type='text/javascript'>
        $(function() {
        	var userIndex = 1;
        	var userLogged = '<s:property value="loggedInUser.username"/>';
        	var fullName = '<s:property value="loggedInUser.fullname"/>';
        	var displayname = '<s:property value="loggedInUser.displayname"/>';
        	var avatar = '<s:property value="loggedInUser.avatarurl"/>';
        	var emailid = '<s:property value="loggedInUser.emailid"/>';
        	var data = '<s:property value="successResponse"/>';
        	var source = '<s:property value="source"/>';
            
            if(userLogged != null && userLogged != ''){
    	     	$(".right-div").html('<img width="32px" height="32px" style="margin:4px;" class="float-lft"  src="'+avatar+'"/><label class="float-lft loginLabel">Hi!, '+fullName+'</label><div class="index-img"><a class="index-img1"/></a></div><div class="index-img"><a class="index-img2"></a></div>');
    	     } 
            var qontextHostUrl = "https://pramati.staging.qontext.com";
        	var current_sprint = 0;
        	var users_arr = [];
        	var sprintinview = 0;
        	var totalsprints = 0;
        	var creator;
        	var users = null;
        	var userObject = new Object();
        	var creatorObj = new Object();
        	var duration = '';
        	var project_view = 1;
        	var firstVisit = false;
        	<% if(visit != null && visit.equals("1")){ %>
        		firstVisit = true;
        	<%}%>
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
            		url: '/scrumr/api/v1/projects/<%= projectId%>',
            		type: 'GET',
            		async:false,
            		success: function( project ) {
            			if(project != null && project.length > 0){
            				project = project[0];
            				current_sprint = project.current_sprint > 0?project.current_sprint:1;
            				sprintinview = current_sprint;
            				creator = project.createdby;
            				var title = project.title;
            				if(title.length > 30){
            					title = title.substring(0,30)+" ...";
            				}
    						/* if(project.start_date != null){
    							var startdate = new Date(project.start_date);
    							startdate = startdate.format("mm/dd/yyyy");
    							duration += '<span></span>'+startdate;
    						}else{
    							duration += 'No Start Date';
    						}
    						if(project.end_date != null){
    							var enddate = new Date(project.end_date);
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
            						$("#people").append('<li><img title="'+users[i].fullname+'" src="'+users[i].avatarurl+'" /></li>');
            						userObject[users[i].username] = users[i];
            					}
            				}else{
            					$("#people").html('No assignees for the project');
            				}
            				
            				var user_html = '';
            				if(users.length == 0){
            					user_html += 'No users in the project';
            				}else{
	            				for(var i=0;i<users.length;i++){
	            					user_html += "<li id='"+users[i].username+"'><img src='"+users[i].avatarurl+"' title='"+users[i].fullname+"''/></li>";
	            				}
            				}
           					$('#proj-user-list').html(user_html);
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
	        					creatorObj = userObject[story.creator];
							var userObj = userObject[story.creator];
	        					story_unassigned += '<li id="st'+story.pkey+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"><img src="'+creatorObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="strRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';

	        				}
	        			}else{
	        				story_unassigned += '<b>No pending stories for the project</b>';
	        				$('#storyList ul').css({'height': (($(window).height()) - 250) + 'px'});
	        			}
        				$("#storyList ul").html(story_unassigned);
        				$( "#storyList").jScrollPane({});
        				$( "#storyList ul" ).sortable({
        	        		connectWith: ".story",
        	        		//appendTo: 'body',
        	        		forcePlaceholderSize: true,
        	        		placeholder: 'ui-state-highlight',
        	        		update: function( event, ui ) {
        	        			if(ui.item.closest('section').hasClass('right')){ //dropped to the stages section
        	        				ui.item.find('a.remove').removeClass('strRmv').addClass('sptRmv');
        	        			};
        	        			$('#storyList ul').css({'height': (($(window).height()) - 500) + 'px'});
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
		        		url: '/scrumr/api/v1/sprints/project/<%= projectId%>',
		        		type: 'GET',
		        		async:false,
		        		success: function( sprints ) {
		        			if(sprints.length > 0){
		        				var duration = '<span></span>';
								if(sprints[0].project.start_date != null){
									var startdate = new Date(sprints[0].project.start_date);
									startdate = startdate.format("dd mmm yyyy");
									duration += startdate;
								}else{
									duration += 'No Start Date';
								}
								if(sprints[0].project.end_date != null){
									var enddate = new Date(sprints[0].project.end_date);
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
			        				var post_data2 = 'sprintId='+sprints[k].pkey+'&projectId=<%= projectId%>';
						        	$.ajax({
						        		url: '/scrumr/api/v1/stories/sprint/'+sprints[k].pkey,
						        		type: 'GET',
						        		async:false,
						        		success: function( stories ) {
					        				if(stories != null && stories.length > 0){
					        					for(var i=0;i<stories.length;i++){
						        					var story = stories[i];
						        					var userObj = userObject[story.creator];
						        					 if(story.assignees.length > 0){
							        					var imageHTML = " ";
							        					for(var j=0;j<story.assignees.length;j++){
							        						if(j==2){
																imageHTML+="<a class='moreStory' href='#story-cont'>more</a>";
																//imageHTML+="<label>.....</label>";
																break;
															}
															imageHTML+="<img height='26' width='26' class='' src='"+story.assignees[j].avatarurl+"' title='"+story.assignees[j].fullname+"'>";
														}
							        					sprint_html +=  '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont">'+imageHTML+'</div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
						        					}else{
						        						sprint_html += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"><img src="'+creatorObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
						        					} 
						        				}
					        					$(".viewStory, .moreStory").fancybox({
					        		        		'overlayColor' : '#000',
					        		                'overlayOpacity' : '0.6',
					        		                'autoScale' : false,
					        		                'onComplete' : (function(){
					        		                    //scrollpane =$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
					        		                       }),
					        		                'onStart' : (function(el){
					        		                        }),
					        		                'onClosed' : (function() {
					        		                	$("#stPeople").hide();
					        		                    // scrollpane.destroy();
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
			
			function refreshStoryPortlet(storyId,stageId,creatorObj){
				var post_data="storyId="+storyId+"&stage="+stageId;
				$.ajax({
					//url : '/scrumr/restapi/stories/getusers?storyid='+storyId+'&stage='+stageId+'', 
					url : '/scrumr/api/v1/stories/getusers',
					type : 'POST',
					data : post_data,
					async : false,
					success : function(users) {
						var imageHTML = " ";
						if(users!=null && users.length > 0){
							for(var i=0;i<users.length;i++){
								if(i==2){
									imageHTML+="<a class='moreStory' href='#story-cont'>more</a>";
									//imageHTML+="<label>.....</label>";
									break;
								}
								imageHTML+="<img height='26' width='26' class='' id = '"+users[i].user.username+"' src='"+users[i].user.avatarurl+"' title='"+users[i].user.fullname+"'>";
							}
						}else{
							imageHTML+="<img height='26' width='26' class='' src='"+creatorObj.avatarurl+"' title='"+creatorObj.fullname+"'><label class=''>Created by "+creatorObj.fullname+"</label>";
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
				//var bgColor = "";
				$(".viewStory, .moreStory").fancybox({
				'overlayColor' : '#000',
	                'overlayOpacity' : '0.6',
	                'autoScale' : false,
	                'onComplete' : (function(){
	                   // viewStoryScrollpane =$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
	                  //  $('#story-cont').css("background-color",bgColor);
	                       }),
	                'onStart' : (function(el){
	                	/* var storyEl=$(el).closest('li');
	                	bgColor = ($(storyEl).css('backgroundColor')); */
	                        }),
	                'onClosed' : (function() {
	                	$("#stPeople").hide();
	                	//viewStoryScrollpane.destroy();
	                       })

	        	}); 
			}
			
			
			function showAddUserPopup(elOffset){
				users_arr = [];
				$('ul#proj-user-list li').each(function(){
					$(this).css('border','none');
					$(this).removeClass('selected');
				});
				if($('.popup-story-cont').find('div#pointerEl').hasClass('pointer-rgt')){
					$('.popup-story-cont').find('div#pointerEl').removeClass('pointer-rgt').addClass('pointer');
				}
				var elTop = elOffset.top;
				var elLeft = elOffset.left;
				var new_left = elLeft+210;
				var popupWidth =parseInt($('.popup-story-cont').css('width'));
				var sectionWidth = parseInt($('section.right').css('width'))+parseInt($('section.right').css('padding-left'));
				if(new_left+popupWidth > sectionWidth){
					var current_left = elLeft-240;
					$('.popup-story-cont').find('div#pointerEl').removeClass('pointer').addClass('pointer-rgt');
					$('.popup-story-cont').css('top',elTop);
					$('.popup-story-cont').css('left',current_left);
				}else if(new_left > sectionWidth){
					var current_left = elLeft-500;
					$('.popup-story-cont').css('top',elTop);
					$('.popup-story-cont').css('left',current_left);
				}else{
					$('.popup-story-cont').css('top',elTop);
					$('.popup-story-cont').css('left',new_left);
				}
				
				$('.popup-story-cont').show();
			}
			
			function populateSprintStories(sprint){
					var userObj='';
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
			        		url: '/scrumr/api/v1/sprints/'+sprint+'/project/<%= projectId%>',
			        		type: 'GET',
			        		async:false,
			        		success: function( result ) {
			        			result=result[0];
			        			if(result != null){
				        			var duration = '<span></span>';
		    						if(result.startdate != null){
		    							var startdate = new Date(result.startdate);
		    							startdate = startdate.format("dd mmm yyyy");
		    							duration += startdate;
		    						}else{
		    							duration += 'No Start Date';
		    						}
		    						if(result.enddate != null){
		    							var enddate = new Date(result.enddate);
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
			        		url: '/scrumr/api/v1/stories/'+sprint+'/project/<%= projectId%>',
			        		type: 'GET',
			        		async:false,
			        		success: function( stories ) {
				        			$("#sprint-view tbody").html('<tr><td colspan="1"  class="green stages"></td><td colspan="1"  class="yellow stages" ></td><td colspan="1" class="blue stages"></td><td colspan="1" class="pink stages"></td></tr>');
			        				var story_unassigned = '<div class="header" ><span></span>Sprint Stories</div><div class="sprintCont"><ul id="notstarted" class="story">';
			        				var story_dev = '<div class="header" ><span></span>Development</div><div class="sprintCont"><ul id="dev" class="story">';
			        				var story_review = '<div class="header" ><span></span>Review &amp; QA</div><div class="sprintCont"><ul id="review" class="story">';
			        				var story_finished = '<div class="header" ><span></span>Finished</div><div class="sprintCont"><ul id="finished" class="story">';
				        			if(stories != null && stories.length > 0){
			        					var str = '';
			        					var story='';
			        					for(var i=0;i<stories.length;i++){
				        					story = stories[i];
				        					userObj = userObject[story.creator];
				        					creatorObj = userObject[story.creator];
				        					var post_data="storyId="+story.pkey+"&stage="+story.status;
				        					$.ajax({
				        						url : '/scrumr/api/v1/stories/getusers',
				        						type : 'POST',
				        						async : false,
				        						data: post_data,
				        						success : function(users) {	
						        					 if(users !=null && users.length > 0){
								        					var imageHTML = " ";
								        					for(var j=0;j<users.length;j++){
								        						if(j==2){
																	imageHTML+="<a class='moreStory' href='#story-cont'>more</a>";
																	//imageHTML+="<a class='viewStory' href='#story-cont'>.....</a>";
																	break;
																}
																imageHTML+="<img height='26' width='26' class='' src='"+users[j].user.avatarurl+"' title='"+users[j].user.fullName+"'>";
															}
								        					str =  '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont">'+imageHTML+'</div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
							        					}else{
						        							str = '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"><img src="'+creatorObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
							        					}
				        						}
				        					});

				        					if(story.status == "notstarted"){
				        						story_unassigned += str;
				        					}else if(story.status == "dev"){
				        						story_dev += str;
				        					}else if(story.status == "review"){
				        						story_review += str;
				        					}else if(story.status == "finished"){
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
				        				
				        				$("#notstarted").html('<label id="noStories" style="margin:5px;float:left">No Stories assigned to this sprint.</label>');
				        			}
				        			$( ".stages ul" ).sortable({
			        	        		connectWith: ".story",
			        	        		//appendTo: 'body',
			        	        		forcePlaceholderSize: true,
			        	        		placeholder: 'ui-state-highlight',
			        	        		update: function( event, ui ) {
			        	   				 	var id = ui.item.attr("id");
			        	   				 	var status = "notstarted";
			        		   				var stat = $(this).attr("id");
			        		   				if(stat == "dev"){
			        		       				status = "dev";
			        		       			}else if(stat == "review"){
			        		       				status = "review";
			        		       			}else if(stat == "notstarted"){
			        		       				status = "notstarted";
			        		       				$(this).find("label#noStories").remove();
			        		       			}else if(stat == "finished"){
			        		       				status = "finished";
			        		       			}
			        		   				var success = updateStoryStatus(id.split("st")[1],status,sprint); 
			        		   				var elOffset = $(ui.item[0]).offset();
			        		   				 if($(ui.item[0]).closest('ul').attr('id') == 'notstarted'){
			        		   					$(ui.item[0]).find('.img-cont').html("<img src='"+creatorObj.avatarurl+"' width='26' height='26' class=''/><label class=''>Created by "+creatorObj.fullname+"</label>");
			        		   					removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id'));
			        		   				}
			        		   				if($(ui.item[0]).closest('ul').attr('id') == 'finished'){			        		   								        		   				
			        		   					removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id'));
			        		   					//alert(creatorObj.userName);
			        		   					refreshStoryPortlet(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id'),creatorObj);
			        		   				}
			        		   				if(!($(ui.item[0]).closest('ul').attr('id') == 'notstarted') && !($(ui.item[0]).closest('ul').attr('id') == 'finished')&& !($(ui.item[0]).closest('section').hasClass('left'))){
			        		   					
			        		   					
			        		   					showAddUserPopup(elOffset);
			        		   					removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id'));
			        		   					var existing_user_arr = [];
			        		   					$("#"+id).find('img').each(function(){
			        		   						existing_user_arr.push($(this).attr('id'));
			        		   					});
			      		   						addUserToStory(existing_user_arr,id.split("st")[1],$(ui.item[0]).closest('ul').attr('id')); 
			        		   					//refreshStoryPortlet(id.split("st")[1]);
			        		   					$('#popup_story_done').live("click",function(){
			        		   						if(users_arr.length > 0){
				        		   						removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id'));
					      		   						addUserToStory(users_arr,id.split("st")[1],$(ui.item[0]).closest('ul').attr('id')); 
				        		   						refreshStoryPortlet(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id'),creatorObj);
				        		   						$(this).closest('.popup-story-cont').hide();
				        		   						$('#popup_story_done').die();
			        		   						}
			        		   					});
			        		   				}
											if(($(ui.item[0]).closest('section').hasClass('left'))) {
			        		   					var new_id = id.replace("st","");
			        		   					$(ui.item[0]).find('a.remove').removeClass('sptRmv').addClass('strRmv');
			        		   					removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id'));
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
    			var post_data = 'projectId='+projectId+'&stories='+storyList+'&status=notstarted&sprint='+sprint;
    			
    			$.ajax({
    				url: '/scrumr/api/v1/stories/addtosprint',
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
	        		url: '/scrumr/api/v1/stories/'+sprint+'/project/<%= projectId%>',
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
	        					var userObj = userObject[story.creator];
	        					if(story.status == "notstarted"){
	        						story_unassigned += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "dev"){
	        						story_dev += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "review"){
	        						story_review += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "finished"){
	        						story_finished += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+userObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
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
				var post_data = 'stories=' + id + '&status='+status+ '&sprint='+sprint +'&projectId=<%= projectId %>';
					$.ajax({
						url: '/scrumr/api/v1/stories/addtosprint',
						type: 'POST',
						data: post_data,
						async:false,
						success: function( rec ) {
							var records = eval("("+rec+")");
							if(records != null && records.result == "success"){
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
			//	var post_data = 'storyid=' + id;
					$.ajax({
						url: '/scrumr/api/v1/stories/delete/'+id,
						type: 'GET',
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
			
			function addUser(userDetails){
				var stat = false;
				var post_data = 'userid='+userDetails.username+'&projectId=<%= projectId%>';
				$.ajax({
					url: '/scrumr/api/v1/users/create',
					type: 'POST',
					data: userDetails,
					async:false,
					success: function( rec ) {
						var records = eval("("+rec+")");
						if(records != null && records.result == "success"){
							stat = true;
						}
						$.ajax({
							url: '/scrumr/api/v1/projects/adduser',
							type: 'POST',
							data: post_data,
							async:false,
							success: function( rec ) {
								var records = eval("("+rec+")");
								if(records != null && records.result == "success"){
									stat = true;
								}
							},
							error: function(data) { },
							complete: function(data) { }
						});
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
					url: '/scrumr/api/v1/projects/removeuser',
					type: 'POST',
					data: post_data,
					async:false,
					success: function( rec ) {
						var records = eval("("+rec+")");
						if(records != null && records.result == "success"){
							stat = true;
						}
					},
					error: function(data) { },
					complete: function(data) { }
				});
				
				return stat;
			}
			
			function addUserToStory(user_arr,storyid,stageId){
				var stat = false;
				 var post_data = 'userids='+user_arr+'&storyId='+storyid+'&stage='+stageId;
				$.ajax({
					url: '/scrumr/api/v1/stories/adduserswithstage',
					type: 'POST',
					data: post_data,
					async:false,
					success: function( rec ) {
						var records = eval("("+rec+")");
						if(records != null && records.result == "success"){
							stat = true;
						}
					},
					error: function(data) { },
					complete: function(data) { }
				});
				
				return stat;
			}
			
			function removeUserFromStoryInStage(storyid,stageid){
				var post_data = 'storyId='+storyid+"&stage="+stageid;				
				$.ajax({
					url: '/scrumr/api/v1/stories/clearstoryassignees',
					type: 'POST',
					data: post_data,
					async:false,
					success: function( rec ) {						
					
					},
					error: function(data) { },
					complete: function(data) { }
					});
				}
			
			function removeUserFromStory(id,storyid,stageId){
				var stat = false;
				 var post_data = 'userid='+id+'&storyId='+storyid+'&stageId='+stageId;
				$.ajax({
					url: '/scrumr/api/v1/stories/removeuser',
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
	        		url: '/scrumr/api/v1/stories/'+id,
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
	        			if(stories != null && stories.length > 0){
							stories = stories[0];
	        				$("#st-title").html(stories.title);
	        				var userObj = userObject[stories.creator];
	        				$("#st-creator").html('<img title="'+userObj.fullname+'" src="'+userObj.avatarurl+'"/>');
	        				var assign = '';
	        				var post_data="storyId="+id+"&stage="+stories.status;
	        				$.ajax({
	        					url : '/scrumr/api/v1/stories/getusers',
	        					type : 'POST',
	        					data : post_data,
	        					async : false,
	        					success : function(users) {
	    	        				if(users.length > 0){
	    	        					for(var i=0;i < users.length; i++){
	    	        						userObj = userObject[users[i].user.username];
	    	        						assign += '<div class="user"><img class="remove-user" alt="'+userObj.username+'" title="'+userObj.fullname+'" src="'+userObj.avatarurl+'"/><span class="remvUser" >Remove</span></div>';
	    	        					}
	    	        				}else{
	    	        					assign = "<label style=\"margin:5px;\">No user assgined</labal>";
	    	        				}
	        					}
	        				});

	        				
	        				$("#st-assignees").html(assign);
	        				if(stories.status !== "notstarted" && stories.status !=="Not Started"){
	        					$('.stAddmore').show();
	        				}else{
	        					$('.stAddmore').hide();
	        				}
	        				var people = '';
	        				if(users && users.length > 0){
	        					var user;
	        					for(var i=0;i < users.length; i++){
	        						user = userObject[users[i].username];
	        						people += '<div class="user"><img class="add-user" alt="'+user.username+'" title="'+user.fullname+'" src="'+user.avatarurl+'"/></div>';
	        					}
	        				}else{
	        					people = "<label style=\"margin:5px;\">No people in the project</labal>";
	        				}
	        				$("#st-users").html(people);
	        				// Comment Section 
	        				$('<input>').attr({type: 'hidden', id: 'current_story_id',value: id }).appendTo('form');	
							
							// Ends Here
	        			}
	        			
	        		},
	        		error: function(data) { },
	        		complete: function(data) { }
	        	});
			}
			
			$('ul#proj-user-list li').live("click",function(){
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
			
			function populateUserDetails(startIndex, isAppend){
				
				var no_of_users = $('.popup-proj-cont .c-box-content li').length;
				//alert((10+users.length)-no_of_users);
				if (no_of_users < 10 || isAppend){
					if (isAppend){
						startIndex = startIndex+users.length;		
					}
				
				var post_data = "startIndex="+startIndex+"&count="+((10+users.length));
				
				var post_data = 'source=DATABASE&index=0&count=10';
				$.ajax({
        			url: '/scrumr/api/v1/users/fetchqontextusers',
        			type: 'POST',
        			data: post_data,
        			async:false,
        			success: function( records ) {
        				if(records != null){
							records = $.parseJSON(records);
							if(records.success.body.totalObjects > 0){
								var total_users = records.success.body.objects;
								for(var j=0;j<records.length;j++){
									for(var k=0;k<users.length;k++){
										if(records[j].ownerAccountId == users[k].username ){
											total_users.splice(j,1);
										}
									}
								}
							//	var users_html = "<ul>";
								var users_html="";
								var apiVersion = records.success.headers["api-version"];
								for(var i=0;i<total_users.length;i++){
									if(!total_users[i].avatar){
			        					total_users[i].avatar = "/portal/st/"+apiVersion+"/profile/defaultUser.gif"; //place default url here.
			        				}
									users_html += '<li><img src="'+qontextHostUrl+total_users[i].avatar+'"/></div><div class="details"><label class="name">'+total_users[i].name+'</label><a class="email">'+total_users[i].profilePrimaryEmail+'</a></div><div style="float:left;" class="adduser float-rgt enable" id="'+total_users[i].ownerAccountId+'"></div></li>';
								}
								
								
								if (isAppend){
									$('.popup-proj-cont .c-box-content .jspContainer .jspPane').append(users_html);							
									userIndex += 1;
								}else{
									$('.popup-proj-cont .c-box-content ul').html(users_html);
									userIndex += 10;
								}	
								//alert('length : '+$('.popup-proj-cont .c-box-content li').size());
        				}else{
							$('.popup-proj-cont .c-box-content ul').html("No users found for the query..");	
						}
        			 }
					},
					failure :function(){
						},					
					complete : function(){}											
					});
				}
				//	var users_html = "<ul>";
					
					if(firstVisit){
						$('.popup-proj-cont').show();
						$('.popup-proj-cont .c-box-content ul').jScrollPane();
					}
					$("#addPeople").live('click', function(){
						alert('add people');						
						$('.story-popup').hide();
						$('.popup-proj-cont').show();
						$('.popup-proj-cont .c-box-content ul').jScrollPane();
												
					});
					$(".adduser").live('click',function(){
						var id = ($(this).attr("id"));
						var userDetails = {};
						userDetails.username = id;
						userDetails.fullname = $(this).prev('.details').find('label.name').html();
						userDetails.avatarurl = $(this).closest('li').find('img').attr('src');
						userDetails.displayname = $(this).prev('.details').find('label.name').html();
						userDetails.emailid = $(this).prev('.details').find('a.email').html();
						var success = addUser(userDetails);
						if(success ==true){
							populateProjectDetails();
							$(this).parent().hide();
							populateUserDetails(userIndex,true);
						}
						
					});
					
				
			}
        
			
			$(document).live("click",function(event){
				// if that user assign story popup is visible, hide it on click of anywhere outside the popup
				var el = event.target;
				if($(el).closest('.popup-story-cont').length === 0){
					$('.popup-story-cont').hide();
				}
			});
			
			
			$('#popup_proj_done').live("click",function(){
           	 $('.popup-proj-cont').hide();
           	 if(firstVisit){
           		 $('.story-popup').show();
           	 }
            }); 
			
			$("#removePeople").live('click', function(){
					var records = users;
					//var users_html = "<ul>";
					var users_html="";
					for(var i=0;i<records.length;i++){
						if(records[i].username != creator){
							users_html += '<li><img src="'+records[i].avatarurl+'"/></div><div class="details"><label class="name">'+records[i].fullname+'</label><a class="email">'+records[i].emailid+'</a></div><div style="float:left;" class="removeUser float-rgt disable" id="'+records[i].username+'"></div></li>';
						}
					}
					//users_html += "</ul>";
					$('.popup-proj-cont .c-box-content ul').html(users_html);
					$('.popup-proj-cont').show();
					$('.popup-proj-cont .c-box-content ul').jScrollPane();
					//$("#userList-cont").html(users_html);
				
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
            //populateUserDetails();
            populateUserDetails(userIndex,false);
            
			
			// Comments Section
	       /* 	populateCommentingUserDetails();
	       	populateStoryComments();
	       	populateStoryTodos(); */
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
        	
        	<%-- $('#story_form').submit(function(){
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
        	}); --%>
        	
        	$('#addAnotherStory,#createStory').live("click",function(){
        		var title = $('textarea[name=storyDesc]');
        		var description = "No Description";
        		var priority = $('select[name=stPriority]');
        		if(title.val()==""){
        			return;
        		}
        		var user = userLogged;
        		var projectId= <%= projectId%>;
        		var post_data = 'stTitle=' +title.val() + '&projectId='+projectId+'&stDescription=' + description + '&stPriority=' + priority.val() + '&user=' +user;
        		$.ajax({
        			url: '/scrumr/api/v1/stories/create',
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
        		if($(this).attr('id') == "addAnotherStory"){
        			$('textarea[name=storyDesc]').val("");
        			var select = $('select[name=stPriority]');
        			select.val(jQuery('options:first', select).val());
        		}else{
        			$('.story-popup').hide();
        		}
        		
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
    			var selector = $('#storyList').find('ul.story');
    			//query = query.replace(/ /gi, '|'); //add OR for regex query  

    			$(selector).children('li').each(
    					function() {
    						($(this).find('p').text()
    								.search(new RegExp(query, "i")) < 0) ? $(this)
    								.hide() : $(this).show();
    					});
    		});
    		
    		$("#searchUser").live("keyup",function(event) {
    			if (event.which == 13 ||event.which == 8) {
    				event.preventDefault();
    			}
    			//$(this).closest('.popup-proj-cont').find('.user-loading').show();
    			//$(this).closest('.popup-proj-cont').find('#total-user-list').hide();
    			var query=$('#searchUser').val();
    			var selector = $('.popup-proj-cont .c-box .c-box-content').find('ul');
    			
    			var post_data = "sortType="+query+"&showTotalCount=false&startIndex=1&count=10";
    					$.ajax({
    						url : '/scrumr/api/v1/users/searchqontext/',
    						type : 'POST',
    						data : post_data,
    						async :false,
    						success :function(userList){
    							var users_html="";
    							var users_object = $.parseJSON(userList);
    							if(users_object.success.body.totalObjects > 0){
	    							var total_users = users_object.success.body.objects;
	    							var apiVersion = users_object.success.headers["api-version"];
	    							//alert("total users length in search result:"+users_object.success.body.count);
	    							if (users_object.success.body.count==0){
	    								users_html += '<li></li>';
	    								$('.popup-proj-cont .c-box-content ul').html(users_html);
	    							}
	    							for(var i=0;i<total_users.length&&i<10;i++){
	    								
	    								//alert(total_users[i].basicInfo.displayName);
	    								if(!total_users[i].basicInfo.avatarUrl){
	    			    					total_users[i].basicInfo.avatarUrl = "/portal/st/"+apiVersion+"/profile/defaultUser.gif"; //place default url here.
	    			    				}
	    								users_html += '<li><img src="'+qontextHostUrl+total_users[i].basicInfo.avatarUrl+'"/></div><div class="details"><label class="name">'+total_users[i].basicInfo.displayName+'</label><a class="email">'+total_users[i].basicInfo.primaryEmail+'</a></div><div style="float:left;" class="adduser float-rgt enable" id="'+total_users[i].accountId+'"></div></li>';
	    								$('.popup-proj-cont .c-box-content ul').html(users_html);							    								
	    							}    							
	    						}else{
	    							$('.popup-proj-cont .c-box-content ul').html("No users found for the query..");	
	    						}
    						}
    					});    			    		
    		});
        	
        	/* $("#addPeople").fancybox({
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

        	});*/
			viewStoryFancyBox();
			$(".stAddmore").live('click', function(){
				 if($(".stAddmore").html() == "+Add People"){
					$(".stAddmore").html("Hide");
					$("#stPeople").show();
				}else{
					$("#stPeople").hide();
					$(".stAddmore").html("+Add People");
				} 
				$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
			});
			
			$(".viewStory, .moreStory").live('click', function(){
				var id = $(this).closest('li').attr("id");
        		id = id.replace("st","");
        		var stageId = $(this).closest('ul').attr('id');
        		populateStoryAssignees(id);
        		populateCommentingUserDetails();
    	       	populateStoryComments();
    	       	populateStoryTodos();
				$(".add-user").live('click', function(){
					var eid = $(this).attr("alt");
					var eid_arr = [eid];
					addUserToStory(eid_arr, id,stageId);
					populateStoryAssignees(id);	
				});
				
				$(".remvUser").live('click', function(){
					var eid = $(this).parent().find('img').attr("alt");
					removeUserFromStory(eid, id,stageId);
					populateStoryAssignees(id);
				});
				viewStoryFancyBox();
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
            
          
//code for highcharts
    	/* var areaChart; // globally available
    	var pieChart;
    	var storyData=[];
    	var colors = Highcharts.getOptions().colors;
    	//areaChart = new Highcharts.Chart({
    		var areaChartOptions = {
	         chart: {
	            renderTo: 'lineChart',
	            defaultSeriesType: 'area'
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
	            tickmarkPlacement:'on'
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
	            gridLineDashStyle:'longdash'
	            
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
		            showInLegend: true
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
		}); */
	// Comment Section Code
        	
        	var month_list=new Array("January","Febraury","March","April","May","June","July","August","September","October","November","December");
        	
        	function populateCommentingUserDetails(){
            	
        		var commentBoxHtml = '';
				var user = userLogged;	
				var current_user = userObject[user];
				commentBoxHtml = '<img src="'+current_user.avatarurl+'">';							
				$(".comment-box-user").html(commentBoxHtml);
				$(".todo-box-user").html(commentBoxHtml);
				
            }
        	
			function populateStoryComments(){
				var loggedDate='';
				var id = $("#current_story_id").val();	
				var commentsHtml = '<div>';								
				$.ajax({
					url : '/scrumr/api/v1/comments/story/'+id,
					type : 'GET',
					async : false,					
					success :function(comments){
											    
						if (comments != null){	
							$('#comments_section').prev().find('label').html('Comments ('+comments.length+')');							
	        				if(comments.length > 0){
		        				for (var i=0;i<comments.length;i++){
			        				comment = comments[i];
			        				var newDate = new Date(comment.logDate);
			        				var dtString = newDate.getDate()+" "+month_list[newDate.getMonth()]+","+newDate.getFullYear();				        				    				
			        				//alert($.datepicker.parseDate('MM d,yy',new Date(parseInt(comment.logDate))));			        																						        		
			        				commentsHtml += '<li class="comment-list" style="width:100%;"><a id='+comment.pkey+' class="cmtRmvComment remove" href="javascript:void(0);"></a><img title="'+comment.user.fullname+'" src="'+comment.user.avatarurl+'"><div><span><pre style="float:right;">'+comment.content+'</pre></span><div style="clear:both;">'+dtString+'</div></div></li>';				        						        	
			        				}
		        				commentsHtml += '</div>';
		        			}			        				        			        			
						}else{
								commentsHtml += '</div>';
							}
						
						$(".comment-display ul").html(commentsHtml);
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
          			url: '/scrumr/api/v1/comments/delete/'+commentID+'',
          			type: 'GET',
          			//data: post_data,
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
                	 if(commentText =="") return;
					 commentText = parsedString(commentText);		                    	 			                   	               	        	          	                 	      
                     var post_data = '&content='+ commentText+'&storyid='+story_id+'&logdate='+$.datepicker.formatDate('yy-mm-dd', new Date())+'&user='+user;                     
                     $.ajax({
              			url: '/scrumr/api/v1/comments/create',
              			type: 'POST',
              			data: post_data,
              			async:false,
              			success: function( comment ) {
              				
              				comment = comment[0];
                  			e.preventDefault();
              				$('.comment-text').val('');							            				             			
              				var newDate = new Date(comment.logDate);
              				var dtString = newDate.getDate()+" "+month_list[newDate.getMonth()]+","+newDate.getFullYear();              				         				              			
              				$('.comment-display ul').append('<li class="comment-list" style="width:100%";><a id='+comment.pkey+' class="cmtRmvComment remove" href="javascript:void(0);"></a><img title="'+comment.user.fullname+'" src="'+comment.user.avatarurl+'"><div><span><pre style="float:right;">'+comment.content+'</pre></span><div style="clear:both;">'+dtString+'</div></div></li>');
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
                  
         
         $('#todo-form .submit').live("click",function(){
             
        	 var user = userLogged;
        	 var story_id = $("#current_story_id").val();	
        	 var todoText = $(".todo-text").val();                       	  	 
			 var milestonePeriod = $("#todo-milestones").val();		
			 if(todoText == "") return;   
			 todoText = parsedString(todoText);
             var post_data = '&content='+ todoText+'&storyid='+story_id+'&milestonePeriod='+milestonePeriod+'&user='+user;                     
             $.ajax({
      			url: '/scrumr/api/v1/todo/create',
      			type: 'POST',
      			data: post_data,
      			async:false,
      			success: function( todo ) {  
      				todo = todo[0];
      				$('.todo-text').val('');
      				$('#todo-milestones').val('1 Day');							            				             			      				              				         				              			
      				$('.todo-display ul').prepend('<li class="todo-list" style="width:100%";><a id='+todo.pkey+' class="cmtRmvTodo remove" href="javascript:void(0);"></a><img title="'+todo.user.fullname+'" src="'+todo.user.avatarurl+'"><div><span><pre style="float:right;">'+todo.content+'</pre></span><div style="clear:both;">Milestone Period :'+todo.milestonePeriod+'</div></div></li>');
      				$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
      				$('.todo-text').focus();         				              				                 
      			},
      			error: function(data) { },
     			complete: function(data) { }            		
              	});
        
             	return false;
             });

         function deleteStoryTodo(todoID){

     		$.ajax({
       			url: '/scrumr/api/v1/todo/delete/'+todoID,
       			type: 'GET',
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
					url : '/scrumr/api/v1/todo/story/'+id,
					type : 'GET',
					async : false,					
					success :function(todos){
						if (todos != null){		
							$('#todo_section').prev().find('label').html('Todos ('+todos.length+')');	
	        				if(todos.length > 0){
		        				todosHtml += '<ul style="list-style:none;">';
		        				for (var i=0;i<todos.length;i++){
		        					todo = todos[i];			        								        				    						        							        																						        	
			        				todosHtml += '<li class="todo-list" style="width:100%;"><a id='+todo.pkey+' class="cmtRmvTodo remove" href="javascript:void(0);"></a><img title="'+todo.user.fullname+'" src="'+todo.user.avatarurl+'"><div><span><pre style="float:right;">'+todo.content+'</pre></span><div style="clear:both;">Milestone Period: '+todo.milestonePeriod+'</div></div></li>';				        						        	
			        				}
		        				todosHtml += '</ul></div>';
		        			}			        				        			        			
						}else{
								todosHtml += '</div>';
							}
						
						$(".todo-display ul").html(todosHtml);
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
    <a href="<%= request.getContextPath() %>/home.action" class="logo float-lft"></a>
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
            <a id="addPeople" href="javascript:void(0);">Add</a> /
            <a id="removePeople" href="javascript:void(0);">Remove</a> 
            <div style="display:none;overflow:hidden !important;">
            	<div id="people-cont">
	                <div id="userInput" class="">
		            <!--     <input type="text" id="searchUser" placeholder="Search user by name..."/> -->
	                </div>
	                <div id="userList-cont">
	                </div>
            	</div>
            </div>
        </div>
        
        <div class="scroll-cont float-lft">
            <div class="stories-cont">
                <div id="storyLabel" class="float-lft">
                       <label>User stories</label>
                       <a id="addStory" style="float:right;" href="javascript:void(0);">Add Story</a>
                      
                </div>
                <div id="storyInput" class="float-lft">
	                <input type="text" id="searchStory" placeholder="Search story by keyword..."/>
	                
                </div>
                 <div class="priority">
							<div class="p1"></div>
							<div class="p2" ></div>
							<div class="p3" ></div>
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
            <%-- <div class="chart-container">
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
				</div> --%>
				
				
           
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
                       <div class="popup">
    						<div class="acc-cont">
        						<div class="acc">
	        						<div class="acc-head">
	            						<label>Story details</label>
	            						<div class="down"></div>
	        						</div>
	        						<div id="story_details_section" class="acc-content">
	            						<p id="st-title">Hong Kong Phooey, number one super guy. Hong Kong Phooey. The stories begin at the police headquarters,
							                where Hong Kong Phooey's alter ego, Penry, works as a mild-mannered janitor under the glare of Sergeant
							                Flint.</p>
	            						<div class="div">
	            							<label>Created by</label>
	            							<div id="st-creator" style="clear:both;" class="user"><img src="themes/images/1.jpg"/></div>
	           							</div>
	            						<div class="div">
	            							<label>Owned by</label>
	            							<div id="st-assignees" style="clear:both;">
					                       		<div class="user"><img class="remove-user"  title="aomkaram" src="themes/1.jpg"/><span class="remvUser" >Remove</span></div>
					                       		<div class="user"><img class="remove-user"  title="aomkaram" src="themes/2.jpg"/><span class="remvUser" >Remove</span></div>
					                       		<div class="user"><img class="remove-user"  title="aomkaram" src="themes/3.jpg"/><span class="remvUser" >Remove</span></div>
					                       </div>
					                       <span class="stAddmore" style="font-size:12px;cursor:pointer;">+Add People</span>
	            						</div>
	            						<div style="display:none;clear:both;" id="stPeople">
					                       <label>People available:</label>
					                       <div id="st-users">
						                      
						                   </div>
						                </div>
	        						</div>
            					</div>
          						<div class="acc">
        							<div class="acc-head">
            							<label>Todos</label>
							            <div class="open"></div>
							        </div>
							        <div id="todo_section" class="acc-content" style="display:none">
							            <div class="todo" class="float-lft">
						                    <label>Todo List:</label>
					                       	<div id="todo-box" class="comment-cont">
					                       		<div class="todo-box-user float-lft">
													<img src="themes/images/1.jpg"/>
												</div>
					  							<form id="todo-form"> 
							                 	<textarea class="todo-text" placeholder="Write a Todo..." name="todo"></textarea>
							                 	<select id="todo-milestones">							                 		
							                 		<option value="1 Day" selected="selected">1 Day</option>
							                 		<option value="2 Days">2 Days</option>
							                 		<option value="3 Days">3 Days</option>
							                 		<option value="4 Days">4 Days</option>
							                 		<option value="5 Days">5 Days</option>
							                 		<option value="6 Days">6 Days</option>
							                 		<option value="7 Days">7 Days</option>
							                 	</select>
							                 	<input class="submit" type="button" value="Done" />
							                 	</form>
							                 	<div class="todo-display">
							                 		<ul>
							                	 		<li>
							                	 			<a href="javascript:void(0);" class="cmtRmvTodo remove"></a>
							                	 			<img src="themes/images/1.jpg"/>
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
						            </div>
            					</div>
         						<div class="acc">
        							<div class="acc-head">
            							<label>History</label>
							            <div class="open"></div>
							            <div id="history_section" class="acc-content" style="display:none"></div>
        							</div>

            					</div>
         						<div class="acc">
        							<div class="acc-head">
            							<label>Comments</label>
							            <div class="open"></div>
        							</div>
        							<div id="comments_section" class="acc-content" style="display:none">
	        							<div class="storyComments" class="float-lft">
							                 <label>Comments:</label>
							                 <div class="comment-cont">
												<div class="comment-display">
								                 <ul>

								                 </ul>
												</div>
								                 <div class="comment-box">
													<div class="comment-box-user float-lft">
														<img src="themes/images/1.jpg"/>
													</div>
								                 	<textarea class="comment-text" placeholder="Write a comment..." name="commment"></textarea>
								                 </div>
							                 </div>
							                </div>
							            </div>
					            </div>
   							 </div>
						</div>
                       
                       
	                </div>
            	</div>
            </div>
        </div>
        
        <div class="popup-proj-cont" style="display:none;">
           <div class="c-box">
               <div class="c-box-head">Add people to the project</div>
               <div class="c-box-content">
               		<div id="userInput" class="">
		                <input type="text" id="searchUser" placeholder="Search user by name..."/>
	                </div>
	               <div class="user-loading" id="user_loading" style="display:none;"></div>
	               
                   <ul id="total-user-list">
                   
                       
                   </ul>

               </div>
                 <div class="actions-cont float-rgt">

                       <input id="popup_proj_done" type="button" class="float-rgt submit" value="Done"/>
                   </div>
               <div id="pointerEl" class="pointer"></div> </div>
       </div>
        
         <div class="popup-story-cont" style="display:none;">
           <div class="c-box">
               <div class="c-box-head">Assign to </div>
               <div class="c-box-content">
                   <ul id="proj-user-list">
                   
                       
                   </ul>

               </div>
                 <div class="actions-cont float-rgt">

                       <input id="popup_story_done" type="button" class="float-rgt submit" value="Done"/>
                       <a id="popup_cancel">later</a>
                   </div>
               <div id="pointerEl" class="pointer"></div> </div>
       </div>
       
       <div class="story-popup" style="display:none;">
           <div class="c-box">
               <div class="c-box-head">Add Story to this project </div>
               <div class="c-box-content">
                   <form id="story_form" method="POST">
						<div class="sTitle">
							<textarea id="storyDesc" name="storyDesc" rows="5" cols="1" placeholder="Enter Story Desc" required="required"></textarea>
						</div>
						<select name="stPriority" id="storyPriority">
							<option selected="selected" value="1">Priority 1</option>
							<option value="2">Priority 2</option>
							<option value="3">Priority 3</option>							
						</select>
						<a id="addAnotherStory" href="javascript:void(0);" >+ Add another story</a>
					</form>
               </div>
                 <div class="actions-cont float-rgt">

                       <input id="createStory" type="button" class="float-rgt submit" value="Done"/>
                       <a id="popup_story_cancel">Later</a>
                   </div>
               <div id="pointerEl" class="pointer"></div> </div>
       </div>
       <script>
                     $('#popup_cancel').live("click",function(){
                    	 $('.popup-story-cont').hide();
                     }); 
                     
                     $('#popup_story_cancel').live("click",function(){
                    	 $('.story-popup').hide();
                     }); 
                     
					 //code for story details accordion
                     
                     $('div.down,div.open').live("click",function(){
                    	 if($(this).hasClass('open')){
                    		var current_div = $(this);
                    		$('div.down').each(function(){
                    			if($(this) != current_div){
                    				$(this).removeClass('down').addClass('open');
                    				$(this).parent().next('.acc-content').slideUp("slow");
                    			}
                    		}); 
                    		$(this).removeClass('open').addClass('down');
                    		$(this).parent().next(".acc-content").slideDown("slow");
                    	}else{
                    		$(this).removeClass('down').addClass('open');
                    		$(this).parent().next(".acc-content").slideUp("slow");
                    	}
                    	 $("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
                     });
                     
                     
                     
                     
                     $('#addStory').live("click",function(){
                    	 $('.popup-proj-cont').hide();
                    	 $('.story-popup').show();
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