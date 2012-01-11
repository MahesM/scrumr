package com.imaginea.scrumr.resources;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.imaginea.scrumr.entities.ProjectEntity;
import com.imaginea.scrumr.entities.SprintEntity;
import com.imaginea.scrumr.entities.UserEntity;
import com.imaginea.scrumr.json.ProjectListObject;
import com.imaginea.scrumr.json.ProjectObject;
import com.imaginea.scrumr.utilities.DatabaseConnection;
import com.imaginea.scrumr.utilities.DatabaseHandler;
import com.sun.jersey.api.json.JSONConfiguration;

	@Path("/project")
	public class ProjectResource {

		@Context
		UriInfo uriInfo;
		@Context
		Request request;
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("{id}")
		public String viewProject(@PathParam("id") String id) {
			DatabaseHandler data = new DatabaseHandler();
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString(data.getProject(Long.parseLong(id)));
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{}";
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/search/{id}")
		public String viewAllProjects(@PathParam("id") String id) {
			DatabaseHandler data = new DatabaseHandler();
			List<ProjectEntity> projectList = data.getAllProjects(id);
			try {
				ObjectMapper mapper = new ObjectMapper();
				if(projectList != null){
					return mapper.writeValueAsString(new ProjectListObject(projectList.size(),"","",projectList));
				}else{
					return mapper.writeValueAsString("{\"error\":\"No projects\"}");
				}
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{}";
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/adduser")
		public String addUser(MultivaluedMap<String, String> params) {
			DatabaseHandler data = new DatabaseHandler();
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString("{\"result\":"+data.addUserToProject(params.getFirst("userid"),Long.parseLong(params.getFirst("projectId")))+"}");
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{}";
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/removeuser")
		public String removeUser(MultivaluedMap<String, String> params) {
			DatabaseHandler data = new DatabaseHandler();
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString("{\"result\":"+data.removeUserFromProject(params.getFirst("userid"),Long.parseLong(params.getFirst("projectId")))+"}");
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{}";
		}
		
		@POST
        @Produces(MediaType.APPLICATION_JSON)
        @Path("/create")
        public String createProject(MultivaluedMap<String, String> params ) {
			try {
				String current_user = params.getFirst("current_user");
				System.out.println("IN CREATE");
				DatabaseHandler data = new DatabaseHandler();
				System.out.println(params);
				ProjectEntity project = new ProjectEntity();
				project.setTitle(params.getFirst("pTitle"));
				project.setDescription(params.getFirst("pDescription"));
				Date date = null;
				Date date1 = null;
				int sprint_count =0;
				int duration = Integer.parseInt(params.getFirst("pSprintDuration"));
				if(params.getFirst("pStartDate")!= "" && params.getFirst("pEndDate") != ""){
					try {
						SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
						date = format.parse(params.getFirst("pStartDate"));
						project.setStart_date(new java.sql.Date(date.getTime()));
						if(date.after(new Date()) || date.equals(new Date())){
							project.setCurrent_sprint(1);
							project.setStatus("Not Started");
						}else{
							int cur = getCurrentSprint(date,Integer.parseInt(params.getFirst("pSprintDuration")));
							System.out.println("CS: "+cur);
							project.setCurrent_sprint(cur <= 0? 1: cur);
							project.setStatus("In Progress");
						}
						SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
						date1 = format1.parse(params.getFirst("pEndDate"));
						project.setEnd_date(new java.sql.Date(date1.getTime()));
						sprint_count  = getSprintCount(date, date1,Integer.parseInt(params.getFirst("pSprintDuration")));
						project.setNo_of_sprints(sprint_count);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
				String userStr = params.getFirst("assignees");
				String[] users = userStr.split(",");
				Set<UserEntity> userList =  new HashSet<UserEntity>();
				for(String s: users){
					System.out.println("user: "+s);
					UserEntity user = data.getUserInfo(s);
					userList.add(user);
					System.out.println("Added: "+user.toString());
				}
				project.setAssignees(userList);
				project.setSprint_duration(duration);
				project.setCreatedby(current_user);
				project.setLast_updated(new java.sql.Date(System.currentTimeMillis()));
				project.setLast_updatedby(current_user);
				project.setCreation_date(new java.sql.Date(System.currentTimeMillis()));

				data.createProject(project);
				Date currentdate = date;
				for(int i=0;i<sprint_count;i++){

					SprintEntity sprint = new SprintEntity();
					sprint.setStartdate(new java.sql.Date(currentdate.getTime()));
					Date enddate = new Date(currentdate.getTime() + ((7*duration)*23*59*58*1000));
					if(enddate.before(date1)){
						sprint.setEnddate(new java.sql.Date(enddate.getTime()));
					}else{
						sprint.setEnddate(new java.sql.Date(date1.getTime()));
					}
					if(currentdate.after(new Date())){
	        			sprint.setStatus("Not Started");
	        		}else{
	        			if(enddate.before(new Date())){
	            			sprint.setStatus("Finished");
	            		}else{
	            			sprint.setStatus("In Progress");
	            		}
	        		}
					data.createSprint(sprint,project.getId());
					currentdate = new Date(currentdate.getTime() + ((7*duration)*24*60*60*1000));;
				}
				
                ObjectMapper mapper = new ObjectMapper();
                       return mapper.writeValueAsString(project);
            } catch (JsonGenerationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "{}";
        }
		
		int getSprintCount(Date start, Date end, int duration){
			int count = (int)(((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))/(7*duration));
			int rem = (int)(((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))%(7*duration));
			if(rem > 0){
				return count + 1;
			}
			return count;
		}
		
		int getCurrentSprint(Date start, int duration){
			return (int) Math.ceil((( System.currentTimeMillis() - start.getTime())/ (1000 * 60 * 60 * 24))/(7*duration)) + 1;
		}
}