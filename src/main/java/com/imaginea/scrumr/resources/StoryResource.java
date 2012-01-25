package com.imaginea.scrumr.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Status;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.StatusManager;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@Controller
@RequestMapping("/stories")
public class StoryResource {

	@Autowired
	ProjectManager projectManager;

	@Autowired
	SprintManager sprintManager;

	@Autowired
	UserServiceManager userServiceManager;

	@Autowired
	StoryManager storyManager;
	
	@Autowired
	StatusManager statusManager;

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Story> fetchStory(@PathVariable("id") String id) {

		Story story = storyManager.readStory(Integer.parseInt(id));
		List<Story> stories = new ArrayList<Story>();
		stories.add(story);
		return stories;
	}

	@RequestMapping(value="/project/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Story> fetchStoriesByProject(@PathVariable("id") String id) {

		return storyManager.fetchStoriesByProject(Integer.parseInt(id));

	}
	
	@RequestMapping(value="{sprintid}/project/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Story> fetchStoriesByProjectSprint(@PathVariable("sprintid") String sid, @PathVariable("id") String pid) {
		Sprint sprint = sprintManager.selectSprintByProject(projectManager.readProject(Integer.parseInt(pid)), Integer.parseInt(sid));
		return storyManager.fetchStoriesBySprint(sprint.getPkey());

	}

	@RequestMapping(value="/sprint/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Story> fetchStoriesBySprint(@PathVariable("id") String id) {

		return storyManager.fetchStoriesBySprint(Integer.parseInt(id));

	}

	@RequestMapping(value="/status", method = RequestMethod.POST)
	public @ResponseBody List<Story> fetchStoriesByStatus(
			@RequestParam String sprintid,
			@RequestParam String status
			) {

		return storyManager.fetchStoriesByStatus(Integer.parseInt(sprintid),status);

	}

	@RequestMapping(value="/backlog/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Story> fetchUnAssignedStories(@PathVariable("id") String id) {
		System.out.println("UnAssigned Stories");
		return storyManager.fetchUnAssignedStories(Integer.parseInt(id));

	}

	@RequestMapping(value="/create", method = RequestMethod.POST)
	public @ResponseBody String createStory(
			@RequestParam String stTitle,
			@RequestParam String stPriority,
			@RequestParam String user,
			@RequestParam String projectId
			) {

		Story story = new Story();
		try {
			story.setTitle(stTitle);
			story.setPriority(Integer.parseInt(stPriority));
			story.setCreatedby(user);
			story.setLast_updated(new java.sql.Date(System.currentTimeMillis()));
			story.setLast_updatedby(user);
			story.setCreation_date(new java.sql.Date(System.currentTimeMillis()));
			story.setStatus("Not Started");
			story.setView_count(0);
			story.setProject(projectManager.readProject(Integer.parseInt(projectId)));
			storyManager.createStory(story);
		}catch(Exception e){
			return "{\"result\":\"failure\"}";
		}
		return "{\"result\":\"success\"}";
	}


	@RequestMapping(value="/addtosprint", method = RequestMethod.POST)
	public @ResponseBody String addStoryToSprint(
			@RequestParam String sprint,
			@RequestParam String stories,
			@RequestParam String status,
			@RequestParam String projectId
			) {

		try{
			Story story = storyManager.readStory(Integer.parseInt(stories));
			Sprint toSprint = sprintManager.selectSprintByProject(projectManager.readProject(Integer.parseInt(projectId)),Integer.parseInt(sprint));
			story.setSprint_id(toSprint);
			System.out.println(toSprint);
			story.setStatus(status);
			storyManager.updateStory(story);
		}catch(Exception e){
			return "{\"result\":\"failure\"}";
		}
		return "{\"result\":\"success\"}";
	}

	@RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody String deleteStory(@PathVariable("id") String id) {

		storyManager.deleteStory(storyManager.readStory(Integer.parseInt(id)));
		return "{\"result\":\"success\"}";
	}

	@RequestMapping(value="/{id}/adduser/{uid}", method = RequestMethod.GET)
	public @ResponseBody String addUserToStory(@PathVariable("id") String id, @PathVariable("uid") String uid) {

		User user = userServiceManager.readUser(id);
		Story story = storyManager.readStory(Integer.parseInt(id));
		story.addAssignees(user);
		storyManager.updateStory(story);
		return "{\"result\":\"success\"}";
	}

	@RequestMapping(value="/{id}/removeuser/{uid}", method = RequestMethod.GET)
	public @ResponseBody String removeUserFromStory(@PathVariable("id") String id, @PathVariable("uid") String uid) {

		User user = userServiceManager.readUser(id);
		Story story = storyManager.readStory(Integer.parseInt(id));
		story.removeAssignees(user);
		storyManager.updateStory(story);
		return "{\"result\":\"success\"}";
	}

	@RequestMapping(value="/{id}/addusers/{uids}", method = RequestMethod.GET)
	public @ResponseBody String addUsersToStory(@PathVariable("id") String id, @PathVariable("uids") String uids) {

		Story story = storyManager.readStory(Integer.parseInt(id));
		String[] users = uids.split(",");
		for(String s: users){
			User user = userServiceManager.readUser(s);
			story.addAssignees(user);
		}
		storyManager.updateStory(story);
		return "{\"result\":\"success\"}";
	}

	@RequestMapping(value="/{id}/removeusers/{uidlist}", method = RequestMethod.GET)
	public @ResponseBody String removeUsersFromStory(@PathVariable("id") String id, @PathVariable("uids") String uids) {

		Story story = storyManager.readStory(Integer.parseInt(id));
		String[] users = uids.split(",");
		for(String s: users){
			User user = userServiceManager.readUser(s);
			story.removeAssignees(user);
		}
		storyManager.updateStory(story);
		return "{\"result\":\"success\"}";
	}
	
	@RequestMapping(value="/adduserwithstage", method = RequestMethod.POST)
	public @ResponseBody String addUserWithStage(
			@RequestParam String userid,
			@RequestParam String storyId,
			@RequestParam String stage
		) {

		Status story_status = new Status();
		story_status.setUser(userServiceManager.readUser(userid));
		story_status.setStory(storyManager.readStory(Integer.parseInt(storyId)));
		story_status.setStage(stage);
		statusManager.createStatus(story_status);
		return "{\"result\":\"success\"}";
	}
}