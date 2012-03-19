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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.ProjectPreferences;
import com.imaginea.scrumr.entities.ProjectPriority;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectPreferencesManager;
import com.imaginea.scrumr.interfaces.ProjectPriorityManager;
import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

@Controller
@RequestMapping("/projectpreferences")
public class ProjectPreferencesResource {

	@Autowired
    ProjectManager projectManager;

    @Autowired
    ProjectPreferencesManager projectPreferencesManager;

    @Autowired
    ProjectPriorityManager projectPriorityManager;

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
                                    @RequestParam String storyPointType,
                                    @RequestParam String storyPointLimit,
                                    @RequestParam String mileStoneType,@RequestParam String mileStoneRange)

    {
        ProjectPreferences projectPreferences = new ProjectPreferences();
        try {
            Project project = projectManager.readProject(Integer.parseInt(projectId));
            projectPreferences.setProject(project);
            
            projectPreferences.setStorypriorityEnabled(Boolean.valueOf(storypriorityEnabled));
            projectPreferences.setStoryPointType(Integer.parseInt(storyPointType));
            projectPreferences.setStoryPointLimit(Integer.parseInt(storyPointLimit));
            
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
    List<ProjectPreferences> updateProjectPreferences(@RequestParam String projectPreference)

    {
        List<ProjectPreferences> result = new ArrayList<ProjectPreferences>();        
        JsonElement jsonElement = new JsonParser().parse(projectPreference);
        JsonArray projectPreferencesJson = jsonElement.getAsJsonObject().get("projectPreferences").getAsJsonArray();
        for(Object preference:projectPreferencesJson){
            JsonObject jsonPreference;
            if(preference instanceof JsonObject){
                jsonPreference = (JsonObject)preference;
                Boolean storypriorityEnabled = jsonPreference.get("storypriorityEnabled").getAsBoolean();
                int storySizeType = jsonPreference.get("storySizeType").getAsInt();
                int storySizeLowRangeIndex = jsonPreference.get("storySizeLowRangeIndex").getAsInt();
                int storySizeHighRangeIndex = jsonPreference.get("storySizeHighRangeIndex").getAsInt();
                int taskmileStoneType = jsonPreference.get("taskmileStoneType").getAsInt();
                int taskmileStoneUpperRange = jsonPreference.get("taskmileStoneUpperRange").getAsInt();
                int pPreferenceNo = jsonPreference.get("pPreferenceNo").getAsInt();
                
                ProjectPreferences projectPreferences = projectPreferencesManager.readProjectPreferences(pPreferenceNo);
                
                if (projectPreferences != null) {
                    try {
                        projectPreferences.setStorypriorityEnabled(storypriorityEnabled);
                        projectPreferences.setStoryPointType(storySizeType);
                        int storyPointLimit = 0 | 1<<(storySizeLowRangeIndex) | 1<<(storySizeHighRangeIndex);
                        projectPreferences.setStoryPointLimit(storyPointLimit);
                        
                        projectPreferences.setMileStoneType(taskmileStoneType);
                        projectPreferences.setMileStoneRange(taskmileStoneUpperRange);
                        
                        projectPreferencesManager.updateProjectPreferences(projectPreferences);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        String exceptionMsg = "Error occured during updation of the project preferences with pKey "+pPreferenceNo ;
                        ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
                    }
                    result.add(projectPreferences);
                }
                
                JsonArray projectPriorities = jsonPreference.get("projectPriority").getAsJsonArray();
                updateProjectPriorities(projectPriorities);
            }
        }
        return result;
    }

    private void updateProjectPriorities(JsonArray projectPriorities) {
        for(Object prjectPriority:projectPriorities){
            JsonObject priority;
            if(prjectPriority instanceof JsonObject){
                priority = (JsonObject)prjectPriority;
                int projectid =  priority.get("projectid").getAsInt();
                String description =  priority.get("description").getAsString();
                String color =  priority.get("color").getAsString();
                int rank =  priority.get("rank").getAsInt();
                JsonElement pPriorityNo =  priority.get("pPriorityNo");
                ProjectPriority projectPriority = null;
                if(pPriorityNo != null){
                    String priorityId = pPriorityNo.getAsString();
                    if(priorityId != null && !priorityId.isEmpty())
                        projectPriority = projectPriorityManager.readProjectPriority(pPriorityNo.getAsInt());
                }
                
                if (projectPriority != null) {
                    try {
                        projectPriority.setColor(color);
                        projectPriority.setDescription(description);
                        projectPriority.setRank(rank);
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
                        projectPriority.setProject(projectManager.readProject(projectid));
                        projectPriority.setRank(rank);
                        projectPriorityManager.createProjectPriority(projectPriority); 
                    }catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        String exceptionMsg = "Error occured during creation of the project priority "+description ;
                        ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
                    }                                                        
                }
            }
        }
        
    }  
    
}
