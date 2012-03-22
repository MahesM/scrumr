<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>

<section>
	<div style="width: 70%;display: inline-block;float: left;">
		<textarea type="text" class="sty_desp_input" placeholder="Story Description..." style="height: 60px;" ></textarea>
	</div>
	<div style="float:right" >
		<select name="stSprint" ></select>
		<div><select name="stSprint" ></select><input id="add_task" type="button" class="submit" value="Add" style="margin: 0 5px;" /></div>
	</div>
	<div style="clear:both;padding:10px;">
		<div class="user_img_cont" >
			<img src="themes/images/1.jpg" class="user_image" />
		</div>
		<div class="task_infor_div" >
			<div>
				<span>#01</span>|
				<span>8 Hours milestone</span>|
				<div class="selectbox_cont" >
               		<select class="sty_status_select" id="" name="">
						<option value="locked">Assigned</option>
						<option value="unlocked">In progress</option>
						<option value="completed">Completed</option>
					</select>
               </div>
			</div>
			<div>.................................................................................</div>
			<div>orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</div>
		</div>
		<div class="contrl_div" >
			<a><img src="themes/images/edit.png" class="cntrl_image" title="Charts" /></a>
			<img src="themes/images/seperator_white.png"  >
			<a ><img src="themes/images/delete.gif" class="cntrl_image" title="Delete"  id="" /></a>			
		</div>
	</div>
</section>