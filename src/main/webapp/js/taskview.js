 
function populateProjectStatistics(){
	//populate task status drop down
   	var status_dropdown = '<ul class="todo-status-list">';
   	for(var i in taskStatusColors){
   		status_dropdown +='<li><label style="color:'+taskStatusColors[i]+'">'+i+'</label></li>';
   	}
   	status_dropdown +="</ul>";
	 var post_data = 'projectId='+projectId+'&pageNumber=1&maxCount=10';
	 $.ajax({
 		url: '/scrumr/api/v1/todo/details',
 		type: 'GET',
 		data : post_data,
 		async:false,
 		success: function( reports ) {
 			var taskReportHtml = "";
 			for(var i=0;i<reports.length;i++){
 				var report = reports[i];
 				var even = "odd";
 				if((i+1)%2 == 0){
 					even = "even";
 				}
 				taskReportHtml +='<tr id="todoReport-'+report.pkey+'" class='+even+'><td>'+report.content+'</td><td>'+report.milestonePeriod+' Day </td><td><div class="todo-status"><label style="color:'+taskStatusColors[report.status]+'" class="todo-status-text">'+report.status+'</label><a href="javascript:void(0);"></a>'+status_dropdown+'</div></td><td><img src="'+qontextHostUrl+report.user.avatarurl+'"></img></td><td><div class="todo-action"><a id=tedit'+report.pkey+' class="cmtEditTodo ctedit" href="javascript:void(0);"></a><a id=tdelete'+report.pkey+' class="cmtRmvTodo ctremove" href="javascript:void(0)"></a></div></td></tr>';
 			}
 			
 			$('#pstat-view').find('tbody').html(taskReportHtml);
 		}
	 });
 }

$('tbody.content .todo-status a').live('click',function(event){
	 var id = $(this).closest('tr').attr('id').split('todoReport-')[1];
	 $(this).closest('tr').find('ul.todo-status-list').attr('id','status-'+id);
     if($(this).closest('tr').find('ul.todo-status-list').is(':visible')){
    	 $(this).closest('tr').find('ul.todo-status-list').slideUp();
     }else{
    	 $(this).closest('tr').find('ul.todo-status-list').css('left',($(this).position().left - 50)+'px');
    	 $(this).closest('tr').find('ul.todo-status-list').css('top',($(this).position().top + 20) +'px');
    	 $(this).closest('tr').find('ul.todo-status-list').slideDown();
    }
    

});

$('ul.todo-status-list li').live('click',function(event){
	var user = userLogged;
    var task_id = $(this).closest('ul').attr('id').split("status-")[1];
    var c = $('tr#todoReport-'+task_id).find('.todo-status');
    var status = $(this).find('label').html();
    var post_data = 'id='+task_id+'&status='+status+'&user='+user;
    var el = $(this);
    $.ajax({
			url: '/scrumr/api/v1/todo/update/'+task_id,
			type: 'POST',
			data: post_data,
			async:false,
			success: function( todo ) {  
				c.find('label:first').replaceWith($(el).html());
				c.find('label:first').addClass('todo-status-text');
				$(el).closest('ul.todo-status-list').hide();
			},
			error: function(data) { },
			complete: function(data) { }            		
      	});
    
    return false;
});

$('.task_sort a').live("click",function(){
	if($(this).parent().find('ul#sort-list').is(':visible')){
		$(this).parent().find('ul#sort-list').hide();
	}else{
		$(this).parent().find('ul#sort-list').show();
	}
});