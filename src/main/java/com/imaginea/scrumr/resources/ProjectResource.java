package com.imaginea.scrumr.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import com.imaginea.scrumr.entities.ProjectLane;
import com.imaginea.scrumr.entities.ProjectPriority;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectLaneManager;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectPriorityManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@Controller
@RequestMapping("/projects")
public class ProjectResource {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SprintManager sprintManager;
    
    @Autowired
    ProjectLaneManager projectLaneManager;
    
    @Autowired
    ProjectPriorityManager projectPriorityManager;
    
    @Autowired
    UserServiceManager userServiceManager;

    private static final Logger logger = LoggerFactory.getLogger(ProjectResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Project> fetchProject(@PathVariable("id") String id) {
        Project project = projectManager.readProject(Integer.parseInt(id));
        List<Project> projects = new ArrayList<Project>();
        projects.add(project);
        return projects;
    }

    @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
    public @ResponseBody
    List<Project> fetchProjectsByUser(@PathVariable("userid") String id) {
        User user = userServiceManager.readUser(id);
        if (user != null) {
            return user.getProjects();
        } else {
            return null;
        }

    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public @ResponseBody
    String addUser(@RequestParam String userid, @RequestParam String projectId) {
        String uid = userid;
        logger.debug("User Id :" + uid);
        Integer pid = Integer.parseInt(projectId);
        Project project = projectManager.readProject(pid);
        logger.debug("User Object :" + userServiceManager.readUser(uid));
        project.addAssignees(userServiceManager.readUser(uid));
        projectManager.updateProject(project);
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/removeuser", method = RequestMethod.POST)
    public @ResponseBody
    String removeUser(@RequestParam String userid, @RequestParam String projectId) {
        String uid = userid;
        Integer pid = Integer.parseInt(projectId);
        Project project = projectManager.readProject(pid);
        project.removeAssignees(userServiceManager.readUser(uid));
        projectManager.updateProject(project);
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/storycount/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String projectCounts(@PathVariable("id") String id) {
        Integer pid = Integer.parseInt(id);
        Project project = projectManager.readProject(pid);
        Set<Story> stories = (Set<Story>) project.getStories();
        if (stories != null) {
            return "{\"result\":" + stories.size() + "}";
        }
        return "{\"result\":\"failure\"}";

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<Project> createProject(@RequestParam String pTitle, @RequestParam String pDescription,
                                    @RequestParam String pSprintDuration,
                                    @RequestParam String pStartDate, @RequestParam String pEndDate,
                                    @RequestParam String assignees,
                                    @RequestParam String current_user)

    {
        Project project = new Project();
        try {

            project.setTitle(pTitle);
            project.setDescription(pDescription);
            Date date = null;
            Date date1 = null;
            int sprint_count = 0;
            int duration = Integer.parseInt(pSprintDuration);
            try {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                if (pStartDate != "" && pEndDate != "") {
                    date = format.parse(pStartDate);
                    date1 = format.parse(pEndDate);
                    project.setStart_date(date);
                    project.setEnd_date(date1);
                    sprint_count = getSprintCount(date, date1, Integer.parseInt(pSprintDuration));
                    project.setNo_of_sprints(sprint_count);
                } else {
                    date = format.parse(pStartDate);
                    project.setStart_date(date);
                    project.setCurrent_sprint(0);
                    project.setEnd_date(null);
                    sprint_count = getSprintCount(date, null, Integer.parseInt(pSprintDuration));
                    logger.debug("Test: SC: " + sprint_count);
                    project.setNo_of_sprints(sprint_count);
                }
            } catch (ParseException e1) {
                logger.error(e1.getMessage(), e1);
            }
            String userStr = assignees;
            String[] users = userStr.split(",");
            Set<User> userList = new HashSet<User>();
            for (String s : users) {
                logger.debug("user: " + s);
                User user = userServiceManager.readUser(s);
                userList.add(user);
            }

            if (date.after(new Date())) {
                project.setCurrent_sprint(0);
                project.setStatus("Not Started");
            } else {
                project.setCurrent_sprint(getCurrentSprint(date, date1, duration));
                project.setStatus("In Progress");
            }

            project.setAssignees(userList);
            project.setSprint_duration(duration);
            project.setCreatedby(current_user);
            logger.info("CurrentUser:" + current_user + " " + current_user.length());
            project.setLast_updated(new java.sql.Date(System.currentTimeMillis()));
            project.setLast_updatedby("'" + current_user+"'");
            project.setCreation_date(new java.sql.Date(System.currentTimeMillis()));

            projectManager.createProject(project);
            Date currentdate = date;
            for (int i = 0; i < sprint_count; i++) {

                Sprint sprint = new Sprint();
                sprint.setId(i + 1);
                sprint.setStartdate(new java.sql.Date(currentdate.getTime()));
                Date enddate = new Date(currentdate.getTime() + ((7 * duration) * 86400000L)
                                                - 3600000L);
                if (date1 != null && i == (sprint_count - 1)) {
                    enddate = new Date(currentdate.getTime() + ((7 * duration) * 86400000L));
                }
                if (date1 != null) {
                    if (enddate.before(date1)) {
                        sprint.setEnddate(new java.sql.Date(enddate.getTime()));
                    } else {
                        sprint.setEnddate(new java.sql.Date(date1.getTime()
                                                        + (86400000L - 3600000L)));
                    }
                } else {
                    sprint.setEnddate(new java.sql.Date(enddate.getTime()));
                }
                if (currentdate.after(new Date())) {
                    sprint.setStatus("Not Started");
                } else {
                    if (enddate.before(new Date())) {
                        logger.debug(enddate.toString());
                        sprint.setStatus("Finished");
                    } else {
                        logger.debug("Past sprint");
                        sprint.setStatus("In Progress");
                        project.setCurrent_sprint(sprint.getId());
                        project.setStatus("In Progress");
                        projectManager.updateProject(project);
                    }
                }
                sprint.setProject(project);
                sprintManager.createSprint(sprint);
                currentdate = new Date(currentdate.getTime() + ((7 * duration) * 86400000L));                
            }
            createDefaultLanes(project);
            createDefaultPriorities(project);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        List<Project> result = new ArrayList<Project>();
        result.add(project);

        return result;

    }

    private void createDefaultPriorities(Project project) {
        ProjectPriority.DefaultPriority[] priorities = ProjectPriority.DefaultPriority.values();
        for(ProjectPriority.DefaultPriority priority: priorities){
            createProjectPriority(project,priority.getDescription(),priority.getColor());
        }        
    }

    private void createProjectPriority(Project project, String description, String color) {
        ProjectPriority projectPriority = new ProjectPriority();
        projectPriority.setColor(color);
        projectPriority.setProject(project);
        projectPriority.setDescription(description);
        projectPriorityManager.createProjectPriority(projectPriority);
        
    }

    private void createDefaultLanes(Project project) {
        ProjectLane.DefaultProjectLanes[] projectLanes = ProjectLane.DefaultProjectLanes.values();
        for(ProjectLane.DefaultProjectLanes projectLane: projectLanes){
            createProjectLane(project,projectLane.getDescription(),projectLane.getRank(),projectLane.getType());
        }
	}   

	private void createProjectLane(Project project, String description, int rank, String type) {
	    ProjectLane projectLane = new ProjectLane();
        projectLane.setColor(123);
        projectLane.setProject(project);
        projectLane.setDescription(description);
        projectLane.setRank(rank);
        projectLane.setType(type);
        projectLaneManager.createProjectLane(projectLane);        
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    List<Project> updateProject(@RequestParam String pNo, @RequestParam String pTitle,
                                    @RequestParam String pDescription,
                                    @RequestParam String pSprintDuration,
                                    @RequestParam String pStartDate, @RequestParam String pEndDate,
                                    @RequestParam String assignees,
                                    @RequestParam String current_user)

    {
        Project project = projectManager.readProject(Integer.parseInt(pNo));
        List<Project> result = new ArrayList<Project>();

        if (project != null) {
            try {

                project.setTitle(pTitle);
                project.setDescription(pDescription);
                project.setLast_updated(new java.sql.Date(System.currentTimeMillis()));
                project.setLast_updatedby(current_user);

                projectManager.updateProject(project);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
            result.add(project);
        }
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteProject(@PathVariable("id") String id) {
        Project project = projectManager.readProject(Integer.parseInt(id));
        if (project != null) {           
            deleteDependantEntities(project.getPkey());                        
            projectManager.deleteProject(project);
            logger.debug("SUCCESS");
            return "{\"result\":\"success\"}";
        } else {
            logger.debug("FAILURE");
            return "{\"result\":\"failure\"}";
        }
    }

    private void deleteDependantEntities(int projectId) {
        deleteProjectLanesByProject(projectId);
        deleteProjectPrioritiesByProject(projectId);
    }

    private void deleteProjectPrioritiesByProject(int projectId) {
       List<ProjectPriority> projectProrityList = projectPriorityManager.fetchAllProjectPrioritiesByProject(projectId);
        
        for(ProjectPriority projectPriority:projectProrityList)
            projectPriorityManager.deleteProjectPriority(projectPriority);        
    }

    private void deleteProjectLanesByProject(int projectId) {
        List<ProjectLane> projectLaneList = projectLaneManager.fetchAllProjectLaneByProject(projectId);
        
        for(ProjectLane projectLane:projectLaneList)
           projectLaneManager.deleteProjectLane(projectLane);        
    }

    // TODO - fix this crap
    int getSprintCount(Date start, Date end, int duration) {
        if (end != null) {
            int count = (int) (((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24)) / (7 * duration));
            int rem = (int) (((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24)) % (7 * duration));
            if (rem > 0) {
                return count + 1;
            }
            return count;
        } else {
            return 1;
        }
    }

    // TODO - fix this crap
    int getCurrentSprint(Date start, Date end, int duration) {
        if (end != null) {
            int count = (int) (((System.currentTimeMillis() - start.getTime()) / (1000 * 60 * 60 * 24)) / (7 * duration));
            int rem = (int) (((System.currentTimeMillis() - start.getTime()) / (1000 * 60 * 60 * 24)) % (7 * duration));
            if (rem > 0) {
                return count + 1;
            }
            return count;
        } else {
            return 1;
        }
    }
}
