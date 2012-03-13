

var months = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ];

$(document).ready(function(){
	
	// To Enable Tabs
	$('#tabs').tabs();
	
	// For Slider
	$( "#slider-range" ).slider({
		range: true,
		min: 0,
		max: 500,
		values: [ 75, 300 ],
		slide: function( event, ui ) {
			$( "#amount" ).val( ui.values[ 0 ] + " - " + ui.values[ 1 ] );
		}
	});
	$( "#amount" ).val(  $( "#slider-range" ).slider( "values", 0 ) +
		" - " + $( "#slider-range" ).slider( "values", 1 ) );
	
	$( "#slider" ).slider();
	//$( "#slider" ).slider({ range: 'min' });
	//$( ".selector" ).slider( "option", "range", 'min' );
	
	// To get list of all Projects
	//$('#tab_All').unbind('click').live("click",function(){
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
						 $('#mainContainer').html("<label style='color:#fff' >Currently there are no projects.</label>");
					}    					
			}
		//});
	});
	
	
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
	$('#nxt_pjt_details, #pjt_stag').live('click',function(){
		$("#pjt_create_details, #story_parms").hide();
		$("#pjt_stages").show();
		
		$("#new_pjt, #sty_parm").removeClass("pro_head_anchor_enable");
		$("#pjt_stag").addClass("pro_head_anchor_enable");
		
	});
	
	$('#nxt_pjt_stages, #sty_parm').live('click',function(){
		$("#pjt_create_details , #pjt_stages").hide();
		$("#story_parms").show();
		
		$("#new_pjt, #pjt_stag").removeClass("pro_head_anchor_enable");
		$("#sty_parm").addClass("pro_head_anchor_enable");
		
	});
	
	/* on click of previous button */
	$('#pjt_stages_prv, #new_pjt').live('click',function(){
		$("#pjt_create_details").show();
		$("#pjt_stages, #story_parms").hide();
		
		$("#new_pjt").addClass("pro_head_anchor_enable");
		$("#pjt_stag, #sty_parm").removeClass("pro_head_anchor_enable");
		
	});	
	
	$('#story_prv').live('click',function(){
		$("#pjt_create_details, #story_parms").hide();
		$("#pjt_stages").show();
		
		$("#new_pjt, #sty_parm").removeClass("pro_head_anchor_enable");
		$("#pjt_stag").addClass("pro_head_anchor_enable");
		
	});
	
	
	function createProject(update){

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
									if(records[0].pkey){
										window.location.href = 'sprint.action?&visit=1&projectId='+records[0].pkey;
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
	 
	function mycarousel_initCallback(){
		$('.jcarousel-container-horizontal').append("<div>Click to edit properties or drag to rearrange | <a id='new_stage' style='color:blue;' href='javascript:void(0);'>Introduce new stage</a></div>");
	} 
	 
	  $('#stageCarousel').jcarousel({
		 scroll:1,
		 initCallback: mycarousel_initCallback,
	  });
	  
	  $('#stageCarousel').sortable({
		items:'li',
		cursor:'move',
		//helper:'clone',
		//appendTo: 'body',
		forcePlaceholderSize: true,
		placeholder: 'stage_highlight',
		update: function( event, ui ) {
		}
	  });
	 /**********************STORY PARAMETERS*************************/

	  	  
	  $(".pColor").unbind('click').live("click", function(event){
		  var curEle = $(this);
		  $(document).colorpicker(event,function(color){
			  curEle.css('background-color',color);
		  });
	  });
	  
	  //This is to render priority lines
	  
	  	for(var cnt=0; cnt < 0; cnt++ ) {
			priority_string += new EJS({url: 'ejs/sty_priority_temp.ejs'}).render(proj);
	  	}
	  	
	  	$('.addPos').unbind('click').live("click", function(event){
	  		var jsonObj = {'priorityNo': '1'}
		  	var new_priority_string = new EJS({url: 'ejs/sty_priority_temp.ejs'}).render(jsonObj);
		  	$('#sty_content_priority').append(new_priority_string);
		  	$(this).removeClass("addPos");
		  	$(this).addClass("remPos");
	  	});
	  	
	  	$('.remPos').unbind('click').live("click", function(event){
	  		$(this).parent().remove();
	  	});
	  	
	  	$(".sty_checkbox").unbind('click').live("click", function(){
	  		$(this).parent().parent().find("#disable_overlay").toggleClass("disabled_overlay");
	  	});
});