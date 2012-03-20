package com.imaginea.scrumr.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.ProjectSummaryReport;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.entities.Task.TaskStatus;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.TaskManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;
import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

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
        int taskId = ResourceUtil.stringToIntegerConversion("task_id", id);
        List<Task> tasks = new ArrayList<Task>();
        try{
            Task task = taskManager.readTask(taskId);
            tasks.add(task);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the task (pKey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        
        return tasks;
    }

    @RequestMapping(value = "/story/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Task> fetchStoryTask(@PathVariable("id") String id) {
        int storyId = ResourceUtil.stringToIntegerConversion("story_id", id);
        List<Task> tasks = null;
        try{
            tasks = taskManager.fetchTasksByStory(storyId); 
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the task of the story (pKey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }        
        return tasks;
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public @ResponseBody
    List<ProjectSummaryReport> fetchTaskStatusSummary(@RequestParam String projectId,
                                    @RequestParam(required = false) String sprintId) {

        Integer projId = ResourceUtil.stringToIntegerConversion("project_id", projectId);
        Integer sprId = ResourceUtil.stringToIntegerConversion("sprint_id", sprintId);
        
        List<ProjectSummaryReport> reportList = new ArrayList<ProjectSummaryReport>();
        
        try{
            List<Object> results = taskManager.fetchTaskStatusSummary(projId, sprId);
            
            for (Iterator<Object> iterator = results.iterator(); iterator.hasNext();) {
                Object[] object = (Object[]) iterator.next();
                Task task = (Task) object[0];
                ProjectSummaryReport report = new ProjectSummaryReport();
                report.setTask(task);
                // report.setTimeWorked(timeWorked);
                report.setTotalTasks(((Long) object[1]).intValue());
                report.setTotalTime(((Long) object[2]).intValue());
                reportList.add(report);
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the project summary of the project (pKey) "+projectId;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }

        return reportList;
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public @ResponseBody
    List<Task> fetchTaskStatusDetails(@RequestParam String projectId,
                                    @RequestParam(required = false) String sprintId,
                                    @RequestParam(required = false) String userId,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam String pageNumber, @RequestParam String maxCount) {

        int projId = Integer.parseInt(projectId);
        int pagenum = ResourceUtil.stringToIntegerConversion("page_number", pageNumber);
        int pageSize = ResourceUtil.stringToIntegerConversion("page_size", maxCount);
        
        Integer sprId = null,usrId = null;
        if (sprintId != null)
            sprId = ResourceUtil.stringToIntegerConversion("sprint_id", sprintId);
        if (userId != null)
            usrId = ResourceUtil.stringToIntegerConversion("user_id", userId);
        
        return taskManager.fetchTaskStatusDetails(projId, sprId, usrId, orderBy, pagenum, pageSize);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<Task> createTask(@RequestParam String milestonePeriod, @RequestParam String timeInDays,
                                    @RequestParam String user, @RequestParam String content,
                                    @RequestParam String assigneeId,
                                    @RequestParam(required = false) String storyid) {

        
        List<Task> result = new ArrayList<Task>();
        try {
            int storyId = ResourceUtil.stringToIntegerConversion("story_id", storyid);
            int time_In_Days = ResourceUtil.stringToIntegerConversion("task_time_in_days",timeInDays);
            Story story = storyManager.readStory(storyId);
            User assigneeUser = null;
            
            Project project = story.getProject();
            Sprint sprint = story.getSprint_id();
            User createdBy = userServiceManager.readUser(user);
            
            Task task = new Task();
            task.setContent(content);
            task.setSprint(sprint);
            task.setProject(project);
            if (createdBy != null)
                task.setCreatedByUser(createdBy);
            task.setMilestonePeriod(milestonePeriod);
            task.setTimeInDays(time_In_Days);
            
            if (assigneeId != null) {
                assigneeUser = userServiceManager.readUser(assigneeId);
                task.setUser(assigneeUser);
                addUserToStory(assigneeUser,storyid, task);
            }
            // independent task support is ok 
            taskManager.createTask(task);
            result.add(task);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while creating the task with content "+content+ " in the story (pkey)"+storyid;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }        
        return result;

    }

    private void addUserToStory(User assigneeUser, String storyid,Task task) {
            Story story = storyManager.readStory(Integer.parseInt(storyid));
            Set<User> userList = story.getAssignees();
            userList.add(assigneeUser);
            story.setAssignees(userList);
            task.setStory(story);     
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
        
        int taskId = ResourceUtil.stringToIntegerConversion("task_id", id);
        int time_In_Days = ResourceUtil.stringToIntegerConversion("task_time_in_days", timeInDays);
        List<Task> result = new ArrayList<Task>();
        
        try {            
            Task task = new Task();
            task = taskManager.readTask(taskId);
            
            if (content != null)
                task.setContent(content);
            if (status != null)
                task.setStatus(TaskStatus.valueOf(status));
            if (assigneeId != null)
                task.setUser(userServiceManager.readUser(assigneeId));
            if (timeInDays != null) {
                task.setMilestonePeriod(timeInDays);
                task.setTimeInDays(time_In_Days);
            }            
            // if none of these was changed, do not update
            if (content != null || status != null || assigneeId != null || timeInDays != null)
                taskManager.updateTask(task);
            result.add(task);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while updating the task (pKey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteTask(@PathVariable("id") String id) {

        Task task = taskManager.readTask(Integer.parseInt(id));
        int userPkey = task.getUser().getPkey();
        int storyId = task.getStory().getPkey();
        
        try{
            List<Task> taskList = taskManager.getTasksByUser(userPkey, storyId);            
            //The user is assigned to only one task in this story if the size is 1
            if(taskList.size() == 1){
                Story story = storyManager.readStory(storyId);
                Set<User> userList = story.getAssignees();
                userList.remove(userServiceManager.readUser(userPkey));
                story.setAssignees(userList);
                storyManager.updateStory(story);
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while deleting the user from the story (pKey) "+storyId;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        try{
         taskManager.deleteTask(task);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while deleting the task (pKey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return ResourceUtil.SUCCESS_JSON_MSG;
    }
    
    @RequestMapping(value = "/fetchtasks/{sprintid}/{userid}", method = RequestMethod.GET)
    public @ResponseBody
    List<Task> fetchTasksByUser(@PathVariable("sprintid") String sprintId, @PathVariable("userid") String userId) {
        
        int sprint_id = ResourceUtil.stringToIntegerConversion("sprint_id", sprintId);
        List<Task> tasks = null;
        try{
            tasks = taskManager.fetchAssignedTaskByCurrentUser(sprint_id,userId); 
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the tasks for the user "+userId+" in the sprint (pKey) "+sprintId;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e); 
        }        
        return tasks;
    }
    
    @RequestMapping(value = "/uncompletedtasks/{sprintid}", method = RequestMethod.GET)
    public @ResponseBody
    List<Task> fetchUnAssignedTasks(@PathVariable("sprintid")  String sprintId) {
        List<Task> tasks = null;
        int sprint_id = ResourceUtil.stringToIntegerConversion("sprint_id", sprintId);        
        try{
            tasks = taskManager.fetchUnAssignedTaskBySprint(sprint_id);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the uncompleted tasks for the sprint "+sprintId;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e); 
        }
        return tasks; 
    }
}
