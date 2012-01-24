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

import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.TaskManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@Controller
@RequestMapping("/todo")
public class TaskResource {

	@Autowired
	StoryManager storyManager;

	@Autowired
	UserServiceManager userServiceManager;

	@Autowired
	TaskManager taskManager;


	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Task> fetchTask(@PathVariable("id") String id) {

		Task task = taskManager.readTask(Integer.parseInt(id));
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(task);
		return tasks;
	}
	
	@RequestMapping(value="/story/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Task> fetchStoryTask(@PathVariable("id") String id) {

		return taskManager.fetchTasksByStory(Integer.parseInt(id));
	}

	@RequestMapping(value="/create", method = RequestMethod.POST)
	public @ResponseBody String createSprint(
			@RequestParam String milestonePeriod,
			@RequestParam String user,
			@RequestParam String content,
			@RequestParam String storyid
			) {

		Task task = new Task();

		try {

			task.setContent(content);
			task.setMilestonePeriod(milestonePeriod);
			task.setUser(userServiceManager.readUser(user));
			task.setStory(storyManager.readStory(Integer.parseInt(storyid)));

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"failure\"}";
		}

		return "{\"result\":\"success\"}";

	}

	@RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody String deleteTask(@PathVariable("id") String id) {

		taskManager.deleteTask(taskManager.readTask(Integer.parseInt(id)));
		return "{\"result\":\"success\"}";
	}
}
