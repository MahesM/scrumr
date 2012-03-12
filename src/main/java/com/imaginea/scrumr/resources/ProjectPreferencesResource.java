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
import com.imaginea.scrumr.entities.ProjectPreferences;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectPreferencesManager;

@Controller
@RequestMapping("/projectpreferences")
public class ProjectPreferencesResource {

	@Autowired
    ProjectManager projectManager;

    @Autowired
    ProjectPreferencesManager projectPreferencesManager;


    private static final Logger logger = LoggerFactory.getLogger(ProjectPreferencesResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<ProjectPreferences> fetchProjectPreferences(@PathVariable("id") String id) {
    	ProjectPreferences projectPreference = projectPreferencesManager.readProjectPreferences(Integer.parseInt(id));
        List<ProjectPreferences> projectPreferences = new ArrayList<ProjectPreferences>();
        projectPreferences.add(projectPreference);
        return projectPreferences;
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectPreferences> createProjectPreferences(@RequestParam String projectId, @RequestParam String storypriorityEnabled,
                                    @RequestParam String storyPointMandatory, @RequestParam String storyPointType,
                                    @RequestParam String storyPointLimit,@RequestParam String storySizeEnabled,
                                    @RequestParam String taskMileStoneMandatory,@RequestParam String taskMileStoneEnabled,
                                    @RequestParam String mileStoneType,@RequestParam String mileStoneRange)

    {
        ProjectPreferences projectPreferences = new ProjectPreferences();
        try {
            Project project = projectManager.readProject(Integer.parseInt(projectId));
            projectPreferences.setProject(project);
            
            projectPreferences.setStorypriorityEnabled(Boolean.valueOf(storypriorityEnabled));
            
            projectPreferences.setStoryPointMandatory(Boolean.valueOf(storyPointMandatory));
            projectPreferences.setStoryPointType(Integer.parseInt(storyPointType));
            projectPreferences.setStoryPointLimit(Integer.parseInt(storyPointLimit));
            projectPreferences.setStoryPointEnabled(Boolean.valueOf(storySizeEnabled));            
            
            projectPreferences.setTaskMileStoneMandatory(Boolean.valueOf(taskMileStoneMandatory));
            projectPreferences.setTaskMileStoneEnabled(Boolean.valueOf(taskMileStoneEnabled));
            projectPreferences.setMileStoneType(Integer.parseInt(mileStoneType));
            projectPreferences.setMileStoneRange(Integer.parseInt(mileStoneRange));
                       
            projectPreferencesManager.createProjectPreferences(projectPreferences);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        List<ProjectPreferences> result = new ArrayList<ProjectPreferences>();
        result.add(projectPreferences);

        return result;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectPreferences> updateProjectPreferences(@RequestParam String pKey,@RequestParam String projectId, @RequestParam String storypriorityEnabled,
                                    @RequestParam String storyPointMandatory, @RequestParam String storyPointType,
                                    @RequestParam String storyPointLimit,@RequestParam String storySizeEnabled,
                                    @RequestParam String taskMileStoneMandatory,@RequestParam String taskMileStoneEnabled,
                                    @RequestParam String mileStoneType,@RequestParam String mileStoneRange)

    {
        ProjectPreferences projectPreferences = projectPreferencesManager.readProjectPreferences(Integer.parseInt(pKey));
        List<ProjectPreferences> result = new ArrayList<ProjectPreferences>();

        if (projectPreferences != null) {
            try {
                projectPreferences.setStorypriorityEnabled(Boolean.valueOf(storypriorityEnabled));
                
                projectPreferences.setStoryPointMandatory(Boolean.valueOf(storyPointMandatory));
                projectPreferences.setStoryPointType(Integer.parseInt(storyPointType));
                projectPreferences.setStoryPointLimit(Integer.parseInt(storyPointLimit));
                projectPreferences.setStoryPointEnabled(Boolean.valueOf(storySizeEnabled));            
                
                projectPreferences.setTaskMileStoneMandatory(Boolean.valueOf(taskMileStoneMandatory));
                projectPreferences.setTaskMileStoneEnabled(Boolean.valueOf(taskMileStoneEnabled));
                projectPreferences.setMileStoneType(Integer.parseInt(mileStoneType));
                projectPreferences.setMileStoneRange(Integer.parseInt(mileStoneRange));
                
                projectPreferencesManager.updateProjectPreferences(projectPreferences);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
            result.add(projectPreferences);
        }
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteProject(@PathVariable("id") String id) {
        ProjectPreferences projectPreferences = projectPreferencesManager.readProjectPreferences(Integer.parseInt(id));
        if (projectPreferences != null) {
            projectPreferencesManager.deleteProjectPreferences(projectPreferences);
            logger.debug("SUCCESS");
            return "{\"result\":\"success\"}";
        } else {
            logger.debug("FAILURE");
            return "{\"result\":\"failure\"}";
        }
    }
}
