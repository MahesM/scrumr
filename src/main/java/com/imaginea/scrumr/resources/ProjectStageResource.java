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
import com.imaginea.scrumr.entities.ProjectStage;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectStageManager;
import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

@Controller
@RequestMapping("/projectstage")
public class ProjectStageResource {

	@Autowired
    ProjectManager projectManager;

    @Autowired
    ProjectStageManager projectStageManager;


    private static final Logger logger = LoggerFactory.getLogger(ProjectStageResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<ProjectStage> fetchProject(@PathVariable("id") String id) {
    	ProjectStage projectStage = projectStageManager.readProjectStage(Integer.parseInt(id));
        List<ProjectStage> ProjectStages = new ArrayList<ProjectStage>();
        ProjectStages.add(projectStage);
        return ProjectStages;
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectStage> createProjectStage(@RequestParam String projectStageList)

    {
        JsonElement jsonElement = new JsonParser().parse(projectStageList);
        JsonArray projectStages = jsonElement.getAsJsonObject().get("projectStages").getAsJsonArray();
        for(Object stage:projectStages){
            JsonObject jsonStage;
            if(stage instanceof JsonObject){
                jsonStage = (JsonObject)stage;
                String description = jsonStage.get("description").getAsString();
                int rank = jsonStage.get("rank").getAsInt();
                int projectID = jsonStage.get("projectid").getAsInt();
            }
        }
        ProjectStage projectStage = new ProjectStage();
        try {
        //	Project project = projectManager.readProject(Integer.parseInt(projectId));
        //	projectStage.setProject(project);
        //	projectStage.setDescription(description);
        //    projectStageManager.createProjectStage(projectStage);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        List<ProjectStage> result = new ArrayList<ProjectStage>();
        result.add(projectStage);

        return result;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectStage> updateProjectStage(@RequestParam String projectid, @RequestParam String pStageNo, @RequestParam String title, 
                                    @RequestParam String description,@RequestParam String imageUrlIndex,@RequestParam String rank)

    {
        int stageNo = 0;
        ProjectStage projectStage = null;
        try{
            stageNo = Integer.parseInt(pStageNo);
            projectStage = projectStageManager.readProjectStage(stageNo);
        }catch(NumberFormatException e){
            projectStage = null;
        }        
        
        List<ProjectStage> result = new ArrayList<ProjectStage>();
        
        if (projectStage != null) {
            try {
                projectStage.setTitle(title);
                projectStage.setDescription(description);
                projectStage.setImageUrlIndex(Integer.parseInt(imageUrlIndex));
                projectStage.setRank(Integer.parseInt(rank));
                projectStageManager.updateProjectStage(projectStage);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
            result.add(projectStage);
        }else{
            projectStage = new ProjectStage();
            projectStage.setTitle(title);
            projectStage.setDescription(description);
            projectStage.setImageUrlIndex(Integer.parseInt(imageUrlIndex));
            projectStage.setRank(Integer.parseInt(rank));
            projectStage.setProject(projectManager.readProject(Integer.parseInt(projectid)));
            projectStageManager.createProjectStage(projectStage);            
        }
        return result;
    }
    
    @RequestMapping(value = "/updatestagerank", method = RequestMethod.POST)
    public @ResponseBody
    void updateStageRank(@RequestParam String orderedStageIdList)

    {
        String[] stages = orderedStageIdList.split("&");
        int rank = 0;
        for(String stage:stages){
            int stageId = Integer.parseInt(stage.split("=")[1]);
            ProjectStage projectStage = projectStageManager.readProjectStage(stageId);
            projectStage.setRank(rank);
            projectStageManager.updateProjectStage(projectStage);
            rank ++;
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteProject(@PathVariable("id") String id) {
        ProjectStage projectStage = projectStageManager.readProjectStage(Integer.parseInt(id));
        if (projectStage != null) {
            projectStageManager.deleteProjectStage(projectStage);
            logger.debug("SUCCESS");
            return "{\"result\":\"success\"}";
        } else {
            logger.debug("FAILURE");
            return "{\"result\":\"failure\"}";
        }
    }   
}
