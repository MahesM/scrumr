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

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.ProjectPriority;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectPriorityManager;

@Controller
@RequestMapping("/projectpriority")
public class ProjectPriorityResource {

	@Autowired
    ProjectManager projectManager;

    @Autowired
    ProjectPriorityManager projectPriorityManager;


    private static final Logger logger = LoggerFactory.getLogger(ProjectResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<ProjectPriority> fetchProjectPriority(@PathVariable("id") String id) {
    	ProjectPriority projectPriority = projectPriorityManager.readProjectPriority(Integer.parseInt(id));
        List<ProjectPriority> projectPriorities = new ArrayList<ProjectPriority>();
        projectPriorities.add(projectPriority);
        return projectPriorities;
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectPriority> createProjectPriority(@RequestParam String description, @RequestParam String color,
                                    @RequestParam String projectId)

    {
        ProjectPriority projectPriority = new ProjectPriority();
        try {
            Project project = projectManager.readProject(Integer.parseInt(projectId));
            projectPriority.setColor(color);
            projectPriority.setProject(project);
            projectPriority.setDescription(description);
            projectPriorityManager.createProjectPriority(projectPriority);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        List<ProjectPriority> result = new ArrayList<ProjectPriority>();
        result.add(projectPriority);

        return result;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectPriority> updateProjectPriority(@RequestParam String pPriorityNo, @RequestParam String description,
                                    @RequestParam String color)

    {
        ProjectPriority projectPriority = projectPriorityManager.readProjectPriority(Integer.parseInt(pPriorityNo));
        List<ProjectPriority> result = new ArrayList<ProjectPriority>();

        if (projectPriority != null) {
            try {
                projectPriority.setColor(color);
                projectPriority.setDescription(description);
                projectPriorityManager.updateProjectPriority(projectPriority);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
            result.add(projectPriority);
        }
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteProject(@PathVariable("id") String id) {
        ProjectPriority projectPriority = projectPriorityManager.readProjectPriority(Integer.parseInt(id));
        if (projectPriority != null) {
            projectPriorityManager.deleteProjectPriority(projectPriority);
            logger.debug("SUCCESS");
            return "{\"result\":\"success\"}";
        } else {
            logger.debug("FAILURE");
            return "{\"result\":\"failure\"}";
        }
    }
}
