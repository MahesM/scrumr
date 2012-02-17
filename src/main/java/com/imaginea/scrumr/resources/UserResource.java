package com.imaginea.scrumr.resources;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.apache.struts2.interceptor.ServletRequestAware;
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
import com.imaginea.scrumr.security.AbstractAuthenticationFactory;
import com.imaginea.scrumr.security.AuthenticationSource;
import com.imaginea.scrumr.utils.QontextHelperUtil;

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
	
	@Autowired
	AbstractAuthenticationFactory abstractAuthenticationFactory;
	
	AuthenticationSource authenticationSource;
	
	@Autowired
	HttpServletRequest request;

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public @ResponseBody User fetchUser(@PathVariable("id") String id) {

		User user = userServiceManager.readUser(Integer.parseInt(id));
		return user;
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public @ResponseBody List<User> fetchAllUsers() {

		List<User> users = userServiceManager.fetchAllUsers();
		return users;
	}
	
	@RequestMapping(value="/fetchusers", method = RequestMethod.POST)
	public @ResponseBody List<User> fetchUsers(
			@RequestParam String index,
			@RequestParam String source,
			@RequestParam String count		
	) {

		List<User> users = null;
		authenticationSource = abstractAuthenticationFactory.getInstance(request);
		authenticationSource.getFriends(Integer.parseInt(index), Integer.parseInt(count));
		return users;
	}
	
	@RequestMapping(value="/fetchqontextusers", method = RequestMethod.POST)
	public @ResponseBody String fetchQontextUsers(
			@RequestParam String index,
			@RequestParam String count		
	) {

		authenticationSource = abstractAuthenticationFactory.getInstance(request);
		String str = authenticationSource.getFriends(Integer.parseInt(index), Integer.parseInt(count));
		System.out.println("Output: "+str);
		return str;
	}

	@RequestMapping(value="/searchqontext", method = RequestMethod.POST)
	public @ResponseBody String searchUsers (@RequestParam String sortType, @RequestParam boolean showTotalCount,@RequestParam int startIndex,@RequestParam int count){
		String userList = null;
		authenticationSource = abstractAuthenticationFactory.getInstance(request);
		userList = authenticationSource.searchFriends(sortType, showTotalCount, startIndex, count);
		System.out.println("Users List Arun:"+userList);
		return userList;
		
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
			@RequestParam String displayname,
			@RequestParam String fullname,
			@RequestParam String emailid,
			@RequestParam String avatarurl
			) {

		try {
			System.out.println("Create User: "+displayname);
			User user = new User();
			user.setUsername(username);
			user.setFullname(fullname);
			user.setAvatarurl(avatarurl);
			user.setDisplayname(displayname);
			user.setEmailid(emailid);
			System.out.println(user.toString());
			User userExisting = userServiceManager.readUser(username);
			if(userExisting == null){
				userServiceManager.createUser(user);
			}

		}catch(Exception e){
			return "{\"result\":\"failure\"}";
		}
		return "{\"result\":\"success\"}";
	}
}