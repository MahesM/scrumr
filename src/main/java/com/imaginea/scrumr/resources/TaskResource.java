package com.imaginea.scrumr.resources;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.entities.Task.TaskStatus;
import com.imaginea.scrumr.entities.User;
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

    private static final Logger logger = LoggerFactory.getLogger(TaskResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Task> fetchTask(@PathVariable("id") String id) {

        Task task = taskManager.readTask(Integer.parseInt(id));
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(task);
        return tasks;
    }

    @RequestMapping(value = "/story/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Task> fetchStoryTask(@PathVariable("id") String id) {

        return taskManager.fetchTasksByStory(Integer.parseInt(id));
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public @ResponseBody
    List<Object> fetchTaskStatusSummary(@RequestParam String projectId,
                                    @RequestParam String sprintId) {

        Integer projId = Integer.parseInt(projectId);
        Integer sprId = Integer.parseInt(sprintId);

        return taskManager.fetchTaskStatusSummary(projId, sprId);
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public @ResponseBody
    List<Task> fetchTaskStatusDetails(@RequestParam String projectId,
                                    @RequestParam String sprintId, @RequestParam String userId,
                                    @RequestParam String orderBy, @RequestParam String startIndex,
                                    @RequestParam String maxCount) {

        Integer projId = Integer.parseInt(projectId);
        Integer sprId = Integer.parseInt(sprintId);
        Integer usrId = Integer.parseInt(userId);
        // TODO : orderBy, startIndex and maxCount params specific queries
        // A version of NamedQuery to raw query method can be added to GenericJpaDao
        return taskManager.fetchTaskStatusDetails(projId, sprId, usrId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<Task> createTask(@RequestParam String milestonePeriod, @RequestParam String timeInDays,
                                    @RequestParam String user, @RequestParam String content,
                                    @RequestParam String assigneeId, @RequestParam String storyid) {

        Task task = new Task();

        try {

            User createdBy = userServiceManager.readUser(user);
            task.setContent(content);
            task.setCreatedBy(createdBy);
            task.setMilestonePeriod(milestonePeriod);
            task.setTimeInDays(Integer.parseInt(timeInDays));
            if (assigneeId != null) {
                User assigneeUser = userServiceManager.readUser(assigneeId);
                task.setUser(assigneeUser);
            }
            // independent task support is ok
            if (storyid != null)
                task.setStory(storyManager.readStory(Integer.parseInt(storyid)));
            taskManager.createTask(task);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        List<Task> result = new ArrayList<Task>();
        result.add(task);
        return result;

    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public @ResponseBody
    String updateStatus(@RequestParam String id, @RequestParam String content,
                                    @RequestParam String status, @RequestParam String assigneeId) {
        // TODO: need to take care of task content changes , user assignees
        // TODO: maintain the history - or even if we go github way, backend needs to handle it
        try {
            Task task = taskManager.readTask(Integer.parseInt(id));
            if (content != null)
                task.setContent(content);
            if (status != null)
                task.setStatus(TaskStatus.valueOf(status));
            if (assigneeId != null)
                task.setUser(userServiceManager.readUser(assigneeId));
            taskManager.updateTask(task);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "{\"result\":\"failure\"}";
        }
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteTask(@PathVariable("id") String id) {

        Task task = taskManager.readTask(Integer.parseInt(id));
        taskManager.deleteTask(task);
        return "{\"result\":\"success\"}";
    }
}
