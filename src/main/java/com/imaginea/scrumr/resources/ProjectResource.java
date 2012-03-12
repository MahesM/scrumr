package com.imaginea.scrumr.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

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
import com.imaginea.scrumr.entities.ProjectStage;
import com.imaginea.scrumr.entities.ProjectPriority;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectStageManager;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectPriorityManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;
import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

@Controller
@RequestMapping("/projects")
public class ProjectResource {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SprintManager sprintManager;
    
    @Autowired
    ProjectStageManager projectStageManager;
    
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
        Project project = null;
        
        int currentSprint;
        String status;
        Date projectStartDate = null;
        Date projectEndDate = null;
        int sprint_count = 0;            
        int sprintDuration = Integer.parseInt(pSprintDuration);
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            projectStartDate = format.parse(pStartDate);                
            
            if (pEndDate != "") {                    
                projectEndDate = format.parse(pEndDate);
            } else {
                projectEndDate = null;                       
            }                
            sprint_count = getSprintCount(projectStartDate, projectEndDate, Integer.parseInt(pSprintDuration));                
        } catch (ParseException e1) {
            String exceptionMsg = "Date field values are not proper";
            logger.error(e1.getMessage(), e1);
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, null);
        }
        try {            
            String userStr = assignees;
            String[] users = userStr.split(",");
            Set<User> userList = new HashSet<User>();
            for (String s : users) {
                logger.info("user: " + s);
                User user = userServiceManager.readUser(s);
                userList.add(user);
            }
            if (projectStartDate.after(new Date())) {
                currentSprint = 0;
                status = "Not Started";
                
            } else if(projectEndDate!=null && projectEndDate.before(new Date())){
                currentSprint = sprint_count;
                status = "Finished";
            }else{
                currentSprint = getCurrentSprint(projectStartDate, projectEndDate, sprintDuration);
                status = "In Progress";
            }    

            logger.info("CurrentUser:" + current_user + " " + current_user.length());
            
            project = initializeProject(pTitle, pDescription, projectStartDate, projectEndDate, sprint_count, currentSprint, status, userList, current_user,sprintDuration);
            projectManager.createProject(project);
            
        } catch (Exception e) {
            if(e instanceof PersistenceException){
                PersistenceException persistentException = (PersistenceException)e;
                String exceptionMsg = "Kindly contact the adminstrator";
                logger.error(persistentException.getCause().getCause().getMessage());
                logger.error(e.getMessage(), e);
                ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, null);
            }
            ScrumrException.create(e.getMessage(), MessageLevel.SEVERE, null);           
        }
        try{
            Date sprintFirstDay = projectStartDate;
            for (int sprintNo = 1; sprintNo <= sprint_count; sprintNo++) {

                createSprint(sprintNo,sprint_count,sprintFirstDay, projectEndDate, sprintDuration, project);
                sprintFirstDay = new Date(sprintFirstDay.getTime() + ((7 * sprintDuration) * 86400000L));                
            } 
        }catch(Exception e){
            if(e instanceof PersistenceException){
                PersistenceException persistentException = (PersistenceException)e;
                String exceptionMsg = "Kindly contact the adminstrator";
                logger.error(persistentException.getCause().getCause().getMessage());
                logger.error(e.getMessage(), e);
                ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, null);
            }
            ScrumrException.create(e.getMessage(), MessageLevel.SEVERE, null);  
        }  
        
        List<Project> result = new ArrayList<Project>();
        result.add(project);

        return result;
    }
    
    private void createSprint(int sprintNo, int totalSprints, Date sprintFirstDay, Date projectEndDate, int sprintDuration, Project project) {
        
        String sprintStatus = null;
        Date sprintEndDate = new Date(sprintFirstDay.getTime() + ((7 * sprintDuration) * 86400000L)
                                        - 3600000L);
        if (sprintNo == (totalSprints)) {
            sprintEndDate = new Date(sprintFirstDay.getTime() + ((7 * sprintDuration) * 86400000L));
        }
        if (projectEndDate != null){
            if(sprintEndDate.before(projectEndDate)) {
                sprintEndDate = new java.sql.Date(sprintEndDate.getTime());
            } else {
            sprintEndDate = new java.sql.Date(projectEndDate.getTime()
                                            + (86400000L - 3600000L));                    
            }
        }        
        
        if (sprintFirstDay.after(new Date())) {
            sprintStatus = "Not Started"; 
            
        } else {
            if (sprintEndDate.before(new Date())) {
                logger.debug(sprintEndDate.toString());
                sprintStatus = "Finished"; 
            } else {
                logger.debug("Past sprint");
                sprintStatus = "In Progress"; 
            }
        }
        
        Sprint sprint = new Sprint();
        sprint.setId(sprintNo);
        sprint.setStartdate(new java.sql.Date(sprintFirstDay.getTime()));
        sprint.setEnddate(sprintEndDate);
        sprint.setStatus(sprintStatus);        
        sprint.setProject(project);
        sprintManager.createSprint(sprint);        
    }

    private Project initializeProject(String title, String description, Date startDate, Date endDate,
                                    int sprint_count, int currentSprint,String status, Set<User> userList, String current_user, int duration){
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setStart_date(startDate);
        project.setEnd_date(endDate);
        project.setNo_of_sprints(sprint_count);
        project.setCurrent_sprint(sprint_count);
        project.setStatus(status);
        project.setAssignees(userList);
        project.setSprint_duration(duration);
        project.setCreatedby(current_user);
        project.setLast_updated(new java.sql.Date(System.currentTimeMillis()));
        project.setLast_updatedby(current_user);
        project.setCreation_date(new java.sql.Date(System.currentTimeMillis()));
        return project;
        
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
        deleteProjectStagesByProject(projectId);
        deleteProjectPrioritiesByProject(projectId);
    }

    private void deleteProjectPrioritiesByProject(int projectId) {
       List<ProjectPriority> projectProrityList = projectPriorityManager.fetchAllProjectPrioritiesByProject(projectId);
        
        for(ProjectPriority projectPriority:projectProrityList)
            projectPriorityManager.deleteProjectPriority(projectPriority);        
    }

    private void deleteProjectStagesByProject(int projectId) {
        List<ProjectStage> projectStageList = projectStageManager.fetchAllProjectStageByProject(projectId);
        
        for(ProjectStage projectStage:projectStageList)
           projectStageManager.deleteProjectStage(projectStage);        
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
