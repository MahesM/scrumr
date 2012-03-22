$(document).ready(function() {
        	var userIndex = 0;
        	var current_sprint = 0;
        	var users_arr = [];
        	var sprintinview = 0;
        	var totalsprints = 0;
        	var creator;
        	var projectLanes = new Object();
        	var projectPriorities = new Object();
        	var projectPreferences = new Object();
        	var editStory = new Object();
        	var edit = false;
        	var mileStoneType = projectPreferences.mileStoneType == 0 ?"hour":"day";
        	var users = null;
        	var userObject = new Object();
        	var creatorObj = new Object();
        	var storyObj = new Object();
        	var duration = '';
        	var projectStatus = "";
        	var storyListScroll = null;
        	var userListScroll = null;
        	var sprintStageScroll = new Object();
        	var projStageScroll = new Object();
        	var addUserScroll = null;
        	var storyDetailScroll = new Object();
        	var left_pane_collapsed = false;
        	var perPage = 3;
        	var backlogSearchSource= null;
        	var backlogentry = false;
        	var imageCollections = [{value:0,url:"themes/images/project_stages/repository1.png"},{value:1,url:"themes/images/project_stages/repository2.png"},{value:2,url:"themes/images/project_stages/repository3.png"},{value:3,url:"themes/images/project_stages/repository4.png"},{value:4,url:"themes/images/project_stages/repository5.png"},{value:5,url:"themes/images/project_stages/repository6.png"},{value:6,url:"themes/images/project_stages/repository7.png"},{value:7,url:"themes/images/project_stages/repository8.png"},{value:8,url:"themes/images/project_stages/repository9.png"},{value:9,url:"themes/images/project_stages/repository10.png"}];
        	
    		$(document).ajaxError(function(e, jqxhr, settings, exception) {
		//			window.location.href="auth.action";    			
    		});
    		
    		localStorage.clear();  
    		$("#backlog_seach_input" ).autocomplete({
    			source: backlogSearchSource
    		}).data( "autocomplete" )._renderItem = function( ul, item ) {
    			ul.attr('id','backlog-search-menu');
				var el = $( "<li></li>" ).data( "item.autocomplete", item );
					el.append( "<a style='color:black'>" + item.value + " - <label style='color:grey;'> " + item.type + "</label></a>" );
    			return el.appendTo( ul );
		};
    		
    		function populateSearchDataSource(){
    			$.ajax({
            		url: 'api/v1/stories/fetchstorydata/'+projectId,
            		type: 'GET',
            		async:false,
            		success: function( source ) {
            			backlogSearchSource = source;
            		}
    			});
    		}
       	
        	function populateProjectDetails(){
        		$('#peopleview').css({'height': (($(window).height()) -150) + 'px'});
        		$.ajax({
            		url: 'api/v1/projects/'+projectId,
            		type: 'GET',
            		async:false,
            		success: function( project ) {
            			if(project != null && project.length > 0){
            				project = project[0];
            				projectStatus = project.status;
            				projectLanes = project.projectStages;
            				projectPriorities = project.projectPriorities;
            				projectPreferences = project.projectPreferences;
            				totalsprints = project.no_of_sprints;
            				current_sprint = project.current_sprint > 0?project.current_sprint:1;
            				sprintinview = current_sprint;
            				creator = project.createdby;
            				var title = project.title;
            				if(title.length > 30){
            					title = title.substring(0,30)+" ...";
            				}
            				$('header .proj_title label').html(title);
            				users = project.assignees;
            				$('section.left .peopleview label').html("Members ("+users.length+")");
            				$('section.left_collapsed .peopleview label').html(users.length+" Members");
            				render_proj_sprint();
            				$("#people").html('');
            				if(users != null && users.length > 0){
            					for(var i=0;i< users.length;i++){
            						var userFullName = users[i].fullname;
            						if( userLogged ==  users[i].username )  userFullName += " ( You )" ; 
            						$("#people").append('<li><img title="'+ users[i].fullname +'" src="'+qontextHostUrl+users[i].avatarurl+'" /><div class="proj_mem_name" id= "'+ users[i].fullName +'" >'+ userFullName +'</div><img class="remove_mem" title="Remove" src="themes/images/delete_member.png" id="'+users[i].username+'" ></li>');
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
	            					user_html += "<li id='"+users[i].username+"'><img src='"+qontextHostUrl+users[i].avatarurl+"' title='"+users[i].fullname+"''/></li>";
	            				}
            				}
           					$('#proj-user-list').html(user_html);
           					if(userListScroll){
	   	       					 var api = $('#peopleview').data('jsp');
	   	       					 if(api)api.destroy();
          					}
           					if(!$('#peopleview').is(':visible')){
           						userListScroll = $('#peopleview').show().jScrollPane({showArrows: true, scrollbarWidth : '20'}).hide().data().jsp;
           					}else{
           						userListScroll = $('#peopleview').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
           					}
            			}
            			constructProjectFooter();
            		},
            		error: function(data) { },
            		complete: function(data) { }
            	});
        	}
        	
        	function constructProjectFooter(){
        		$.ajax({
            		url: 'api/v1/stories/fetchprojectstorycount/'+projectId,
            		type: 'GET',
            		async:false,
            		success: function( details ) {
		        		var totalStories = 0;
		        		var completedStories = 0;
		        		for(var i=0;i<details.length;i++){
		        			totalStories +=details[i].totalStories;
		        			completedStories +=details[i].completedStories;
		        		}
		        		if(totalStories == 0){
		        			$('#project_footer .total_class label span').html('(0/0)');
		        		}else{
		        			$('#project_footer .total_class label span').html('('+completedStories+'/'+totalStories+')');
		        			var str = "";
		        			for(var i=0;i<details.length;i++){
		        				str+="<div class='size_class'><label><span>"+details[i].value+"</span> ("+details[i].completedStories+"/"+details[i].totalStories+")</label></div>";
		        			}
		        			$('#project_footer').append(str);
		        		}
            		}
        		});
        	}
        	
        	function constructSprintFooter(sprintId){
        		$.ajax({
            		url: 'api/v1/stories/fetchsprintstorycount/'+sprintId,
            		type: 'GET',
            		async:false,
            		success: function( details ) {
		        		var totalStories = 0;
		        		var completedStories = 0;
		        		for(var i=0;i<details.length;i++){
		        			totalStories +=details[i].totalStories;
		        			completedStories +=details[i].completedStories;
		        		}
		        		if(totalStories == 0){
		        			$('#sprint_footer .total_class label span').html('(0/0)');
		        		}else{
		        			$('#sprint_footer .total_class label span').html('('+completedStories+'/'+totalStories+')');
		        			var str = "";
		        			for(var i=0;i<details.length;i++){
		        				str+="<div class='size_class'><label><span>"+details[i].value+"</span> ("+details[i].completedStories+"/"+details[i].totalStories+")</label></div>";
		        			}
		        			$('#sprint_footer').append(str);
		        		}
            		}
        		});
        	}
        	
        	function constructTaskFooter(details){
        		
        	}
        	
        	
			function populateUnassignedStories(name){
				populateSearchDataSource(); //populate datasource everytime stories are added to backlog
				// $('#storyList ul').css({'height': (($(window).height()) - 320) + 'px'});
				 $('#storyList').css({'height': (($(window).height()) -180) + 'px'});
	        	 $.ajax({
	        		url: 'api/v1/stories/backlog/'+projectId,
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
        				var story_unassigned = '';
	        			if(stories != null && stories.length){
	        				$('#add_ppl_info').hide();
	        				$('section.left .backlogview label').html("Backlog ("+stories.length+")");
	        				$('section.left_collapsed .backlogview label').html(stories.length+" Backlog");
	        				for(var i=0;i<stories.length;i++){
	        					var story = stories[i];
	        					creatorObj = userObject[story.creator];
	        					var userObj = userObject[story.creator];
	        					storyObj = {story:story,backlog:true,image:null}
	        					story_unassigned += new EJS({url: 'ejs/story_template.ejs'}).render(storyObj);
	        				}
	        			}else{
	        				story_unassigned += '<b>No pending stories for the project</b>';
	        				$('section.left .backlogview label').html("Backlog (0)");
	        				$('section.left_collapsed .backlogview label').html("0 Backlog");
	        				//$('#storyList ul').css({'height': (($(window).height()) - 250) + 'px'});
	        			}
        				$("#storyList ul").html(story_unassigned);
        				if($('#storyList ul').find('li').length > 0){
        					var ulHeight = ($('#storyList ul').find('li').outerHeight() * $('#storyList ul').find('li').length) + ($('#storyList ul').find('li').outerHeight());
        					$('#storyList ul').css({'height': ulHeight + 'px'});
        				}else {
        					$('#storyList ul').css({'height': '150px'});
        				}
        				$( "#storyList ul" ).sortable({
        	        		connectWith: ".story",
        	        		items:'li',
        	        		helper:'clone',
        	        		appendTo: 'body',
        	        		forcePlaceholderSize: true,
        	        		placeholder: 'ui-state-highlight',
        	        		update: function( event, ui ) {
        	        			if(ui.item.closest('section').hasClass('right')){ //dropped to the stages section
        	        				ui.item.find('a.remove').removeClass('strRmv').addClass('sptRmv');
        	        			};
        	        			//$('#storyList ul').css({'height': (($(window).height()) - 500) + 'px'});
        	        			if($('#storyList ul').find('li').length > 0){
        	    					var ulHeight = ($('#storyList ul').find('li').outerHeight() * $('#storyList ul').find('li').length) + ($('#storyList ul').find('li').outerHeight());
        	    					$('#storyList ul').css({'height': ulHeight + 'px'});
        	    				}else {
        	    					$('#storyList ul').css({'height': '150px'});
        	    				}
        	        			if(storyListScroll){
        	   					 var api = $('#storyList').data('jsp');
        	   					 if(api)api.destroy();
        	   				 	}
        	        			storyListScroll = $('#storyList').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
        	        		},
        	        		start:function(event,ui){
        	        			ui.helper.attr('id','backlog');
        	        		},
        	        		over : function(event,ui){
        	        			if($(ui.sender).find('li').length > 0){
        	    					var ulHeight = ($(ui.sender).find('li').outerHeight() * $(ui.sender).find('li').length) + 150;
        	    					$(ui.sender).css({'height': ulHeight + 'px'});
        	    				}else {
        	    					$(ui.sender).css({'height': '150px'});
        	    				}
        	        			if(sprintStageScroll[$(ui.sender).attr('id')]) {
	        		   				sprintStageScroll[$(ui.sender).attr('id')].destroy();
        		   					sprintStageScroll[$(ui.sender).attr('id')] =$(ui.sender).closest('.sprintCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
        		   				} 
        	        			if(projStageScroll[$(ui.sender).attr('id')]) {
        	        				projStageScroll[$(ui.sender).attr('id')].destroy();
        	        				projStageScroll[$(ui.sender).attr('id')] =$(ui.sender).closest('.projectCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
        		   				} 
	        		   			
        	        		}
        	    		}).disableSelection();
        				if(storyListScroll){
	       					 var api = $('#storyList').data('jsp');
	       					 if(api)api.destroy();
       					 }
        				storyListScroll = $('#storyList').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
	        		},
	        		error: function(data) { },
	        		complete: function(data) { }
	        	});
	        	
			}
			 
			function populateSprints(){
       		 var post_data2 = 'projectId='+projectId;
		        	$.ajax({
		        		url: 'api/v1/sprints/project/'+projectId,
		        		type: 'GET',
		        		async:false,
		        		success: function( sprints ) {
		        			if(sprints.length > 0){
		        				var duration = '';
								if(sprints[0].projectStartDate != null){
									var startdate = new Date(sprints[0].projectStartDate);
									startdate = startdate.format("mmm dd");
									duration += startdate;
								}else{
									duration += 'No Start Date';
								}
								if(sprints[0].projectEndDate != null){
									var enddate = new Date(sprints[0].projectEndDate);
									enddate = enddate.format("mmm dd");
									duration += '&nbsp;&nbsp;-&nbsp;&nbsp;'+ enddate;
								}else{
									duration += ' - No End Date';
								}

								//var status = 'Completed:<span id="project_finished" class="finished">0</span> Total: <span id="project_total" class="total">0</span>';
								$.ajax({
					        		url: 'api/v1/projects/storycount/'+projectId,
					        		type: 'GET',
					        		async:false,
					        		success: function( result ) {
					        			result = $.parseJSON(result);
										status = 'Stories Completed: <span id="project_finished" class="finished">'+result.result+'</span>Total: <span id="project_total" class="total">'+result.result+'</span>';
					        		}
								});
								var sprintTitle = '<b>Sprint '+current_sprint+' </b>';
								var sprint_html = "<div class=\"sprintInfoBand\" >"+duration+"&nbsp;&nbsp; | "+sprintTitle+"  "+sprints[0].projectStatus+"&nbsp;&nbsp;| <a href=\"javascript:void(0);\" class=\"new_sprint\" >Add New Sprint</a><div class=\"pageCtrls\" style=\"float:right;height:30px;\"></div></div>";
								var finished = 0;
				        		sprint_html += '<ul id="project_holder" class="col"><div class="holder_round">';
			        			//$("ul.col li").css("width",100/sprints.length+'%');
			        			for(var k=0; k<sprints.length;k++ ){
			        				var projectStageDetails = sprints[k].storyStageDetails;
			        				//sprint_html += '<li class="stages"><div class="header "><span></span>Sprint '+(k+1)+'</div><div class="projectCont"><ul id="sp'+sprints[k].id+'"class="story">';
			        				var sprint_startdate = new Date(sprints[k].startdate);
			        				sprint_startdate = sprint_startdate.format("mmm dd");
			        				var sprint_enddate = new Date(sprints[k].enddate);
			        				sprint_enddate = sprint_enddate.format("mmm dd");
			        				sprint_html += '<li class="stages"><div class="header "><label>Sprint '+(k+1)+' </label> |&nbsp;&nbsp;'+sprint_startdate+' - '+sprint_enddate+'<a class="editSprint" style="display:none;" href="javascript:void(0);"></a></div><div class="projectCont"><ul id="sp'+sprints[k].id+'"class="story">';
			        				var post_data2 = 'sprintId='+sprints[k].pkey+'&projectId='+projectId;
						        	$.ajax({
						        		url: 'api/v1/stories/sprint/'+sprints[k].pkey,
						        		type: 'GET',
						        		async:false,
						        		success: function( stories ) {
					        				if(stories != null && stories.length > 0){
					        					for(var i=0;i<stories.length;i++){
						        					var story = stories[i];
						        					var creatorObj = userObject[story.creator];
						        					if(story.status == "finished"){
						        						finished = finished+1;
						        					}
						        					
						        					storyObj = {story:story,backlog:false,image:null};
						        					//sprint_html += '<li id="st'+story.pkey+'" class="p'+story.priority+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
						        					sprint_html += new EJS({url: 'ejs/story_template.ejs'}).render(storyObj);
						           				}
					        				}
						        		},
						        		error: function(data) { },
						        		complete: function(data) { }
						        	});
			        				sprint_html += '</ul></div>';
			        				sprint_html += '<table cellspacing="0" cellpadding="4" class="sprint-info" id="sprint-info'+sprints[k].pkey+'" style="display:none;"><tr></tr>';
			        				sprint_html +='</table></li>';
			        				
			        			}
			        			$("#project_finished").html(finished);
			        			
			        			sprint_html +='</div></ul>';
			        			$("#project-view").html(sprint_html);
			        			constructSprintStageBar();
			        	        $('ul.col li.stages').hover(function(){
			        	        	$(this).find('a.editSprint').show(); 
			        	        	$(this).find('table.sprint-info').show(); 
			        	         },function(){
			        	        	 $(this).find('a.editSprint').hide();
			        	        	 $(this).find('table.sprint-info').hide(); 
			        	         });
			        	        
			        	        
			        			$('#project-view .pageCtrls').html("");
			        			var current_page = current_sprint - 1;
			        			if(left_pane_collapsed){
			        				$('section.right').css('width','96%');
			        				if($('section.right #project-view li.stages').length > 3){
			        					$('section.right #project-view li.stages').css('width','24.9%');
			        				}else{
			        					$('section.right #project-view li.stages').css('width','33.2%');
			        				}
			        			}else{
		        					$('section.right #project-view li.stages').css('width','33.2%');
		        				}
			        			setTimeout(function(){
			        				constructPagination($('.col:visible'),perPage,current_page);
			        			},1);
			        			$( ".stages ul" ).sortable({
		        	        		connectWith: ".story",
		        	        		items:'li',
		        	        		helper:'clone',
		        	        		appendTo: 'body',
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
			        		   				//$(ui.item[0]).closest('ul').css({'height': (($(window).height()) - 195) + 'px'});
			        		   				var ulHeight = ($(ui.sender).find('li').outerHeight() * $(ui.sender).find('li').length) + 150;
			        		   				$(ui.sender).css({'height': ulHeight + 'px'});
			        		   					
		        		   					if(projStageScroll[$(ui.sender).attr('id')]) {
		        		   						projStageScroll[$(ui.sender).attr('id')].destroy();
		        		   						projStageScroll[$(ui.sender).attr('id')] =$(ui.sender).closest('.projectCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
			        		   				}  
		        	        			}
		        	        			
		        		   			},
		        		   			start:function(event,ui){
		        	        			ui.helper.attr('id','backlog');
		        	        		},
		        		   			
		        		   			receive :function(event,ui){
		        		   				var ulHeight = ($(ui.item[0]).outerHeight() * $(ui.item[0]).closest('ul').find('li').length) + 150;
		        		   				$(ui.item[0]).closest('ul').css({'height': ulHeight + 'px'});
		        		   				if(projStageScroll[$(ui.item[0]).closest('ul').attr('id')]) {
		        		   					projStageScroll[$(ui.item[0]).closest('ul').attr('id')].destroy();
		        		   					projStageScroll[$(ui.item[0]).closest('ul').attr('id')] =$(ui.item[0]).closest('.projectCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
		        		   				} else {
		        		   					projStageScroll[$(ui.item[0]).closest('ul').attr('id')] =$(ui.item[0]).closest('.projectCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp; 
		        		   				}
		        		   			}
		        	    		}).disableSelection();
			        			
			        			$('.stages .projectCont').css({'height': (($(window).height()) - 160) + 'px'});
			        			$('.stages').parent('.holder_round').css({'height': (($(window).height()) - 130) + 'px'});
			        			$( ".stages .projectCont ul").each(function(){
									if($(this).find('li').length > 0){
										//$(this).css({'height': (($(window).height()) - 195) + 'px'});
										var ulHeight = ($(this).find('li').outerHeight() * $(this).find('li').length) + 150;
										$(this).css({'height': ulHeight + 'px'});
										projStageScroll[$(this).attr('id')] = $(this).parent().jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;	
									}else {
										//$(this).css({'height': (($(window).height()) - 180) + 'px'});
										$(this).css({'height': '150px'});
									}
								});
		        			}
		        			
		        			
		        		},
		        		error: function(data) { },
		        		complete: function(data) { 
		        			
		        		}
		        		
		        	});
			}
			
			function constructSprintStageBar(){
				$.ajax({
	        		url: 'api/v1/stories/fetchsprintstagecount/'+projectId,
	        		type: 'GET',
	        		async:false,
	        		success: function( projectStageDetails ) {
						for(var i=0;i<projectStageDetails.length;i++){
							var sprint_html="";
							var sprintId = "";
							//var stageWidth = ($('.sprint-info').outerWidth()/projectStageDetails[i].length) - projectStageDetails[i].length;
							for(var k=0;k<projectStageDetails[i].length;k++){
								sprintId = projectStageDetails[i][k].sprintId;
								sprint_html += "<td align='center' id='projectStage"+projectStageDetails[i][k].id+"'><img src='"+imageCollections[projectStageDetails[i][k].imageUrlIndex].url+"'/><label>("+projectStageDetails[i][k].storyCount+")</label></td>";
							}
							$('#sprint-info'+sprintId+" tr").html(sprint_html);
							//$('.projectStage').css('width',stageWidth+"px");
							
						}
						 $('.sprint-info td').unbind('click').bind('click',function(){
							 	var tdEl = $(this);
							 	$(this).toggleClass('bg');
							 	$(this).siblings().removeClass('bg');
		        	        	var query = $(this).attr('id').split('projectStage')[1];
		        	        	var selector = $(this).closest('li').find('ul.story');
		        	        	$('ul.story li').removeClass('selected');
		        	        	$(selector).find('li').each(function() {
		        	        		if(tdEl.hasClass('bg')){
		        	        			if($(this).attr('data-stage').search(new RegExp(query, "i")) >= 0) {
		            						$(this).addClass('selected');
		            					}
		            				}else{
		            					if($(this).attr('data-stage').search(new RegExp(query, "i")) >= 0) {
		            						$(this).removeClass('selected');
		            					}
		            				}
		        	        	});
						 });
	        		}
				});
			}
			
			function refreshStoryPortlet(storyId,stageId,creatorObj){
				var post_data="storyId="+storyId+"&stage="+stageId;
				$.ajax({
					//url : 'restapi/stories/getusers?storyid='+storyId+'&stage='+stageId+'', 
					url : 'api/v1/stories/getusers',
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
								imageHTML+="<img height='26' width='26' class='' id = '"+users[i].user.username+"' src='"+qontextHostUrl+''+users[i].user.avatarurl+"' title='"+users[i].user.fullname+"'>";
							}
						}else{
							//imageHTML+="<img height='26' width='26' class='' src='"+qontextHostUrl+''+creatorObj.avatarurl+"' title='"+creatorObj.fullname+"'><label class=''>Created by "+creatorObj.fullname+"</label>";
						}
						$('li#st'+storyId).find('.img-cont').html(imageHTML);
						viewStoryFancyBox();
					}
				});
						
			}
			
			function viewStoryFancyBox(){
				
			//	$("#add_story_popup").show();
			//	$("#custom_overlay").fadeIn('slow');
				
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
				
		/*	$(".viewStory, .moreStory").fancybox({
				'overlayColor' : '#000',
	                'overlayOpacity' : '0.6',
	                'autoScale' : false,
	                'onComplete' : (function(){
	                   // viewStoryScrollpane =$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
	                  //  $('#story-cont').css("background-color",bgColor);
	                       }),
	                'onStart' : (function(el){
	                	// var storyEl=$(el).closest('li');
	                	//bgColor = ($(storyEl).css('backgroundColor')); 
	                        }),
	                'onClosed' : (function() {
	                	$("#stPeople").hide();
	                	//viewStoryScrollpane.destroy();
	                	
	                	var storyObj =$(this).attr('orig').context['parentElement'];
	                	var storyId = storyObj['id'].split("st")[1];
	                	var stageId = $(storyObj).parent().attr('id');
	                	if (storyId == null || stageId == null){
	                		
	                		storyObj = $(this).attr('orig').context['offsetParent'];
	                		storyId = storyObj['id'].split("st")[1];
	                		stageId = $(storyObj).parent().attr('id');
	                		//alert('story id='+storyId+", stage id="+stageId);
	                		//alert($(this).attr('orig').context['offsetParent']['id']);	                		
	                	}
	                	refreshStoryPortlet(storyId, stageId, creatorObj);
	                	$(".stAddmore").html("Add Members");
	                	//close all accordion except story details
	                	$('div.down').each(function(){
	                		if($(this).parent().next('#story_details_section').length == 0){
	                			$(this).removeClass('down').addClass('open');
	                			$(this).parent().next(".acc-content").hide();
	                		}
	                	});
	                	$('div.open').each(function(){
	                		if($(this).parent().next('#story_details_section').length != 0){
	                			$(this).removeClass('open').addClass('down');
	                		}
	                	});
	                   })

	        	}); */
			}
			
			
			function populateSprintStories(sprint){
					constructSprintFooter(sprint);
					var userObj='';
					 var sprintTitle = '';
					 var story_html = "";
	        		 if(totalsprints == 0){
	        			 sprintTitle = '<label>No Sprints available for this project.</label>';
		        	 }else{
		        		 sprintTitle = '<b>Sprint '+sprint+' </b>';
		        	 }
	        		 
	        		 var post_data2 = 'sprintId='+sprint+'&projectId='+projectId;
			        	$.ajax({
			        		url: 'api/v1/sprints/'+sprint+'/project/'+projectId,
			        		type: 'GET',
			        		async:false,
			        		success: function( result ) {
			        			
			        			result=result[0];
			        			if(result != null){
				        			var duration = '';
		    						if(result.startdate != null){
		    							var startdate = new Date(result.startdate);
		    							startdate = startdate.format("mmm dd");
		    							duration += startdate;
		    						}else{
		    							duration += 'No Start Date';
		    						}
		    						if(result.enddate != null){
		    							var enddate = new Date(result.enddate);
		    							enddate = enddate.format("mmm dd");
		    							duration += '&nbsp;&nbsp;-&nbsp;&nbsp;'+ enddate;
		    						}else{
		    							duration += ' - No End Date';
		    						}
		    						//var status = 'Stories Completed: <span id="sprint_finished" class="finished">0</span> Total: <span id="sprint_total" class="total">0</span>';
		    						story_html = "<div class=\"sprintInfoBand\" >"+duration+"&nbsp;&nbsp; | "+sprintTitle+" "+result.status+"&nbsp;&nbsp;<div class=\"pageCtrls\" style=\"float:right;height:30px;\"></div></div>";
		    						
		    						story_html += '<ul id="sprint_holder" class="col"><div class="holder_round">';
		    	        			//sort the array based on rank as it comes in random order.
		    	        			projectLanes.sort(function compareRank(a, b){
		    	        				return a.rank> b.rank? 1: -1;
		    						});	
		    						for(var i=0;i<projectLanes.length;i++){
		            				story_html += '<li class="stages"><div class="header "><img src="'+imageCollections[projectLanes[i].imageUrlIndex].url+'"></span>'+projectLanes[i].title+'</div><div class="sprintCont"><ul data-type="'+projectLanes[i].rank+'" id="stage'+projectLanes[i].pkey+'"class="story"></ul></div></li>';
		    	        			}
		    	        			story_html +='</div></ul>';
		    	        			$('#sprint-view').html(story_html);
		    	        			if(left_pane_collapsed){
		    	        				$('section.right').css('width','96%');
		    	        				if($('section.right #sprint-view li.stages').length > 3){
		    	        					$('section.right #sprint-view li.stages').css('width','24.9%');
		    	        				}else{
		    	        					$('section.right #sprint-view li.stages').css('width','33.2%');
		    	        				}
		    	        			}else{
		            					$('section.right #sprint-view li.stages').css('width','33.2%');
		            				}
		    	        			setTimeout(function(){
		    	        				constructPagination($('.col:visible'), perPage, 0);
		    	        			},1);
		    	        			//$('.stages ul:visible').parent().css({'height': (($(window).height()) - 175) + 'px'});
		    			        			
			        			}
			        		},
			        		error: function(data) { },
			        		complete: function(data) { }
			        	});
			        	
					
	        		 var post_data2 = 'sprintId='+sprint+'&projectId='+projectId;
			        	$.ajax({
			        		url: 'api/v1/stories/'+sprint+'/project/'+projectId,
			        		type: 'GET',
			        		async:false,
			        		success: function( stories ) {
				        			var finished = 0;
			        				if(stories != null && stories.length > 0){
			        					var str = '';
			        					var story='';
			        					for(var i=0;i<stories.length;i++){
				        					story = stories[i];
				        					userObj = userObject[story.creator];
				        					creatorObj = userObject[story.creator];
				        					var story_assignees = story.assignees;
				        					if(story_assignees !=null && story_assignees.length > 0){								        					
					        					for(var j=0;j<story_assignees.length;j++){
					        						if(j==2){
														imageHTML+="<a class='moreStory' href='#story-cont'>more</a>";
														break;
													}
													imageHTML+="<img height='26' width='26' class='' src='"+qontextHostUrl+""+story_assignees[j].user.avatarurl+"' title='"+story_assignees[j].user.fullName+"'>";
												}
					        					storyObj = {story:story, backlog:false, image:imageHTML};
				        					 	str = new EJS({url: 'ejs/story_template.ejs'}).render(storyObj);
				        					}
				        					/*var post_data="storyId="+story.pkey+"&stage="+story.status;
				        					$.ajax({
				        						url : 'api/v1/stories/getusers',
				        						type : 'POST',
				        						async : false,
				        						data: post_data,
				        						success : function(users) {	
				        							 var imageHTML = "";
						        					 if(users !=null && users.length > 0){								        					
								        					for(var j=0;j<users.length;j++){
								        						if(j==2){
																	imageHTML+="<a class='moreStory' href='#story-cont'>more</a>";
																	break;
																}
																imageHTML+="<img height='26' width='26' class='' src='"+qontextHostUrl+""+users[j].user.avatarurl+"' title='"+users[j].user.fullName+"'>";
															}							        					
							        					}
						        					 	storyObj = {story:story, backlog:false, image:imageHTML};
						        					 	str = new EJS({url: 'ejs/story_template.ejs'}).render(storyObj);
				        						}
				        					});*/
				        					
				        					$('ul#stage'+story.ststage.pkey).append(str);
				        					if(story.status == 0){
				        						$('ul#stage'+story.status).find("label#noStories").remove();
				        					}
				        					if(story.status == projectLanes[projectLanes.length-1].rank){
				        						finished = finished + 1;
				        					}

				        				}
				        				$("#sprint_total").html(stories.length);
				        				$("#sprint_finished").html(finished);

				        			}else{
				        				$("ul#stage0").html('<label id="noStories" style="margin:5px;float:left">No Stories assigned to this sprint.</label>');
				        			}
				        			$( ".stages ul" ).sortable({
			        	        		connectWith: ".story",
			        	        		items:'li',
			        	        		helper:'clone',
			        	        		appendTo: 'body',
			        	        		forcePlaceholderSize: true,
			        	        		placeholder: 'ui-state-highlight',
			        	        		update: function( event, ui ) {
			        	   				 	var id = ui.item.attr("id");
			        		   				if($(ui.item[0]).closest('ul').attr('data-type') == 0){
			        		   					$(ui.item[0]).closest('ul').find("label#noStories").remove();
			        		   					var success = updateStoryStatus(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1],sprint); 
			        		   					//$(ui.item[0]).find('.img-cont').html("");
			        		   					//removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]);
			        		   				}else if(($(ui.item[0]).closest('section').hasClass('left'))) {
			        		   					var new_id = id.replace("st","");
			        		   					$(ui.item[0]).find('a.remove').removeClass('sptRmv').addClass('strRmv');
			        		   				//	removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]);
			        		   					addtoCurrentSprint(new_id, 0);
			        		   					populateUnassignedStories('');
			        		   				}else{
				        		   				if(projectStatus !="Not Started" && projectStatus !="Finished"){
					        		   				var success = updateStoryStatus(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1],sprint); 
					        		   				$(ui.item[0]).find('a.remove').removeClass('strRmv').addClass('sptRmv');
					        		   				if(success == false){
					        		   					$(this).sortable('cancel');
					        		   				}  
				        		   					var ulHeight = ($(ui.sender).find('li').outerHeight() * $(ui.sender).find('li').length) + 150;
				        		   					$(ui.sender).css({'height': ulHeight + 'px'});
				        		   					
				        		   					if(sprintStageScroll[$(ui.sender).attr('id')]) {
						        		   				sprintStageScroll[$(ui.sender).attr('id')].destroy();
					        		   					sprintStageScroll[$(ui.sender).attr('id')] =$(ui.sender).closest('.sprintCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
					        		   				} 
				        		   				}
		        		   					
		        		   					}
			        		   					
			        		   			},
			        		   			start:function(event,ui){
			        	        			ui.helper.attr('id','backlog');
			        	        		},
			        		   			
			        		   			receive :function(event,ui){
			        		   				var ulHeight = ($(ui.item[0]).outerHeight() * $(ui.item[0]).closest('ul').find('li').length) + 150;
			        		   				$(ui.item[0]).closest('ul').css({'height': ulHeight + 'px'});
			        		   				if(sprintStageScroll[$(ui.item[0]).closest('ul').attr('id')]) {
				        		   				sprintStageScroll[$(ui.item[0]).closest('ul').attr('id')].destroy();
			        		   					sprintStageScroll[$(ui.item[0]).closest('ul').attr('id')] =$(ui.item[0]).closest('.sprintCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
			        		   				}else {
			        		   					sprintStageScroll[$(ui.item[0]).closest('ul').attr('id')] =$(ui.item[0]).closest('.sprintCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp; 
			        		   				}
			        		   				if($(ui.item[0]).closest('ul').attr('data-type') !="BACKLOG"){
			        		   					if(projectStatus == "Finished" || projectStatus == "Not Started"){
				        		   					$(ui.sender).sortable("cancel");
				        		   					$('.error-hd label').html("Stories cannot be moved when project has "+projectStatus.toLowerCase());
				        		   					$('.error-hd').fadeIn("slow");
				        		   					 setTimeout(function(){
				        		   						$('.error-hd').fadeOut("slow");
				        		   					},3000); 
				        		   					
				        		   				}
			        		   				}
			        		   			}
			        		   			
			        	    		}).disableSelection();
			        				
				        			$('.stages ul:visible').parent('.sprintCont').css({'height': (($(window).height()) - 160) + 'px'});
				        			$('.stages').parent('div.holder_round').css({'height': (($(window).height()) - 130) + 'px'});
				        			$( ".stages ul:visible").each(function(){
										if($(this).find('li').length > 0){
											var ulHeight = ($(this).find('li').outerHeight() * $(this).find('li').length) + 150;
											$(this).css({'height': ulHeight + 'px'});
											sprintStageScroll[$(this).attr('id')] = $(this).parent().jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;	
										}else {
											$(this).css({'height': '150px'});
										}
									});
			        		},
			        		error: function(data) { },
			        		complete: function(data) { }
			        	});
			}
			
			$('#error_hd_close').unbind('click').live("click",function(){
				$('.error-hd').fadeOut("slow");
			});
			
			function addtoCurrentSprint(storyList,sprint){
    			var post_data = 'projectId='+projectId+'&stories='+storyList+'&status=0&sprint='+sprint;
    			
    			$.ajax({
    				url: 'api/v1/stories/addtosprint',
    				type: 'POST',
    				data: post_data,
    				async:false,
    				success: function( result ) {
    				},
    				error: function(data) { },
    				complete: function(data) { }
    			});
			}
			
			function updateStoryStatus(id,status,sprint){
				var stat = false;
				var post_data = 'stories=' + id + '&status='+status+ '&sprint='+sprint +'&projectId='+projectId;
					$.ajax({
						url: 'api/v1/stories/addtosprint',
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
						url: 'api/v1/stories/delete/'+id,
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
				var post_data = 'userid='+userDetails.username+'&projectId='+projectId;
				$.ajax({
					url: 'api/v1/users/create',
					type: 'POST',
					data: userDetails,
					async:false,
					success: function( rec ) {
						var records = eval("("+rec+")");
						if(records != null && records.result == "success"){
							stat = true;
						}
						$.ajax({
							url: 'api/v1/projects/adduser',
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
				 var post_data = 'userid='+id+'&projectId='+projectId;
				$.ajax({
					url: 'api/v1/projects/removeuser',
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
					url: 'api/v1/stories/adduserswithstage',
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
					url: 'api/v1/stories/clearstoryassignees',
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
					url: 'api/v1/stories/removeuser',
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
			
			function populateStoryPopup(id){
				$.ajax({
	        		url: 'api/v1/stories/'+id,
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
	        			if(stories != null && stories.length > 0){
							stories = stories[0];
							editStory = stories;
							edit = true;
							render_add_story_tab( stories );
							change_cntrl_buttons();
							
	        				/*var stpriority = "<div class='option'><div class='color p"+stories.priority+"'></div><div class=label data-value=1>Priority "+stories.priority+"</div></div>";
	        				$("#st-priority").html(stpriority);
	        				var stsprint = "";
	        				if(stories.sprint_id == null){
	        					stsprint = "Added to Project Backlog";
	        				}else{
	        					stsprint = "Sprint "+stories.sprint_id.id;
	        				}
	        				$("#st-sprint").html(stsprint);
	        				$(".st_edit_story").attr('id',id);
	        				var userObj = userObject[stories.creator];
	        				
	        				var createdDate = new Date(stories.creationDate);
	        				var createdOn = createdDate.format("mmm dd");
	        				createdOn +=", "+createdDate.getHours()+":"+createdDate.getMinutes();
	        				$("#st-creator").html('<img title="'+userObj.fullname+'" src="'+qontextHostUrl+''+userObj.avatarurl+'"/>');
	        				$("#st-createdOn").html(createdOn);*/
	       /* 			var assign = '';
	        				var post_data="storyId="+id+"&stage="+stories.status;
	        				$.ajax({
	        					url : 'api/v1/stories/getusers',
	        					type : 'POST',
	        					data : post_data,
	        					async : false,
	        					success : function(users) {
	    	        				if(users.length > 0){
	    	        					for(var i=0;i < users.length; i++){
	    	        						userObj = userObject[users[i].user.username];
	    	        						assign += '<div class="user"><img class="remove-user" alt="'+userObj.username+'" title="'+userObj.fullname+'" src="'+qontextHostUrl+''+userObj.avatarurl+'"/><span style="display:none;" class="remvUser" ></span></div>';
	    	        					}
	    	        				}else{
	    	        					assign = "<label style=\"margin:5px;\">No user assgined</labal>";
	    	        				}
	        					}
	        				});

	        				
	        				$("#st-assignees").html(assign);
	        				$("#st-assignees div.user").hover(function(){
	        					$(this).find('img').addClass('opaque_user');
	        					$(this).find('.remvUser').show();
	        				},function(){
	        					$(this).find('img').removeClass('opaque_user');
	        					$(this).find('.remvUser').hide();
	        				});
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
	        						people += '<div class="user"><img class="add-user" alt="'+user.username+'" title="'+user.fullname+'" src="'+qontextHostUrl+''+user.avatarurl+'"/></div>';
	        					}
	        				}else{
	        					people = "<label style=\"margin:5px;\">No people in the project</labal>";
	        				}
	        				$("#st-users").html(people);
	        				if(storyDetailScroll[id+"detail"]){
	        					var api = $('#story_details_section').data('jsp');
	        					if(api)api.destroy();
	        				}
	        				storyDetailScroll[id+"detail"] = $('#story_details_section').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
	     	*/			}
	        			
	        		},
	        		error: function(data) { },
	        		complete: function(data) { }
	        	});
			}
			
			/*$('div.user').hover(function(){
				$(this).addClass('opaque_user');
				$(this).find('.remvUser').show();
			},function(){
				$(this).removeClass('opaque_user');
				$(this).find('.remvUser').hide();
			});*/
			
			$('ul#proj-user-list li').unbind('click').live("click",function(){
				if($(this).hasClass("selected")){
					$(this).removeClass("selected");
					$(this).css('opacity','1');
					users_arr.splice($(this).attr('id').indexOf(),1);
				}else{
					$(this).addClass("selected");
					$(this).css('opacity','0.2');
					users_arr.push($(this).attr('id'));
				}
			});
			
			
			function populateUserDetails(){
				$('ul#selected_user').find('li').each(function(){
					$(this).remove();
				})
				$('ul#selected_user input').val("");
				$('.popup-proj-cont').show();
				$('#addusers').autocomplete({
					source:[]
	    		}).data( "autocomplete" )._renderItem = function( ul, item ) {
					var el = $( "<li></li>" ).data( "item.autocomplete", item );
					el.append( "<a>" + item.value + " </a>" );
	    			return el.appendTo( ul );
				}
			}
			
			$('#addusers').live('keydown',function(){
				$('.no_match').hide();
				var userSource = [];
				var callApi = true;
				delay(function(){
					var query=$('#addusers').val();
					for(var i in localStorage){
						if (query.toLowerCase().indexOf(i) !== -1){
							var retrievedObject = localStorage.getItem(i);
							userSource = JSON.parse(retrievedObject);
							callApi = false;
							return false;
						}else{
							callApi = true;
						}
					}
					if(callApi && query!=""){
						//var post_data = "sortType="+query+"&showTotalCount=false&startIndex=0&count=20";
						var post_data = {"sortType":query,"showTotalCount":false,"startIndex":0,"count":20};
		    			$('.suggestion').show();
						$.ajax({
							url : 'api/v1/users/searchqontext/',
							type : 'POST',
							data : post_data,
							success: function( data ) {
								var data_obj = $.parseJSON(data);
								var apiVersion = data_obj.success.headers["api-version"];
								if(data_obj.success.body.objects){
									var user_obj = data_obj.success.body.objects;
									for(var i=0;i<user_obj.length;i++){
										if(!user_obj[i].basicInfo.avatarUrl){
											user_obj[i].basicInfo.avatarUrl = "/portal/st/"+apiVersion+"/profile/defaultUser.gif"; //place default url here.
				        				}
										userSource[userSource.length] = {value:user_obj[i].basicInfo.fullName,type:user_obj[i].accountId,avatarurl:user_obj[i].basicInfo.avatarUrl,displayname:user_obj[i].basicInfo.displayName,emailid:user_obj[i].basicInfo.userId};
									}
									if (localStorage)  {
										localStorage.setItem(query, JSON.stringify(userSource));
									}
									$('.suggestion').hide();
									$("#addusers").autocomplete( "option", "source", userSource).autocomplete( "search" , query );
									$("#addusers").unbind('autocompleteselect').bind('autocompleteselect', function(event, ui) { 
										var str = "<li id='"+ui.item.type+"' data-displayname='"+ui.item.displayname+"' data-emailid='"+ui.item.emailid+"' data-avatarurl='"+ui.item.avatarurl+"' class='selectedUser' ><p>"+ui.item.value+"</p><a href='javascript:void(0);'class='user_remove' /></li>";
										$('#selected_user input').val("");
										$('#selected_user input').focus();
										$('#selected_user input').before(str);
										$('.user_remove').unbind('click').bind('click',function(){
											$(this).closest('li').remove();
										});
									});
								}else{
									$('.suggestion').hide();
									$('.no_match').show();
								}
							}
						});
					}else{
						$("#addusers").autocomplete( "option", "source", userSource).autocomplete( "search" , query );
						$("#addusers").unbind('autocompleteselect').bind('autocompleteselect', function(event, ui) { 
							var str = "<li id='"+ui.item.type+"' data-displayname='"+ui.item.displayname+"' data-emailid='"+ui.item.emailid+"' data-avatarurl='"+ui.item.avatarurl+"' class='selectedUser' ><p>"+ui.item.value+"</p><a href='javascript:void(0);'class='user_remove' /></li>";
							$('#selected_user input').val("");
							$('#selected_user input').focus();
							$('#selected_user input').before(str);
							$('.user_remove').unbind('click').bind('click',function(){
								$(this).closest('li').remove();
							});
						});
					}
	    			
				},1000);
			});
			
			$('#selected_user').unbind('click').bind('click',function(){
				$(this).find('input').focus();
			});
			
			$('#popup_proj_user_done').unbind('click').live('click',function(){
				$('#add_ppl_info').hide();
				$('ul#selected_user').find('li').each(function(){
					var userDetails = {};
					userDetails.username = $(this).attr('id');
					userDetails.fullname = $(this).find('p').html();
					userDetails.avatarurl = $(this).attr('data-avatarurl');
					userDetails.displayname =$(this).attr('data-displayname');
					userDetails.emailid = $(this).attr('data-emailid');
					var success = addUser(userDetails);
					if(success ==true){
						populateProjectDetails();
						$('.popup-proj-cont').hide();
					}
				})
			});
			
			
			function constructPagination(element,perPage,currentPage){
				var el = element;
				var paginationCtrl =el.prev('.sprintInfoBand').find('.pageCtrls');
				el.sweetPagesDestroy();
				paginationCtrl.html("");
				el.sweetPages({perPage:perPage,curPage:currentPage});
				var controls = el.find('.swControls').detach();
				controls.appendTo(paginationCtrl); 
			}
			
			
			//expand and collapse left panel
			$('.left_pane a').unbind('click').live('click',function(){
				var pagingEl = $('.col:visible');
				if($(this).hasClass('collapse')){
					perPage = 4;
					left_pane_collapsed = true;
					//$('section.left').hide("slide","fast",function(){
					$('section.left').stop().animate({'margin-left':-$('section.left').width()},"slow",function(){
						$('section.left_collapsed').show();
						$('section.right').css('width','96%');
						if($('section.right li.stages:visible').length > 3){
							$('section.right li.stages:visible').css('width','24.9%');
						}
						constructPagination(pagingEl,perPage,0);
					});
				}else{
					perPage = 3;
					left_pane_collapsed = false;
					//$('section.left_collapsed').hide("slide","fast",function(){
					$('section.left_collapsed').hide("fast",function(){
						$('section.left').stop().animate({'margin-left':0});
						$('section.right').css('width','72%');
						$('section.right li.stages:visible').css('width','33.2%');
						constructPagination(pagingEl,perPage,0);
						
					});
				}
			});
			
			$("#addPeople").unbind('click').live('click', function(){
				populateUserDetails(userIndex,false);
				$('.story-popup').hide();
				//$('.popup-proj-cont .c-box-content ul').jScrollPane();
										
			});
			$(".adduser").unbind('click').live('click',function(){
				$(this).css('background', 'url("themes/images/ajax-loader.gif") no-repeat');
				var el = $(this);
				setTimeout(function(){
					var id = (el.attr("id"));
					var userDetails = {};
					userDetails.username = id;
					userDetails.fullname = el.prev('.details').find('label.name').html();
					userDetails.avatarurl = el.closest('li').find('img').attr('url');
					userDetails.displayname = el.prev('.details').find('label.name').html();
					userDetails.emailid = el.prev('.details').find('a.email').html();
					var success = addUser(userDetails);
					if(success ==true){
						populateProjectDetails();
						el.parent().hide();
					//	populateUserDetails(userIndex,false);
					}
				},1000);
				
			});
        
			
			$(document).live("click",function(event){
				// if that user assign story popup is visible, hide it on click of anywhere outside the popup
				var el = event.target;
				if($(el).closest('.popup-story-cont').length === 0){
					$('.popup-story-cont').hide();
				}
				//if custom select of priority is open, close it on click anywhere outside the popup
				if($(el).closest('.custom-select').length === 0){
					if($(this).find('ul.option-list').is(':visible')){
						$(this).find('ul.option-list').hide();
					}
				}
				/*//add user popup close on clicking anywhere outside popup.
				if($(el).closest('.popup-proj-cont').length == 0 && $(el).closest('#addPeople').length == 0 && $(el).closest('#removePeople').length == 0){
					$('.popup-proj-cont').hide();
				}*/
				
				//add story popup close on clicking anywhere outside popup.
				if($(el).closest('.story-popup').length == 0 && $(el).closest('#addStory').length == 0 && ($(el).closest('#popup_proj_done').length == 0)){
					$('.story-popup').hide();
				}
				
				//edit/create sprint popup close on clicking anywhere outside popup
				if($(el).closest('.sprint-popup').length == 0 && !($(el).hasClass('editSprint')) &&  $(el).closest('.ui-datepicker-calendar').length == 0 && !($(el).hasClass('new_sprint'))){
					$('.sprint-popup').hide();
				}
				
				//task status popup close on clicking anywhere outside popup
				if($(el).closest('.todo-status-list').length == 0 && $(el).closest('.todo-status a').length == 0){
					$('.todo-status-list').hide();
				}
				
				//task sort popup close on clicking anywhere outside popup
				if($(el).closest('#sort-list').length == 0 && $(el).closest('.task_sort a').length == 0){
					$('#sort-list').hide();
				}
			});
			
			
			$('#popup_proj_done').unbind('click').live("click",function(){
           	 $('.popup-proj-cont').hide();
           	 userIndex = 0;
           	 if(firstVisit){
           		 $('.story-popup').show();
           		 firstVisit = false;
           	 }
            }); 
			
			$("#removePeople").unbind('click').live('click', function(){
					 if(addUserScroll){
						var api = $('.popup-proj-cont .c-box-content ul').data('jsp');
						if(api)api.destroy();
					} 
					var records = users;
					$('.popup-proj-cont').hide();
					var users_html="";
					for(var i=0;i<records.length;i++){
						if(records[i].username != creator){
							users_html += '<li><img src="'+qontextHostUrl+records[i].avatarurl+'"/></div><div class="details"><label class="name">'+records[i].fullname+'</label><a class="email">'+records[i].emailid+'</a></div><div style="float:left;" class="removeUser float-rgt disable" id="'+records[i].username+'"></div></li>';
						}
					}
					$('.popup-proj-cont .c-box-head').html("Remove people from the project");
					$('.popup-proj-cont .c-box-content input').attr('id',"searchRmvUser");
					$('#searchRmvUser').val("");
					$('#searchRmvUser').next().removeClass().addClass('search-input').css('background','url("themes/images/search.jpg") no-repeat');;
					$('.popup-proj-cont .c-box-content ul').html(users_html);
					$('.popup-proj-cont').show();
					addUserScroll = $('.popup-proj-cont .c-box-content ul').jScrollPane();
				
					$(".removeUser").unbind('click').live('click',function(){
						$(this).css('background', 'url("themes/images/ajax-loader.gif") no-repeat');
						var el = $(this);
						setTimeout(function(){
							var id = (el.attr("id"));
							if(id != creator){
							var success = removeUser(id);
								if(success == true){
									populateProjectDetails();
									el.parent().hide();
								}
							}
						},1000);
					
				});
				
			});
			
        	
            $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
            $(window).resize(function() {
                $('.bg-pat').css({'height': (($(window).height()) - 40) + 'px'});
            });
            
            $('.right').css({'height': (($(window).height()) - 40) + 'px'});
            $(window).resize(function() {
                $('.right').css({'height': (($(window).height()) - 40) + 'px'});
            });

            $('.main').css({'width': (($(window).width())) + 'px'});
            $(window).resize(function() {
                $('.main').css({'width': (($(window).width())) + 'px'});
            });
            
            populateProjectDetails();
            populateUnassignedStories('');
            //populateUserDetails();
            if(firstVisit){
            	populateUserDetails(userIndex,false);
            }
            
			
			// Comments Section
	       /* 	populateCommentingUserDetails();
	       	populateStoryComments();
	       	populateStoryTodos(); */
	       	// Ends Here
			
            if(project_view ==1){
     	       	populateSprints();
				$("#sprint-view").hide();
				$("#project-view").show();
				$('#pstat-view').hide();
				
				//var selectAllLbl = $(".view-hd div");				
				//selectAllLbl.addClass("tabHolder");
				//$('.projectview').parent().removeClass("tabHolder");
				$('.projectview').addClass('active_tab');
				$('.projectview img').show();
				$("#project_footer").show();
				$('#sprint_footer').hide();
				$('#task_footer').hide();
			}else{
     	       	populateSprintStories(current_sprint);
				$("#sprint-view").show();
				$("#project-view").hide();
				$('#pstat-view').hide();
				$('#task_footer').hide();
				
				/*var selectAllLbl = $(".view-hd div");				
				selectAllLbl.addClass("tabHolder");
				$('.sprintview').parent().removeClass("tabHolder");*/
				$('.sprintview').addClass('active_tab');
				$('.sprintview img').show();
				$("#project_footer").hide();
				$('#sprint_footer').show();
			}
            
        	$(".currentSprint").unbind('click').live('click', function(){
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
        	

        	
        	/* $('#story_form').submit(function(){
        		var title = $('input[name=stTitle]');
        		var description = "No Description";
        		var priority = $('select[name=stPriority]');
        		var user = "<%= username %>";
        		var projectId= <%= projectId%>;
        		var post_data = 'stTitle=' +title.val() + '&projectId='+projectId+'&stDescription=' + description + '&stPriority=' + priority.val() + '&user=' +user;
        		$.ajax({
        			url: 'restapi/stories/create',
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
        	}); */
        	var storyFormValid = true;
        	$('input[name=storyTitle],textarea[name=storyDesc]').focus(function(){
    			$(this).css('background-color','#FFFFFF !important');
    			//$('.story_error').html(" ");
    		});
        	
        	$('input[name=storyTitle]').blur(function(){
    			if($(this).val().length > storyTitleLimit){
    				$(this).css('background-color','#FFD3D3 !important');
    				$('.story_error').html("Title can't exceed "+storyTitleLimit+" chars");
        			storyFormValid = false;
        		}else {
        			$(this).css('background-color','#FFFFFF !important');
        			$('.story_error').html(" ");
        			storyFormValid = true;
        		}
    		});
        	
        	$('textarea[name=storyDesc]').blur(function(){
    			if($(this).val().length > storyDescLimit){
    				$(this).css('background-color','#FFD3D3 !important');
    				$('.story_error').html("Desc can't exceed "+storyDescLimit+" chars");
        			storyFormValid = false;
        		}else {
        			$(this).css('background-color','#FFFFFF !important');
        			$('.story_error').html(" ");
        			storyFormValid = true;
        		}
    		});
        	
        	
        	/*$('#addAnotherStory,#createStory').unbind('click').live("click",function(){
        		var title = $('input[name=storyTitle]');
        		var description = $('textarea[name=storyDesc]');
        		if(!storyFormValid)
        			return;
        		var priority = $('.story-popup .custom-select .option .label').attr('data-value');
        		var sprint = $('select[name=stSprint]');
        		if(title.val()==""){
        			return;
        		}
        		var user = userLogged;
        		var post_data = 'stTitle=' +title.val() + '&projectId='+projectId+'&stDescription=' + description.val() + '&stPriority=' + priority + '&user=' +user + '&stSprint=' + sprint.val()+'&storyTags={tags:[1,2,3]}';
        		$.ajax({
        			url: 'api/v1/stories/create',
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
        			$('input[name=storyTitle]').val("");
        			$('textarea[name=storyDesc]').val("");
        			var select = $('select[name=stSprint]');
        			select.val(jQuery('options:first', select).val());
        			//$('.story-popup .custom-select .option').html('<div class="color p1"></div><div class="label" data-value="1">Priority 1</div></div>');
        		}else{
        			$('.story-popup').hide();
        		}
        		
        	});*/
        	
        	$(".sptRmv").unbind('click').live('click', function(){
        		 var id = $(this).parent().attr("id");
        		id = id.replace("st","");
        		addtoCurrentSprint(id,0);
        		var ulElement = $(this).parent().closest('ul');
        		$(this).parent().hide("fade","slow");
        		var c = parseInt($("#sprint_total").html());
        		if(c > 0){
	        		$("#sprint_total").html(c - 1);
        		}
        		if($(this).parent().parent().attr("id") == "finished"){
        			var f = parseInt($("#sprint_finished").html());
            		if(f > 0){
    	        		$("#sprint_finished").html(f - 1);
            		}
        		}
        		setTimeout(function(){ //execute with a delay inorder to show the fade effect
        			if(ulElement.find('li:visible').length > 0){
    					var ulHeight = (ulElement.find('li').outerHeight() * ulElement.find('li:visible').length) + 150;
    					ulElement.css({'height': ulHeight + 'px'});
    				}else {
    					ulElement.css({'height': '150px'});
    				}
        			if(sprintStageScroll[ulElement.attr('id')]) {
		   				sprintStageScroll[ulElement.attr('id')].destroy();
	   					sprintStageScroll[ulElement.attr('id')] =ulElement.closest('.sprintCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
	   				} 
        			if(projStageScroll[ulElement.attr('id')]) {
        				projStageScroll[ulElement.attr('id')].destroy();
        				projStageScroll[ulElement.attr('id')] =ulElement.closest('.projectCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
	   				} 
        			populateUnassignedStories('');
        			//commented this out as i feel it is unnecessary to populate content. On refresh, it will populate anyways.
            		/* if(project_view ==1){
           				populateSprints();
    				}else{
   	     	       		populateSprintStories(sprintinview);
    				}  */
        		},1000);
        		
        	});
        	
        	$(".strRmv").unbind('click').live('click', function(){
        		
        		customAlert({ message: {'text':'Do you want to remove this story?'} },removeStoryCallback,$(this)); 
        		
        	});
        	
        	function removeStoryCallback(handlerResponse,curObj) {

    			if(handlerResponse){
            		var id = curObj.parent().attr("id");
            		id = id.replace("st","");
            		curObj.parent().hide("fade","slow");
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
    			}
    		 }
        	
        	$("#backlog_seach_input").keydown(function(event) {
    			if (event.which == 13) {
    				event.preventDefault();
    			}
    			var el = $(this);
    			var selector = $('#storyList').find('ul.story');
    			setTimeout(function(){
    				for(var i=0;i<backlogSearchSource.length;i++){
        				if(backlogSearchSource[i].type == "Search Backlog" && backlogentry){
        					backlogSearchSource[i].value = $("#backlog_seach_input").val();
        				}
        			}
        			if(!backlogentry){
        				var current_entry = {value:$("#backlog_seach_input").val(),type:"Search Backlog"};
        				backlogSearchSource.push(current_entry);
        				backlogentry = true;
        			}
        			$( "#backlog_seach_input" ).autocomplete( "option", "source",backlogSearchSource );
        			$( "#backlog_seach_input" ).bind( "autocompleteselect", function(event, ui) {
        				  var query=ui.item.value;
        				  if(ui.item.type == "Search Backlog"){
        					  $("#backlog_seach_input").val(ui.item.value);
        					  el.next().addClass('close-BacklogSearch').attr('src',"themes/images/close.png");
        					  $(selector).find('li').each(
    	        					function() {
    	        						($(this).find('p').text()
    	        								.search(new RegExp(query, "i")) < 0) ? $(this)
    	        								.hide() : $(this).show();
    	        			 });
        				  }else if(ui.item.type == "Priority"){
        					  $("#backlog_seach_input").val(ui.item.value+":"+ui.item.type);
        					  el.next().addClass('close-BacklogSearch').attr('src',"themes/images/close.png");
        					  $(selector).find('li').each(
    	        					function() {
    	        						($(this).attr('data-priority')
    	        								.search(new RegExp(query, "i")) < 0) ? $(this)
    	        								.hide() : $(this).show();
    	        			 });
        					  
        				  }else if(ui.item.type == "Size"){
        					  $("#backlog_seach_input").val(ui.item.value+":"+ui.item.type);
        					  el.next().addClass('close-BacklogSearch').attr('src',"themes/images/close.png");
        					  $(selector).find('li').each(
    	        					function() {
    	        						($(this).attr('data-size')
    	        								.search(new RegExp(query, "i")) < 0) ? $(this)
    	        								.hide() : $(this).show();
    	        			 });
        					  
        				  }else if(ui.item.type == "tag"){
        					  //todo:
        				  }
        			});
    			},1);
    			
    			$('.close-BacklogSearch').unbind('click').live('click',function(){
					el.val("");
	        		el.next().attr('src',"themes/images/search.png");
	        		 $(selector).find('li').each(function(){
	        			$(this).show();
	        		}); 
    			});
    		});
        	
        	$("#searchRmvUser").unbind('keyup').live('keyup',function(event) {
    			if (event.which == 13) {
    				event.preventDefault();
    			}
    			var el = $(this);
    			el.next().css('background','url("themes/images/ajax-loader.gif") no-repeat');
    			var query=$('#searchRmvUser').val();
    			var selector = $('.popup-proj-cont .c-box .c-box-content').find('ul');
    			//query = query.replace(/ /gi, '|'); //add OR for regex query  

    			$(selector).find('li').each(
    					function() {
    						($(this).find('label').text()
    								.search(new RegExp(query, "i")) < 0) ? $(this)
    								.hide() : $(this).show();
    					});
    			el.next().addClass('close-Rmvsearch').css('background','url("themes/images/close.png") no-repeat');
				$('.close-Rmvsearch').unbind('click').live('click',function(){
					el.val("");
	        		el.next().removeClass('close-Rmvsearch').css('background','url("themes/images/search.jpg") no-repeat');
	        		 $(selector).find('li').each(function(){
	        			$(this).show();
	        		}); 
    			});
        	});
        	
    		$("#searchUser").unbind('click').live("keyup",function(event) {
    			if (event.which == 13 ||event.which == 8) {
    				event.preventDefault();
    			}
    			
    			var el = $(this);
    			el.next().css('background','url("themes/images/ajax-loader.gif") no-repeat');
    			delay(function(){
    				var query=$('#searchUser').val();
        			var post_data = "sortType="+query+"&showTotalCount=false&startIndex=0&count=20";
    				$.ajax({
						url : 'api/v1/users/searchqontext/',
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
    							for(var i=0;i<total_users.length;i++){
    								
    								//alert(total_users[i].basicInfo.displayName);
    								if(!total_users[i].basicInfo.avatarUrl){
    			    					total_users[i].basicInfo.avatarUrl = "/portal/st/"+apiVersion+"/profile/defaultUser.gif"; //place default url here.
    			    				}
    								users_html += '<li><img url="'+total_users[i].basicInfo.avatarUrl+'" src="'+qontextHostUrl+total_users[i].basicInfo.avatarUrl+'"/></div><div class="details"><label class="name">'+total_users[i].basicInfo.displayName+'</label><a class="email">'+total_users[i].basicInfo.primaryEmail+'</a></div><div style="float:left;" class="adduser float-rgt enable" id="'+total_users[i].accountId+'"></div></li>';
    								
    							}
    								
								if(addUserScroll){
									var api = $('.popup-proj-cont .c-box-content ul').data('jsp');
									if(api){
										api.getContentPane().html(users_html);
										$('.popup-proj-cont .c-box-content ul').unbind();
										api.reinitialise();
									}
								}else{
									$('.popup-proj-cont .c-box-content ul').html(users_html);
								}
    						}else{
    							//if(addUserScroll)addUserScroll.destroy();
    							if(addUserScroll){
									var api = $('.popup-proj-cont .c-box-content ul').data('jsp');
									if(api){
										api.getContentPane().html("No users found for the query..");	
										api.reinitialise();
									}
								}else {
    								$('.popup-proj-cont .c-box-content ul').html("No users found for the query..");
								}
    						}
							el.next().addClass('close-search').css('background','url("themes/images/close.png") no-repeat');
							$('.close-search').unbind('click').live('click',function(){
								el.val("");
				        		populateUserDetails(userIndex,false);
				        		$('.close-search').die();
				        		el.next().removeClass('close-search').css('background','url("themes/images/search.jpg") no-repeat');
				        	});
						}
					});    			
    			},2000);
    			//$('#searchUser').die();		    		
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
        	
        	
			/*$(".stAddmore").unbind('click').live('click', function(){
				 if($(".stAddmore").html() == "Add Members"){
					$(".stAddmore").html("Hide Members");
					$("#stPeople").show();
					
				}else{
					$("#stPeople").hide();
					$(".stAddmore").html("Add Members");
				} 
				 var id=$('#st_edit_story').attr('id');
				 if(storyDetailScroll[id+"detail"]){
 					var api = $('#story_details_section').data('jsp');
 					if(api)api.destroy();
 				}
 				storyDetailScroll[id+"detail"] = $('#story_details_section').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
			});
			*/
			function setStoryId (storyId){
				// Comment Section				
				//$('<input>').attr({type: 'hidden', id: 'current_story_id',value: storyId }).appendTo('form');
				$("#current_story_id").val(storyId);
				// Ends Here
			}
			
			/*$(".viewStory, .moreStory").unbind('click').live('click', function(){
				
				$("#add_story_popup").show();
				$("#custom_overlay").fadeIn('slow');
				
				var id = $(this).closest('li').attr("id");
        		id = id.replace("st","");
        		var stageId = $(this).closest('ul').attr('id');
        		populateStoryPopup(id);
			
        		setStoryId(id);
        		populateCommentingUserDetails();
    	       	populateStoryComments();
    	       	populateStoryTodos();
				$(".add-user").unbind('click').live('click', function(){
					var eid = $(this).attr("alt");
					var eid_arr = [eid];
					addUserToStory(eid_arr, id,stageId);
					populateStoryPopup(id);	
				});
				
				$(".remvUser").unbind('click').live('click', function(){
					var eid = $(this).parent().find('img').attr("alt");
					removeUserFromStory(eid, id,stageId);
					populateStoryPopup(id);
				});
				viewStoryFancyBox();
			}); */
			
			$('.detailview').unbind('click').live('click',function(){
				var ulHeight=$(this).closest('ul').height();
				if($(this).hasClass('expandStory')){
					$(this).closest('li').find('.meta').show();
					$(this).closest('li').find('.story_desc').show();
					ulHeight += $(this).closest('li').find('.meta').outerHeight();
					$(this).closest('li').find('p').css('padding-bottom','0px');
					$(this).removeClass('expandStory').addClass('collapseStory');
				}else{
					$(this).closest('li').find('.meta').hide();
					$(this).closest('li').find('.story_desc').hide();
					ulHeight -= $(this).closest('li').find('.meta').outerHeight();
					$(this).closest('li').find('p').css('padding-bottom','10px');
					$(this).removeClass('collapseStory').addClass('expandStory');
				}
				$(this).closest('ul').css({'height': ulHeight + 'px'});
				if(storyListScroll){
  					 var api = $('#storyList').data('jsp');
  					 if(api)api.destroy();
  				 	}
       			storyListScroll = $('#storyList').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
       			if(sprintStageScroll[$(this).closest('ul').attr('id')]) {
	   				sprintStageScroll[$(this).closest('ul').attr('id')].destroy();
   				}
       			if($(this).closest('.sprintCont').length > 0){
       				sprintStageScroll[$(this).closest('ul').attr('id')] =$(this).closest('.sprintCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
       			}
       			if(projStageScroll[$(this).closest('ul').attr('id')]) {
       				projStageScroll[$(this).closest('ul').attr('id')].destroy();
   				}
       			if($(this).closest('.projectCont').length > 0){
       				projStageScroll[$(this).closest('ul').attr('id')] =$(this).closest('.projectCont').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
       			}
			});
			
			$(".projectview").unbind('click').live('click',function(){
				project_view = 1;
				$('.sprints').hide();
		       
				$(".project_tab div").removeClass('active_tab');				
				$(".project_tab img").hide();
				$('.projectview').addClass('active_tab');
				$('.projectview img').show();
				$("#project_footer").show();
				$('#sprint_footer').hide();
				$('#task_footer').hide();
				
				$("#sprint-view").hide();
				$("#pstat-view").hide();
				$("#project-view").show();

				populateSprints();
				viewStoryFancyBox();
			});
			
			$(".sprintview ").unbind('click').live('click',function(){
				project_view = 0;
				$('.sprints').show();
				
				$(".project_tab div").removeClass('active_tab');				
				$(".project_tab img").hide();
				$('.sprintview').addClass('active_tab');
				$('.sprintview img').show();
				$("#project_footer").hide();
				$('#sprint_footer').show();
				$('#task_footer').hide();
				
				$("#project-view").hide();
				$("#pstat-view").hide();
				$("#sprint-view").show();
				

				populateSprintStories(current_sprint);
				viewStoryFancyBox();
			});
			
			$(".projectstatview").unbind('click').live('click',function(){
				project_view = 0;
				
				$('.sprints').hide();
				
				$(".project_tab div").removeClass('active_tab');				
				$(".project_tab img").hide();
				$('.projectstatview').addClass('active_tab');
				$('.projectstatview img').show();
				$("#project_footer").hide();
				$('#sprint_footer').hide();
				$('#task_footer').show();
				
				$("#sprint-view").hide();
				$("#project-view").hide();
				$("#pstat-view").show();
				populateProjectStatistics();
				viewStoryFancyBox();
			});
			$(".project_chart").unbind('click').live('click',function(){
				project_view = 0;
				
				$('.sprints').hide();
				
				$(".project_tab div").removeClass('active_tab');				
				$(".project_tab img").hide();
				$('.project_chart').addClass('active_tab');
				$('.project_chart img').show();
				
				$("#sprint-view").hide();
				$("#project-view").hide();
				$("#pstat-view").hide();

				viewStoryFancyBox();
			});
			$(".project_customize").unbind('click').live('click',function(){
				project_view = 0;
				
				$('.sprints').hide();
				
				$(".project_tab div").removeClass('active_tab');				
				$(".project_tab img").hide();
				$('.project_customize').addClass('active_tab');
				$('.project_customize img').show();
				
				$("#sprint-view").hide();
				$("#project-view").hide();
				$("#pstat-view").hide();

				viewStoryFancyBox();
			});
			
			
			$(".peopleview").unbind('click').live('click',function(){
				$(".peopleview").addClass("active_tab");
				$(".backlogview").removeClass("active_tab");
				$(".backlogview img").hide();
				$(".peopleview img").show();
				$('#backlogview').hide();
				$('#peopleview').show();
			});
			
			$(".backlogview").unbind('click').live('click',function(){
				$(".backlogview").addClass("active_tab");
				$(".peopleview").removeClass("active_tab");
				$(".peopleview img").hide();
				$(".backlogview img").show();
				$('#backlogview').show();
				$('#peopleview').hide();
			});
			
			//$('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
           // $(window).resize(function() {
           //     $('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
            //});
            
          
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

         // Task Code starts here
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
         
         $('#add_task').unbind('click').live("click",function(){
        	 var user = userLogged;
        	 var story_id = $("#current_story_id").val();	
        	 var todoText = $("#task_content").val();                       	  	 
			 var milestonePeriod = $("#taskMilestone").val();		
			 var taskUser = $("#taskUser").val();
			 if(todoText == "") return;   
			 todoText = parsedString(todoText);
  	       	 var taskHtml = '';
             var post_data = 'content='+ todoText+'&storyid='+story_id+'&timeInDays='+milestonePeriod+'&assigneeId='+taskUser+'&milestonePeriod='+milestonePeriod+'&user='+user;                     
             $.ajax({
      			url: 'api/v1/todo/create',
      			type: 'POST',
      			data: post_data,
      			async:false,
      			success: function( todo ) {  
      				todo = todo[0];
      				var count = parseInt($('#todos-count').val());
      				count = count+1;
      				$("#task_content").val("");  
      				$('#taskMilestone').val('1');	
      				$('#taskUser').val('0');	
      				//todoHtml = '<li id="todo'+todo.pkey+'" class="todo-list"><div class="todo-img"><img class="todo-user" title="'+todo.user.fullname+'" src="'+qontextHostUrl+todo.user.avatarurl+'"></img></div><div class="todo-content" ><div id="todoText">'+todo.content+'</div><div id="todoMilestone"><label style="font-weight:none;">'+todo.milestonePeriod+' Milestone Period | </label><div class="todo-status"><label style="color:#1e9ce8;" class="todo-status-text">CREATED</label><a href="javascript:void(0);" ></a></div></div></div><div class="todo-action"><a id=tedit'+todo.pkey+' class="cmtEditTodo ctedit" href="javascript:void(0);"></a><a id=tdelete'+todo.pkey+' class="cmtRmvTodo ctremove" href="javascript:void(0)"></a></div></li>';
      				var imageUrl= "";
      				var user_fullName="";
      				var task_status = "";
      				if(todo.user == null){
      					imageUrl = "themes/images/notyetassigned_withoutarrow.png";
      					user_fullName = "Not yet assigned";
      				}else{
      					imageUrl = qontextHostUrl+todo.user.avatarurl;
      					user_fullName = todo.user.fullname;
      				}
      				if(todo.status == "IN_PROGRESS"){
      					task_status = "IN PROGRESS";
      				}else if(todo.status == "NOT_YET_ASSIGNED"){
      					task_status="NOT YET ASSIGNED";
      				}else{
      					task_status=todo.status;
      				}
      				taskObj = {task:todo,imageUrl:imageUrl,mileStoneType:mileStoneType,statusColor:taskStatusColors[todo.status],fullName:user_fullName,task_status:task_status};
      				taskHtml += new EJS({url: 'ejs/task_template.ejs'}).render(taskObj);
      				$("#task_content").focus();  
      			//	$('#story_task_view ul').append(taskHtml);
      				if(storyDetailScroll[story_id+"task"]){
						var api = $('#story_task_view').data('jsp');
						if(api){
							api.getContentPane().find('ul.todo-total-display').append(taskHtml);
							api.reinitialise();
						}
					}else{
						$('#story_task_view ul').append(taskHtml);
						storyDetailScroll[story_id+"task"] = $('#story_task_view').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
					}
      			},
      			error: function(data) { },
     			complete: function(data) { }            		
              	});
        
             	return false;
             });

         function deleteStoryTodo(todoID,storyId){

     		$.ajax({
       			url: 'api/v1/todo/delete/'+todoID,
       			type: 'GET',
       			async:false,
       			success: function( result ) {
       				populateStoryTodos(storyId);							                  			
       			},
       			error: function(data) { },
      			complete: function(data) { }            		
               	 });          	
         }
         
         function populate_task_selects(){
        	 var id = $("#current_story_id").val();		
				//populate user assignees drop down
				var taskUserHtml = '<option value="0">--Assign User--</option>';
				for(var i=0;i<users.length;i++){
					taskUserHtml +='<option value="'+users[i].username+'">'+users[i].fullname+'</option>';
				}
				$('#taskUser').html(taskUserHtml);
				//populateMilestone drop down
				var taskMileStoneHtml="";
				for(var i=1;i<=projectPreferences.mileStoneRange;i++){
					if(projectPreferences.mileStoneType == 0){
						taskMileStoneHtml +='<option value="'+i+'">'+i+' hour milestone</option>';
					}else{
						taskMileStoneHtml +='<option value="'+i+'">'+i+' day milestone</option>';
					}
				}
				
				$('#taskMilestone').html(taskMileStoneHtml);
				//populate task status drop down
 	       	var status_dropdown = '<ul class="todo-status-list">';
 	       	for(var i in taskStatusColors){
 	       		if(i !== "NOT_YET_ASSIGNED"){
 	       			status_dropdown +='<li><label style="color:'+taskStatusColors[i]+'">'+i+'</label></li>';
 	       		}
 	       	}
 	       	status_dropdown +="</ul>";
 	       	$('#story_task_view').prepend(status_dropdown);
         }

         function populateStoryTodos(id){		
				var taskHtml = '';								
				$.ajax({
					url : 'api/v1/todo/story/'+id,
					type : 'GET',
					async : false,					
					success :function(todos){
						if (todos != null){		
	        				if(todos.length > 0){
		        				for (var i=0;i<todos.length;i++){
		        					todo = todos[i];	
		        					var imageUrl= "";
		              				var user_fullName="";
		              				var task_status = "";
		              				if(todo.user == null){
		              					imageUrl = "themes/images/notyetassigned_withoutarrow.png";
		              					user_fullName = "Not yet assigned";
		              				}else{
		              					imageUrl = qontextHostUrl+todo.user.avatarurl;
		              					user_fullName = todo.user.fullname;
		              				}
		              				if(todo.status == "IN_PROGRESS"){
		              					task_status = "IN PROGRESS";
		              				}else if(todo.status == "NOT_YET_ASSIGNED"){
		              					task_status="NOT YET ASSIGNED";
		              				}else{
		              					task_status=todo.status;
		              				}
		        					taskObj = {task:todo,imageUrl:imageUrl,mileStoneType:mileStoneType,statusColor:taskStatusColors[todo.status],fullName:user_fullName,task_status:task_status};
		              				taskHtml += new EJS({url: 'ejs/task_template.ejs'}).render(taskObj);
		        					//todosHtml += '<li id="todo'+todo.pkey+'" class="todo-list"><div class="todo-img"><img class="todo-user" title="'+todo.user.fullname+'" src="'+qontextHostUrl+todo.user.avatarurl+'"></img></div><div class="todo-content" ><div id="todoText">'+todo.content+'</div><div id="todoMilestone"><label style="font-weight:none;">'+todo.milestonePeriod+' Milestone Period | </label><div class="todo-status" ><label style="color:'+taskStatusColors[todo.status]+';" class="todo-status-text">'+todo.status+'</label><a href="javascript:void(0);" ></a></div></div></div><div class="todo-action"><a id=tedit'+todo.pkey+' class="cmtEditTodo ctedit" href="javascript:void(0);"></a><a id=tdelete'+todo.pkey+' class="cmtRmvTodo ctremove" href="javascript:void(0)"></a></div></li>';
			        				}
		        			}			        				        			        			
						}else{
								taskHtml += 'No Todos for this story';
							}
						
						$("ul.todo-total-display").html(taskHtml);
						$('#tabs_addstory').bind('tabsshow',function(event,ui){
				        	 if(storyDetailScroll[id+"task"]){
									var api = $('#story_task_view').data('jsp');
									if(api)api.destroy();
								}
							 storyDetailScroll[id+"task"] = $('#story_task_view').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
				     		
				     	});
						
					},
					error : function(data){},
					complete : function(data){}					
					});	
			}	
         
       /*  $('#addStory').unbind('click').live("click",function(){
        	 $('.popup-proj-cont').hide();
        	 $('input[name=storyTitle]').val("");
 			$('textarea[name=storyDesc]').val("");
 			var select = $('select[name=stSprint]');
 			select.val(jQuery('options:first', select).val());
 			//$('.story-popup .custom-select .option').html('<div class="color p1"></div><div class="label" data-value="1">Priority 1</div></div>');
        	 $('.story-popup').show();
         });*/
         
         $('#popup_cancel').unbind('click').live("click",function(){
        	 $('.popup-story-cont').hide();
         }); 
         
         $('#popup_user_cancel,.user_close').unbind('click').live("click",function(){
        	 $('.popup-proj-cont').hide();
         }); 
         
         $('#popup_story_cancel').unbind('click').live("click",function(){
        	 $('.story-popup').hide();
         }); 
         
         $('#storyClose').unbind('click').live("click",function(){
        	 $('.story-popup').hide();
         }); 
         
		
         
        
         $('.custom-select').bind('click',function(){
             if($(this).find('ul.option-list').is(':visible')){
            	 $(this).find('ul.option-list').slideUp();
             }else{
            	 $(this).find('ul.option-list').slideDown();
             }
             

         }).delegate('ul.option-list li','click',function(){
             var c = $(this).parents('.custom-select');
             c.find('>.option').replaceWith($(this).html());
             c.find('>ul.option-list').slideUp();
             return false;
         });
         
         
         $('.ctedit').unbind('click').live("click",function(){
        	 var taskId = $(this).attr('id').split("tedit")[1];
        	 $.ajax({
       			url: 'api/v1/todo/'+taskId,
       			type: 'GET',
       			async:false,
       			success: function( todo ) {
       				todo = todo[0];
           			$('#task_content').val(todo.content);
           			$('#taskMilestone').val(todo.milestonePeriod);
           			if(todo.user !== null){
           				$('#taskUser').val(todo.user.username);
           			}
           			$('#add_task').attr("value","Update").attr("id","tupdate").attr('data-task',taskId).attr('data-status',todo.status);;
           			$('#task_content').parent().css("width","69%");
       			},
       			error: function(data) { },
      			complete: function(data) { }            		
               	});
        	 
         });
         
         $('#tupdate').unbind('click').live("click",function(){
        	 var taskHtml="";
        	 var user = userLogged;
        	 var status= $(this).attr("data-status");
        	 var story_id = $("#current_story_id").val();	
        	 var todoText = $("#task_content").val();                       	  	 
			 var milestonePeriod = $("#taskMilestone").val();		
			 var taskUser = $("#taskUser").val();
			 if(todoText == "") return;   
			 todoText = parsedString(todoText);
			 var task_id = $(this).attr("data-task");
             var post_data = 'content='+ todoText+'&timeInDays='+milestonePeriod+'&assigneeId='+taskUser+'&status='+status+'&user='+user;                     
             $.ajax({
      			url: 'api/v1/todo/update/'+task_id,
      			type: 'POST',
      			data: post_data,
      			async:false,
      			success: function( todo ) {  
      				todo = todo[0];
      				$('#task_content').val('');
      				$('#taskMilestone').val('1');	
      				$('#taskUser').val('0');	
      				$('#add_task').attr("value","Add").attr("id","tAdd").attr('data-task',task_id).attr('data-status',todo.status);
      				var imageUrl= "";
      				var user_fullName="";
      				var task_status = "";
      				if(todo.user == null){
      					imageUrl = "themes/images/notyetassigned_withoutarrow.png";
      					user_fullName = "Not yet assigned";
      				}else{
      					imageUrl = qontextHostUrl+todo.user.avatarurl;
      					user_fullName = todo.user.fullname;
      				}
      				if(todo.status == "IN_PROGRESS"){
      					task_status = "IN PROGRESS";
      				}else if(todo.status == "NOT_YET_ASSIGNED"){
      					task_status="NOT YET ASSIGNED";
      				}else{
      					task_status=todo.status;
      				}
					taskObj = {task:todo,imageUrl:imageUrl,mileStoneType:mileStoneType,statusColor:taskStatusColors[todo.status],fullName:user_fullName,task_status:task_status};
      				taskHtml += new EJS({url: 'ejs/task_template.ejs'}).render(taskObj);
      				//var todoUpdateHtml = '<div class="todo-img"><img class="todo-user" title="'+todo.user.fullname+'" src="'+qontextHostUrl+todo.user.avatarurl+'"></img></div><div class="todo-content" ><div id="todoText">'+todo.content+'</div><div id="todoMilestone"><label style="font-weight:none;">'+todo.milestonePeriod+' Milestone Period | </label><div class="todo-status"><label style="color:'+taskStatusColors[todo.status]+';" class="todo-status-text">'+todo.status+'</label><a href="javascript:void(0);" ></a></div></div></div><div class="todo-action"><a id=tedit'+todo.pkey+' class="cmtEditTodo ctedit" href="javascript:void(0);"></a><a id=tdelete'+todo.pkey+' class="cmtRmvTodo ctremove" href="javascript:void(0)"></a></div>';
      				$("#task_content").focus();  
      				$('#story_task_view ul').find('li#task'+task_id).replaceWith(taskHtml);
      			},
      			error: function(data) { },
     			complete: function(data) { }            		
              	});
         });
         
         $(".cmtRmvTodo").unbind('click').live('click',function(){        		
          	var todoID = $(this).attr("id").split("tdelete")[1];
          	var storyId = $('#current_story_id').val();
          	deleteStoryTodo(todoID,storyId);
          	//if edit was clicked and nothing was done.
          	$('#add_task').attr("value","Add").attr("id","tAdd").attr('data-task',todoID);
          	});
         
         $('#story_task_view .todo-status a').unbind('click').live('click',function(event){
        	 var id = $(this).closest('li.todo-list').attr('id').split("task")[1];
        	 $(this).closest('#story_task_view').find('ul.todo-status-list').attr('id','status-'+id);
             if($(this).closest('#story_task_view').find('ul.todo-status-list').is(':visible')){
            	 $(this).closest('#story_task_view').find('ul.todo-status-list').slideUp();
             }else{
            	 $(this).closest('#story_task_view').find('ul.todo-status-list').css('left',($(this).position().left - 50)+'px');
            	 $(this).closest('#story_task_view').find('ul.todo-status-list').css('top',($(this).position().top + 20 + $(this).closest('.jspPane').position().top)+'px');
            	 $(this).closest('#story_task_view').find('ul.todo-status-list').slideDown();
             }
             

         });
         
         $('#story_task_view ul.todo-status-list li').unbind('click').live('click',function(event){
        	 var user = userLogged;
             var task_id = $(this).closest('ul').attr('id').split("status-")[1];
             var c = $('#task'+task_id).find('.todo-status');
             var status = $(this).find('label').html();
             var post_data = '&status='+status;
             var el = $(this);
             $.ajax({
       			url: 'api/v1/todo/update/'+task_id,
       			type: 'POST',
       			data: post_data,
       			async:false,
       			success: function( todo ) {  
       				c.find('label').html($(el).html()).addClass('todo-status-text');
                    $(el).closest('#story_task_view').find('ul.todo-status-list').hide();
       			},
       			error: function(data) { },
      			complete: function(data) { }            		
               	});
             
             return false;
         });
         
         var dates = $( "#sprint_start, #sprint_end" ).datepicker({
     		defaultDate: "+1w",
     		changeMonth: true,
     		numberOfMonths: 1,
     		onSelect: function( selectedDate ) {
     		var option = this.id == "sprint_start" ? "minDate" : "maxDate",
     		instance = $( this ).data( "datepicker" ),
     		date = $.datepicker.parseDate(
     		instance.settings.dateFormat ||
     		$.datepicker._defaults.dateFormat,
     		selectedDate, instance.settings );
     		dates.not( this ).datepicker( "option", option, date );
     		}
     	}); 
         
         $('ul.col li.stages .header a.editSprint').unbind('click').live("click",function(){
    		 var sprint_id = $(this).closest('li.stages').find('ul.story').attr('id').split('sp')[1];
    		 $('.sprint-popup .c-box .c-box-head').html("Sprint"+sprint_id+" Details");
    		 var top = $(this).position().top + 50;
    		 var left = $(this).position().left + 30;
    		 var contentWidth = parseInt($('div.content').css('width'));
    		 var popupWidth = parseInt($('.sprint-popup').css('width'));
    		 if(left + popupWidth > contentWidth){
    			 $('.sprint-popup').find('div#pointerEl').removeClass('pointer').addClass('pointer-rgt');
    			 var current_left = left - popupWidth - 50;
				 $('.sprint-popup').css('top',top);
				 $('.sprint-popup').css('left',current_left);
    		 }else {
    			 $('.sprint-popup').find('div#pointerEl').removeClass('pointer-rgt').addClass('pointer');
    			 $('.sprint-popup').css({'top':top,'left':left});
    		 }
    		 $('.sprint-popup').find('#createSprint').addClass("edit");
    		 $('.sprint-popup').find('#createSprint').attr('data-id',sprint_id);
    		 $.ajax({
	        		url: 'api/v1/sprints/'+sprint_id+'/project/'+projectId,
	        		type: 'GET',
	        		async:false,
	        		success: function( result ) {
	        			var sprint_startdate = new Date(result[0].startdate);
	        			var sprintStart = sprint_startdate.format("mm/dd/yyyy");
	        			$('.sprint-popup').find('#sprint_start').datepicker("setDate", sprintStart );
	        			var sprint_enddate = new Date(result[0].enddate);
	        			var sprintEnd = sprint_enddate.format("mm/dd/yyyy");
	        			$('.sprint-popup').find('#sprint_end').datepicker("setDate", sprintEnd );
	        		}
    		 });
    		 $('.sprint-popup').show();
         });
         
         $('.new_sprint').unbind('click').live("click",function(){
    		 $('.sprint-popup .c-box .c-box-head').html("Add New Sprint");
    		 var top = $(this).position().top + 15;
    		 var left = $(this).position().left + 100;
    		 $('.sprint-popup').css({'top':top,'left':left});
    		 $('.sprint-popup').find('#createSprint').removeClass("edit");
    		 $('.sprint-popup').show();
         });
         
         $('#createSprint').unbind('click').live("click",function(){
        	 if($(this).hasClass('edit')){
        		 //edit sprint 
        		 var sprint_id = $(this).attr('data-id');
        		 var start_date = $('.sprint-popup').find('input[name=sprintStart]');
        		 var end_date =  $('.sprint-popup').find('input[name=sprintEnd]');
        		 var post_data = 'sprintId='+sprint_id+'&start_date='+start_date.val()+'&end_date='+end_date.val()+'&projectId='+projectId;
        		 $.ajax({
        			url: 'api/v1/sprints/update',
        			type: 'POST',
        			data: post_data,
        			async:false,
        			success: function() { 
        				populateProjectDetails();
        				populateSprints();
        				populateSprintStories(sprint_id);
        				$('.sprint-popup').hide();
        			},
        			error: function(data) { },
       			    complete: function(data) { }            		
                });
        	 }else{
        		 //create sprint
        		 var start_date = $('.sprint-popup').find('input[name=sprintStart]');
        		 var end_date =  $('.sprint-popup').find('input[name=sprintEnd]');
        		 var post_data = 'start_date='+start_date.val()+'&end_date='+end_date.val()+'&projectId='+projectId;
        		 $.ajax({
        			url: 'api/v1/sprints/create',
        			type: 'POST',
        			data: post_data,
        			async:false,
        			success: function() { 
        				populateProjectDetails();
        				populateSprints();
        				$('.sprint-popup').hide();
        			},
        			error: function(data) { },
       			    complete: function(data) { }            		
                });
        	 }
         });
         
         $('#popup_sprint_cancel, .sprint_close').unbind('click').live("click",function(){
        	 $('.sprint-popup').hide();
         });
         
         /***************************************/
         
         	$("ul.proj_ppl_list li").live('mouseover',function() {
        	 		$(this).find("img.remove_mem[id != '"+userLogged+"']").css("display","inline");
        	  	}).live('mouseout',function(){
        		  	$(this).find("img.remove_mem").css("display","none");
        	});
         	

			$(".remove_mem").unbind('click').live('click',function(){
				$(this).parent().css('background', 'url("themes/images/ajax-loader.gif") no-repeat 50% 50% #fff');
				var el = $(this);
				setTimeout(function(){
					var id = (el.attr("id"));
					if(id != creator){
					var success = removeUser(id);
						if(success == true){
							populateProjectDetails();
							el.parent().hide();
						}
					}
				},1000);
			
		});
         /***************************************/
	 	$('ul.story li').unbind('hover').live('hover',function(){
			$(this).find('a.viewStory').toggle();
			$(this).find('a.remove').toggle();
		})
			
         /*************** ADD STORY, TASK, DISCUSSION ************************/
	  	
		populate_task_selects();
	 	
		$('#addStory').unbind("click").live('click',function(){
			edit = false;
			$("#add_story_popup").show();
			$("#custom_overlay").fadeIn('slow');
    		$('#createStory').show();
    		$('#addAnotherStory').show();
			$('#updateStory').hide();
			render_add_story_tab( false );

        	  		
		});
	 	
        $('#popup_story_cancel_btn,.addStory_user_close').unbind('click').live("click",function(){
       	 	$('#add_story_popup').hide();
       	 	$("#custom_overlay").fadeOut('slow');
        }); 
		
		function render_proj_title( editStory_details ){
			var title, desp = "";
			if( editStory_details ){
				title = editStory_details.title;
				desp = editStory_details.description;
			}
			$('input#sty_title_input').val(title);
 			$('textarea#sty_desp_input').val(desp);	
		}
		

		function render_add_story_tab( editStory_detail_Json ){

			
			$('#tabs_addstory').tabs({
				selected: 0 ,
			    select: function(event, ui ) {
			    		if( edit || ui.panel.id == "tabs_story") {
			    			return true;
			    		}else{
			    			customAlert( { message: {'text':'You can not create task / discussion without creating a story.'},btnStyle: {'confirm' : 'false'}  }  );
			    			return false;
			    		}
			    }
			});
			
    		$('#createStory').show();
    		$('#addAnotherStory').show();
			$('#updateStory').hide();
			render_proj_title( editStory_detail_Json );
			render_proj_priority( editStory_detail_Json );
			render_proj_pref( editStory_detail_Json );
			render_proj_sprint( editStory_detail_Json );
			render_proj_assignees( editStory_detail_Json );
			render_proj_tags( editStory_detail_Json );
		}
		
		function render_proj_priority( editStory_details ){
			
			var optionsHtml = "";
			for(priority in projectPriorities){
				var curPriority = projectPriorities[priority];
				optionsHtml += '<li><div class="option"><div class="color" style="background-color:'+curPriority.color+'" ></div><div class="label" data-rank="'+curPriority.rank+'" data-pkey="'+curPriority.pkey+'" >'+curPriority.description+'</div></div></li>';
			}
			$('#story_details_container .option-list').html(optionsHtml);
	        
			var c = $('#story_details_container .option-list :first-child').parents('.custom-select');
	        c.find('>.option').replaceWith($("#story_details_container .option-list :first-child").html());	       
	        
			if( editStory_details ) {
				c.find('>.option').replaceWith(  $('div[data-pkey="'+editStory_details.priority.pkey+'"]').parent().parent().html() );
			} 
			 c.find('>ul.option-list').slideUp();
		}
		
		function render_proj_pref( editStory_details ){			
			createSlider_sty_size( editStory_details );
		}
		
		function render_proj_sprint( editStory_details ){  //populate story popup sprint select box
						
			var optionsHtml = '<option selected="selected" value="0">Add story to Project Backlog</option>';
        	 for(var cnt=1; cnt <= totalsprints; cnt++){
        		 optionsHtml +='<option value="'+cnt+'" class="sprint_options" >Sprint '+cnt+'</option>';
        	 }
        	 $('#story_details_container #storySprint').html(optionsHtml);
        	 
        	 if( editStory_details ){
        		 $('#story_details_container #storySprint').val( editStory_details.sprintNo );
        	 }
		}
		
		function render_proj_assignees( editStory_details ){
			$('#mem_list_holder').html("");
			if( editStory_details && editStory_details.assignees ){
				var assignee = "<ul>";
				for(var cnt = 0; cnt < editStory_details.assignees.length ; cnt++) {
					
					assignee += "<li class='padding3' >'"+ editStory_details.assignees[cnt].fullname+"'</li>";
				}
				assignee += "</ul>"
				$('#mem_list_holder').html(assignee);
			}

		}
		
	  	function createSlider_sty_size( editStory_details ){
	  		
	  		var sizes, lowEnd , highEnd , new_Size_Array = [];
	  		var value = projectPreferences.storyPointType;
	  		if( value == 0 ){
	  			 sizes = ["1","2","4","8","16","32"];
	  			 lowEnd = parseInt ( sizes[projectPreferences.storySizeLowRangeIndex] );
	  			 highEnd = parseInt ( sizes[projectPreferences.storySizeHighRangeIndex] );    	  			  
	  		} else if( value == 1 ) {
	  			 sizes = ["1","2","3","5","8","13","21","34","45"];
	  			 lowEnd = sizes[projectPreferences.storySizeLowRangeIndex];
	  			 highEnd = sizes[projectPreferences.storySizeHighRangeIndex];
	  		} else {
	  			 sizes = ["XS", "S", "M", "L", "XL", "XXL", "XXXL"];
	  			 lowEnd = sizes[projectPreferences.storySizeLowRangeIndex];
	  			 highEnd = sizes[projectPreferences.storySizeHighRangeIndex];
	  		}
	  			 for ( var cnt = 0; cnt < sizes.length ; cnt++ ){
  				 var curValue = parseInt ( sizes[cnt] );
  				 if( curValue >= lowEnd  && curValue <= highEnd) {
  					new_Size_Array.push( curValue );
  				}
  			 }
  			 $($("#sty_size_indicator").find('sup')[0]).html( lowEnd );
	  			 $($("#sty_size_indicator").find('sup')[1]).html( highEnd );
	  			   		 
	  		$( "#sty_slider-range" ).slider({
			  		  min: 0,
			  		  max: new_Size_Array.length - 1,
			  		  step: 1,
			  		  range: 'min',
			  		  value: projectPreferences.storyPointLimit,
			  		  slide: function(event, ui) {
			  		    $("#sty_amount_slider").val(new_Size_Array[ui.value]);
			  		  }
			  });
	  		
	  		if( editStory_details ) {
	  			 $("#sty_amount_slider").val( editStory_details.storyPoint );
	  		} else {
	  			 $("#sty_amount_slider").val(new_Size_Array[new_Size_Array.length - 1]);
	  		}
	  	}
	  	
	  	function render_proj_tags( editStory_details ){
	  		if( editStory_details ){
		  		if ( editStory_details.storyTag.length < 1  || editStory_details.storyTag[0] == "" ) {
		  			$("#tag_list_holder ul").html("");
		  		} else {
		  			$("#tag_list_holder ul").html("");
		  			for ( var cnt = 0; cnt < editStory_details.storyTag.length ; cnt++ ) {
		  				var str = "<li class='newTags_li' data-tagValue = '"+editStory_details.storyTag[ cnt ]+"'  ><p>"+editStory_details.storyTag[ cnt ]+"</p><a href='javascript:void(0);'class='tag_remove' /></li>";
		  				$("#tag_list_holder ul").append(str);
	  				}
		  		}
	  		} else {
	  			$("#tag_list_holder ul").html("");
	  		}
	  	}
	  	
	  	$('#add_tags').keyup(function(e) {	  	
	  		$('span.error-keyup-2').remove();
	  		var inputVal = $(this).val();
	  	    var characterReg = /^\s*[a-zA-Z0-9,\s]+\s*$/;
	  	    if(!characterReg.test(inputVal)) {
	  	        $(this).after('<span class="error error-keyup-2">No special characters allowed.</span>');
	  	        return;
	  	    }
	  	    if(e.keyCode == 13) {
	  	    	var str = "<li class='newTags_li' data-tagValue = '"+inputVal+"' ><p>"+inputVal+"</p><a href='javascript:void(0);'class='tag_remove' /></li>";
		  		$("#tag_list_holder ul").append(str);
		  		$(this).val("");
	  	    }	  		
	  		
	  	});

	  	$(".tag_remove").unbind('click').live('click',function(){
	  		$(this).closest('li').remove();
		});
	  	
	  	
    	$('#addAnotherStory,#createStory,#save_continue_sty,#updateStory').unbind('click').live("click",function(){
    		
    		var title = $('input#sty_title_input');
    		var description = $('textarea#sty_desp_input');
    		if(!storyFormValid)
    			return;
    		
    		if(title.val()==""){
    			return;
    		}
    		
    		var newly_created_story;

    		//var newObject = jQuery.extend(true, {}, oldObject); /**  Deep copy  **/
    		var new_story_Json = jQuery.extend(true, {}, story_JSON);  
    		update_story_details( new_story_Json );
    		var post_data = "story="+JSON.stringify(new_story_Json);
    		
    		if( $(this).attr('id') == "updateStory" ){
	    		$.ajax({
	    			url: 'api/v1/stories/update',
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
    		}else {
	    		$.ajax({
	    			url: 'api/v1/stories/create',
	    			type: 'POST',
	    			data: post_data,
	    			async:false,
	    			success: function( result ) {
	    				newly_created_story = result[0];
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
    		}
    		if($(this).attr('id') == "addAnotherStory"){
    			title.val("");
    			description.val("");
    			edit = false;
    			var select = $('#story_details_container select[name=stSprint]');
    			select.val(jQuery('options:first', select).val());
    			$('.add_tag_container').find('ul li').remove();
    			//<div class="option"><div class="color" style="background-color:#7395DC"></div><div class="label" data-rank="1" data-pkey="2">Priority 2</div></div>
    			$('section#story_details_container').find('#story_detail_custom').find('div:first').replaceWith('<div class="option"><div class="color" style="background-color:#FC5F5F"></div><div class="label" data-rank="0" data-pkey="1">Priority 1</div></div>');
    			$('ul.todo-total-display').find('li').remove();
    			$("#task_content").val("");  
  				$('#taskMilestone').val('1');	
  				$('#taskUser').val('0');	
    			//$('.story-popup .custom-select .option').html('<div class="color p1"></div><div class="label" data-value="1">Priority 1</div></div>');
    			$("#tabs_addstory").tabs({ selected: 0 });
    		} else if ( $(this).attr('id') == "save_continue_sty" ){
    			edit = true;
    				editStory = newly_created_story;
					render_add_story_tab( newly_created_story );
					//change_cntrl_buttons(); //todo:check with paridhi
					setStoryId(newly_created_story.pkey);
	    			$("#tabs_addstory").tabs({ selected: 1 });
    		}else{
	       	 	$('#add_story_popup').hide();
	       	 	$("#custom_overlay").fadeOut('slow');
    		}
    		
    	});
    	

    	function update_story_details(new_sty_json){
    		
    		new_sty_json.story[0].stTitle = $('input#sty_title_input').val();
    		new_sty_json.story[0].stDescription  = $('textarea#sty_desp_input').val();
    		new_sty_json.story[0].user = userLogged;
    		get_sty_tags( new_sty_json );
    		new_sty_json.story[0].storyPointSize = $("#sty_amount_slider").val();
    		new_sty_json.story[0].stPriority = $('#story_details_container .custom-select .option .label').attr('data-pkey');
    		new_sty_json.story[0].projectId = projectId;
    		new_sty_json.story[0].stSprint = $('#story_details_container select[name=stSprint]').val();
    		new_sty_json.story[0].storyId =  ( editStory )? editStory.pkey : 0;
    	}
    	
    	function get_sty_tags( new_sty_json ){
    		
    		var pjt_tags_list =  $("#tag_list_holder ul li");
    		
    		if( pjt_tags_list.length == 0) {
    			new_sty_json.story[0].storyTags = [];
    		} 
    		for(var cnt=0 ; cnt < pjt_tags_list.length; cnt++ ){
    			
    			var tagObj = {"tagName": $( pjt_tags_list[cnt] ).attr("data-tagValue") };
    			new_sty_json.story[0].storyTags.push( tagObj );
    		} 
    		
    	}
    	
    	$("#story_members, #story_edit").bind('click').live('click',function(){
    		var id = $(this).closest('li').attr("id");
    		edit_story_popup( id );
    		
    		$("#tabs_addstory").tabs({ selected: 0 });
    	});
    	$("#story_tasks").bind('click').live('click',function(){
    		var id = $(this).closest('li').attr("id");
    		edit_story_popup( id );
    		$("#tabs_addstory").tabs({ selected: 1 });
    	});
    	$("#story_discussions").bind('click').live('click',function(){
    		var id = $(this).closest('li').attr("id");
    		edit_story_popup( id );
    		$("#tabs_addstory").tabs({ selected: 2 });
    	});
    	
		$(".viewStory, .moreStory").unbind('click').live('click', function(){			
			var id = $(this).closest('li').attr("id");
			edit_story_popup( id );
		});
		
		function edit_story_popup( id ){
			$("#add_story_popup").show();
			$("#custom_overlay").fadeIn('slow');
    		id = id.replace("st","");
    		populateStoryPopup(id);	
    		populateStoryTodos(id);
    		setStoryId(id);
		}
		
    	function change_cntrl_buttons(){
    		$('#createStory').hide();
    		$('#addAnotherStory').hide();
    		$('#updateStory').show();
    	}
    	
		var story_JSON = {'story':[{
							'stTitle':'',
							'stDescription':'',
							'user':'',
							'storyTags':[],
							'storyPointSize':'',
							'stPriority':0,
							'projectId':0,
							'stSprint':0,
							'storyId' : 0 
							}]
						 };
	  	
});
