package com.imaginea.scrumr.resources;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.TaskManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@Controller
@RequestMapping("/users")
public class UserResource {

	@Autowired
	ProjectManager projectManager;

	@Autowired
	StoryManager storyManager;

	@Autowired
	UserServiceManager userServiceManager;

	@Autowired
	TaskManager taskManager;

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public @ResponseBody User fetchUser(@PathVariable("id") String id) {

		User user = userServiceManager.readUser(Integer.parseInt(id));
		return user;
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public @ResponseBody List<User> fetchUsers() {

		List<User> users = userServiceManager.fetchAllUsers();
		return users;
	}

	@RequestMapping(value="/project/{id}", method = RequestMethod.GET)
	public @ResponseBody Set<User> fetchUsersByProject(@PathVariable("id") String id) {

		Project project = projectManager.readProject(Integer.parseInt(id));
		return project.getAssignees();
	}

	@RequestMapping(value="/story/{id}", method = RequestMethod.GET)
	public @ResponseBody Set<User> fetchUsersByStory(@PathVariable("id") String id) {

		Story story = storyManager.readStory(Integer.parseInt(id));
		return story.getAssignees();
	}

	@RequestMapping(value="/task/{id}", method = RequestMethod.GET)
	public @ResponseBody User fetchUserByTask(@PathVariable("id") String id) {

		Task task = taskManager.readTask(Integer.parseInt(id));
		return task.getUser();
	}

	@RequestMapping(value="/create", method = RequestMethod.POST)
	public @ResponseBody String cacheUser(
			@RequestParam String username,
			@RequestParam String fullname,
			@RequestParam String emailid
			) {

		try {

			User user = new User();
			user.setEmailId(emailid);
			user.setUsername(username);
			user.setFullName(fullname);
			userServiceManager.createUser(user);

		}catch(Exception e){
			return "{\"result\":\"failure\"}";
		}
		return "{\"result\":\"success\"}";

	}
}