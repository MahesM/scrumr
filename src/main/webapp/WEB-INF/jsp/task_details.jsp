<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>

<section>
	<input type="hidden" id="current_story_id"></input>
	<div style="width: 70%;display: inline-block;float: left;">
		<textarea id="task_content" class="sty_desp_input" placeholder="Story Description..." style="height: 60px;" ></textarea>
	</div>
	<div style="float:right" >
		<select name="task_milestone" id="taskMilestone"></select>
		<div><select name="task_user" id="taskUser"></select><input id="add_task" type="button" class="submit" value="Add" style="margin: 0 5px;" /></div>
	</div>
	<div id="story_task_view">
		<ul class="todo-total-display">
		</ul>
	</div>
</section>