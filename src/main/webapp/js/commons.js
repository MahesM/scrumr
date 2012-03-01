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

/***********************************************************************/ 