package com.imaginea.scrumr.resources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.UserTaskReport;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.TaskManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@Controller
@RequestMapping("/sprints")
public class SprintResource {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SprintManager sprintManager;
    
    @Autowired
    TaskManager taskManager;

    @Autowired
    UserServiceManager userServiceManager;

    private static final Logger logger = LoggerFactory.getLogger(SprintResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Sprint> fetchSprint(@PathVariable("id") String id) {

        Sprint sprint = sprintManager.readSprint(Integer.parseInt(id));
        List<Sprint> sprints = new ArrayList<Sprint>();
        sprints.add(sprint);
        return sprints;
    }

    @RequestMapping(value = "{sprintid}/project/{projectid}", method = RequestMethod.GET)
    public @ResponseBody
    List<Sprint> fetchSprintByProject(@PathVariable("sprintid") String id,
                                    @PathVariable("projectid") String pid) {

        Project project = projectManager.readProject(Integer.parseInt(pid));
        Sprint sprint = sprintManager.selectSprintByProject(project, Integer.parseInt(id));
        List<Sprint> sprints = new ArrayList<Sprint>();
        sprints.add(sprint);
        return sprints;
    }
    
    @RequestMapping(value = "{sprintid}/project/{projectid}/users", method = RequestMethod.GET)
    public @ResponseBody
    List<UserTaskReport> fetchUserTasksBySprint(@PathVariable("sprintid") String id,
                                    @PathVariable("projectid") String pid) {

        Project project = projectManager.readProject(Integer.parseInt(pid));
        Sprint sprint = sprintManager.readSprint(Integer.parseInt(id));
        return taskManager.fetchUserTaskBySprint(project, sprint);
    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Sprint> fetchSprintsByProject(@PathVariable("id") String id) {

        Project project = projectManager.readProject(Integer.parseInt(id));
        List<Sprint> sprints = sprintManager.selectSprintsByProject(project);
        return sprints;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    String createSprint(@RequestParam String start_date, @RequestParam String end_date,
                                    @RequestParam String projectId) {

        Project project = projectManager.readProject(Integer.parseInt(projectId));
        Sprint sprint = new Sprint();
        int totalSprints = sprintManager.getSprintCountForProject(project);
        sprint.setId(totalSprints + 1);
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date startdate = format.parse(start_date);
            sprint.setStartdate(startdate);
            Date enddate = format.parse(end_date);
            sprint.setEnddate(enddate);
            if (startdate.after(new Date())) {
                sprint.setStatus("Not Started");
            } else {
                if (enddate.before(new Date())) {
                    sprint.setStatus("Finished");
                } else {
                    sprint.setStatus("In Progress");
                }
            }
            // TODO: get rid of this
            project.setNo_of_sprints(totalSprints + 1);
            sprint.setProject(project);
            sprintManager.createSprint(sprint);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "{\"result\":\"failure\"}";
        }
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    List<Sprint> updateSprint(@RequestParam String start_date, @RequestParam String end_date,
                                    @RequestParam String sprintId, @RequestParam String projectId) {
        List<Sprint> result = new ArrayList<Sprint>();
        Project project = projectManager.readProject(Integer.parseInt(projectId));
        Sprint sprint = sprintManager.selectSprintByProject(project, Integer.parseInt(sprintId));
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date startdate = format.parse(start_date);
            sprint.setStartdate(startdate);
            Date enddate = format.parse(end_date);
            sprint.setEnddate(enddate);
            if (startdate.after(new Date())) {
                sprint.setStatus("Not Started");
            } else {
                if (enddate.before(new Date())) {
                    sprint.setStatus("Finished");
                } else {
                    sprint.setStatus("In Progress");
                }
            }
            sprintManager.updateSprint(sprint);
            result.add(sprint);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result;
        }
        return result;
    }
}