package com.imaginea.scrumr.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imaginea.scrumr.entities.Comment;
import com.imaginea.scrumr.interfaces.CommentManager;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@Controller
@RequestMapping("/comments")
public class CommentResource {

	@Autowired
	ProjectManager projectManager;

	@Autowired
	CommentManager commentManager;

	@Autowired
	StoryManager storyManager;

	@Autowired
	UserServiceManager userServiceManager;

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Comment> fetchComment(@PathVariable("id") String id) {

		Comment comment = commentManager.readComment(Integer.parseInt(id));
		List<Comment> comments = new ArrayList<Comment>();
		comments.add(comment);
		return comments;
	}
	
	@RequestMapping(value="/story/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Comment> fetchCommentsByStory(@PathVariable("id") String id) {

		return commentManager.fetchCommentsByStory(Integer.parseInt(id));
	}

	@RequestMapping(value="/create", method = RequestMethod.POST)
	public @ResponseBody List<Comment> createSprint(
			@RequestParam String logdate,
			@RequestParam String user,
			@RequestParam String content,
			@RequestParam String storyid
			) {

		Comment comment = new Comment();

		DateFormat dateFormat = new SimpleDateFormat("yy-mm-dd");
		Date logDate=null;		

		try {

			logDate = dateFormat.parse(logdate);
			comment.setContent(content);
			comment.setLogDate(logDate);
			comment.setUser(userServiceManager.readUser(user));
			comment.setStory(storyManager.readStory(Integer.parseInt(storyid)));
			
			commentManager.createComment(comment);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		List<Comment> result = new ArrayList<Comment>();
		result.add(comment);
		return result;

	}

	@RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody String deleteComment(@PathVariable("id") String id) {
		Comment comment = commentManager.readComment(Integer.parseInt(id));
		System.out.println("Comment : "+comment.getContent());		
		commentManager.deleteComment(comment);
		System.out.println("Comment deleted");
		return "{\"result\":\"success\"}";
	}

}
