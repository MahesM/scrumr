//execute the ajax call after user has stopped typing for a specified amt of time, here i take 2s
var delay = (function(){
	  var timer = 0;
	  return function(callback, ms){
	    clearTimeout (timer);
	    timer = setTimeout(callback, ms);
	  };
})();

function days_between(date1, date2) {
    var ONE_DAY = 1000 * 60 * 60 * 24;
    var date1_ms = date1.getTime();
    var date2_ms = date2.getTime();
    var difference_ms = Math.abs(date1_ms - date2_ms);
    return Math.round(difference_ms/ONE_DAY);
}

/************************************** CUSTOM ALERT***************************************/ 


var customAlert=  function(options,callback,curObj) {
	
	var curEl = curObj;
	
	var htmlStr = '<div id="alertMsgDiv">'+
					'<div id="confirmheader">Pramati Confirm Box</div>'+
					'<div id="message"></div>'+
					'<div id="btncontainer" >'+
						'<input id="ok" type="button" class="submit" value="Ok" />'+
						'<input id="cancel" type="button" class="submit" value="Cancel" />'+
					'</div>'+
				  '</div>'+
				  //'<div id="overlay" class="ui-widget-overlay" style="z-index: 1002; width: 1263px; height: 1251px; "></div>';
				  '<div id="customAlert_overlay" ></div>';

	var alertMsgStr = ( $("#alertMsgDiv").length ) ? alertMsgStr : htmlStr;
		
	$("body").append(alertMsgStr);
	
	$("#alertMsgDiv").slideDown('slow');
	$("#customAlert_overlay").fadeIn('slow');
	
	// message Style
	{
		$("#message").html(options.message["text"]);
		for(cssprop in options.message) {
			$('#message').css(cssprop, options.message[cssprop]);
		}
	}
	
	//container Style 
	{					
		for(cssprop in options.containerStyle) {
			$('#alertMsgDiv').css( cssprop, options.containerStyle[cssprop] );
		}			
	}
	
	//Button Style 
	{
		for(cssprop in options.btnStyle) {
			$('#round').css( cssprop , options.btnStyle[cssprop] );
		}
	}

	//Header Style 
	{
		for(cssprop in options.headerStyle) {
			$('#confirmheader').css( cssprop , options.headerStyle[cssprop] );			
		}
	}

	$("#cancel").unbind('click').bind('click',function(){
		returnCallback(false);
		hidepopupboxes();
	});
	
	$("#ok").unbind('click').bind('click',function(){
		returnCallback(true);
		hidepopupboxes();
	});
	
	function hidepopupboxes(){
		$("#alertMsgDiv").slideUp('slow');
		$("#customAlert_overlay").fadeOut('slow')
	}
	
	function returnCallback(response) {

		if (typeof callback == 'function') {					
			callback.call(this,response,curEl);
		}
	}
};

/******************************COLOR PICKER - PLUGIN*****************************************/
//PLUGIN FOR SIMPLE COLOR PICKER
/******************************************************************************/

 (function($){
	 $.fn.extend({   
			colorpicker: function(evt, callback) {
				var returnCol ;					
				var htmlStr = 			'<div id="simple_eight_colorpicker" class="cust_col_main_holder" >'+
											'<ul class="color-chooser">'+
												'<li class="labelstyle-444444 ">'+
												  '<label>'+
													'<input id="label_color_444444" name="label[color]" type="radio" value="444444">'+
												  '</label>'+
												'</li>'+
												'<li class="labelstyle-DDDDDD ">'+
												  '<label>'+
													'<input id="label_color_dddddd" name="label[color]" type="radio" value="DDDDDD">'+
												  '</label>'+
												'</li>'+
												'<li class="labelstyle-e10c02 ">'+
												  '<label>'+
													'<input id="label_color_e10c02" name="label[color]" type="radio" value="e10c02">'+
												  '</label>'+
												'</li>'+
												'<li class="labelstyle-d7e102 ">'+
												  '<label>'+
													'<input id="label_color_d7e102" name="label[color]" type="radio" value="d7e102">'+
												  '</label>'+
												'</li>'+
												'<li class="labelstyle-02e10c">'+
												  '<label>'+
													'<input id="label_color_02e10c" name="label[color]" type="radio" value="02e10c">'+
												  '</label>'+
												'</li>'+
												'<li class="labelstyle-02d7e1 ">'+
												  '<label>'+
													'<input id="label_color_02d7e1" name="label[color]" type="radio" value="02d7e1">'+
												  '</label>'+
												'</li>'+
												'<li class="labelstyle-0b02e1 ">'+
												  '<label>'+
													'<input id="label_color_0b02e1" name="label[color]" type="radio" value="0b02e1">'+
												  '</label>'+
												'</li>'+
												'<li class="labelstyle-e102d8 ">'+
												  '<label>'+
													'<input id="label_color_e102d8" name="label[color]" type="radio" value="e102d8">'+
												  '</label>'+
												'</li>'+
											'</ul>'+
											
											'<input style="border-radius:3px;float:right" type="submit" class="submit" value="Done" id="custom_col_save" >'+
										'</div>';
							  
				
				colorpicker = ( $("#simple_eight_colorpicker").length ) ? $("#simple_eight_colorpicker") : htmlStr;
		
				$("body").append(colorpicker);
				$("#simple_eight_colorpicker").css({'left':evt.pageX, 'top':evt.pageY});
				$("#simple_eight_colorpicker").slideDown('slow');
				
				$("input").click(function() {
					$('input[type=radio]').not(':checked').parent().parent().removeClass("selected");
					$(":checked").parent().parent().addClass("selected");
				});
				
				$("#custom_color_link").live('click',function(){
					$(".custom_color_input").slideToggle();
				});
				
				$("#custom_col_save").live('click',function(){
					$("#simple_eight_colorpicker").slideUp('slow');
					returnCallback("#"+$("input[name='label[color]']:checked").val());
				});	
				
				function returnCallback(responseColor) {

					if (typeof callback == 'function') {					
						callback.call(this,responseColor);
					}
					callback = null;
				}
		}
	 });  
 })(jQuery);
 
/*************************************************************************/