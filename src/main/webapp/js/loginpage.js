

var months = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ];
var new_proj_response;

$(document).ready(function(){
	var stageCarousel = null;
	var projectId = null;
	var stage_rank = 0;
	var imageCollections = [{value:0,url:"themes/images/project_stages/repository1.png"},{value:1,url:"themes/images/project_stages/repository2.png"},{value:2,url:"themes/images/project_stages/repository3.png"},{value:3,url:"themes/images/project_stages/repository4.png"},{value:4,url:"themes/images/project_stages/repository5.png"},{value:5,url:"themes/images/project_stages/repository6.png"},{value:6,url:"themes/images/project_stages/repository7.png"},{value:7,url:"themes/images/project_stages/repository8.png"},{value:8,url:"themes/images/project_stages/repository9.png"},{value:9,url:"themes/images/project_stages/repository10.png"}];
	// To Enable Tabs
	$('#tabs').tabs();
	
	// For Slider
	//createSlider_sty_size("pow_2");
	//createSlider_sty_unit("hours");
	 populatePro();

	// To get list of all Projects
	//$('#tab_All').unbind('click').live("click",function(){
	function populatePro(){
		$.ajax({
			url: 'api/v1/projects/user/'+userLogged,
			type: 'GET',
			async:false,
			success: function( obj ) {
				var prjt_string = "";
				if(obj && obj.length > 0){
						
						for( var cnt = 0 ; cnt < obj.length ; cnt++) {
							var duration = getSprintDuration(obj[cnt]);
							var proj = { pjt : obj[cnt] , duration : duration};
							prjt_string += new EJS({url: 'ejs/project_list_temp.ejs'}).render(proj);					
						} 
						 $('#mainContainer').html(prjt_string);
						
					} else {
						 $('#mainContainer').html("<label style='color:#fff' >Currently there are no projects. Click New tab to create one.</label>");
					}    					
			}
		//});
	});
   }
	
	
	// To get the sprint duration with json response
	function getSprintDuration(project){
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
		return duration;
	}
	
	/******* Create Project / Update Project****************/
	
	$('#create_pjt').live('click',function(){
		createProject(false);
	});
	
	$('#update').live('click',function(){
		createProject(true);
	});
	
	$("#new_pjt").addClass("pro_head_anchor_enable");
	
	/* on click of next button */
	$('#pjt_stag').unbind("click").live('click',function(){
		if(! new_proj_response ) {
			return false;
		}
		$('#proj-error').hide();
		$("#pjt_create_details, #story_parms").hide();
		$("#pjt_stages").show();
		
		$("#new_pjt, #sty_parm").removeClass("pro_head_anchor_enable");
		$("#pjt_stag").addClass("pro_head_anchor_enable");
		
	});
	
	$('#nxt_pjt_stages, #sty_parm').unbind('click').live('click',function(){
		if(! new_proj_response ) {
			return false;
		}
		$("#pjt_create_details , #pjt_stages").hide();
		$("#story_parms").show();
		
		$("#new_pjt, #pjt_stag").removeClass("pro_head_anchor_enable");
		$("#sty_parm").addClass("pro_head_anchor_enable");
		
	});
	
	/* on click of previous button */
	$('#pjt_stages_prv').unbind("click").live('click',function(){
		$("#pjt_create_details").show();
		$("#pjt_stages, #story_parms").hide();
		
		$("#new_pjt").addClass("pro_head_anchor_enable");
		$("#pjt_stag, #sty_parm").removeClass("pro_head_anchor_enable");
		
	});	
	
	$('#story_prv').unbind("click").live('click',function(){
		$("#pjt_create_details, #story_parms").hide();
		$("#pjt_stages").show();
		
		$("#new_pjt, #sty_parm").removeClass("pro_head_anchor_enable");
		$("#pjt_stag").addClass("pro_head_anchor_enable");
		
	});
	
	function createProject(update,nxtProcess){

		var title = $('input[name=pTitle]');
		var description = $('textarea[name=pDescription]');
		var start_date = $('input[name=pStartDate]');
		var end_date = $('input[name=pEndDate]');
		var duration = $('select[name=pSprintDuration]');

			if(trim(title.val()) == ""){
				$("#proj-error").html("Project title is mandatory");
				return false;
			}
			
			if(start_date.val() == ""){
				$("#proj-error").html("Please provide start date");
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
				var post_data1 = {'username':userLogged,'displayname':displayname,'fullname':fullname,'emailid':emailid,'avatarurl':avatar};
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
									new_proj_response = records;
									if(records[0].pkey){
										 if(!nxtProcess){
											window.location.href = 'sprint.action?&visit=1&projectId='+records[0].pkey;
										 }else{
											render_pjt_paramters();
											projectId = records[0].pkey;
											$("#pjt_create_details, #story_parms").hide();
											var stage_carousel = "";
											for(var i=0;i<records[0].projectStages.length;i++){
												stage_rank++;
												var stageData = records[0].projectStages[i];
												stageData.imageCollections = imageCollections;
												stageData.new_stage = false;
												stage_carousel += new EJS({url: 'ejs/proj_stage_carousel.ejs'}).render(stageData);
											}
											$('ul#stageCarousel').html(stage_carousel);
											 $('#stageCarousel').jcarousel({
												 scroll:1,
												 visible:4,
												 initCallback: mycarousel_initCallback
											  });
											  
											  $('#stageCarousel').sortable({
												items:'li',
												cursor:'move',
												//helper:'clone',
												//appendTo: 'body',
												forcePlaceholderSize: true,
												placeholder: 'stage_highlight',
												update: function( event, ui ) {
													var order = $('#stageCarousel').sortable('serialize');
													alert(order);
													var post_data = { orderedStageIdList: order};
													$.ajax({
														url: 'api/v1/projectstage/updatestagerank',
														type: 'POST',
														data: post_data,
														async:false,
														success: function( records ) {
															
														}
													});
												}
											  });
											$("#pjt_stages").show();
											
											$("#new_pjt, #sty_parm").removeClass("pro_head_anchor_enable");
											$("#pjt_stag").addClass("pro_head_anchor_enable");
										 }
										
									}
								},
								error: function(data) { },
								complete: function(data) { }
							});
						}else{
							var post_data = 'pNo='+title.attr("data-pNo")+'&pTitle=' + title.val() + '&current_user='+userLogged + '&pDescription=' + description.val() + '&assignees='
							+ assignees + '&pStartDate=' + start_date.val() + '&pEndDate=' + end_date.val() +'&pSprintDuration=' +duration.val();
							$.ajax({
								url: 'api/v1/projects/update',
								type: 'POST',
								data: post_data,
								async:false,
								success: function( records ) {
									if(records[0].pkey){
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
	 /***********************************************/
	 
	function mycarousel_initCallback(carousel){
		stageCarousel = carousel;
		$('#stageCarousel li').each(function(){
			$(this).attr('id',$(this).find('#stageId').val());
		});
		$('.jcarousel-container-horizontal').append("<div id='help_stage'>Click to edit properties or drag to rearrange | <a id='new_stage' style='color:blue;' href='javascript:void(0);'>Introduce new stage</a></div>");
	} 
	 
   $('.stage_container').unbind('click').live('click',function(){
		 $(this).parent('li').css('height','198px');
		 $('#help_stage').hide();
		 $('select[name=stage_image]').msDropDown();
		 $(this).hide();
		 $(this).next().show();
   });
   
   $('a.stage_edit_later').unbind('click').live('click',function(){
	   $(this).closest('li').css('height','180px');
	   $('#help_stage').show();
	   $('.stage_container').show();
	   $('.stage_edit_container').hide();
   });
   
   $('a.stage_edit_remove').unbind('click').live('click',function(){
	   $('#help_stage').remove();
	   var carouselIndex = parseInt($(this).closest('li').attr('jcarouselindex'));
	   stageCarousel.removeAndAnimate(carouselIndex);
   });
   
   $('a.stage_delete').unbind('click').live('click',function(){
	   var id = $(this).closest('li').find('input.hidden#stageId').val().split('stage_')[1];
	   var carouselIndex = parseInt($(this).closest('li').attr('jcarouselindex'));
	   $.ajax({
			url: 'api/v1/projectstage/delete/'+id,
			type: 'GET',
			async:false,
			success: function() {
				$('#help_stage').remove();
				stageCarousel.removeAndAnimate(carouselIndex);
			}
	   });
	   
   });
   
   $('a#new_stage').unbind('click').live('click',function(){
	  var stage_container = "";
	  stageData = {new_stage:true,pkey:"",rank:stage_rank,imageUrlIndex:0,imageCollections:imageCollections,title:"",description:""};
	  stage_container += new EJS({url: 'ejs/proj_stage_carousel.ejs'}).render(stageData);
	  $('#help_stage').remove();
	  stageCarousel.addAndAnimate(stage_container);
	  $('#stageCarousel').find('li:last').css('height','198px');
	  $('#help_stage').hide();
	  $('#stageCarousel').find('li:last').find($('select[name=stage_image]')).msDropDown();
	  
   });
   
   $('#stage_edit_done').unbind('click').live('click',function(){
	   var self = $(this); 
	   var id = $(this).closest('li').find('#stageId').val().split('stage_')[1];
	   var rank = $(this).closest('li').find('#stageRank').val();
	   var title=$(this).closest('.stage_edit_container').find('input').val();
	   var description=$(this).closest('.stage_edit_container').find('textarea').val();
	   var oHandler = $('select[name=stage_image]').msDropDown().data("dd");
	   var imageIndex= oHandler.get("selectedIndex");
	   if(id == ""){ //its create
		   var post_data = 'projectid='+projectId+'&title='+title+'&description='+description+'&imageUrlIndex='+imageIndex+'&rank='+rank;
	   }else{ //its update
		   var post_data = 'projectid='+projectId+'&pStageNo='+id+'&title='+title+'&description='+description+'&imageUrlIndex='+imageIndex+'&rank='+rank;
	   }
	   
	   $.ajax({
			url: 'api/v1/projectstage/update',
			type: 'POST',
			data:post_data,
			async:false,
			success: function(stage) {
				 var stage = stage[0];
				 self.closest('li').css('height','180px');
				 $('#help_stage').show();
				 self.closest('.stage_edit_container').hide();
				 self.closest('.stage_edit_container').prev('.stage_container').find('.pro_stage_title').html(stage.title);
				 self.closest('.stage_edit_container').prev('.stage_container').find('.pro_stage_desc').html(stage.description);
				 self.closest('.stage_edit_container').prev('.stage_container').find('.pro_stage_image img').attr('src', imageCollections[stage.imageUrlIndex].url);
				 self.closest('.stage_edit_container').prev('.stage_container').show();
				 
			}
	   });
   });
/*   
   $('a#new_stage').unbind('click').live('click',function(){
	   stageCarousel.add(0,)
   });*/
   
	  //$('select[name=pSprintDuration]').msDropDown();
	  
	 /**********************STORY PARAMETERS*************************/
   	 
	  	  
	  $(".pColor").unbind('click').live("click", function(event){
		  var curEle = $(this);
		  $(document).colorpicker(event,function(color){
			  curEle.css('background-color',color);
		  });
	  });

	  	
	  	$('.addPos').unbind('click').live("click", function(event){
	  		
	  		var pjt_priority = { pjt : {"color":"#FC5F5F"} , last : true};	  		
		  	var new_priority_string = new EJS({url: 'ejs/sty_priority_temp.ejs'}).render(pjt_priority);
		  	$('#sty_content_priority').append(new_priority_string);
		  	$(this).removeClass("addPos");
		  	$(this).addClass("remPos");
	  	});
	  	
	  	$('.remPos').unbind('click').live("click", function(event){
	  		deletePriority( $(this) );
	  	});
	  	
	  	$('input[name="priority"]').change(function() {
	  			$(this).parent().parent().find("#disable_overlay").toggleClass("disabled_overlay");
	  	});
	  	
	  	$('input[name="sty_unit_radio"]').live('click', function() {
	  	    var valu = $(this).val();
	  	    createSlider_sty_unit(valu);
	  	   // $("#selectedSlot").val(valu);
	  	});
	  	$('input[name="sty_size_radio"]').live('click', function() {
	  	    var valu = $(this).val();
	  	    createSlider_sty_size(valu);
	  	    
	  	});
	  	
	  	
	  	function createSlider_sty_unit(value){
	  		var sizes;
	  		if( value == 0 ){
	  			 sizes = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40"];
  	  			 $($("#sty_unit_indicator").find('sup')[1]).html('40');

	  		} else {
	  			 sizes = ["1","2","3","4","5"];
	  			 $($("#sty_unit_indicator").find('sup')[1]).html('5');
	  		}

	  		 $("#sty_milestone_ran_lbl").val("");
	  		 
	  		$("#slider").slider({
	  		  min: 0,
	  		  max: sizes.length - 1,
	  		  step: 1,
	  		  range: 'min',
	  		  value: new_proj_response[0].projectPreferences.mileStoneRange,
	  		  slide: function(event, ui) {
	  		    $("#sty_milestone_ran_lbl").val(sizes[ui.value]);
	  		  }
	  		});
	  	}
	 	
	  	function createSlider_sty_size(value){
	  		var sizes;
	  		if( value == 0 ){
	  			 sizes = ["1","2","4","8","16","32"];
	  			 $($("#sty_size_indicator").find('sup')[0]).html('1');
  	  			 $($("#sty_size_indicator").find('sup')[1]).html('32');

	  		} else if( value == 1 ) {
	  			 sizes = ["1","2","3","5","8","13","21","34","45"];
	  			 $($("#sty_size_indicator").find('sup')[0]).html('1');
	  			 $($("#sty_size_indicator").find('sup')[1]).html('45');
	  		} else {
	  			 sizes = ["XS", "S", "M", "L", "XL", "XXL", "XXXL"];
	  			 $($("#sty_size_indicator").find('sup')[1]).html('XXXL');
	  			 $($("#sty_size_indicator").find('sup')[0]).html('XS');
	  		}

	  		 $("#amount").val("");
	  		 
	  		$( "#slider-range" ).slider({
	  			range: true,
	  			min: 0,
	  			max: sizes.length - 1,
	  			step: 1,
	  			values: [new_proj_response[0].projectPreferences.storySizeLowRangeIndex ,new_proj_response[0].projectPreferences.storySizeHighRangeIndex ],
	  			slide: function( event, ui ) {
	  				$( "#amount" ).val( sizes[ui.values[ 0 ]] + " - " + sizes[ui.values[ 1 ]]);
	  			}
	  		});
	  	}
	  	
	  	function deletePriority(priObj){
	  	   var pKey = priObj.attr("id");
	  	   if(pKey){
		 	   $.ajax({
					url: 'api/v1/projectpriority/delete/'+priObj.attr("id"),
					type: 'GET',
					async:false,
					success: function() {
							priObj.parent().remove();
					}
			   });
	  	   } else{
	  		   priObj.parent().remove();
	  	   }
	  	}
	  	


		
		
		/***********RENDER STORY PARAMETERS BASED ON JSON *****************/
		
		function render_pjt_paramters(){
			renderStoryPriority();
			renderStorySize();
			renderTaskUnit();
		}
		
		function renderStoryPriority(){
			  
			  //This is to render priority lines
			
				if( ! new_proj_response[0].projectPreferences.storypriorityEnabled ) {
					$("#sty_priority_checkbox").attr("checked",false);
					$("#sty_priority_checkbox").parent().parent().find("#disable_overlay").toggleClass("disabled_overlay");
				}
				var priority_string="";
				var priority = new_proj_response[0].projectPriorities;
				var pty_length = new_proj_response[0].projectPriorities.length;
			  	for(var cnt=0; cnt < pty_length; cnt++ ) {
			  		var pjt_priority = { pjt : priority[cnt] , last : false};
			  		if(cnt == pty_length-1) pjt_priority.last = true;
					priority_string += new EJS({url: 'ejs/sty_priority_temp.ejs'}).render(pjt_priority);
			  	}
			  	
			  	$("#sty_content_priority").append(priority_string);
			  			
		}
		
		function renderStorySize(){
			var radio_value;
			var sty_size = new_proj_response[0].projectPreferences;
			var sty_size_selection = sty_size.storyPointType;
			$("input[name='sty_size_radio'][value = '"+sty_size_selection+"']").attr("checked","checked");		
			createSlider_sty_size(sty_size_selection);
		}

		function renderTaskUnit(){
			var radio_value;
			var sty_size = new_proj_response[0].projectPreferences;
			var sty_task_unit_selection = sty_size.mileStoneType;
			$("input[name='sty_unit_radio'][value = '"+sty_task_unit_selection+"']").attr("checked","checked");			
			createSlider_sty_unit(sty_task_unit_selection);
		}
		
		/********************** UPDATE PROJECT PREFERENCES **********************/
		
		function updateProjectPreferences(){
			getLatestProjectPref();
			var post_data = "projectPreference="+JSON.stringify(projectPref);
			$.ajax({
				url: 'api/v1/projectpreferences/update',
				type: 'POST',
				async:false,
				data: post_data,
				success: function( response ) {

				}
			});
		}
		
		function getLatestProjectPref(){
			
			var pjt_pref = projectPref.projectPreferences[0];			
			pjt_pref.storypriorityEnabled = get_sty_pry_status();
			get_pjt_pry_list();
			get_pjt_sty_size();
			get_pjt_task_unit();
		}
		
		function get_sty_pry_status(){
			if( $("#sty_priority_checkbox").attr("checked") == "checked" ){ return true; } else { return false; };
		}
		
		function get_pjt_pry_list(){
			var pjt_pty_list =  $("#sty_content_priority").children();
			for(cnt = 0; cnt< pjt_pty_list.length; cnt++ ){
				
				var curPtyEle = $("#sty_content_priority").children()[cnt];
				
				var description = $(".pInput",curPtyEle ).val();
				var priorityid = $(".pAddRem",curPtyEle ).attr("id");
				var color = $(".pColor",curPtyEle ).css("background-color");
				
				projectPref.projectPreferences[0].projectPriority.push({'projectid':new_proj_response[0].pkey,'description':description,'color':color,'rank':2,'pPriorityNo':priorityid});
				
			}
		}
		
		function get_pjt_sty_size(){
			var pjt_pref = projectPref.projectPreferences[0];
			pjt_pref.storySizeType =  $('input[name="sty_size_radio"]:checked').val()
			var values = $("#slider-range" ).slider( "option", "values" );
			pjt_pref.storySizeHighRangeIndex = values[0];
			pjt_pref.storySizeLowRangeIndex = values[1];
		}
		
		function get_pjt_task_unit(){
			var pjt_pref = projectPref.projectPreferences[0];
			pjt_pref.taskmileStoneType = $('input[name="sty_unit_radio"]:checked').val();
			var value = $( "#slider" ).slider( "option", "value" );
			pjt_pref.taskmileStoneUpperRange = value;
		}
		
		/******************************** NEW PROJECT PREFERENCES - JSON ******************************/
		
		var projectPref = {
				 'projectPreferences':[{
						'storypriorityEnabled':true,
						'storySizeType':1,
						'storySizeLowRangeIndex':2,
						'storySizeHighRangeIndex':5,
						'taskmileStoneType':2,
						'taskmileStoneUpperRange':5,
						'pPreferenceNo':2,
						'projectPriority':[]
					  }]
		};
	  	
	  	/************** Create / Launch project ************************/
	  	
	  	$("#lan_pro").unbind("click").live('click',function(){
	  			updateProjectPreferences();
	  			window.location.href = 'sprint.action?&visit=1&projectId='+new_proj_response[0].pkey;
	  	});
		
		$('#nxt_pjt_details').unbind('click').live('click',function(){
			

			createProject(false,true);
			if( new_proj_response ) {
				$('#proj-error').hide();
				$("#pjt_create_details, #story_parms").hide();
				$("#pjt_stages").show();
				
				$("#new_pjt, #sty_parm").removeClass("pro_head_anchor_enable");
				$("#pjt_stag").addClass("pro_head_anchor_enable");
				
			}

			
		});
		
	  	$("#delete_pro_fromALL").unbind('click').live('click',function(){
	  		customAlert( { message: {'text':'Do you want to remove this Project?'} }, callbackForDeleteProject, $(this) );
	  	});
	  	
		
	  	$("#delete_pro").unbind('click').live('click',function(){
	  		customAlert( { message: {'text':'Do you want to remove this Project?'} }, callbackForDeleteProject );
	  	});

		function callbackForDeleteProject(handerResponse, curEle){
			if(handerResponse){
				var id = (curEle) ? curEle.parent().parent().parent().find('.slNo').html() : new_proj_response[0].pkey;
				$.ajax({
					url: 'api/v1/projects/delete/'+id,
					type: 'GET',
					async:false,
					success: function( obj ) {
						obj = $.parseJSON(obj);
						if(obj && obj.result == "success"){
							populatePro();
							if(!curEle) window.location.href = 'login.action';
						}
					}
				});			
			}
		}
		
		
});