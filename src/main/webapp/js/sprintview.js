$(document).ready(function() {
        	var userIndex = 0;
        	var current_sprint = 0;
        	var users_arr = [];
        	var sprintinview = 0;
        	var totalsprints = 0;
        	var creator;
        	var projectLanes = new Object();
        	var projectPriorities = new Object();
        	var users = null;
        	var userObject = new Object();
        	var creatorObj = new Object();
        	var storyObj = new Object();
        	var duration = '';
        	var projectStatus = "";
        	var storyListScroll = null;
        	var sprintStageScroll = new Object();
        	var projStageScroll = new Object();
        	var addUserScroll = null;
        	var storyDetailScroll = new Object();
        	
    		$(document).ajaxError(function(e, jqxhr, settings, exception) {
					window.location.href="auth.action";    			
    		});
    		    		
       	
        	function populateProjectDetails(){
        		$.ajax({
            		url: 'api/v1/projects/'+projectId,
            		type: 'GET',
            		async:false,
            		success: function( project ) {
            			if(project != null && project.length > 0){
            				project = project[0];
            				projectStatus = project.status;
            				projectLanes = project.projectLanes;
            				projectPriorities = project.projectPriorities;
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
            				//populate story popup sprint select box
            				var optionsHtml = '<option selected="selected" value="0">Add story to Project Backlog</option>';
            	        	 for(var i=1;i<=totalsprints;i++){
            	        		 optionsHtml +='<option value="'+i+'">Sprint '+i+'</option>';
            	        	 }
            	        	 $('.story-popup #storySprint').html(optionsHtml);
            				$("#people").html('');
            				if(users != null && users.length > 0){
            					for(var i=0;i< users.length;i++){
            						$("#people").append('<li><img title="'+users[i].fullname+'" src="'+qontextHostUrl+users[i].avatarurl+'" /></li>');
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
            			}
            		},
            		error: function(data) { },
            		complete: function(data) { }
            	});
        	}
			function populateUnassignedStories(name){
				// $('#storyList ul').css({'height': (($(window).height()) - 320) + 'px'});
				 $('#storyList').css({'height': (($(window).height()) - 300) + 'px'});
	        	 $.ajax({
	        		url: 'api/v1/stories/backlog/'+projectId,
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
        				var story_unassigned = '';
	        			if(stories != null && stories.length){
	        				for(var i=0;i<stories.length;i++){
	        					var story = stories[i];
	        					creatorObj = userObject[story.creator];
	        					var userObj = userObject[story.creator];
	        					storyObj = {story:story,backlog:true,image:null}
	        					story_unassigned += new EJS({url: 'ejs/story_template.ejs'}).render(storyObj);
	        				}
	        			}else{
	        				story_unassigned += '<b>No pending stories for the project</b>';
	        				//$('#storyList ul').css({'height': (($(window).height()) - 250) + 'px'});
	        			}
        				$("#storyList ul").html(story_unassigned);
        				if($('#storyList ul').find('li').length > 0){
        					var ulHeight = ($('#storyList ul').find('li').outerHeight() * $('#storyList ul').find('li').length) + 150;
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
        	    					var ulHeight = ($('#storyList ul').find('li').outerHeight() * $('#storyList ul').find('li').length) + 150;
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
								if(sprints[0].project.start_date != null){
									var startdate = new Date(sprints[0].project.start_date);
									startdate = startdate.format("mmm dd");
									duration += startdate;
								}else{
									duration += 'No Start Date';
								}
								if(sprints[0].project.end_date != null){
									var enddate = new Date(sprints[0].project.end_date);
									enddate = enddate.format("mmm dd");
									duration += '&nbsp;&nbsp;-&nbsp;&nbsp;'+ enddate;
								}else{
									duration += ' - No End Date';
								}

								var status = 'Completed:<span id="project_finished" class="finished">0</span> Total: <span id="project_total" class="total">0</span>';
								$.ajax({
					        		url: 'api/v1/projects/storycount/'+projectId,
					        		type: 'GET',
					        		async:false,
					        		success: function( result ) {
					        			result = $.parseJSON(result);
										status = 'Stories Completed: <span id="project_finished" class="finished">'+result.result+'</span>Total: <span id="project_total" class="total">'+result.result+'</span>';
					        		}
								});
								$('.duration-hd label.duration').html(duration+'&nbsp;&nbsp; | <b>Sprint'+current_sprint+"</b> &nbsp;&nbsp;"+sprints[0].project.status+'&nbsp;&nbsp;|&nbsp;&nbsp;'+status+'&nbsp;&nbsp;| <a href="javascript:void(0);" class="new_sprint" >Add New Sprint</a>');
								var finished = 0;
				        		var sprint_html = '<ul id="holderforpage" class="col">';
			        			$("ul.col li").css("width",100/sprints.length+'%');
			        			for(var k=0; k<sprints.length;k++ ){
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
						        					/*var imageHTML = "";
						        					if(story.assignees.length > 0){							        					
							        					for(var j=0;j<story.assignees.length;j++){
							        						if(j==2){
																imageHTML+="<a class='moreStory' href='#story-cont'>more</a>";
																//imageHTML+="<label>.....</label>";
																break;
															}
															imageHTML+="<img height='26' width='26' class='' src='"+qontextHostUrl+""+story.assignees[j].avatarurl+"' title='"+story.assignees[j].fullname+"'>";
														}							        					
							        					//sprint_html +=  '<li id="st'+story.pkey+'" class="p'+story.priority+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont">'+imageHTML+'</div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
							        				}*/
						        					storyObj = {story:story,backlog:false,image:null};
						        					//sprint_html += '<li id="st'+story.pkey+'" class="p'+story.priority+'"><p class="p'+story.priority+'">'+story.title+'</p><div class="meta "><div class="img-cont"></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
						        					sprint_html += new EJS({url: 'ejs/story_template.ejs'}).render(storyObj);
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
			        				sprint_html += '</ul></div>';
			        				sprint_html +='</li>';
			        				
			        			}
			        			$("#project_finished").html(finished);

			        			sprint_html +='</ul>';
			        			$("#project-view").html(sprint_html);
			        	        $('ul.col li.stages .header').hover(function(){
			        	        	$(this).find('a.editSprint').show(); 
			        	         },function(){
			        	        	 $(this).find('a.editSprint').hide();
			        	         });
			        			$('#pageCtrls').html("");
			        			var current_page = current_sprint - 1;
			        			setTimeout(function(){
			        				$('#holderforpage').sweetPages({perPage:4,curPage:current_page});
				    				var controls = $('.swControls').detach();
			    					controls.appendTo('#pageCtrls'); 
			        			},1);
			        			//$( ".stages " ).jScrollPane({});
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
			        			
			        			$('.stages .projectCont ul').parent().css({'height': (($(window).height()) - 175) + 'px'});
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

	        	}); 
			}
			
			
			function showAddUserPopup(elOffset){
				users_arr = [];
				$('ul#proj-user-list li').each(function(){
					$(this).css('opacity','1');
					$(this).removeClass('selected');
				});
				var origPointerTop = parseInt($('.popup-story-cont').find('div#pointerEl').css('top'));
				if($('.popup-story-cont').find('div#pointerEl').hasClass('pointer-rgt')){
					$('.popup-story-cont').find('div#pointerEl').removeClass('pointer-rgt').addClass('pointer');
				}
				var elTop = elOffset.top;
				var elLeft = elOffset.left;
				var new_left = elLeft+210;
				var popupWidth =parseInt($('.popup-story-cont').css('width'));
				var sectionWidth = parseInt($('section.right').css('width'))+parseInt($('section.right').css('padding-left'));
				var popupHeight =parseInt($('.popup-story-cont').css('height'));
				var sectionHeight = parseInt($('section.right').css('height'));
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
				if(elTop+popupHeight > sectionHeight){
					var new_top = elTop-popupHeight+40;
					$('.popup-story-cont').css('top',new_top);
					$('.popup-story-cont').css('left',new_left);
					$('.popup-story-cont').find('div#pointerEl').css('top',popupHeight-20);
				}else {
					$('.popup-story-cont').css('top',elTop);
					$('.popup-story-cont').css('left',new_left);
					$('.popup-story-cont').find('div#pointerEl').css('top','30px');
				}
				
				$('.popup-story-cont').show();
			}
			
			function populateSprintStories(sprint){
					var userObj='';
					 var sprintTitle = '';
	        		 if(totalsprints == 0){
	        			 sprintTitle = '<label>No Sprints available for this project.</label>';
		        	 }else{
		        		 sprintTitle = '<span class="sprintHead">Sprint '+sprint+' </span>';
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
		    						var status = 'Stories Completed: <span id="sprint_finished" class="finished">0</span> Total: <span id="sprint_total" class="total">0</span>';
		    						$('.duration-hd label.duration').html('&nbsp;&nbsp;'+duration+'&nbsp;&nbsp; |'+sprintTitle+" "+result.status+'&nbsp;&nbsp; |&nbsp;&nbsp;'+status);
		    						$('.duration-hd label.duration').show();
			        			}
			        		},
			        		error: function(data) { },
			        		complete: function(data) { }
			        	});
			        	
	        			var story_html = '<ul id="holderforpage" class="col">';
	        			//sort the array based on rank as it comes in random order.
	        			projectLanes.sort(function compareRank(a, b){
	        				return a.rank> b.rank? 1: -1;
						});	
						for(var i=0;i<projectLanes.length;i++){
        				story_html += '<li class="stages"><div class="header "><span></span>'+projectLanes[i].description+'</div><div class="sprintCont"><ul data-type="'+projectLanes[i].type+'" id="stage'+projectLanes[i].rank+'"class="story"></ul></div></li>';
	        			}
	        			story_html +='</ul>';
	        			$('#sprint-view').html(story_html);
	        			$('#pageCtrls').html("");
	        			setTimeout(function(){
	        				$('#holderforpage').sweetPages({perPage:4});
		    				var controls = $('.swControls').detach();
	    					controls.appendTo('#pageCtrls'); 
	        			},1);
	        			$('.stages ul:visible').parent().css({'height': (($(window).height()) - 175) + 'px'});
			        			
					
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
				        					var post_data="storyId="+story.pkey+"&stage="+story.status;
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
				        					});
				        					
				        					$('ul#stage'+story.status).append(str);
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
			        		   				if($(ui.item[0]).closest('ul').attr('data-type') == 'BACKLOG'){
			        		   					$(ui.item[0]).closest('ul').find("label#noStories").remove();
			        		   					var success = updateStoryStatus(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1],sprint); 
			        		   					$(ui.item[0]).find('.img-cont').html("");
			        		   					removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]);
			        		   				}else if(($(ui.item[0]).closest('section').hasClass('left'))) {
			        		   					var new_id = id.replace("st","");
			        		   					$(ui.item[0]).find('a.remove').removeClass('sptRmv').addClass('strRmv');
			        		   				//	removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]);
			        		   					addtoCurrentSprint(new_id, 0);
			        		   					populateUnassignedStories('');
			        		   				}else{
				        		   				if(projectStatus !="Not Started" && projectStatus !="Finished"){
					        		   				var success = updateStoryStatus(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1],sprint); 
					        		   				var elOffset = $(ui.item[0]).offset();
					        		   				if($(ui.item[0]).closest('ul').attr('data-type') == 'finished'){			        		   								        		   				
					        		   					removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]);
					        		   					refreshStoryPortlet(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1],creatorObj);
					        		   				}
					        		   				if((ui.sender != null) && !($(ui.item[0]).closest('ul').attr('data-type') == 'BACKLOG') && !($(ui.item[0]).closest('ul').attr('data-type') == 'FINISHED')&& !($(ui.item[0]).closest('section').hasClass('left'))){
					        		   					showAddUserPopup(elOffset);
					        		   					removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]);
					        		   					var existing_user_arr = [];
					        		   					if($('#'+id).data("userlist")){
					        		   						existing_user_arr = $('#'+id).data("userlist");
					        		   					}
					      		   						addUserToStory(existing_user_arr,id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]); 
					        		   					$('#popup_story_done').unbind('click').click(function(){
					        		   						if(users_arr.length > 0){
						        		   						removeUserFromStoryInStage(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]);
						        		   						$('#'+id).data("userlist", users_arr);  
							      		   						addUserToStory(users_arr,id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1]); 
						        		   						refreshStoryPortlet(id.split("st")[1],$(ui.item[0]).closest('ul').attr('id').split('stage')[1],creatorObj);
						        		   						$(this).closest('.popup-story-cont').hide();
						        		   						//$('#popup_story_done').die();
					        		   						}
					        		   					});
					        		   				}
					        		   				
					        		   				$(ui.item[0]).find('a.remove').removeClass('strRmv').addClass('sptRmv');
					        		   				if(success == false){
					        		   					$(this).sortable('cancel');
					        		   				}  
												 	var clss = ui.item.attr("class");
						        	   				 if(clss == "unassigned"){
						        	   					ui.item.removeClass("unassigned");
						        	   					var c = parseInt($("#sprint_total").html());
						        	   		        	$("#sprint_total").html(c + 1);
						        	   	        		if($(this).attr("id") == "finished"){
						        	   	        			var f = parseInt($("#sprint_finished").html());
						        	   	    	        	$("#sprint_finished").html(f + 1);
						        	   	        		}
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
			        				
				        			$('.stages ul:visible').parent().css({'height': (($(window).height()) - 175) + 'px'});
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
			
			/*function populateCurrentSprintStatus(){
				$.ajax({
	        		url: 'api/v1/stories/'+sprint+'/project/'+projectId,
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
	    						var status = '<span class="total">12</span><span class="finished">13</span>';
	    						$('.duration-hd label.duration').html(duration+'&nbsp;&nbsp;&nbsp;'+status);
	        					var userObj = userObject[story.creator];
	        					storyObj = { story:story, backlog:false, image: null};
	        					if(story.status == "notstarted"){
	        						story_unassigned += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+qontextHostUrl+''+userObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "dev"){
	        						story_dev        += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+qontextHostUrl+''+userObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "review"){
	        						story_review     += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+qontextHostUrl+''+userObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
	        					}else if(story.status == "finished"){
	        						story_finished   += '<li id="st'+story.pkey+'" class=""><p class="p'+story.priority+'">'+story.title+'</p><div class="meta"><div class="img-cont"><img src="'+qontextHostUrl+''+userObj.avatarurl+'" width="26" height="26" class=""/><label class="">Created by '+creatorObj.fullname+'</label></div></div><a href="javascript:void(0);" class="sptRmv remove"></a><a href="#story-cont" class="viewStory"></a></li>';
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
	        			//$('.stages ul').css({'height': (($(window).height()) - 180) + 'px'});
	        		},
	        		error: function(data) { },
	        		complete: function(data) { }
	        	});
			}*/
			
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
			
			function populateStoryAssignees(id){
				$.ajax({
	        		url: 'api/v1/stories/'+id,
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
	        			if(stories != null && stories.length > 0){
							stories = stories[0];
	        				$("#st-title").html(stories.title);
	        				$("#st-description").html(stories.description);
	        				var stpriority = "<div class='option'><div class='color p"+stories.priority+"'></div><div class=label data-value=1>Priority "+stories.priority+"</div></div>";
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
	        				$("#st-createdOn").html(createdOn);
	        				var assign = '';
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
	        			}
	        			
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
			
			function populateUserDetails(startIndex, isAppend){
				if(!isAppend){
					$('.popup-proj-cont').show();
					$('.popup-proj-cont .c-box-content .user-loading').show();
					$('.popup-proj-cont .c-box-content ul').hide();
					$('.popup-proj-cont .c-box-head').html("Add people to the project");
					$('.popup-proj-cont .c-box-content input').attr('id',"searchUser");
					$('#searchUser').val("");
					$('#searchUser').next().removeClass().addClass('search-input').css('background','url("themes/images/search.jpg") no-repeat');;
				}
				var post_data = "index="+startIndex+"&count=40";
				var _url = "api/v1/users/fetchusers";
				if(source == "qontext"){
					_url = "api/v1/users/fetchqontextusers";
				}
				setTimeout(function(){
					$.ajax({
        			url: _url,
        			type: 'POST',
        			data: post_data,
        			async:false,
        			success: function( records ) {
        				if(records != null){
        					var total_record='';
							if(source == "qontext"){
								total_record = $.parseJSON(records);
								records = total_record.success.body.objects;
							}
							if( records.length > 0){
								var total_users = records;
								for(var j=0;j<records.length;j++){
									for(var k=0;k<users.length;k++){
										if(records[j].ownerAccountId == users[k].username ){
											total_users.splice(j,1);
										}
									}
								}
								var users_html="";
								if(source == "qontext"){
									var apiVersion = total_record.success.headers["api-version"];
									for(var i=0;i<total_users.length;i++){
										if(!total_users[i].avatar){
				        					total_users[i].avatar = "/portal/st/"+apiVersion+"/profile/defaultUser.gif"; //place default url here.
				        				}
										users_html += '<li><img url="'+total_users[i].avatar+'" src="'+qontextHostUrl+total_users[i].avatar+'"/></div><div class="details"><label class="name">'+total_users[i].name+'</label><a class="email">'+total_users[i].profilePrimaryEmail+'</a></div><div style="float:left;" class="adduser float-rgt enable" id="'+total_users[i].ownerAccountId+'"></div></li>';
									}
								}else{
									for(var i=0;i<total_users.length;i++){
										users_html += '<li><img url="'+total_users[i].avatarurl+'" src="'+qontextHostUrl+total_users[i].avatarurl+'"/></div><div class="details"><label class="name">'+total_users[i].fullname+'</label><a class="email">'+total_users[i].emailid+'</a></div><div style="float:left;" class="adduser float-rgt enable" id="'+total_users[i].username+'"></div></li>';
									}
								}
								
								$('.popup-proj-cont .c-box-content .user-loading').hide();
								$('.popup-proj-cont .c-box-content ul').show();
								if(isAppend) {
									  $('.popup-proj-cont .c-box-content .more-load').hide();
									  $('.popup-proj-cont .c-box-content .jspContainer .jspPane').append(users_html);	
								  }else if(addUserScroll == null){
									  $('.popup-proj-cont .c-box-content ul').html(users_html);
								  }
								var atBottom = false;
							 	if(addUserScroll && !isAppend){
									var api = $('.popup-proj-cont .c-box-content ul').data('jsp');
									if(api){
										api.getContentPane().html(users_html);	
										$('.popup-proj-cont .c-box-content ul').bind('jsp-scroll-y',function(event, scrollPositionY, isAtTop, isAtBottom){
											if(isAtBottom & !atBottom){
												$('.popup-proj-cont .c-box-content .more-load').show();
												atBottom = true;
												userIndex += 1;
												var startIndex = (userIndex * 40) + 1;
												populateUserDetails(startIndex,true);
											}
										});
										api.reinitialise();
									}
								  }  else   {
									  
									  addUserScroll = $('.popup-proj-cont .c-box-content ul').bind('jsp-scroll-y',function(event, scrollPositionY, isAtTop, isAtBottom){
										if(isAtBottom & !atBottom){
											$('.popup-proj-cont .c-box-content .more-load').show();
											atBottom = true;
											userIndex += 1;
											var startIndex = (userIndex * 40) + 1;
											populateUserDetails(startIndex,true);
										}
									}).jScrollPane({'maintainPosition':true}).data().jsp;
								  }
        				}else{
        					if (isAppend){
								$('.popup-proj-cont .c-box-content .more-load').hide();
								//$('.popup-proj-cont .c-box-content .jspContainer .jspPane').append(users_html);	
							}else{
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
        				}
        			 }
					},
					failure :function(){
						},					
					complete : function(){}											
					});
				},1000);
				//	var users_html = "<ul>";
					
					/* if(firstVisit){
						$('.popup-proj-cont').show();
						$('.popup-proj-cont .c-box-content ul').jScrollPane();
					} */
					
					
				
			}
			
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
        
			
			$(document).unbind('click').live("click",function(event){
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
				//add user popup close on clicking anywhere outside popup.
				if($(el).closest('.popup-proj-cont').length == 0 && $(el).closest('#addPeople').length == 0 && $(el).closest('#removePeople').length == 0){
					$('.popup-proj-cont').hide();
				}
				
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

            $('.right').css({'width': (($(window).width()) - 300) + 'px'});
            $(window).resize(function() {
                $('.right').css({'width': (($(window).width()) - 300) + 'px'});
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
				$('.projectView').css('color',"#00475C");
		       	$('.projectView').parent().css('background-color',"#F6EEE1");
				$(".sprintview").css('color',"gray");
				$(".sprintview").parent().css('background-color',"#FFFFFF");
				$(".projectstatview").css('color',"gray");
				$(".projectstatview").parent().css('background-color',"#FFFFFF");
				$(".sprintHead").hide();
				$(".duration-hd").find('label.duration').show();
				$(".duration-hd").find('#pageCtrls').show();
				//$(".duration-hd").find('ul').hide();
				$(".duration-hd").find("#task_report").hide();
			}else{
     	       	populateSprintStories(current_sprint);
				$("#sprint-view").show();
				$("#project-view").hide();
				$('#pstat-view').hide();
				$('.sprintview').css('color',"#00475C");
		       	$('.sprintview').parent().css('background-color',"#F6EEE1");
				$(".projectview").css('color',"gray");
				$(".projectview").parent().css('background-color',"#FFFFFF");
				$(".projectstatview").css('color',"gray");
				$(".projectstatview").parent().css('background-color',"#FFFFFF");
				$(".sprintHead").show();
				//$(".duration-hd").find('label').hide();
				$(".duration-hd").find('#pageCtrls').show();
				//$(".duration-hd").find('ul').show();
				$(".duration-hd").find("#task_report").hide();
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
        	
        	/* $(".sprintHead").live('click', function(){
        		var sp = $(".sprints li").index($(this)) + 1;
        		populateSprintStories(sp);
        		sprintinview = sp;
        		current_sprint = sp;
        		viewStoryFancyBox();
        	}); */
        	
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
        	
        	$('#addAnotherStory,#createStory').unbind('click').live("click",function(){
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
        		var post_data = 'stTitle=' +title.val() + '&projectId='+projectId+'&stDescription=' + description.val() + '&stPriority=' + priority + '&user=' +user + '&stSprint=' + sprint.val();
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
        		
        	});
        	
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
        	
        	$("#searchStory").keyup(function(event) {
    			if (event.which == 13) {
    				event.preventDefault();
    			}
    			var query=$('#searchStory').val();
    			var selector = $('#storyList').find('ul.story');
    			//query = query.replace(/ /gi, '|'); //add OR for regex query  

    			$(selector).find('li').each(
    					function() {
    						($(this).find('p').text()
    								.search(new RegExp(query, "i")) < 0) ? $(this)
    								.hide() : $(this).show();
    					});
    		});
        	
        	$("#searchRmvUser").unbind('click').live('keyup',function(event) {
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
        	
        	
			$(".stAddmore").unbind('click').live('click', function(){
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
			
			function setStoryId (storyId){
				// Comment Section				
				//$('<input>').attr({type: 'hidden', id: 'current_story_id',value: storyId }).appendTo('form');
				$("#current_story_id").val(storyId);
				// Ends Here
			}
			
			$(".viewStory, .moreStory").unbind('click').live('click', function(){
				var id = $(this).closest('li').attr("id");
        		id = id.replace("st","");
        		var stageId = $(this).closest('ul').attr('id');
        		populateStoryAssignees(id);
        		
        		setStoryId(id);
        		populateCommentingUserDetails();
    	       	populateStoryComments();
    	       	populateStoryTodos();
				$(".add-user").unbind('click').live('click', function(){
					var eid = $(this).attr("alt");
					var eid_arr = [eid];
					addUserToStory(eid_arr, id,stageId);
					populateStoryAssignees(id);	
				});
				
				$(".remvUser").unbind('click').live('click', function(){
					var eid = $(this).parent().find('img').attr("alt");
					removeUserFromStory(eid, id,stageId);
					populateStoryAssignees(id);
				});
				viewStoryFancyBox();
			});
			
			$(".projectview").unbind('click').live('click',function(){
				project_view = 1;
				$('.sprints').hide();
		       	$(this).css('color',"#00475C");
		       	$(this).parent().css('background-color',"#F6EEE1");
				$(".sprintview").css('color',"gray");
				$(".sprintview").parent().css('background-color',"#FFFFFF");
				$(".projectstatview").css('color',"gray");
				$(".projectstatview").parent().css('background-color',"#FFFFFF");
				$("#sprint-view").hide();
				$("#pstat-view").hide();
				$("#project-view").show();
				$(".sprintHead").hide();
				$(".duration-hd").find('label.duration').show();
				$(".duration-hd").find('#pageCtrls').show();
				//$(".duration-hd").find('ul').hide();
				$(".duration-hd").find("#task_report").hide();
				populateSprints();
				viewStoryFancyBox();
			});
			
			$(".sprintview").unbind('click').live('click',function(){
				project_view = 0;
				$('.sprints').show();
				$(this).css('color',"#00475C");
				$(this).parent().css('background-color',"#F6EEE1");
		       	$(".projectview").css('color',"gray");
		       	$(".projectview").parent().css('background-color',"#FFFFFF");
		       	$(".projectstatview").css('color',"gray");
				$(".projectstatview").parent().css('background-color',"#FFFFFF");
				$("#sprint-view").show();
				$("#project-view").hide();
				$("#pstat-view").hide();
				$(".sprintHead").show();
				$(".duration-hd").find('#pageCtrls').show();
				//$(".duration-hd").find('ul').show();
				$(".duration-hd").find("#task_report").hide();
				populateSprintStories(current_sprint);
				viewStoryFancyBox();
			});
			
			$(".projectstatview").unbind('click').live('click',function(){
				project_view = 0;
				$('.sprints').hide();
				$(this).css('color',"#00475C");
				$(this).parent().css('background-color',"#F6EEE1");
		       	$(".projectview").css('color',"gray");
		       	$(".projectview").parent().css('background-color',"#FFFFFF");
		       	$(".sprintview").css('color',"gray");
				$(".sprintview").parent().css('background-color',"#FFFFFF");
				$("#sprint-view").hide();
				$("#project-view").hide();
				$("#pstat-view").show();
				$(".sprintHead").css('display','none !important');
				$(".duration-hd").find('label.duration').hide();
				$(".duration-hd").find('#pageCtrls').hide();
				//$(".duration-hd").find('ul').hide();
				//todo:enable it when the functionality is complete.
				$(".duration-hd").find("#task_report").show(); 
				populateProjectStatistics();
				viewStoryFancyBox();
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
	// Comment Section Code
        	
        	var month_list=new Array("January","Febraury","March","April","May","June","July","August","September","October","November","December");
        	
        	function populateCommentingUserDetails(){
            	
        		var commentBoxHtml = '';
				var user = userLogged;	
				var current_user = userObject[user];
				commentBoxHtml = '<img src="'+qontextHostUrl+current_user.avatarurl+'">';							
				$(".comment-box-user").html(commentBoxHtml);
				$(".todo-box-user").html(commentBoxHtml);
				
            }
        	
			function populateStoryComments(){
				var loggedDate='';
				var id = $("#current_story_id").val();	
				var commentsHtml = '';								
				$.ajax({
					url : 'api/v1/comments/story/'+id,
					type : 'GET',
					async : false,					
					success :function(comments){
											    
						if (comments != null){	
							$('#comments_section').prev().find('label').html('Comments ('+comments.length+')');	
							$('#comments-count').val(comments.length);
	        				if(comments.length > 0){
		        				for (var i=0;i<comments.length;i++){
			        				comment = comments[i];
			        				var newDate = new Date(comment.logDate);
			        				var dtString = newDate.getDate()+" "+month_list[newDate.getMonth()]+","+newDate.getFullYear();				        				    				
			        				//alert($.datepicker.parseDate('MM d,yy',new Date(parseInt(comment.logDate))));			        																						        		
			        				//commentsHtml += '<li class="comment-list" style="width:100%;"><a id='+comment.pkey+' class="cmtRmvComment remove" href="javascript:void(0);"></a><img title="'+comment.user.fullname+'" src="'+qontextHostUrl+comment.user.avatarurl+'"><div><span><pre style="float:right;">'+comment.content+'</pre></span><div style="clear:both;">'+dtString+'</div></div></li>';
			        				commentsHtml += '<li class="todo-list"><div class="todo-img"><img class="todo-user" title="'+comment.user.fullname+'" src="'+qontextHostUrl+comment.user.avatarurl+'"></img></div><div class="todo-content" ><div id="todoText">'+comment.content+'</div></div><div class="todo-action"><a id=cdelete'+comment.pkey+' class="cmtRmvComment ctremove" href="javascript:void(0)"></a></div></li>';
			        				}
		        			}			        				        			        			
						}else{
								commentsHtml += 'No Comments for this story';
							}
						
						$(".comments-display").find("ul").html(commentsHtml);
						var sectionVisible = false;
						if($('#comments_section').is(':visible')){
							sectionVisible = true;
						}
						if(!sectionVisible)$('#comments_section').show();
						if(storyDetailScroll[id+"comments"]){
							var api = $('.comments-display').data('jsp');
							if(api)api.destroy();
						}
						storyDetailScroll[id+"comments"] = $('.comments-display').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
						if(!sectionVisible)$('#comments_section').hide();
						//$("#story-cont").jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;																
					},
					error : function(data){},
					complete : function(data){}					
					});	
			}					
        	
        	$(".cmtRmvComment").unbind('click').live('click',function(){        		
            	var commentID = $(this).attr("id").split("cdelete")[1];
            	deleteStoryComment(commentID);
            	populateStoryComments();
            	});

        	function deleteStoryComment(commentID){

        		var post_data = 'commentID='+commentID;
        		$.ajax({
          			url: 'api/v1/comments/delete/'+commentID+'',
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
              			url: 'api/v1/comments/create',
              			type: 'POST',
              			data: post_data,
              			async:false,
              			success: function( comment ) {
              				
              				comment = comment[0];
                  			e.preventDefault();

                  			var count =  parseInt($('#comments-count').val());
              				count = count+1;
              				$('#comments-count').val(count);
              				$('#comments_section').prev().find('label').html('Comments ('+count+')');
                  			
              				$('.comment-text').val('');							            				             			
              				var newDate = new Date(comment.logDate);
              				var dtString = newDate.getDate()+" "+month_list[newDate.getMonth()]+","+newDate.getFullYear();
              				var commentHtml = '<li class="todo-list"><div class="todo-img"><img class="todo-user" title="'+comment.user.fullname+'" src="'+qontextHostUrl+comment.user.avatarurl+'"></img></div><div class="todo-content" ><div id="todoText">'+comment.content+'</div></div><div class="todo-action"><a id=cdelete'+comment.pkey+' class="cmtRmvComment ctremove" href="javascript:void(0)"></a></div></li>';
              				$('.comment-text').focus(); 				
              				if(storyDetailScroll[story_id+"comments"]){
    							var api = $('.comments-display').data('jsp');
    							if(api){
    								api.getContentPane().find('ul').append(commentHtml);
    								api.reinitialise();
    							}
    						}else{
    							$('.comments-display ul').append(commentHtml);
    							storyDetailScroll[id+"comments"] = $('.comments-display').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
    						}
    						
              			},
              			error: function(data) { },
             			complete: function(data) { }            		
                      	});
                 	}                     
                });
         // Ends Here

         // Todo Code starts here
                  
         
         $('#todo-form #tAdd').unbind('click').live("click",function(){
             
        	 var user = userLogged;
        	 var story_id = $("#current_story_id").val();	
        	 var todoText = $(".todo-text").val();                       	  	 
			 var milestonePeriod = $("#todo-milestones").val();		
			 var taskUser = $("#todo-user").val();
			 if(todoText == "" || taskUser == 0) return;   
			 todoText = parsedString(todoText);
  	       	 var todoHtml = '';
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
      				$('#todos-count').val(count);
      				$('#todo_section').prev().find('label').html('Todos ('+count+')');
      				$('.todo-text').val('');
      				$('#todo-milestones').val('1');	
      				$('#todo-user').val('0');	
      				todoHtml = '<li id="todo'+todo.pkey+'" class="todo-list"><div class="todo-img"><img class="todo-user" title="'+todo.user.fullname+'" src="'+qontextHostUrl+todo.user.avatarurl+'"></img></div><div class="todo-content" ><div id="todoText">'+todo.content+'</div><div id="todoMilestone"><label style="font-weight:none;">'+todo.milestonePeriod+' Milestone Period | </label><div class="todo-status"><label style="color:#1e9ce8;" class="todo-status-text">CREATED</label><a href="javascript:void(0);" ></a></div></div></div><div class="todo-action"><a id=tedit'+todo.pkey+' class="cmtEditTodo ctedit" href="javascript:void(0);"></a><a id=tdelete'+todo.pkey+' class="cmtRmvTodo ctremove" href="javascript:void(0)"></a></div></li>';
      				$('.todo-text').focus(); 
      				if(storyDetailScroll[story_id+"todos"]){
						var api = $('.todo-display').data('jsp');
						if(api){
							api.getContentPane().find('ul.todo-total-display').append(todoHtml);
							api.reinitialise();
						}
					}else{
						$('.todo-display ul.todo-total-display').append(todoHtml);
						storyDetailScroll[id+"todos"] = $('.todo-display').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
					}
      			},
      			error: function(data) { },
     			complete: function(data) { }            		
              	});
        
             	return false;
             });

         function deleteStoryTodo(todoID){

     		$.ajax({
       			url: 'api/v1/todo/delete/'+todoID,
       			type: 'GET',
       			async:false,
       			success: function( result ) {
           										                  			
       			},
       			error: function(data) { },
      			complete: function(data) { }            		
               	 });          	
         }

         $(".cmtRmvTodo").unbind('click').live('click',function(){        		
         	var todoID = $(this).attr("id").split("tdelete")[1];
         	deleteStoryTodo(todoID);
         	//if edit was clicked and nothing was done.
         	$('#todo-form').find('input.submit').attr("value","Add");
   			$('#todo-form').find('input.submit').attr("id","tAdd").attr('data-task',todoID);
         	populateStoryTodos();
         	});

         function populateStoryTodos(){				
				var id = $("#current_story_id").val();		
				//populate user assignees drop down
				var taskUserHtml = '<option value="0">--Assign User--</option>';
				for(var i=0;i<users.length;i++){
					taskUserHtml +='<option value="'+users[i].username+'">'+users[i].fullname+'</option>';
				}
				$('#todo-user').html(taskUserHtml);
				//populate task status drop down
    	       	var status_dropdown = '<ul class="todo-status-list">';
    	       	for(var i in taskStatusColors){
    	       		status_dropdown +='<li><label style="color:'+taskStatusColors[i]+'">'+i+'</label></li>';
    	       	}
    	       	status_dropdown +="</ul>";
    	       	$('#todo_section').prepend(status_dropdown);
				var todosHtml = '';								
				$.ajax({
					url : 'api/v1/todo/story/'+id,
					type : 'GET',
					async : false,					
					success :function(todos){
						if (todos != null){		
							$('#todo_section').prev().find('label').html('Todos ('+todos.length+')');	
							$('#todos-count').val(todos.length);
	        				if(todos.length > 0){
		        				for (var i=0;i<todos.length;i++){
		        					todo = todos[i];			        								        				    						        							        																						        	
		        					todosHtml += '<li id="todo'+todo.pkey+'" class="todo-list"><div class="todo-img"><img class="todo-user" title="'+todo.user.fullname+'" src="'+qontextHostUrl+todo.user.avatarurl+'"></img></div><div class="todo-content" ><div id="todoText">'+todo.content+'</div><div id="todoMilestone"><label style="font-weight:none;">'+todo.milestonePeriod+' Milestone Period | </label><div class="todo-status" ><label style="color:'+taskStatusColors[todo.status]+';" class="todo-status-text">'+todo.status+'</label><a href="javascript:void(0);" ></a></div></div></div><div class="todo-action"><a id=tedit'+todo.pkey+' class="cmtEditTodo ctedit" href="javascript:void(0);"></a><a id=tdelete'+todo.pkey+' class="cmtRmvTodo ctremove" href="javascript:void(0)"></a></div></li>';
			        				}
		        			}			        				        			        			
						}else{
								todosHtml += 'No Todos for this story';
							}
						
						$(".todo-display ul.todo-total-display").html(todosHtml);
						var sectionVisible = false;
						if($('#todo_section').is(':visible')){
							sectionVisible = true;
						}
						if(!sectionVisible)$('#todo_section').show();
						if(storyDetailScroll[id+"todos"]){
							var api = $('.todo-display').data('jsp');
							if(api)api.destroy();
						}
						storyDetailScroll[id+"todos"] = $('.todo-display').jScrollPane({showArrows: true, scrollbarWidth : '20'}).data().jsp;
						if(!sectionVisible)$('#todo_section').hide();
					},
					error : function(data){},
					complete : function(data){}					
					});	
			}	
         
         $('#addStory').unbind('click').live("click",function(){
        	 $('.popup-proj-cont').hide();
        	 $('input[name=storyTitle]').val("");
 			$('textarea[name=storyDesc]').val("");
 			var select = $('select[name=stSprint]');
 			select.val(jQuery('options:first', select).val());
 			//$('.story-popup .custom-select .option').html('<div class="color p1"></div><div class="label" data-value="1">Priority 1</div></div>');
        	 $('.story-popup').show();
         });
         
         $('#popup_cancel').unbind('click').live("click",function(){
        	 $('.popup-story-cont').hide();
         }); 
         
         $('#popup_story_cancel').unbind('click').live("click",function(){
        	 $('.story-popup').hide();
         }); 
         
         $('#storyClose').unbind('click').live("click",function(){
        	 $('.story-popup').hide();
         }); 
         
		 //code for story details accordion
                
         $('div.acc-head').unbind('click').live("click",function(){
        	 if($(this).find('div').hasClass('open')){
        		var current_div = $(this).find('div');
        		$('div.down').each(function(){
        			if($(this) != current_div){
        				$(this).removeClass('down').addClass('open');
        				$(this).parent().next('.acc-content').slideUp("slow");
        			}
        		}); 
        		$(this).find('div').removeClass('open').addClass('down');
        		$(this).next(".acc-content").slideDown("slow");
        	}else{
        		$(this).find('div').removeClass('down').addClass('open');
        		$(this).next(".acc-content").slideUp("slow");
        	}
         });
         
         
         
         //code to toggle chart container
         $('.toggleChart').unbind('click').live("click",function(){
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
        			 var priorityClass = $(this).attr('class').split(' ').slice(-1)[0]; //get last class since the element has multiple class
        			 if(priorityClass == priorityDisabledObj[i]){
        				 $(this).hide();
        			 }
        		 }
            	 
             }); 
         }
         
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
         
         $('.st_edit_story').unbind('click').live("click",function(){
        	 $('#story-section').hide();
        	 var storyId = $(this).attr('id');
        	 $.ajax({
	        		url: 'api/v1/stories/'+storyId,
	        		type: 'GET',
	        		async:false,
	        		success: function( stories ) {
	        			if(stories != null && stories.length > 0){
							stories = stories[0];
							$('#st-edit-title').val(stories.title);
							$('textarea[name=st-edit-description]').val(stories.description);
							$('#st-edit-priority option').html('<div class="color p'+stories.priority+'"></div><div class="label" data-value="'+stories.priority+'">Priority '+stories.priority+'</div></div>');
				        	var optionsHtml = '<option value="0">Add story to Project Backlog</option>';
				            for(var i=1;i<=totalsprints;i++){
					             optionsHtml +='<option value="'+i+'">Sprint '+i+'</option>';
				        	}
				        	$('#story-edit-section #st-edit-sprint').html(optionsHtml);
				        	if(stories.sprint_id == null){
				        		$('#story-edit-section #st-edit-sprint').val('0');
				        	}else{
				        		$('#story-edit-section #st-edit-sprint').val(stories.sprint_id.id);
				        	}
				        	$('#story-edit-section').show();
	        			}
	        		}
        	 });
        	 
         });
         
         $('.st_edit_cancel').unbind('click').live("click",function(){
        	 $('#story-section').show();
        	 $('#story-edit-section').hide();
         });
         
         $('#st-edit-done').unbind('click').live("click",function(){
        	var title = $('input#st-edit-title');
     		var description = $('textarea[name=st-edit-description]');
     		var priority = $('#st-edit-priority .option .label').attr('data-value');
     		var sprint = $('select[name=st-edit-sprint]');
     		if(title.val()==""){
     			return;
     		}
     		var user = userLogged;
     		var storyId = $('.st_edit_story').attr('id');
        	 //call update story api
        	 var post_data = 'storyId='+storyId+'&stTitle=' +title.val() + '&projectId='+projectId+'&stDescription=' + description.val() + '&stPriority=' + priority + '&user=' +user + '&stSprint=' + sprint.val();
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
     		populateStoryAssignees(storyId);
        	 $('#story-section').show();
        	 $('#story-edit-section').hide();
         });
         
         
         $('.ctedit').unbind('click').live("click",function(){
        	 var taskId = $(this).attr('id').split("tedit")[1];
        	 $.ajax({
       			url: 'api/v1/todo/'+taskId,
       			type: 'GET',
       			async:false,
       			success: function( todo ) {
       				todo = todo[0];
           			$('.todo-text').val(todo.content);
           			$('#todo-milestones').val(todo.milestonePeriod);
           			$('#todo-user').val(todo.user.username);
           			$('#todo-form').find('input.submit').attr("value","Update");
           			$('#todo-form').find('input.submit').css("margin-left","100px");
           			$('#todo-form').find('input.submit').attr("id","tupdate").attr('data-task',taskId).attr('data-status',todo.status);
       			},
       			error: function(data) { },
      			complete: function(data) { }            		
               	});
        	 
         });
         
         $('#tupdate').unbind('click').live("click",function(){
        	 var user = userLogged;
        	 var status= $(this).attr("data-status");
        	 var story_id = $("#current_story_id").val();	
        	 var todoText = $(".todo-text").val();                       	  	 
			 var milestonePeriod = $("#todo-milestones").val();		
			 var taskUser = $("#todo-user").val();
			 if(todoText == "" || taskUser == 0) return;   
			 todoText = parsedString(todoText);
			 var task_id = $(this).attr("data-task");
             var post_data = 'id='+task_id+'&content='+ todoText+'&timeInDays='+milestonePeriod+'&assigneeId='+taskUser+'&status='+status+'&user='+user;                     
             $.ajax({
      			url: 'api/v1/todo/update/'+task_id,
      			type: 'POST',
      			data: post_data,
      			async:false,
      			success: function( todo ) {  
      				todo = todo[0];
      				$('.todo-text').val('');
      				$('#todo-milestones').val('1');	
      				$('#todo-user').val('0');	
      				$('#todo-form').find('input.submit').attr("value","Add");
           			$('#todo-form').find('input.submit').attr("id","tAdd").attr('data-task',task_id).attr('data-status',todo.status);
      				var todoUpdateHtml = '<div class="todo-img"><img class="todo-user" title="'+todo.user.fullname+'" src="'+qontextHostUrl+todo.user.avatarurl+'"></img></div><div class="todo-content" ><div id="todoText">'+todo.content+'</div><div id="todoMilestone"><label style="font-weight:none;">'+todo.milestonePeriod+' Milestone Period | </label><div class="todo-status"><label style="color:'+taskStatusColors[todo.status]+';" class="todo-status-text">'+todo.status+'</label><a href="javascript:void(0);" ></a></div></div></div><div class="todo-action"><a id=tedit'+todo.pkey+' class="cmtEditTodo ctedit" href="javascript:void(0);"></a><a id=tdelete'+todo.pkey+' class="cmtRmvTodo ctremove" href="javascript:void(0)"></a></div>';
      				$('.todo-display').find('ul.todo-total-display').find('li#todo'+task_id).html(todoUpdateHtml);
      				$('.todo-text').focus(); 
      			},
      			error: function(data) { },
     			complete: function(data) { }            		
              	});
         });
         
         $('#todo_section .todo-status a').unbind('click').live('click',function(event){
        	 var id = $(this).closest('li.todo-list').attr('id');
        	 $(this).closest('#todo_section').find('ul.todo-status-list').attr('id','status-'+id);
             if($(this).closest('#todo_section').find('ul.todo-status-list').is(':visible')){
            	 $(this).closest('#todo_section').find('ul.todo-status-list').slideUp();
             }else{
            	 $(this).closest('#todo_section').find('ul.todo-status-list').css('left',($(this).position().left - 50)+'px');
            	 $(this).closest('#todo_section').find('ul.todo-status-list').css('top',($(this).position().top + 20 + $(this).closest('.jspPane').position().top)+'px');
            	 $(this).closest('#todo_section').find('ul.todo-status-list').slideDown();
             }
             

         });
         
         $('#todo_section ul.todo-status-list li').unbind('click').live('click',function(event){
        	 var user = userLogged;
             var task_id = $(this).closest('ul').attr('id').split("status-todo")[1];
             var c = $('#todo'+task_id).find('.todo-status');
             var status = $(this).find('label').html();
             var post_data = 'id='+task_id+'&status='+status+'&user='+user;
             var el = $(this);
             $.ajax({
       			url: 'api/v1/todo/update/'+task_id,
       			type: 'POST',
       			data: post_data,
       			async:false,
       			success: function( todo ) {  
       				c.find('label').replaceWith($(el).html());
                    $(el).closest('#todo_section').find('ul.todo-status-list').hide();
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
         
});
