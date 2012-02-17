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

import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Status;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.StatusManager;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

@Controller
@RequestMapping("/stories")
public class StoryResource {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SprintManager sprintManager;

    @Autowired
    UserServiceManager userServiceManager;

    @Autowired
    StoryManager storyManager;

    @Autowired
    StatusManager statusManager;

    private static final Logger logger = LoggerFactory.getLogger(StoryResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchStory(@PathVariable("id") String id) {

        Story story = storyManager.readStory(Integer.parseInt(id));
        List<Story> stories = new ArrayList<Story>();
        stories.add(story);
        return stories;
    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchStoriesByProject(@PathVariable("id") String id) {

        return storyManager.fetchStoriesByProject(Integer.parseInt(id));

    }

    @RequestMapping(value = "{sprintid}/project/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchStoriesByProjectSprint(@PathVariable("sprintid") String sid,
                                    @PathVariable("id") String pid) {
        Sprint sprint = sprintManager.selectSprintByProject(projectManager.readProject(Integer.parseInt(pid)), Integer.parseInt(sid));
        return storyManager.fetchStoriesBySprint(sprint.getPkey());

    }

    @RequestMapping(value = "/sprint/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchStoriesBySprint(@PathVariable("id") String id) {

        return storyManager.fetchStoriesBySprint(Integer.parseInt(id));

    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public @ResponseBody
    List<Story> fetchStoriesByStatus(@RequestParam String sprintid, @RequestParam String status) {

        return storyManager.fetchStoriesByStatus(Integer.parseInt(sprintid), status);

    }

    @RequestMapping(value = "/backlog/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Story> fetchUnAssignedStories(@PathVariable("id") String id) {
        logger.debug("UnAssigned Stories");
        return storyManager.fetchUnAssignedStories(Integer.parseInt(id));

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    String createStory(@RequestParam String stTitle, @RequestParam String stDescription,
                                    @RequestParam String stPriority, @RequestParam String user,
                                    @RequestParam String projectId, @RequestParam String stSprint) {

        Story story = new Story();
        logger.debug("User :" + user);
        try {
            story.setTitle(stTitle);
            story.setDescription(stDescription);
            Sprint sprint = sprintManager.selectSprintByProject(projectManager.readProject(Integer.parseInt(projectId)), Integer.parseInt(stSprint));
            story.setSprint_id(sprint);
            story.setPriority(Integer.parseInt(stPriority));
            story.setCreator(user);
            story.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
            story.setLastUpdated(new java.sql.Date(System.currentTimeMillis()));
            story.setLastUpdatedby(user);
            story.setStatus("notstarted");
            story.setViewCount(0);
            story.setProject(projectManager.readProject(Integer.parseInt(projectId)));
            storyManager.createStory(story);
        } catch (Exception e) {
            return "{\"result\":\"failure\"}";
        }
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    String updateStory(@RequestParam String storyId, @RequestParam String stTitle,
                                    @RequestParam String stDescription,
                                    @RequestParam String stPriority, @RequestParam String user,
                                    @RequestParam String projectId, @RequestParam String stSprint) {

        try {
            Story story = storyManager.readStory(Integer.parseInt(storyId));
            if (stTitle != null)
                story.setTitle(stTitle);
            if (stDescription != null)
                story.setDescription(stDescription);

            if (stPriority != null)
                story.setPriority(Integer.parseInt(stPriority));

            // story.setCreator(user);
            // story.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
            story.setLastUpdated(new java.sql.Date(System.currentTimeMillis()));
            story.setLastUpdatedby(user);
            Sprint toSprint = sprintManager.selectSprintByProject(projectManager.readProject(Integer.parseInt(projectId)), Integer.parseInt(stSprint));
            story.setSprint_id(toSprint);
            // story.setStatus("notstarted");
            // story.setViewCount(0);
            // story.setProject(projectManager.readProject(Integer.parseInt(projectId)));
            storyManager.updateStory(story);
        } catch (Exception e) {
            return "{\"result\":\"failure\"}";
        }
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/addtosprint", method = RequestMethod.POST)
    public @ResponseBody
    String addStoryToSprint(@RequestParam String sprint, @RequestParam String stories,
                                    @RequestParam String status, @RequestParam String projectId) {

        try {
            Story story = storyManager.readStory(Integer.parseInt(stories));
            Sprint toSprint = sprintManager.selectSprintByProject(projectManager.readProject(Integer.parseInt(projectId)), Integer.parseInt(sprint));
            story.setSprint_id(toSprint);
            // logger.debug(toSprint.toString());
            story.setStatus(status);
            storyManager.updateStory(story);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"result\":\"failure\"}";
        }
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteStory(@PathVariable("id") String id) {

        Story story = storyManager.readStory(Integer.parseInt(id));
        logger.debug("Title: " + story.getTitle());
        storyManager.deleteStory(story);
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/{id}/adduser/{uid}", method = RequestMethod.GET)
    public @ResponseBody
    String addUserToStory(@PathVariable("id") String id, @PathVariable("uid") String uid) {

        User user = userServiceManager.readUser(uid);
        Story story = storyManager.readStory(Integer.parseInt(id));
        story.addAssignees(user);
        storyManager.updateStory(story);
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/{id}/remove/{uid}", method = RequestMethod.POST)
    public @ResponseBody
    String removeUserFromStory(@RequestParam("id") String id, @RequestParam("uid") String uid) {

        User user = userServiceManager.readUser(uid);
        Story story = storyManager.readStory(Integer.parseInt(id));
        story.removeAssignees(user);
        storyManager.updateStory(story);
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "removeuser", method = RequestMethod.POST)
    public @ResponseBody
    String removeUserFromStage(@RequestParam("userid") String uid,
                                    @RequestParam("storyId") String stid,
                                    @RequestParam("stageId") String stage) {

        Status status = statusManager.fetchUserStoryStatus(Integer.parseInt(stid), stage, uid);
        statusManager.deleteStatus(status);
        return "{\"result\":\"success\"}";
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
        return "{\"result\":\"success\"}";
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
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/adduserwithstage", method = RequestMethod.POST)
    public @ResponseBody
    String addUserWithStage(@RequestParam String userid, @RequestParam String storyId,
                                    @RequestParam String stage) throws Exception {
        try {
            Status story_status = new Status();
            story_status.setUser(userServiceManager.readUser(userid));
            story_status.setStory(storyManager.readStory(Integer.parseInt(storyId)));
            story_status.setStage(stage);
            logger.debug("Stage :" + stage + ", User :" + userid + ", story" + storyId);
            statusManager.createStatus(story_status);
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
        return "{\"result\":\"success\"}";
    }

    @RequestMapping(value = "/getusers", method = RequestMethod.POST)
    public @ResponseBody
    List<Status> getUsersFromStory(@RequestParam String storyId, @RequestParam String stage) {

        return statusManager.fetchStoryStatus(Integer.parseInt(storyId), stage);
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
        return "{\"result\":\"success\"}";
        // return "{\"result\":\"failure\"}";
    }

    @RequestMapping(value = "/clearstoryassignees", method = RequestMethod.POST)
    public @ResponseBody
    String removeUsersWithStage(@RequestParam String storyId, @RequestParam String stage) {
        logger.debug("In Story Resource clear:story id =" + storyId + ", stage =" + stage);
        statusManager.clearUsersByStage(Integer.parseInt(storyId), stage);
        return "{\"result\":\"success\"}";
    }
}