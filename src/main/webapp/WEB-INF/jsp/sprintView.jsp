<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE HTML>
<html>
<head>
    <title>Scrumr</title>
    <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/pagination.css" />
    <jsp:include page="header.jsp" />
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/pagination.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/themes/javascript/highcharts.js"></script>
	
 <%
	String view = request.getParameter("view");
   	String projectId = request.getParameter("projectId");
    String visit = request.getParameter("visit");
 %>
 <script type="text/javascript">
	var data = '<s:property value="successResponse"/>';
	var storyDescLimit =  '<s:property value="storyDescLimit"/>';
	var storyTitleLimit =  '<s:property value="storyTitleLimit"/>';
	var projectId="";
	var firstVisit = false;
	var project_view = 1;
	<% if(projectId != ""){ %>
		projectId = <%=projectId%>;
	<%}%>
	<% if(visit != null && visit.equals("1")){ %>
		firstVisit = true;
	<%}%>
	<% if(view != null && view.equalsIgnoreCase("sprint")){ %>
		project_view = 0;
		$('.sprints').show();
		$('.sprintview').css('color',"#00475C");
		$('.sprintview').parent().css('background-color',"#F6EEE1");
	   	$(".projectview").css('color',"gray");
	   	$(".projectview").parent().css('background-color',"#FFFFFF");
	<% } %>
 </script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/sprintview.js"></script>
 
</head>
<body>
<header>
    <a href="<%= request.getContextPath() %>/home.action" class="logo float-lft"></a>
    <div class="tabs project-tab"><a class="projects" href="<%= request.getContextPath() %>/login.action">Projects</a></div>
    <div class="right-div" style="float:right;"></div>
