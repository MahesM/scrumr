<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>


<section id ='story_details_container' >
	<article style="width:60%;float:left" >
		<input type="text" class="sty_title_input" id="sty_title_input" placeholder="Story Title..." />
		<textarea type="text" class="sty_desp_input" id="sty_desp_input" placeholder="Story Description..." ></textarea>
		<div class="add_member_container" style="margin-top:10px" >
			<div class="assignees_header" >Assignees</div>
			<div class="mem_list_holder" id="mem_list_holder" ></div>
		</div>
	</article>
	<article style="display:inline-block;width:40%;">
	 <div style="float:right;width:80%">
		<div class="slider_cont" >
			<p>
				<input type="text" id="sty_amount_slider" disabled style="border:0; color:#000; background:transparent;width:100%;text-align:center" />
			</p>
			<div id="sty_slider-range"></div>
			<div style="padding:10px 5px" id="sty_size_indicator" ><sup>1</sup><span class="sty_point_head" >Story Points</span><sup style="float:right">32</sup></div>
		</div>
		<div id="story_detail_custom" class="custom-select">
	        <div class="option">
	            <div class="color p1"></div>
	            <div class="label" data-value="1">Priority 1</div>
	        </div>
	        <ul class="option-list"></ul>
	    </div>
		<div class="stSprint">
			<select name="stSprint" id="storySprint">
			</select>
		</div>
		<div class="add_tag_container" >
			<div>
				<img src="themes/images/add.png"  style="float:left;position:absolute" />
				<input type="text" class="sty_title_input add_mem" placeholder="Type tag name and press ENTER ... "  id="add_tags" >
			</div>
			<div class="mem_list_holder" id="tag_list_holder" ><ul style="list-style-type: none;" ></ul></div>
		</div>
	  </div>
	</article>
	<div class="border_bottom_div" >
		<a id="save_continue_sty" >Save and continue to add tasks >></a>
	</div>
</section>
