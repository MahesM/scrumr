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

import com.imaginea.scrumr.entities.ProjectPriority;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectPriorityManager;
import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

@Controller
@RequestMapping("/projectpriority")
public class ProjectPriorityResource {

	@Autowired
    ProjectManager projectManager;

    @Autowired
    ProjectPriorityManager projectPriorityManager;


    private static final Logger logger = LoggerFactory.getLogger(ProjectPriorityResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<ProjectPriority> fetchProjectPriority(@PathVariable("id") String id) {
    	ProjectPriority projectPriority = projectPriorityManager.readProjectPriority(Integer.parseInt(id));
        List<ProjectPriority> projectPriorities = new ArrayList<ProjectPriority>();
        projectPriorities.add(projectPriority);
        return projectPriorities;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectPriority> updateProjectPriority(@RequestParam String projectid,@RequestParam(required = false) String pPriorityNo, @RequestParam String description,
                                    @RequestParam String color,@RequestParam String rank)
    {
        int priorityNo = 0;
        ProjectPriority projectPriority = null;
        try{
            priorityNo = Integer.parseInt(pPriorityNo);
            projectPriority = projectPriorityManager.readProjectPriority(priorityNo);
        }catch(NumberFormatException e){
            projectPriority = null;
        }
        
        List<ProjectPriority> result = new ArrayList<ProjectPriority>();

        if (projectPriority != null) {
            try {
                projectPriority.setColor(color);
                projectPriority.setDescription(description);
                projectPriority.setRank(Integer.parseInt(rank));
                projectPriorityManager.updateProjectPriority(projectPriority);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                String exceptionMsg = "Error occured during creation of the project priority "+description ;
                ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            }            
        }else{
            try{
                projectPriority = new ProjectPriority();
                projectPriority.setColor(color);
                projectPriority.setDescription(description);
                projectPriority.setProject(projectManager.readProject(Integer.parseInt(projectid)));
                projectPriority.setRank(Integer.parseInt(rank));
                projectPriorityManager.createProjectPriority(projectPriority); 
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
                String exceptionMsg = "Error occured during creation of the project priority "+description ;
                ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            }                                                        
        }
        result.add(projectPriority);
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteProject(@PathVariable("id") String id) {
        ProjectPriority projectPriority = projectPriorityManager.readProjectPriority(Integer.parseInt(id));
        if (projectPriority != null) {
            try{
                projectPriorityManager.deleteProjectPriority(projectPriority);
                logger.debug("SUCCESS");
                return "{\"result\":\"success\"}";                
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
                String exceptionMsg = "Error occured during deletion of the project priority with pKey "+id ;
                ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            }             
        } 
        logger.debug("FAILURE");
        return "{\"result\":\"failure\"}";
    }
}
