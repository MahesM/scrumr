package com.imaginea.scrumr.resources;

import java.util.ArrayList;
import java.util.Iterator;
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

import com.imaginea.scrumr.entities.ProjectSummaryReport;
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
    List<ProjectSummaryReport> fetchTaskStatusSummary(@RequestParam String projectId,
                                    @RequestParam(required = false) String sprintId) {

        Integer projId = Integer.parseInt(projectId);
        Integer sprId = Integer.parseInt(sprintId);
        List<Object> results = taskManager.fetchTaskStatusSummary(projId, sprId);
        List< ProjectSummaryReport> reportList = new ArrayList<ProjectSummaryReport>();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Object[] object = (Object[]) iterator.next();
            Task task = (Task) object[0];
            ProjectSummaryReport report =new ProjectSummaryReport();
            report.setTask(task);
            //report.setTimeWorked(timeWorked);
            report.setTotalTasks(((Long)object[1]).intValue());
            report.setTotalTime(((Long)object[2]).intValue());
            reportList.add(report);
        }
        // HashMap map = new HashMap();
        // map.put( , results);
        return reportList;
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public @ResponseBody
    List<Task> fetchTaskStatusDetails(@RequestParam String projectId,
                                    @RequestParam(required = false) String sprintId,
                                    @RequestParam(required = false) String userId,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam String pageNumber, @RequestParam String maxCount) {

        Integer projId = Integer.parseInt(projectId);
        Integer sprId = null;
        Integer usrId = null;
        if (sprintId != null)
            sprId = Integer.parseInt(sprintId);
        if (userId != null)
            usrId = Integer.parseInt(userId);
        Integer pagenum = Integer.parseInt(pageNumber);
        Integer pageSize = Integer.parseInt(maxCount);
        return taskManager.fetchTaskStatusDetails(projId, sprId, usrId, orderBy, pagenum, pageSize);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<Task> createTask(@RequestParam String milestonePeriod, @RequestParam String timeInDays,
                                    @RequestParam String user, @RequestParam String content,
                                    @RequestParam String assigneeId,
                                    @RequestParam(required = false) String storyid) {

        Task task = new Task();
        List<Task> result = new ArrayList<Task>();
        try {

            User createdBy = userServiceManager.readUser(user);
            task.setContent(content);
            if (createdBy != null)
                task.setCreatedByUser(createdBy);
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
            return result;
        }
        result.add(task);
        return result;

    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public @ResponseBody
    List<Task> updateStatus(@RequestParam String id,
                                    @RequestParam(required = false) String content,
                                    @RequestParam(required = false) String status,
                                    @RequestParam(required = false) String assigneeId,
                                    @RequestParam(required = false) String timeInDays) {
        // TODO: need to take care of task content changes , user assignees
        // TODO: maintain the history - or even if we go github way, backend needs to handle it
        Task task = new Task();
        List<Task> result = new ArrayList<Task>();
        try {
            task = taskManager.readTask(Integer.parseInt(id));
            if (content != null)
                task.setContent(content);

            if (status != null)
                task.setStatus(TaskStatus.valueOf(status));

            if (assigneeId != null)
                task.setUser(userServiceManager.readUser(assigneeId));
            if (timeInDays != null) {
                task.setMilestonePeriod(timeInDays);
                task.setTimeInDays(Integer.parseInt(timeInDays));
            }
            // if none of these was changed, do not update
            if (content != null || status != null || assigneeId != null || timeInDays != null)
                taskManager.updateTask(task);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result;
        }
        result.add(task);
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteTask(@PathVariable("id") String id) {

        Task task = taskManager.readTask(Integer.parseInt(id));
        taskManager.deleteTask(task);
        return "{\"result\":\"success\"}";
    }
}
