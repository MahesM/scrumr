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
			   <input style="float:right;border-radius:3px;" type="submit" class="submit" value="Next >>" id="nxt_pjt_details" >
		  </div>
		  
		  <div id="pjt_stages" style="display:none" >
		  		 <div>
		  		 	<div class="pro_head backloghead" >Project Backlog</div>
		  		 	<div class="pro_head backlogdesp" >Planned stories for the project</div>
		  		 	<div class="pro_head backlogdesp"><img src="themes/images/qontext.jpg" /></div>
		  		 	<!-- <div class="proj_stage_carousel"> -->
		  		 		<ul id="stageCarousel" class="jcarousel-skin-ie7">
		  		 			<li>
		  		 				<div class="stage_container">
			  		 				<div class="pro_stage_image"><img src="themes/images/comment.png"></img></div>
			  		 				<div class="pro_stage_title">Sprint Backlog</div>
			  		 				<div class="pro_stage_desc">Stories that are to be assigned yet for this sprint</div>
			  		 			</div>
		  		 			</li>
		  		 			<li>
		  		 				<div class="stage_container">
			  		 				<div class="pro_stage_image"><img src="themes/images/comment.png"></img></div>
			  		 				<div class="pro_stage_title">Development</div>
			  		 				<div class="pro_stage_desc">Stories that are to be assigned yet for this sprint</div>
			  		 			</div>
		  		 			</li>
		  		 			<li>
		  		 				<div class="stage_container">
			  		 				<div class="pro_stage_image"><img src="themes/images/comment.png"></img></div>
			  		 				<div class="pro_stage_title">Review</div>
			  		 				<div class="pro_stage_desc">Stories that are to be assigned yet for this sprint</div>
			  		 			</div>
		  		 			</li>
		  		 			<li>
		  		 				<div class="stage_container">
			  		 				<div class="pro_stage_image"><img src="themes/images/comment.png"></img></div>
			  		 				<div class="pro_stage_title">Finished</div>
			  		 				<div class="pro_stage_desc">Stories that are to be assigned yet for this sprint</div>
			  		 			</div>
		  		 			</li>
		  		 			<li>
		  		 				<div class="stage_container">
			  		 				<div class="pro_stage_image"><img src="themes/images/comment.png"></img></div>
			  		 				<div class="pro_stage_title">Test</div>
			  		 				<div class="pro_stage_desc">Stories that are to be assigned yet for this sprint</div>
			  		 			</div>
		  		 			</li>
		  		 			<li>
		  		 				<div class="stage_container">
			  		 				<div class="pro_stage_image"><img src="themes/images/comment.png"></img></div>
			  		 				<div class="pro_stage_title">Test</div>
			  		 				<div class="pro_stage_desc">Stories that are to be assigned yet for this sprint</div>
			  		 			</div>
		  		 			</li>
		  		 		</ul>
		  		 	<!-- </div> -->
		  		 </div>
		  		 <div class="nxt_prv_holder" >
			  		<span  class="prv_link" id="pjt_stages_prv" ><< Previous</span>
			  		<input style="border-radius:3px;" type="submit" class="submit" value="Next >>" id="nxt_pjt_stages" >
			  	 </div>
		  </div>
		  
		  <div id="story_parms" style="display:none" >
		  	
		  	
		  	<section style="border-radius:6px;height:350px;background:#CACACA" class="skinned-form-controls" >
		  	
		  		<article  class="sty_par_article">
		  			<div class="sty_header" >
		  				<input type="checkbox" name="priority" value="Story Priority" />
		  				<span class="chk_header" >Story Priority</span>
		  			</div>
		  			<div class="sty_content" >
						<div class="pHolder" >
							<div class="pOrder" >P1</div>
							<input class="pInput" placeholder="Type Priority Name" />
							<div class="pColor"></div>
							<div class="pAddRem"></div>
						</div>
						<div class="pHolder" >
							<div class="pOrder" >P2</div>
							<input class="pInput" placeholder="Type Priority Name" />
							<div class="pColor"></div>
							<div class="pAddRem"></div>
						</div>
						<div class="pHolder" >
							<div class="pOrder" >P3</div>
							<input class="pInput" placeholder="Type Priority Name"  />
							<div class="pColor"></div>
							<div class="pAddRem"></div>
						</div>
		  			</div>
		  		</article>
		  		
		  		<article class="sty_par_article">
		  			<div class="sty_header">
		  				<input type="checkbox" name="storySize" value="Story Size" />
		  				<span class="chk_header" >Story Size</span>
		  			</div>
		  			<div class="sty_content" >
		  				<label class="radio_header" >Size In</label>
		  				<div class="radio_holder" ><input type="radio" name="sty_size_radio" value="male" /><span>1,2,4,8,16,32</span></div>
		  				<div class="radio_holder" ><input type="radio" name="sty_size_radio" value="male" /><span>1,2,3,5,8,13,21,34,45</span></div>
		  				<div class="radio_holder" ><input type="radio" name="sty_size_radio" value="male" /><span>XS, S, M, L, XL, XXL, XXXL</span></div>
		  				<div>
			  			 	<label class="radio_header jqslider">Size Range</label>
							<p>
								<input type="text" id="amount" style="border:0; color:#000; background:transparent" />
							</p>
			  			 	<div id="slider-range"></div>
			  			 	<div class="padding5" ><sup>XS</sup><sup style="float:right">XXXL</sup></div>
		  			    </div>	
		  			</div>
		  		</article>
		  		
		  		<article class="sty_par_article">
		  			<div class="sty_header">
		  				<input type="checkbox" name="taskMilestone" value="Task Milestone Unit" />
		  				<span class="chk_header" >Task Milestone Unit</span>
		  			</div>
		  			<div class="sty_content" >
		  			 <div>
		  				<label class="radio_header">Unit</label>
		  				<div class="radio_holder" ><input type="radio" name="sty_unit_radio" value="male" /><span>Hours</span></div>
		  				<div class="radio_holder" ><input type="radio" name="sty_unit_radio" value="male" /><span>Days</span></div>
		  			 </div>
		  			 <div>
		  			 	<label class="radio_header">Max Value Allowed</label>
		  			 	<input type="range" min="1" max="40" value="16" class="slider_style" />
		  			 	<div class="padding5" ><sup>1</sup><sup style="float:right">40</sup></div>
		  			 </div>
		  			</div>
		  		</article>
		  		
		  	</section>
	
		  	 <div class="nxt_prv_holder" >
		  		<span  class="prv_link" id="story_prv" ><< Previous</span>
		  		<input style="border-radius:3px;" type="submit" class="submit" value="Launch Project >>" id="create_pjt" >
		  	 </div>
		  </div>
	   <label id="proj-error" class="error-msg"></label>
	</div>
	
	

