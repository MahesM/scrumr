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
import com.imaginea.scrumr.entities.UserStoryEntity;
import com.imaginea.scrumr.json.ProjectListObject;
import com.imaginea.scrumr.json.ProjectObject;
import com.imaginea.scrumr.utilities.DatabaseConnection;
import com.imaginea.scrumr.utilities.DatabaseHandler;
import com.sun.jersey.api.json.JSONConfiguration;

	@Path("/sprints")
	public class SprintResource {

		@Context
		UriInfo uriInfo;
		@Context
		Request request;
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("{id}")
		public String viewSprint(@PathParam("id") String id) {
			DatabaseHandler data = new DatabaseHandler();
			UserStoryEntity story = data.getUserStoryEntity(Long.parseLong(id));
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString(story);
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
		@Path("/search/{sprintid}")
		public String viewAllSprints(@PathParam("sprintid") String sprintid) {
			SprintEntity list = new SprintEntity();
			DatabaseHandler data = new DatabaseHandler();
			list = data.getSprint(Long.parseLong(sprintid));
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString(list);
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
		@Path("/search")
		public String viewAllSprints(MultivaluedMap<String, String> params) {
			List<SprintEntity> list = new ArrayList<SprintEntity>();
			DatabaseHandler data = new DatabaseHandler();
			list = data.getAllSprints(Long.parseLong(params.getFirst("projectId")));
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString(list);
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
		@Path("/lookup")
		public String viewSprint(MultivaluedMap<String, String> params) {
			SprintEntity sprint = null;
			DatabaseHandler data = new DatabaseHandler();
			sprint = data.getSprint(Long.parseLong(params.getFirst("sprintId")),Long.parseLong(params.getFirst("projectId")));
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString(sprint);
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
        public String createSprint(MultivaluedMap<String, String> params) {
        	System.out.println("IN CREATE SPRINT");
        	DatabaseHandler data = new DatabaseHandler();
        	System.out.println(params);
        	SprintEntity sprint = new SprintEntity();
        	try {
        		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        		Date startdate = format.parse(params.getFirst("startdate"));
        		sprint.setStartdate(new java.sql.Date(startdate.getTime()));
        		Date enddate = format.parse(params.getFirst("startdate"));
        		sprint.setEnddate(new java.sql.Date(enddate.getTime()));
        		if(startdate.after(new Date())){
        			sprint.setStatus("Not Started");
        		}else{
        			if(enddate.before(new Date())){
            			sprint.setStatus("Finished");
            		}else{
            			sprint.setStatus("In Progress");
            		}
        		}
        		
        		ObjectMapper mapper = new ObjectMapper();
        		boolean success = data.createSprint(sprint, Long.parseLong(params.getFirst("projectId")));
        		if(success == true){
        			return mapper.writeValueAsString(sprint);
        		}
					   return mapper.writeValueAsString("{\"result\":false}");
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(ParseException e){
				e.printStackTrace();
			}
			return "{}";
        }
		
		@POST
        @Produces(MediaType.APPLICATION_JSON)
        @Path("/addtosprint")
        public String addStoryToCurrentSprint(MultivaluedMap<String, String> params) {
        	System.out.println("IN ADD SPRINT STORY");
        	DatabaseHandler data = new DatabaseHandler();
        	int currentSprint = Integer.parseInt(params.getFirst("sprint"));
        	String storyId = params.getFirst("stories");
        	String status = params.getFirst("status");
        	try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString("{\"result\":"+data.updateCurrentSprint(storyId,status,currentSprint)+"}");
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
        @Path("/updatestatus")
        public String updateStatus(MultivaluedMap<String, String> params) {
        	System.out.println("IN Status update");
        	DatabaseHandler data = new DatabaseHandler();
        	try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString("{\"result\":"+data.updateStatus(Long.parseLong(params.getFirst("storyid")),params.getFirst("status"))+"}");
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
        @Path("/deletestory")
        public String deleteSprint(MultivaluedMap<String, String> params) {
        	System.out.println("IN Story delete");
        	DatabaseHandler data = new DatabaseHandler();
        	try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString("{\"result\":"+data.deleteStory(Long.parseLong(params.getFirst("storyid")))+"}");
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
		
}