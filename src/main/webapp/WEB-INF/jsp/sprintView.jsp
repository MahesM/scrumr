<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE HTML>
<html>
<head>
    <title>Scrumr</title>
    <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/themes/javascript/pagination.css" />
</head>

<body>
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
	<% } %>
	var taskStatusColors = {'CREATED':'#1e9ce8','IN_PROGRESS':'#f4b02c','COMPLETED':'#6b9d1c'};
 </script>
 <script type="text/javascript" src="<%= request.getContextPath() %>/js/commons.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/taskview.js?version=1"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/sprintview.js?version=1"></script>
<div class="main">
    <section class="left bg-pat">
    	<div class="left_tab">
    		<div class="left_pane">
    			<a class="collapse" href="javascript:void(0);"></a>
    		</div>
    		<div class="tab peopleview">
    			<label>Members</label>
    			<a id="addPeople" href="javascript:void(0);"></a>
    			<img class="project_menu_arrow" src="themes/images/menu_arrow.png"></img>
    		</div>
    		<div class="tab backlogview active_tab">
    			<label>Backlog</label>
    			<a id="addStory" href="javascript:void(0);"></a>
    			<img class="project_menu_arrow" src="themes/images/menu_arrow.png"></img>
    		</div>
    	</div>
        <div id="peopleview" class="cont float-lft" style="display:none;">

            <ul id="people" class="proj_ppl_list"></ul>
            
            <div  id="add_ppl_info"  >
            	<div >
            		To add,click 
            		<img src="themes/images/add_people.png" />
            		in the respective tab
            	</div>
            	<div style="overflow:hidden" >..........................................................................</div>
            	<div >To assign drag and drop<b> story </b>to the</br>Sprint lane and <b> Member </b>to sprint story</div>
            </div>

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
        
        <div id="backlogview" class="scroll-cont float-lft">
            <div class="stories-cont">
               <!--  <div id="storyLabel" class="float-lft">
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
						</div> -->
                <div class="cont" >
               		<input id="backlog_seach_input" placeholder="-- Type here to search backlogs --" />
                	<div id="storyList">						
						<img src= "themes/images/search.png" style="margin-left:-25px;vertical-align: middle;" />
                		<ul class="story"></ul>
                	</div>
                </div>
            </div>
        </div>
    </section>
    <section class="left_collapsed bg-pat" style="display:none;">
    	<div class="left_collapsed_tab">
    		<div class="left_pane">
    			<a class="expand" href="javascript:void(0);"></a>
    		</div>
    		<div class="tab backlogview">
    			<a id="addStory" href="javascript:void(0);"></a>
    			<label>7 Backlog</label>
    		</div>
    		<div class="tab peopleview">
    			<a id="addPeople" href="javascript:void(0);"></a>
    			<label>1 Member</label>
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
				
				
           
            <div class="sprint-cont float-lft" style="border:0;" id="contentHolder" >
	            <!-- <div class="view-cont float-lft">
	                <div class="view-hd">
	                	<div class="prj-view-hd"><label class="projectview" >Project View</label></div>
		                <div class="sp-view-hd"><label class="sprintview" >Sprint View</label></div>
		                <div class="pstat-view-hd"><label class="projectstatview">Task View</label></div>
		                <a href="" class="customize float-rgt">Customize</a>
		                <div class="error-hd" style="display:none;">
		                	<label></label><a id="error_hd_close" href="javascript:void(0)"></a>
		                </div>
	                </div>
                 
	            </div> -->
                <div id="project-view" style="overflow:hidden;" class="float-lft clear col-cont" >
                </div>
                
                <div id="sprint-view" style="overflow:hidden;" class="sprint-detail float-lft clear col-cont" >
                </div>
                
                <div id="pstat-view" >
               	   	<div id="task_report" style="float:left;width:100%" class="sprintInfoBand" >
                		<input type="text" value="" id="searchTodos" placeholder="Search todos..."></input>
                		<label style="float:left;margin-top:5px;">&nbsp;&nbsp;| Sort By :</label>
                		<div class="task_sort">
                			<label>Created on</label>
                			<a href="javascript:void(0);"></a>
                			<ul id="sort-list" style="display:none;">
	                			 <li data-order="createdOn">Created on</li>
	                			 <li data-order="content">Content</li>
	                			 <li data-order="timeInDays">Milestone Period</li>
	                			 <li data-order="status">Status</li>
                			</ul>
                		</div>
                	</div>
		            <table class="project-list">
		             	 <thead>
			             	 <tr class="header">
			             	 	<td>To do</td>
			             	 	<td>Milestone</td>
			             	 	<td>Status</td>
			             	 	<td>Member</td>
			             	 	<td>Actions</td>
			             	 </tr>
			             </thead>
		             	 <tbody class="content"></tbody>
		              </table>
	             </div> 
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
	        							<div id="story-section" class="story_section">
	        								<p id="st-title"></p>
	        								<p id="st-description"></p>
	        								<p id="st-priority"></p>
	        								<p id="st-sprint"></p>
	        								<p id="st-edit"><a href="javascript:void(0);" class="st_edit_story">Edit Story</a></p>
	        							</div>
	        							<div id="story-edit-section" style="display:none;" class="story_section">
	        								<input type="text" id="st-edit-title" /><!-- <p id="st-title"></p> -->
	        								<textarea style="resize:none;" name="st-edit-description"></textarea><!-- <p id="st-description"></p> -->
	        								<div id="st-edit-priority" class="custom-select">
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
										    </div><!-- <p id="st-priority"></p> -->
	        								<div class="stSprint">
												<select name="st-edit-sprint" id="st-edit-sprint">
												</select>
											</div><!-- <p id="st-sprint"></p> -->
	        								<div class="st-edit-action">
	        									<a href="javascript:void(0);" class="st_edit_cancel">Cancel</a>
	        									<input id="st-edit-done" type="button" class="float-rgt submit" value="Done"/>
	        								</div>
	        							</div>
	        							<div id="story-creator-section" class="story_creator_section">
	        								<div class="div">
	            								<label>Created On</label>
	            								<div id="st-createdOn" style="clear:both;font-size:13px;" class=""></div>
	           								</div>
	        								<div class="div">
	            								<label>Created By</label>
	            								<div id="st-creator" style="clear:both;" class="user"></div>
	           								</div>
	            							<div class="div">
	            								<label>Assigned to</label>
	            								<div id="st-assignees" style="clear:both;"></div>
					                        	<a class="stAddmore" style="font-size:13px;" href="javascript:void(0);">Add Members</a>
	            							</div>
	            							<div class="div" style="display:none;" id="stPeople">
					                       		<div id="st-users"></div>
						                	</div>
	        							</div>
	        						</div>
            					</div>
          						<div class="acc">
        							<div class="acc-head">
            							<label>To-dos</label>
							            <div class="open"></div>
							            <input type="hidden" id="todos-count" />
							        </div>
							        <div id="todo_section" class="acc-content" style="display:none;position:relative;">
							            <div class="todo float-lft">
							            	<div class="todo-display">
						                 		<ul class="todo-total-display">
						                	 		
						                 		</ul>
											</div>
					                       	<div id="todo-box" class="comment-cont">
					  							<form id="todo-form"> 
							                 	<textarea style="resize:none;" class="todo-text" placeholder="Write a Todo..." name="todo"></textarea>
							                 	<select id="todo-milestones" class="todo-select">							                 		
							                 		<option value="1" selected="selected">1 Day MileStone Period</option>
							                 		<option value="2">2 Days MileStone Period</option>
							                 		<option value="3">3 Days MileStone Period</option>
							                 		<option value="4">4 Days MileStone Period</option>
							                 		<option value="5">5 Days MileStone Period</option>
							                 		<option value="6">6 Days MileStone Period</option>
							                 		<option value="7">7 Days MileStone Period</option>
							                 	</select>
							                 	<select id="todo-user" class="todo-select">							                 		
							                 	</select>
							                 	<input id="tAdd" class="submit" style="margin-left:100px;" type="button" value="Add" />
							                 	</form>
					                       </div>
					                                              
						                </div>
						            </div>
            					</div>
            					<div class="acc">
        							<div class="acc-head">
            							<label>Discussions</label>
							            <div class="open"></div>
							        </div>
							        <div id="discussion_section" class="acc-content" style="display:none"></div>
            					</div>
         						<div class="acc">
        							<div class="acc-head">
            							<label>Story History</label>
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
	        							<div class="comments float-lft">
	        								<div class="comments-display">
								                 <ul>
	
								                 </ul>
											</div>
							                 <div id="comments-box" class="comment-cont">
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
       
       <div class="sprint-popup" style="display:none;">
           <div class="c-box">
               <div class="c-box-head">Add New Sprint </div>
               <div class="c-box-content">
                   <input type="date" id="sprint_start" name="sprintStart" class="cal inp-box" placeholder="Start date" required="required"/>
	           	   <input type="date" id="sprint_end" name="sprintEnd" class="cal inp-box margin-left" placeholder="End date"/>
               </div>
                 <div class="actions-cont">
                 	<a id="popup_sprint_cancel">later</a>
                 	<input id="createSprint" type="button" class="submit" value="Done"/>
                </div>
               <div id="pointerEl" class="pointer"></div> 
               <div id="sprintClose" class="sprint_close"></div> 
           </div>
       </div>
       
    </section>
  
</div>
 
</body>
</html>
