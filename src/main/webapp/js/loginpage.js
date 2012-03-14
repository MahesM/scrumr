

var months = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ];
var new_proj_response;

$(document).ready(function(){
	
	// To Enable Tabs
	$('#tabs').tabs();
	
	// For Slider
	createSlider_sty_size("pow_2");
	createSlider_sty_unit("hours");


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
	
	$('#nxt_pjt_details').unbind('click').live('click',function(){
		
		$('#proj-error').hide();
		$("#pjt_create_details, #story_parms").hide();
		$("#pjt_stages").show();
		
		$("#new_pjt, #sty_parm").removeClass("pro_head_anchor_enable");
		$("#pjt_stag").addClass("pro_head_anchor_enable");
		
		createProject(false,true);
		
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
									if(records[0].pkey && !nxtProcess){
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
	  		if( value == "hours" ){
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
	  		  range:'min',
	  		  slide: function(event, ui) {
	  		    $("#sty_milestone_ran_lbl").val(sizes[ui.value]);
	  		  }
	  		});
	  	}
	  	
	  	function createSlider_sty_size(value){
	  		var sizes;
	  		if( value == "pow_2" ){
	  			 sizes = ["1","2","4","8","16","32"];
	  			 $($("#sty_size_indicator").find('sup')[0]).html('1');
  	  			 $($("#sty_size_indicator").find('sup')[1]).html('32');

	  		} else if( value == "fib_Ser" ) {
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
	  			values: [ sizes.length-6,sizes.length-2 ],
	  			slide: function( event, ui ) {
	  				$( "#amount" ).val( sizes[ui.values[ 0 ]] + " - " + sizes[ui.values[ 1 ]]);
	  			}
	  		});
	  	}
	  	
	  	/************** Create / Launch project ************************/
	  	
	  	$("#lan_pro").unbind("click").live('click',function(){
	  			window.location.href = 'sprint.action?&visit=1&projectId='+new_proj_response[0].pkey;
	  	});
		
		
	  	$("#delete_pro").unbind('click').live('click',function(){
	  		customAlert( { message: {'text':'Do you want to remove this Project?'} }, callbackForDeleteProject, $(this) );
	  	});

		function callbackForDeleteProject(handerResponse, curEle){
			if(handerResponse){
				var id = new_proj_response[0].pkey;
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
			}
		}
});