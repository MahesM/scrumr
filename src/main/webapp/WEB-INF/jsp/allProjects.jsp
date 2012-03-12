<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="http://www.springframework.org/security/tags" %>


		
		

		<div id ='allProjects_container' >
				<div class="allPjt_header" >All Projects</div>
				<div id="mainContainer" >
					<div class="pjt_container">
					<span class='pjt_detail_container' >
						<div class="slNo" >10</div>
						<div class="pjt_name" >Project Name</div>
						<div class="pjt_duration" >Jan 16-Mar 03</div>
						<div class="sprt_No" >Sprint 1</div>
						<div class="sprt_status" >In Progress</div>
						<div class="pjt_desp" >Descripton Text about the project</div>
					</span>
					<div class='pjt_member_container' >
						<img src="<%= request.getContextPath() %>/themes/images/1.jpg" class="pjt_mem" />
						<img src="<%= request.getContextPath() %>/themes/images/2.jpg" class="pjt_mem" />
						<img src="<%= request.getContextPath() %>/themes/images/3.jpg" class="pjt_mem" />
						<img src="<%= request.getContextPath() %>/themes/images/4.jpg" class="pjt_mem" />
						<img src="<%= request.getContextPath() %>/themes/images/5.jpg" class="pjt_mem" />
						<a href="#" class="pjt_mem_more" >more...</a>
					</div>
					<div class='pjt_control_container' >
						<img src="<%= request.getContextPath() %>/themes/icons/edit_white.png"  class="pjt_ctrl"  alt="Edit"  title="Edit" />|
						<img src="<%= request.getContextPath() %>/themes/icons/details_white.png" class="pjt_ctrl" alt="Details"  title="Details" />|
						<img src="<%= request.getContextPath() %>/themes/icons/chart_white.png" class="pjt_ctrl"  alt="Chart" title="Charts"/>
					</div>
				</div>
				
				
				<div class="pjt_container">
					<span class='pjt_detail_container' >
						<div class="slNo" >11</div>
						<div class="pjt_name" >Project Name123</div>
						<div class="pjt_duration" >Jan 16-Mar 03</div>
						<div class="sprt_No" >Sprint 1</div>
						<div class="sprt_status" >In Progress</div>
						<div class="pjt_desp" >Descripton Text about the project</div>
					</span>
					<div class='pjt_member_container' >
						<img src="<%= request.getContextPath() %>/themes/images/1.jpg" class="pjt_mem" />
						<img src="<%= request.getContextPath() %>/themes/images/2.jpg" class="pjt_mem" />
						<img src="<%= request.getContextPath() %>/themes/images/3.jpg" class="pjt_mem" />
						<img src="<%= request.getContextPath() %>/themes/images/4.jpg" class="pjt_mem" />
						<img src="<%= request.getContextPath() %>/themes/images/5.jpg" class="pjt_mem" />
						<a href="#" class="pjt_mem_more" >more...</a>
					</div>
					<div class='pjt_control_container' >
						<img src="<%= request.getContextPath() %>/themes/icons/edit_white.png"  class="pjt_ctrl" />|
						<img src="<%= request.getContextPath() %>/themes/icons/details_white.png" class="pjt_ctrl" />|
						<img src="<%= request.getContextPath() %>/themes/icons/chart_white.png" class="pjt_ctrl" />
					</div>
				</div>
				</div>
				
				
		</div>
