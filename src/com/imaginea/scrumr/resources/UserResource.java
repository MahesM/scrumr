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
import com.imaginea.scrumr.json.ProjectListObject;
import com.imaginea.scrumr.json.ProjectObject;
import com.imaginea.scrumr.utilities.DatabaseConnection;
import com.imaginea.scrumr.utilities.DatabaseHandler;
import com.sun.jersey.api.json.JSONConfiguration;

	@Path("/users")
	public class UserResource {

		@Context
		UriInfo uriInfo;
		@Context
		Request request;
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("{id}")
		public String viewUser(@PathParam("id") String id) {
			DatabaseHandler data = new DatabaseHandler();
			data.getUserInfo(id);
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString(data.getUserInfo(id));
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
		@Path("/search")
		public String viewAllUsers() {
			DatabaseHandler data = new DatabaseHandler();
			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString(data.getAllUsers());
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
		public String viewProjectUser(@PathParam("id") String id) {
			DatabaseHandler data = new DatabaseHandler();
			ProjectEntity project = new ProjectEntity();
			List<ProjectEntity> list = new ArrayList<ProjectEntity>();
			int count = 0;

			try {
				ObjectMapper mapper = new ObjectMapper();
					   return mapper.writeValueAsString(data.getAllUsers(Long.parseLong(id)));
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