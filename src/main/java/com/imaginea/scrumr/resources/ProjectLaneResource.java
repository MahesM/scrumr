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
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectLaneManager;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@Controller
@RequestMapping("/projectlane")
public class ProjectLaneResource {

	@Autowired
    ProjectManager projectManager;

    @Autowired
    ProjectLaneManager projectLaneManager;


    private static final Logger logger = LoggerFactory.getLogger(ProjectResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<ProjectLane> fetchProject(@PathVariable("id") String id) {
    	ProjectLane projectLane = projectLaneManager.readProjectLane(Integer.parseInt(id));
        List<ProjectLane> ProjectLanes = new ArrayList<ProjectLane>();
        ProjectLanes.add(projectLane);
        return ProjectLanes;
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectLane> createProjectLane(@RequestParam String description, @RequestParam String color,
                                    @RequestParam String projectId)

    {
    	ProjectLane projectLane = new ProjectLane();
        try {
        	Project project = projectManager.readProject(Integer.parseInt(projectId));
        	projectLane.setColor(Integer.parseInt(color));
        	projectLane.setProject(project);
        	projectLane.setDescription(description);
            projectLaneManager.createProjectLane(projectLane);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        List<ProjectLane> result = new ArrayList<ProjectLane>();
        result.add(projectLane);

        return result;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectLane> updateProjectLane(@RequestParam String pLaneNo, @RequestParam String description,
                                    @RequestParam int color)

    {
        ProjectLane projectLane = projectLaneManager.readProjectLane(Integer.parseInt(pLaneNo));
        List<ProjectLane> result = new ArrayList<ProjectLane>();

        if (projectLane != null) {
            try {

                projectLane.setColor(color);
                projectLane.setDescription(description);

                projectLaneManager.updateProjectLane(projectLane);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
            result.add(projectLane);
        }
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteProject(@PathVariable("id") String id) {
        ProjectLane projectLane = projectLaneManager.readProjectLane(Integer.parseInt(id));
        if (projectLane != null) {
            projectLaneManager.deleteProjectLane(projectLane);
            logger.debug("SUCCESS");
            return "{\"result\":\"success\"}";
        } else {
            logger.debug("FAILURE");
            return "{\"result\":\"failure\"}";
        }
    }
    
    @RequestMapping(value = "/fetchprojectlanes/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<ProjectLane> fetchProjectLanesByProjectId(@PathVariable("id") String id) {
        return projectLaneManager.fetchAllProjectLaneByProject(Integer.parseInt(id));
    }

   
}
