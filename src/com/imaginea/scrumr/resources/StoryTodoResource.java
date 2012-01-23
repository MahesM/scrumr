package com.imaginea.scrumr.resources;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.imaginea.scrumr.entities.CommentEntity;
import com.imaginea.scrumr.entities.TodoEntity;

import com.imaginea.scrumr.utilities.DatabaseHandler;

@Path("/todo")
public class StoryTodoResource {

	UriInfo uriInfo;
	Request request;
	
	@GET
	@Path("/search/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String viewComments(@PathParam("id") Long storyID)
	{
		DatabaseHandler databaseHandler = new DatabaseHandler();
		//UserStoryEntity userStory = databaseHandler.getUserStoryEntity(storyID);
		List<TodoEntity> todos = databaseHandler.getStoryTodoEntity(storyID);		
		ObjectMapper mapper = new ObjectMapper();
		   try {			   
			   System.out.println("Going to send response through JSON ............................"+mapper.writeValueAsString(todos));
			return mapper.writeValueAsString(todos);
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
    @Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public String addComments(MultivaluedMap<String, String> params){
		
		String userID = params.getFirst("user");		
		Long storyID = Long.parseLong(params.getFirst("storyid"));
		TodoEntity todo = new TodoEntity();
		todo.setContent(params.getFirst("content"));
		System.out.println("------------------------------------------> Creating todo");
		
		todo.setMilestonePeriod(params.getFirst("milestonePeriod"));
		DatabaseHandler databaseHandler = new DatabaseHandler();
		databaseHandler.addTodoToStory(todo, userID, storyID);
			
		try {
			ObjectMapper mapper = new ObjectMapper();	
			return mapper.writeValueAsString(todo);
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
    @Path("/delete")
	public void deleteComment(MultivaluedMap<String, String> params){
		System.out.println("--------------------------------------> reached here");
		Long todoID = Long.parseLong(params.getFirst("todoID"));
		DatabaseHandler databaseHandler = new DatabaseHandler();
		databaseHandler.deleteTodoEntity(todoID);
		System.out.println("Todo Deleted Successfully");
	
	}
}