</header>
<div class="content">
    <section class="left bg-pat">
        <div class="cont float-lft">
            <label id="projectName" class="title"></label>
            <ul id="people" class="img-team">
            </ul>
            <a id="addPeople" href="javascript:void(0);">Add</a> /
            <a id="removePeople" href="javascript:void(0);">Remove</a> 
            <div style="display:none;overflow:hidden !important;">
            	<div id="people-cont">
	                <div id="userInput" class="">
		            <!--     <input type="text" id="searchUser" placeholder="Search user by name..."/> -->
	                </div>
	                <div id="userList-cont">
	                </div>
            	</div>
            </div>
        </div>
        
        <div class="scroll-cont float-lft">
            <div class="stories-cont">
                <div id="storyLabel" class="float-lft">
                       <label>User stories</label>
                       <a id="addStory" style="float:right;" href="javascript:void(0);">Add Story</a>
                      
                </div>
                <div id="storyInput" class="float-lft">
	                <input type="text" id="searchStory" placeholder="Search story by keyword..."/>
	                
                </div>
                 <div class="priority">
							<div class="p1"></div>
							<div class="p2" ></div>
							<div class="p3" ></div>
						</div>
                <div class="cont" >
                	<div id="storyList">
                	<ul class="story">
                	</ul>
                	</div>
                </div>
            </div>
        </div>
    </section>
    <section class="right">
        <div class="content float-lft">
            <%-- <div class="chart-container">
					<div class="float-lft" style="width: 50%;">
						<p>Burn out charts</p>
						<select class="float-lft" name="sprintDuration">
						<option name="xxx">xxx</option>
						<option name="yyy">yyy</option>
						</select>
						<div id="lineChart"></div>
					</div>
					<div class="float-lft" style="width: 50%;">
						<p>Sprint Status</p>
						<select class="float-lft" name="sprint">
						<option name="xxx">xxx</option>
						<option name="yyy">yyy</option>
						</select>
						<div id="pieChart"></div>
					</div>
				</div>
				
				<div class=toggleChartLine>
				<a class="toggleChart" href="javascript:void(0);"></a>
				</div> --%>
				
				
           
            <div class="sprint-cont float-lft" style="border:0;">
	            <div class="view-cont float-lft">
	                <!-- <ul class="sprints float-lft">
	                </ul> -->
	                <div class="view-hd">
	                	<div class="prj-view-hd"><label class="projectview">Project View</label></div>
		                <div class="sp-view-hd"><label class="sprintview">Sprint View</label></div>
		                <div class="pstat-view-hd"><label class="projectstatview">Project Statistics</label></div>
		                <!-- <a href="" class="customize float-rgt">Customize</a> -->
		                <div class="error-hd" style="display:none;">
		                	<a id="error_hd_close" href="javascript:void(0)"></a>
		                </div>
	                </div>
	                <div class="duration-hd">
	                	<label style="display:none;"></label>
	                	<ul class="sprints float-lft">
	                	</ul> 
	                	<div id="pageCtrls" style="display:none;float:right;width:200px;height:30px;"></div>
	                </div>
	                 
	            </div>
                <div id="project-view" style="overflow:hidden;" class="float-lft clear col-cont" >
                </div>
                
                <table id="sprint-view" class="sprint-detail">
                    <tbody>
                    </tbody>
                </table>
                
                <table id="pstat-view" class="project-list">
             	 <thead>
	             	 <tr class="header">
	             	 	<td>Users</td>
	             	 	<td>No.of tasks</td>
	             	 	<td>Total Working hours</td>
	             	 </tr>
	             </thead>
             	 <tbody class="content">
             	 	<tr class="odd">
             	 		<td>Sangeetha</td>
	             	 	<td>10</td>
	             	 	<td>12</td>
             	 	</tr>
             	 	<tr class="even">
             	 		<td>Chander Pechetty</td>
	             	 	<td>38</td>
	             	 	<td>78</td>
             	 	</tr>
              	 </tbody>
              </table> 
            </div>
            
            <div style="display:none;overflow:hidden !important;">
            	<div id="story-cont">
                       <div class="popup">
    						<div class="acc-cont">
        						<div class="acc">
	        						<div class="acc-head">
	            						<label>Story Details</label>
	            						<div class="down"></div>
	        						</div>
	        						<div id="story_details_section" class="acc-content">
	            						<p id="st-title"></p>
							                <p id="st-description"></p>
	            						<div class="div">
	            							<label>Created by</label>
	            							<div id="st-creator" style="clear:both;" class="user"></div>
	           							</div>
	            						<div class="div">
	            							<label>Owned by</label>
	            							<div id="st-assignees" style="clear:both;">
					                       		
					                       </div>
					                       <span class="stAddmore" style="font-size:12px;cursor:pointer;">+Add People</span>
	            						</div>
	            						<div style="display:none;clear:both;" id="stPeople">
					                       <label>People available:</label>
					                       <div id="st-users">
						                      
						                   </div>
						                </div>
	        						</div>
            					</div>
          						<div class="acc">
        							<div class="acc-head">
            							<label>Todos</label>
							            <div class="open"></div>
							            <input type="hidden" id="todos-count" />
							        </div>
							        <div id="todo_section" class="acc-content" style="display:none">
							            <div class="todo" class="float-lft">
						                    <label>Todo List:</label>
					                       	<div id="todo-box" class="comment-cont">
					                       		<div class="todo-box-user float-lft">
													<img src="themes/images/1.jpg"/>
												</div>
					  							<form id="todo-form"> 
							                 	<textarea style="resize:none;" class="todo-text" placeholder="Write a Todo..." name="todo"></textarea>
							                 	<select id="todo-milestones">							                 		
							                 		<option value="1 Day" selected="selected">1 Day</option>
							                 		<option value="2 Days">2 Days</option>
							                 		<option value="3 Days">3 Days</option>
							                 		<option value="4 Days">4 Days</option>
							                 		<option value="5 Days">5 Days</option>
							                 		<option value="6 Days">6 Days</option>
							                 		<option value="7 Days">7 Days</option>
							                 	</select>
							                 	<input class="submit" type="button" value="Done" />
							                 	</form>
							                 	<div class="todo-display">
							                 		<ul>
							                	 		
							                 		</ul>
												</div>
					<!--  		                 	<textarea class="todo-text" placeholder="Write a Todo..." name="todo"></textarea>
							                 	<input type="hidden" id="todo-milestone-picker" /> -->
					                       </div>
					                                              
						                </div>
						            </div>
            					</div>
         						<div class="acc">
        							<div class="acc-head">
            							<label>History</label>
							            <div class="open"></div>
							            <div id="history_section" class="acc-content" style="display:none"></div>
        							</div>

            					</div>
         						<div class="acc">
        							<div class="acc-head">
            							<label>Comments</label>
            							<input type="hidden" id="comments-count" />
							            <div class="open"></div>
        							</div>
        							<div id="comments_section" class="acc-content" style="display:none">
	        							<div class="storyComments" class="float-lft">
							                 <label>Comments:</label>
							                 <div class="comment-cont">
												<div class="comment-display">
								                 <ul>

								                 </ul>
												</div>
								                 <div class="comment-box">
													<div class="comment-box-user float-lft">
														<img src="themes/images/1.jpg"/>
													</div>
								                 	<textarea style="resize:none;" class="comment-text" placeholder="Write a comment..." name="commment"></textarea>
								                 </div>
							                 </div>
							                </div>
							            </div>
					            </div>
   							 </div>
						</div>
                       
                       
	                </div>
            	</div>
            </div>
        </div>
        <input type="hidden" id="current_story_id" />
        
        <div class="popup-proj-cont" style="display:none;">
           <div class="c-box">
               <div class="c-box-head">Add people to the project</div>
               <div class="c-box-content">
               		<div id="userInput" class="">
		                <input type="text" id="searchUser" placeholder="Search user by name..."/>
		                <a class="search-input" href="javascript:void(0);" ></a>
	                </div>
	               <div class="user-loading" id="user_loading" style="display:none;"></div>
	               
                   <ul id="total-user-list">
                   
                       
                   </ul>
				   <div class="more-load" style="display:none;"></div>
               </div>
              
                 <div class="actions-cont float-rgt">

                       <input id="popup_proj_done" type="button" class="float-rgt submit" value="Done"/>
                   </div>
               <div id="pointerEl" class="pointer"></div> </div>
       </div>
        
         <div class="popup-story-cont" style="display:none;">
           <div class="c-box">
               <div class="c-box-head">Assign to </div>
               <div class="c-box-content">
                   <ul id="proj-user-list">
                   
                       
                   </ul>

               </div>
                 <div class="actions-cont float-rgt">

                       <input id="popup_story_done" type="button" class="float-rgt submit" value="Done"/>
                       <a id="popup_cancel">later</a>
                   </div>
               <div id="pointerEl" class="pointer"></div> </div>
       </div>
       
       <div class="story-popup" style="display:none;">
           <div class="c-box">
               <div class="c-box-head">Add Story to this project </div>
               <div class="c-box-content">
                   <form id="story_form" method="POST">
						<div class="sTitle">
							<input id="storyDesc" style="resize:none;" name="storyTitle" placeholder="Enter Story Title" size="100" required="required"></input>
						</div>
						<div class="sDesc" >
							<textarea required="required" id="storyDesc" style="resize:none;" maxlength="500" name="storyDesc" rows="5" cols="1" placeholder="Enter Story Desc"></textarea>
						</div>
						<label class="story_error"></label>
						
						<div class="custom-select">
					        <div class="option">
					            <div class="color p1"></div>
					            <div class="label" data-value="1">Priority 1</div>
					        </div>
					        <ul class="option-list">
					            <li>
					                <div class="option">
					                    <div class="color p1"></div>
					                    <div class="label" data-value="1">Priority 1</div>
					                </div>
					            </li>
					            <li>
					                <div class="option">
					                    <div class="color p2"></div>
					                    <div class="label" data-value="2">Priority 2</div>
					                </div>
					            </li>
					            <li>
					                <div class="option">
					                    <div class="color p3"></div>
					                    <div class="label" data-value="3">Priority 3</div>
					                </div>
					            </li>
					        </ul>
					    </div>
						<%-- <div class="sPriority" >
							<select name="stPriority" id="storyPriority">
								<option selected="selected" value="1">Priority 1</option>
								<option value="2">Priority 2</option>
								<option value="3">Priority 3</option>							
							</select>
						</div> --%>
						<div class="stSprint">
							<select name="stSprint" id="storySprint">
							</select>
						</div>
						
					</form>
               </div>
                 <div class="actions-cont">
                 	<a id="popup_story_cancel">later</a>
                 	<input id="createStory" type="button" class="submit" value="Done"/>
					<input id="addAnotherStory" type="button" class="submit" value="Add another story"/>
                </div>
               <div id="pointerEl" class="pointer"></div> 
               <div id="storyClose" class="story_close"></div> 
           </div>
       </div>
    </section>
</div>
</body>
</html>
