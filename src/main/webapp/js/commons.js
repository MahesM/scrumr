//execute the ajax call after user has stopped typing for a specified amt of time, here i take 2s
var delay = (function(){
	  var timer = 0;
	  return function(callback, ms){
	    clearTimeout (timer);
	    timer = setTimeout(callback, ms);
	  };
})();
    		