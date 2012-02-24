 var status_dropdown = '<ul class="todo-status-list">';
   	for(var i in taskStatusColors){
   		status_dropdown +='<li><label style="color:'+taskStatusColors[i]+'">'+i+'</label></li>';
   	}
   	status_dropdown +="</ul>";
function populateProjectStatistics(){
	//populate task status drop down
	 var post_data = 'projectId='+projectId+'&pageNumber=1&maxCount=10';
	 $.ajax({
 		url: 'api/v1/todo/details',
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
			url: 'api/v1/todo/update/'+task_id,
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

$('ul#sort-list li').live('click',function(event){
	$(this).closest('ul#sort-list').hide();
	$('.task_sort label').html($(this).html());
	var orderBy = $(this).attr('data-order');
	var post_data = 'projectId='+projectId+'&orderBy='+orderBy+'&pageNumber=1&maxCount=10';
	 $.ajax({
		url: 'api/v1/todo/details',
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
});

$("#searchTodos").live("keyup",function(event) {
	if (event.which == 13 ||event.which == 8) {
		event.preventDefault();
	}
	
	var el = $(this);
	el.css('background-image',"themes/images/ajax-loader.gif");
	delay(function(){
		var query=$('#searchTodos').val();
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
						users_html += '<li><img src="'+qontextHostUrl+total_users[i].basicInfo.avatarUrl+'"/></div><div class="details"><label class="name">'+total_users[i].basicInfo.displayName+'</label><a class="email">'+total_users[i].basicInfo.primaryEmail+'</a></div><div style="float:left;" class="adduser float-rgt enable" id="'+total_users[i].accountId+'"></div></li>';
						
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
				$('.close-search').live('click',function(){
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
