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
import com.imaginea.scrumr.entities.UserEntity;
import com.imaginea.scrumr.entities.UserStoryEntity;
import com.imaginea.scrumr.json.ProjectListObject;
import com.imaginea.scrumr.json.ProjectObject;
import com.imaginea.scrumr.utilities.DatabaseConnection;
import com.imaginea.scrumr.utilities.DatabaseHandler;
import com.sun.jersey.api.json.JSONConfiguration;

	@Path("/stories")
	public class UserStoryResource {

		@Context
		UriInfo uriInfo;
		@Context
		Request request;
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("{id}")
		public String viewStory(@PathParam("id") String id) {
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
		@Path("/search/{projectid}")
		public String viewAllStories(@PathParam("projectid") String projectId) {
			List<UserStoryEntity> list = new ArrayList<UserStoryEntity>();
			DatabaseHandler data = new DatabaseHandler();
			list = data.getAllUserStoryEntities(Long.parseLong(projectId));
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
		public String viewUnAssignedStories(MultivaluedMap<String, String> params) {
			List<UserStoryEntity> list = new ArrayList<UserStoryEntity>();
			DatabaseHandler data = new DatabaseHandler();
			String nameString = params.getFirst("nameString");
			list = data.getAllUserStoryEntities(Long.parseLong(params.getFirst("projectId")),Integer.parseInt(params.getFirst("sprintId")),nameString);
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
        @Path("/create")
        public String createUserStory(MultivaluedMap<String, String> params) {
        	System.out.println("IN CREATE STORY");
        	DatabaseHandler data = new DatabaseHandler();
        	System.out.println(params);
        	UserStoryEntity story = new UserStoryEntity();
        	story.setTitle(params.getFirst("stTitle"));
        	story.setDescription(params.getFirst("stDescription"));
        	story.setPriority(Integer.parseInt(params.getFirst("stPriority")));
        	story.setCreatedby(params.getFirst("user"));
        	story.setLast_updated(new java.sql.Date(System.currentTimeMillis()));
        	story.setLast_updatedby(params.getFirst("user"));
        	story.setCreation_date(new java.sql.Date(System.currentTimeMillis()));
        	story.setStatus("Not Started");
        	story.setView_count(0);
        	
        	
        	try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString("{\"result\":"+data.createUserStory(story,Long.parseLong(params.getFirst("projectId")))+"}");
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
        @Path("/addtosprint")
        public String addToCurrentSprint(MultivaluedMap<String, String> params) {
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
        public String deleteStory(MultivaluedMap<String, String> params) {
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
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/adduser")
		public String addUser(MultivaluedMap<String, String> params) {
			DatabaseHandler data = new DatabaseHandler();
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString("{\"result\":"+data.addUserToStory(params.getFirst("userid"),Long.parseLong(params.getFirst("storyId")))+"}");
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
					   return mapper.writeValueAsString("{\"result\":"+data.removeUserFromStory(params.getFirst("userid"),Long.parseLong(params.getFirst("storyId")))+"}");
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