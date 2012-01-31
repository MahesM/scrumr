package com.imaginea.scrumr.resources;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectManager;
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
	UserServiceManager userServiceManager;

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Project> fetchProject(@PathVariable("id") String id) {
		Project project = projectManager.readProject(Integer.parseInt(id));
		List<Project> projects = new ArrayList<Project>();
		projects.add(project);
		return projects;
	}
	
	@RequestMapping(value="/user/{userid}", method = RequestMethod.GET)
	public @ResponseBody List<Project> fetchProjectsByUser(@PathVariable("userid") String id) {
		User user = userServiceManager.readUser(id);
		return user.getProjects();
			
	}
	
	@RequestMapping(value="/adduser", method = RequestMethod.POST)
	public @ResponseBody String addUser(
			@RequestParam String userid,
    		@RequestParam String projectId
		) {
		String uid = userid;
		System.out.println("User Id :"+uid);
		Integer pid = Integer.parseInt(projectId);
		Project project = projectManager.readProject(pid);
		System.out.println("User Object :"+userServiceManager.readUser(uid));
		project.addAssignees(userServiceManager.readUser(uid));
		projectManager.updateProject(project);
		return "{\"result\":\"success\"}";
	}
	
	@RequestMapping(value="/removeuser", method = RequestMethod.POST)
	public @ResponseBody String removeUser(
			@RequestParam String userid,
    		@RequestParam String projectId
		) {
		String uid = userid;
		Integer pid = Integer.parseInt(projectId);
		Project project = projectManager.readProject(pid);
		project.removeAssignees(userServiceManager.readUser(uid));
		projectManager.updateProject(project);
		return "{\"result\":\"success\"}";
	}
	
	@RequestMapping(value="/create", method = RequestMethod.POST)	
    public @ResponseBody List<Project> createProject(
    		@RequestParam String pTitle,
    		@RequestParam String pDescription,
    		@RequestParam String pSprintDuration,
    		@RequestParam String pStartDate,
    		@RequestParam String pEndDate,
    		@RequestParam String assignees,
    		@RequestParam String current_user
    		)
	
	{
		Project project = new Project();	
		try {
			
			project.setTitle(pTitle);
			project.setDescription(pDescription);
			Date date = null;
			Date date1 = null;
			int sprint_count =0;
			int duration = Integer.parseInt(pSprintDuration);
			if(pStartDate != "" && pEndDate != ""){
				try {
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
					date = format.parse(pStartDate);
					project.setStart_date(date);
					project.setCurrent_sprint(0);
					project.setStatus("Not Started");
					SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
					date1 = format1.parse(pEndDate);
					project.setEnd_date(date1);
					sprint_count  = getSprintCount(date, date1,Integer.parseInt(pSprintDuration));
					project.setNo_of_sprints(sprint_count);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String userStr = assignees;
			String[] users = userStr.split(",");
			Set<User> userList =  new HashSet<User>();
			for(String s: users){
				System.out.println("user: "+s);
				User user = userServiceManager.readUser(s);
				userList.add(user);
			}
			project.setAssignees(userList);
			project.setSprint_duration(duration);
			project.setCreatedby(current_user);
			project.setLast_updated(new java.sql.Date(System.currentTimeMillis()));
			project.setLast_updatedby(current_user);
			project.setCreation_date(new java.sql.Date(System.currentTimeMillis()));

			projectManager.createProject(project);
			Date currentdate = date;
			for(int i=0;i<sprint_count;i++){

				Sprint sprint = new Sprint();
				sprint.setId(i+1);
				sprint.setStartdate(new java.sql.Date(currentdate.getTime()));
				Date enddate = new Date(currentdate.getTime() + ((7*duration)*24*60*60*1000));
				if(enddate.before(date1)){
					sprint.setEnddate(new java.sql.Date(enddate.getTime()));
				}else{
					sprint.setEnddate(new java.sql.Date(date1.getTime()));
				}
				if(currentdate.after(new Date())){
        			sprint.setStatus("Not Started");
        		}else{
        			if(enddate.before(new Date())){
            			sprint.setStatus("Finished");
            		}else{
            			sprint.setStatus("In Progress");
            			project.setCurrent_sprint(sprint.getId());
            			project.setStatus("In Progress");
            			projectManager.updateProject(project);
            		}
        		}
				sprint.setProject(project);
				sprintManager.createSprint(sprint);
				currentdate = new Date(currentdate.getTime() + ((7*duration)*24*60*60*1000));;
			}
			
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
       
        List<Project> result = new ArrayList<Project>();
        result.add(project);
		
	    return result;
		
    }
	
	int getSprintCount(Date start, Date end, int duration){
		int count = (int)(((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))/(7*duration));
		int rem = (int)(((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))%(7*duration));
		if(rem > 0){
			return count + 1;
		}
		return count;
	}
	
	int getCurrentSprint(Date start, int duration){
		return (int) Math.ceil((( System.currentTimeMillis() - start.getTime())/ (1000 * 60 * 60 * 24))/(7*duration)) + 1;
	}
}

