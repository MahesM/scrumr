package com.imaginea.scrumr.resources;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.ProjectPreferences;
import com.imaginea.scrumr.entities.ProjectPriority;
import com.imaginea.scrumr.entities.ProjectStage;
import com.imaginea.scrumr.entities.SearchStoryParameters;
import com.imaginea.scrumr.entities.Sprint;

import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.StoryHistory;
import com.imaginea.scrumr.entities.StorySizeInfo;
import com.imaginea.scrumr.entities.StoryStageInfo;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectPreferencesManager;
import com.imaginea.scrumr.interfaces.ProjectPriorityManager;
import com.imaginea.scrumr.interfaces.ProjectStageManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.StoryHistoryManager;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;
import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

@Controller
@RequestMapping("/stories")
public class StoryResource {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SprintManager sprintManager;
    
    @Autowired
    ProjectPriorityManager projectPriorityManager;
    
    @Autowired
    ProjectPreferencesManager projectPreferencesManager;
    
    @Autowired
    ProjectStageManager projectStageManager;

    @Autowired
    UserServiceManager userServiceManager;

    @Autowired
    StoryManager storyManager;

    @Autowired
    StoryHistoryManager storyHistoryManager;

    private static final Logger logger = LoggerFactory.getLogger(StoryResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchStory(@PathVariable("id") String id) {
        
        List<Story> stories = new ArrayList<Story>();
        int storyId = ResourceUtil.stringToIntegerConversion("story_id", id);
        
        try{
            Story story = storyManager.readStory(storyId);            
            stories.add(story);            
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the story with pKey "+id ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        
        return stories;
    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchStoriesByProject(@PathVariable("id") String id) {

        List<Story> stories = null;
        int projectId = ResourceUtil.stringToIntegerConversion("project_id", id);
        
        try{
            stories = storyManager.fetchStoriesByProject(projectId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the stories of the project with pKey "+id ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return stories;
    }
    
    @RequestMapping(value = "/fetchprojectstorycount/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<StorySizeInfo> getProjectStoryCount(@PathVariable("id") String id) {
        Project project = projectManager.readProject(Integer.parseInt(id));
        
        List<Integer> completedStoriesBySize = new ArrayList<Integer>();
        List<Integer> totalStoriesBySize = new ArrayList<Integer>();
        List<String> storySizeValue = new ArrayList<String>();
        List<StorySizeInfo> storySizeDetails = new ArrayList<StorySizeInfo>();

        
        ProjectPreferences preference = project.getProjectPreferences();
        int highIndex = preference.getStorySizeHighRangeIndex();
        int lowIndex = preference.getStorySizeLowRangeIndex();
        int storyType = preference.getStoryPointType();
        
        for(int count=lowIndex; count<=highIndex;count++){
            storySizeValue.add(ProjectPreferences.defaultStoryTypes[storyType][count]);
            totalStoriesBySize.add(0);
            completedStoriesBySize.add(0);            
        }
        Set<Story> storyList = project.getStories(); 
        if(storyList != null){         
            for(Story story:storyList){
                ProjectStage stage = story.getStstage();
                if(stage != null){
                    String storyPoint = story.getStoryPoint()+"";
                    int storyPointIndex = storySizeValue.indexOf(storyPoint);
                    if(storyPointIndex != -1){
                        int incrementCount = totalStoriesBySize.get(storyPointIndex) + 1;
                        totalStoriesBySize.set(storyPointIndex,incrementCount);
                        
                        if(stage.getPkey() == project.getMaxRankStageId()){
                            incrementCount = completedStoriesBySize.get(storyPointIndex) + 1;
                            completedStoriesBySize.set(storyPointIndex,incrementCount);
                        }
                    }                                    
                }       
            } 
        }
        for(int count = 0;count < storySizeValue.size(); count ++){
            StorySizeInfo storySizeInfo = new StorySizeInfo();
            storySizeInfo.setCompletedStories(completedStoriesBySize.get(count));
            storySizeInfo.setTotalStories(totalStoriesBySize.get(count));
            storySizeInfo.setValue(storySizeValue.get(count));
            storySizeDetails.add(storySizeInfo);
        }
        return storySizeDetails;
    }
    @RequestMapping(value = "/fetchsprintstagecount/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<List<StoryStageInfo>> getProjectStageCount(@PathVariable("id") String id) {
        Project project = projectManager.readProject(Integer.parseInt(id));
        List<List<StoryStageInfo>> sprintStoryStage = new ArrayList<List<StoryStageInfo>>();
        
        Set<Sprint> sprints = project.getSprints();
        for(Sprint sprint:sprints){
            List<Integer> storyCountByStages = new ArrayList<Integer>();
            List<Integer> stageImageUrl =new ArrayList<Integer>();
            List<Integer> stageId =new ArrayList<Integer>();

            List<StoryStageInfo> storyStageDetails = new ArrayList<StoryStageInfo>();
            
            List<ProjectStage> projectStages = project.getProjectStages();
           
            for(ProjectStage projectStage:projectStages){
                stageImageUrl.add(projectStage.getImageUrlIndex());
                stageId.add(projectStage.getPkey());
                storyCountByStages.add(0);
            }
            Set<Story> storyList = sprint.getStoryList();
            if(storyList != null){         
                for(Story story:storyList){
                    ProjectStage stage = story.getStstage();
                    if(stage != null){
                        int pKey = stage.getPkey();
                        int index = stageId.indexOf(pKey);
                        
                        if(index != -1){
                            int currentCount = storyCountByStages.get(index);
                            currentCount++;
                            storyCountByStages.set(index,currentCount);  
                        }                    
                    }       
                } 
            }
            
            for(int count = 0;count < stageId.size(); count ++){
                StoryStageInfo storyStage = new StoryStageInfo();
                storyStage.setId(stageId.get(count));
                storyStage.setImageUrlIndex(stageImageUrl.get(count));
                storyStage.setStoryCount(storyCountByStages.get(count));
                storyStage.setSprintId(sprint.getPkey());
                storyStageDetails.add(storyStage);
            }
            sprintStoryStage.add(storyStageDetails);
        }
        return sprintStoryStage;
    }
    
    @RequestMapping(value = "/fetchsprintstorycount/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<StorySizeInfo> fetchSprintStoryCount(@PathVariable("id") String id) {

        Sprint sprint = sprintManager.readSprint(Integer.parseInt(id));
        Project project = sprint.getProject();
        
       
            List<Integer> completedStoriesBySize = new ArrayList<Integer>();
            List<Integer> totalStoriesBySize = new ArrayList<Integer>();
            List<String> storySizeValue = new ArrayList<String>();
            List<StorySizeInfo> storySizeDetails = new ArrayList<StorySizeInfo>();
            
            ProjectPreferences preference = project.getProjectPreferences();
            int highIndex = preference.getStorySizeHighRangeIndex();
            int lowIndex = preference.getStorySizeLowRangeIndex();
            int storyType = preference.getStoryPointType();
            
            for(int count=lowIndex; count<=highIndex;count++){
                storySizeValue.add(ProjectPreferences.defaultStoryTypes[storyType][count]);
                totalStoriesBySize.add(0);
                completedStoriesBySize.add(0);            
            }
            
            Set<Story> storyList = sprint.getStoryList();
            if(storyList != null){         
                for(Story story:storyList){
                    ProjectStage stage = story.getStstage();
                    if(stage != null){
                        String storyPoint = story.getStoryPoint()+"";
                        int storyPointIndex = storySizeValue.indexOf(storyPoint);
                        if(storyPointIndex != -1){
                            int incrementCount = totalStoriesBySize.get(storyPointIndex) + 1;
                            totalStoriesBySize.set(storyPointIndex,incrementCount);
                            
                            if(stage.getPkey() == project.getMaxRankStageId()){
                                incrementCount = completedStoriesBySize.get(storyPointIndex) + 1;
                                completedStoriesBySize.set(storyPointIndex,incrementCount);
                            }
                        }                                    
                    }       
                } 
            }
            for(int count = 0;count < storySizeValue.size(); count ++){
                StorySizeInfo storySizeInfo = new StorySizeInfo();
                storySizeInfo.setCompletedStories(completedStoriesBySize.get(count));
                storySizeInfo.setTotalStories(totalStoriesBySize.get(count));
                storySizeInfo.setValue(storySizeValue.get(count));
                storySizeDetails.add(storySizeInfo);
            }
        return storySizeDetails;
    }
    
    @RequestMapping(value = "/fetchstorydata/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<SearchStoryParameters> fetchStories(@PathVariable("id") String id) {

        int projectId = ResourceUtil.stringToIntegerConversion("project_id", id);
        List<SearchStoryParameters> searchStoryParameters = new ArrayList<SearchStoryParameters>();
        
        try{
            fetchPriorityInfo(projectId,searchStoryParameters); 
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the priorities of the project with pKey "+id ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        
        try{
            fetchStoryPointInfo(projectId, searchStoryParameters);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the story points of the project with pKey "+id ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        
        try{
            fetchStoryTagInfo(projectId, searchStoryParameters);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the story tags of the project with pKey "+id ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return searchStoryParameters;
    }

    private void fetchStoryTagInfo(int projectId, List<SearchStoryParameters> searchStoryParameters) {
        List<Object> tags = storyManager.searchAllStoryTagsByProject(projectId);
        if(ResourceUtil.isNotEmpty(tags)){
            for (Iterator<Object> iterator = tags.iterator(); iterator.hasNext();) {
                String searchTags = (String)iterator.next();                
                if(searchTags != null){
                    String[] storyTags = searchTags.split(",");
                    for(String storyTag:storyTags){
                        searchStoryParameters.add(createStoryParameter("story_tag",storyTag));
                    } 
                }
            }
        }
    }

    private void fetchStoryPointInfo(int projectId, List<SearchStoryParameters> searchStoryParameters) {
        ProjectPreferences projectPreference = projectPreferencesManager.getPreferencesByProject(projectId);
        int highRangeIndex = projectPreference.getStorySizeHighRangeIndex();
        int lowRangeIndex = projectPreference.getStorySizeLowRangeIndex();
        int storyType = projectPreference.getStoryPointType();
        
        for(int index =lowRangeIndex; index <= highRangeIndex;index++ ){
            searchStoryParameters.add(createStoryParameter("story_size",ProjectPreferences.defaultStoryTypes[storyType][index]));
        }    
    }

    private void fetchPriorityInfo(int projectId, List<SearchStoryParameters> searchStoryParameters) {
        List<ProjectPriority> projectPrioritiesList = projectPriorityManager.fetchAllProjectPrioritiesByProject(projectId);
        if(ResourceUtil.isNotEmpty(projectPrioritiesList)){
            for(ProjectPriority projectPriority:projectPrioritiesList){                
                searchStoryParameters.add(createStoryParameter("story_priority",projectPriority.getDescription()));            }
        }
    }

    private SearchStoryParameters createStoryParameter(String type, String description) {
        SearchStoryParameters storyParameters = new SearchStoryParameters();
        storyParameters.setType(type);
        storyParameters.setValue(description);
        return storyParameters;
    }

    @RequestMapping(value = "{sprintid}/project/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchStoriesByProjectSprint(@PathVariable("sprintid") String sid,
                                    @PathVariable("id") String pid) {
        List<Story> stories = null; 
        int projectId = ResourceUtil.stringToIntegerConversion("project_id", pid);
        int sprintId = ResourceUtil.stringToIntegerConversion("sprint_id", sid);
        Sprint sprint = sprintManager.selectSprintByProject(projectManager.readProject(projectId), sprintId);
        
        if(sprint != null)
            stories = storyManager.fetchStoriesBySprint(sprint.getPkey());
        return stories;
    }

    @RequestMapping(value = "/sprint/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchStoriesBySprint(@PathVariable("id") String id) {
        
        List<Story> stories = null;        
        int sprintId = ResourceUtil.stringToIntegerConversion("sprint_id", id);
        
        try{
            stories = storyManager.fetchStoriesBySprint(sprintId);  
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the stories of the sprint (pKey) "+id ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return stories;
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public @ResponseBody
    List<Story> fetchStoriesByStatus(@RequestParam String sprintid, @RequestParam String status) {
        
        List<Story> stories = null;        
        int sprintId = ResourceUtil.stringToIntegerConversion("sprint_id", sprintid);
        
        try{
            stories = storyManager.fetchStoriesByStatus(sprintId, status);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the stories of the sprint (pKey) "+sprintId +"with status id "+status;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return stories;
    }

    @RequestMapping(value = "/backlog/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchUnAssignedStories(@PathVariable("id") String id) {
        
        List<Story> stories = null;        
        int projectId = ResourceUtil.stringToIntegerConversion("project_id", id);
        
        try{
            stories = storyManager.fetchUnAssignedStories(projectId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the unassigned stories of the project (pKey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return stories;

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    String createStory(@RequestParam String story) {

        JsonElement jsonElement = new JsonParser().parse(story);
        JsonArray storyJson = jsonElement.getAsJsonObject().get("story").getAsJsonArray();
        for(Object storyObj:storyJson){
            JsonObject jsonStory;
            if(storyObj instanceof JsonObject){
                jsonStory = (JsonObject)storyObj;
                String stTitle = jsonStory.get("stTitle").getAsString();
                String stDescription = jsonStory.get("stDescription").getAsString();
                String user = jsonStory.get("user").getAsString();
                JsonElement tags = jsonStory.get("storyTags");
                String storyTags = null;
              /*  try{
                    JsonObject tagsObject = tags.getAsJsonObject();
                    JsonArray tagsJson = tagsObject.get("tags").getAsJsonArray();
                    for(Object tagsObj:tagsJson){
                        if()
                    }
                }catch(Exception e){
                    storyTags = null;
                }
                */
                
          
                String storyPointSize = jsonStory.get("storyPointSize").getAsString();
                
                int priorityId = jsonStory.get("stPriority").getAsInt();
                int projectId = jsonStory.get("projectId").getAsInt();
                int sprintId = jsonStory.get("stSprint").getAsInt();

        
                Project project = projectManager.readProject(projectId);
                Sprint sprint = sprintManager.selectSprintByProject(project, sprintId);
                ProjectPriority priority = projectPriorityManager.readProjectPriority(priorityId);
        
                try {
                    createStory(stTitle, stDescription, sprint, priority, user, storyPointSize, storyTags, project);
                    return ResourceUtil.SUCCESS_JSON_MSG;
                    
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    String exceptionMsg = "Error occured while creating the story with title "+stTitle;
                    ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
                }
            }
        }
        return ResourceUtil.FAILURE_JSON_MSG;
    }

    private void createStory(String stTitle, String stDescription, Sprint sprint,
                                    ProjectPriority priority, String user, String storyPointSize,
                                    String storyTags, Project project) {
        
        Date date = new java.sql.Date(System.currentTimeMillis());
        Story story = new Story();
        story.setTitle(stTitle);
        story.setDescription(stDescription);            
        story.setSprint_id(sprint);
        story.setPriority(priority);
        story.setCreator(user);
        if(storyPointSize != null)
            story.setStoryPoint(storyPointSize);
        story.setStoryTags(storyTags);
        story.setCreationDate(date);
        story.setLastUpdated(date);
        story.setLastUpdatedby(user);
        story.setProject(project);
        storyManager.createStory(story);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    String updateStory(@RequestParam String storyId, @RequestParam String stTitle,@RequestParam String stDescription,
                                    @RequestParam String stPriority, @RequestParam String user,@RequestParam String projectId,
                                    @RequestParam String stSprint,@RequestParam(required = false) String storyPointSize,@RequestParam(required = false) String storyTags) {

        try {            
            int story_id = ResourceUtil.stringToIntegerConversion("story_id", storyId);
            int project_Id = ResourceUtil.stringToIntegerConversion("project_id", projectId);
            int sprint_Id = ResourceUtil.stringToIntegerConversion("project_id", stSprint);
            
            Sprint toSprint = sprintManager.selectSprintByProject(projectManager.readProject(project_Id), sprint_Id);
            Story story = storyManager.readStory(story_id);
            
            if (stTitle != null)
                story.setTitle(stTitle);
            if (stDescription != null)
                story.setDescription(stDescription);
            if (stPriority != null){
                int priorityId = ResourceUtil.stringToIntegerConversion("priority_id", stPriority);
                story.setPriority(projectPriorityManager.readProjectPriority(priorityId));                
            }
            if(storyPointSize != null){
                story.setStoryPoint(storyPointSize);
            }
            if(storyTags != null){
               story.setStoryTags(storyTags);
            }
            story.setLastUpdated(new java.sql.Date(System.currentTimeMillis()));
            story.setLastUpdatedby(user);
            story.setSprint_id(toSprint);
            
            storyManager.updateStory(story);
            return ResourceUtil.SUCCESS_JSON_MSG;
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while updating the story with title "+stTitle;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            return ResourceUtil.FAILURE_JSON_MSG;
        }        
    }

    @RequestMapping(value = "/addtosprint", method = RequestMethod.POST)
    public @ResponseBody
    String addStoryToSprint(@RequestParam String sprint, @RequestParam String stories,
                                    @RequestParam String status, @RequestParam String projectId) {

        try {
            int story_id = ResourceUtil.stringToIntegerConversion("story_id", stories);
            int project_Id = ResourceUtil.stringToIntegerConversion("project_id", projectId);
            int sprint_Id = ResourceUtil.stringToIntegerConversion("project_id", sprint);
            int stage = 0;
            try{
                stage = Integer.parseInt(status);
            }catch(NumberFormatException e){
                stage = 0; 
            }
            
            Project project = projectManager.readProject(project_Id);
            Story story = storyManager.readStory(story_id);
            Sprint toSprint = sprintManager.selectSprintByProject(project, sprint_Id);
            ProjectStage projectStage = null;
            if(stage != 0){
                projectStage = projectStageManager.readProjectStage(stage); 
            }
            story.setSprint_id(toSprint);            
            story.setStstage(projectStage);

            storyManager.updateStory(story);
            return ResourceUtil.SUCCESS_JSON_MSG;
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while adding the story (pkey) "+stories+" to the sprint with id "+sprint;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            return ResourceUtil.FAILURE_JSON_MSG;
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteStory(@PathVariable("id") String id) {
        int story_id = ResourceUtil.stringToIntegerConversion("story_id", id);
        try{
            Story story = storyManager.readStory(story_id);
            storyManager.deleteStory(story);
            return ResourceUtil.SUCCESS_JSON_MSG;
            
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while deleting the story (pkey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            return ResourceUtil.FAILURE_JSON_MSG;
        }
    }

    @RequestMapping(value = "/{id}/adduser/{uid}", method = RequestMethod.GET)
    public @ResponseBody
    String addUserToStory(@PathVariable("id") String id, @PathVariable("uid") String uid) {
        int story_id = ResourceUtil.stringToIntegerConversion("story_id", id);
        try{
            User user = userServiceManager.readUser(uid);
            Story story = storyManager.readStory(story_id);
            story.addAssignees(user);
            storyManager.updateStory(story);
            return ResourceUtil.SUCCESS_JSON_MSG;
            
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while adding the user (Pkey) "+uid+" to the story (pkey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            return ResourceUtil.FAILURE_JSON_MSG;
        }        
    }

    @RequestMapping(value = "/{id}/remove/{uid}", method = RequestMethod.POST)
    public @ResponseBody
    String removeUserFromStory(@RequestParam("id") String id, @RequestParam("uid") String uid) {
        int story_id = ResourceUtil.stringToIntegerConversion("story_id", id);
        try{
            User user = userServiceManager.readUser(uid);
            Story story = storyManager.readStory(story_id);
            story.removeAssignees(user);
            storyManager.updateStory(story);
            return ResourceUtil.SUCCESS_JSON_MSG;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while removing the user (Pkey) "+uid+" from the story (pkey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            return ResourceUtil.FAILURE_JSON_MSG;
        }        
    }

    @RequestMapping(value = "removeuser", method = RequestMethod.POST)
    public @ResponseBody
    String removeUserFromStage(@RequestParam("userid") String uid,@RequestParam("storyId") String stid,
                                    @RequestParam("stageId") String stage) {

        StoryHistory storyHistory = storyHistoryManager.fetchUserStoryHistory(Integer.parseInt(stid), stage, uid);
        storyHistoryManager.deleteStoryHistory(storyHistory);
        return ResourceUtil.SUCCESS_JSON_MSG;
    }

    @RequestMapping(value = "/{id}/addusers/{uids}", method = RequestMethod.GET)
    public @ResponseBody
    String addUsersToStory(@PathVariable("id") String id, @PathVariable("uids") String uids) {

        Story story = storyManager.readStory(Integer.parseInt(id));
        String[] users = uids.split(",");
        for (String s : users) {
            User user = userServiceManager.readUser(s);
            story.addAssignees(user);
        }
        storyManager.updateStory(story);
        return ResourceUtil.SUCCESS_JSON_MSG;
    }

    @RequestMapping(value = "/{id}/removeusers/{uidlist}", method = RequestMethod.GET)
    public @ResponseBody
    String removeUsersFromStory(@PathVariable("id") String id, @PathVariable("uids") String uids) {

        Story story = storyManager.readStory(Integer.parseInt(id));
        String[] users = uids.split(",");
        for (String s : users) {
            User user = userServiceManager.readUser(s);
            story.removeAssignees(user);
        }
        storyManager.updateStory(story);
        return ResourceUtil.SUCCESS_JSON_MSG;
    }

    @RequestMapping(value = "/adduserwithstage", method = RequestMethod.POST)
    public @ResponseBody
    String addUserWithStage(@RequestParam String userid, @RequestParam String storyId,
                                    @RequestParam String stageid) throws Exception {
        try {
            StoryHistory storyHistory = new StoryHistory();
            storyHistory.setUser(userServiceManager.readUser(userid));
            storyHistory.setStory(storyManager.readStory(Integer.parseInt(storyId)));
            storyHistory.setStage(projectStageManager.readProjectStage(Integer.parseInt(stageid)));
            logger.debug("Stage :" + stageid + ", User :" + userid + ", story" + storyId);
            storyHistoryManager.createStoryHistory(storyHistory);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception(e.toString());
        }
        return ResourceUtil.SUCCESS_JSON_MSG;
    }

    @RequestMapping(value = "/getusers", method = RequestMethod.POST)
    public @ResponseBody
    List<StoryHistory> getUsersFromStory(@RequestParam String storyId, @RequestParam String stage) {
        ProjectStage projectStage;
        int stageNo = 0;
        try{
            stageNo = Integer.parseInt(stage); 
        }catch(NumberFormatException e){
            stageNo = 0;
        }
        projectStage = projectStageManager.readProjectStage(stageNo);
        return storyHistoryManager.fetchStoryHistory(Integer.parseInt(storyId), projectStage);
    }

    @RequestMapping(value = "/adduserswithstage", method = RequestMethod.POST)
    public @ResponseBody
    String addUsersWithStage(@RequestParam String userids, @RequestParam String storyId,
                                    @RequestParam String stage) {
        boolean isFailed = false;
        String exceptionString = "";
        String[] users = userids.split(",");
        for (String s : users) {
            try {
                addUserWithStage(s, storyId, stage);
            } catch (Exception e) {
                isFailed = true;
                exceptionString += s + ",";
            }
        }
        if (isFailed) {
            // System.out.println("string :"+exceptionString.substring(0,exceptionString.length()-1));
            exceptionString = exceptionString.substring(0, exceptionString.length() - 1);
            return "{\"result\":\"failure: Exception occured while adding some users. Users :"
                                            + exceptionString + "\"}";
        }
        return ResourceUtil.SUCCESS_JSON_MSG;
        // return "{\"result\":\"failure\"}";
    }
    
    //TODO Remove unused methods from this class
    @RequestMapping(value = "/clearstoryassignees", method = RequestMethod.POST)
    public @ResponseBody
    String removeUsersWithStage(@RequestParam String storyId, @RequestParam String stage) {
        logger.debug("In Story Resource clear:story id =" + storyId + ", stage =" + stage);
        storyHistoryManager.clearUsersByStage(Integer.parseInt(storyId), stage);
        return ResourceUtil.SUCCESS_JSON_MSG;
    }
}