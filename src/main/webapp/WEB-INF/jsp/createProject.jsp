<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>


	<div id="new_pro_container" >
		  <div class="pro_head" id="create_pro_brdcrum" >
		  	<a id="new_pjt" class="pro_head_anchor" >1. New Project</a>
		  	<a id="pjt_stag" class="pro_head_anchor" ><span class="nxt_span" >>></span>2. Project Stages</a>
		  	<a id="sty_parm" class="pro_head_anchor"><span class="nxt_span" >>></span>3. Story Parameters</a>
		  </div>
		  
		  <div id="pjt_create_details" >
			   <input type="text" class="input_title input" name="pTitle" placeholder="Provide project name" required="required" data-pNo="">
			   <textarea cols="160" rows="3" class="input textarea" name="pDescription" placeholder="Provide project description ( optional ) "></textarea>
			   <div>
					<input type="date" id="datepickerFrom" name="pStartDate"  placeholder="Start date" class="cal input" required="required">
					<input type="date" id="datepickerTo" name="pEndDate"  placeholder="End date ( Optional) " class="cal input" >
			   </div>
				<select class="input selectbox" name="pSprintDuration" >
				   <option value="1">1 Week</option>
				   <option value="2">2 Week</option>
				   <option value="3">3 Week</option>
				   <option value="4">4 Week</option>
				   <option value="5">5 Week</option>
			   </select>
			    <div style="float:right;margin-top: 30px;">
			   		<span  class="prv_link" id="create_pjt" >Launch Project ></span>
			   		<input style="border-radius:3px;" type="submit" class="submit" value="Create and continue >>" id="nxt_pjt_details" >
			   </div>
		  </div>
		  
		  <div id="pjt_stages" style="display:none" >
		  		 <div>
		  		 	<div class="pro_head backloghead" >Project Backlog</div>
		  		 	<div class="pro_head backlogdesp" >Planned stories for the project</div>
		  		 	<div class="pro_head backlogdesp"><img src="themes/images/bigarrow_down.png" /></div>
		  		 	<!-- <div class="proj_stage_carousel"> -->
		  		 		<ul id="stageCarousel" class="jcarousel-skin-ie7">
		  		 			
		  		 		</ul>
		  		 	<!-- </div> -->
		  		 </div>
		  		 <div class="nxt_prv_holder" >
		  		 	<span style="float:right">
			  			<!--  span  class="prv_link" id="pjt_stages_prv" ><< Previous</span>  -->
			  			<input style="border-radius:3px;margin-top:0px" type="submit" class="submit" value="Continue >>" id="nxt_pjt_stages" >
			  			<span  class="prv_link" id="lan_pro" >Launch Project ></span>
			  		</span>
			  		<span class="prv_link" id="delete_pro">Delete Project</span>
			  	 </div>
		  </div>
		  
		  <div id="story_parms" style="display:none" >
				  	
		  	<section style="border-radius:6px;height:350px;background:#CACACA" class="skinned-form-controls" >
		  	
		  		<article  class="sty_par_article">
		  			<div class="sty_header" >
		  				<input id="sty_priority_checkbox" type="checkbox" name="priority" value="Story Priority" checked="checked" class="sty_checkbox" />
		  				<span class="chk_header" >Story Priority</span>
		  			</div>
		  			<div class="sty_content" id="sty_content_priority" ></div>
		  			<div id="disable_overlay" class="" style="display:none"><div class="disable_notify" >Select checkbox to add</br><span style="font-weight:bold;padding:5px">Story Priority</span>as Parameter</div></div>
		  		</article>
		  		
		  		<article class="sty_par_article">
		  			<div class="sty_header">
		  				<span class="chk_header" >Story Size</span>
		  			</div>
		  			<div class="sty_content" >
		  				<label class="radio_header" >Size In</label>
		  				<div class="radio_holder" ><input type="radio" name="sty_size_radio" value="0" checked="checked"  /><span>1,2,4,8,16,32</span></div>
		  				<div class="radio_holder" ><input type="radio" name="sty_size_radio" value="1" /><span>1,2,3,5,8,13,21,34,45</span></div>
		  				<div class="radio_holder" ><input type="radio" name="sty_size_radio" value="2" /><span>XS, S, M, L, XL, XXL, XXXL</span></div>
		  				<div>
			  			 	<label class="radio_header jqslider">Size Range</label>
							<p>
								<input type="text" id="amount" style="border:0; color:#000; background:transparent;width:100%;text-align:center" />
							</p>
			  			 	<div id="slider-range"></div>
			  			 	<div class="padding5" id="sty_size_indicator" ><sup>XS</sup><sup style="float:right">XXXL</sup></div>
		  			    </div>	
		  			</div>
		  		</article>
		  		
		  		<article class="sty_par_article">
		  			<div class="sty_header">
		  				<span class="chk_header" >Task Milestone Unit</span>
		  			</div>
		  			<div class="sty_content" >
		  			 <div>
		  				<label class="radio_header">Unit</label>
		  				<div class="radio_holder" ><input type="radio" name="sty_unit_radio" value="0"  checked="checked" /><span>Hours</span></div>
		  				<div class="radio_holder" ><input type="radio" name="sty_unit_radio" value="1" /><span>Days</span></div>
		  			 </div>
		  			 <div>
		  			 	<label class="radio_header">Max Value Allowed</label>
		  			 		<p>
								<input type="text" id="sty_milestone_ran_lbl" value="1" style="border:0; color:#000; background:transparent;width:100%;text-align:center" />
							</p>
		  			 	<div id="slider"></div>
		  			 	<div class="padding5" id="sty_unit_indicator" ><sup>1</sup><sup style="float:right">40</sup></div>
		  			 </div>
		  			</div>
		  		</article>
		  		
		  	</section>
	
			<div class="nxt_prv_holder">
				<span style="float: right;">
		  			<span class="prv_link" id="story_prv">&lt;&lt; Previous</span>
		  			<input style="border-radius:3px;" type="submit" class="submit" value="Launch Project &gt;&gt;"  id="lan_pro" >
		  	 	</span>
		  	 	<span class="prv_link" id="delete_pro">Delete Project</span>
		  	 </div>
		 
		  </div>
	  
	  	  <label id="proj-error" class="error-msg"></label>
	</div>
	
	

