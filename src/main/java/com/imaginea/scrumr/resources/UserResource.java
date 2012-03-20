package com.imaginea.scrumr.resources;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.TaskManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;
import com.imaginea.scrumr.security.AbstractAuthenticationFactory;
import com.imaginea.scrumr.security.AuthenticationSource;
import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

@Controller
@RequestMapping("/users")
public class UserResource {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    StoryManager storyManager;

    @Autowired
    UserServiceManager userServiceManager;

    @Autowired
    TaskManager taskManager;

    @Autowired
    AbstractAuthenticationFactory abstractAuthenticationFactory;

    AuthenticationSource authenticationSource;

    @Autowired
    HttpServletRequest request;

    public static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User fetchUser(@PathVariable("id") String id) {

        int userId = ResourceUtil.stringToIntegerConversion("user_id", id);
        User user = null;
        try{
            user = userServiceManager.readUser(userId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the user with userId "+userId;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e); 
        }
        
        return user;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<User> fetchAllUsers() {

        List<User> users = userServiceManager.fetchAllUsers();
        return users;
    }

    @RequestMapping(value = "/fetchusers", method = RequestMethod.POST)
    public @ResponseBody
    List<User> fetchUsers(@RequestParam String index, @RequestParam String count) {

        List<User> users = userServiceManager.fetchAllUsers();
        return users;
    }

    @RequestMapping(value = "/fetchqontextusers", method = RequestMethod.POST)
    public @ResponseBody
    String fetchQontextUsers(@RequestParam String index, @RequestParam String count) {

        authenticationSource = abstractAuthenticationFactory.getInstance(request);
        String str = null;
        try {
            str = authenticationSource.getFriends(Integer.parseInt(index), Integer.parseInt(count));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while reading the qontext user ";
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e); 
            return ResourceUtil.FAILURE_JSON_MSG;
        }
        return str;
    }

    @RequestMapping(value = "/searchqontext", method = RequestMethod.POST)
    public @ResponseBody
    String searchUsers(@RequestParam String sortType, @RequestParam boolean showTotalCount,
                                    @RequestParam int startIndex, @RequestParam int count) {
        String userList = null;
        authenticationSource = abstractAuthenticationFactory.getInstance(request);
        try {
            userList = authenticationSource.searchFriends(sortType, showTotalCount, startIndex, count);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while searching the qontext user with sortType "+sortType;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e); 
            return ResourceUtil.FAILURE_JSON_MSG;
        }
        return userList;

    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Set<User> fetchUsersByProject(@PathVariable("id") String id) {
        int projectId = ResourceUtil.stringToIntegerConversion("project_id", id);
        Project project = projectManager.readProject(projectId);
        return project.getAssignees();
    }

    @RequestMapping(value = "/story/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Set<User> fetchUsersByStory(@PathVariable("id") String id) {
        int storyId = ResourceUtil.stringToIntegerConversion("story_id", id);
        Story story = storyManager.readStory(storyId);
        return story.getAssignees();
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User fetchUserByTask(@PathVariable("id") String id) {
        int taskId = ResourceUtil.stringToIntegerConversion("task_id", id);
        Task task = taskManager.readTask(taskId);
        return task.getUser();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    String cacheUser(@RequestParam String username, @RequestParam String displayname,
                                    @RequestParam String fullname, @RequestParam String emailid,
                                    @RequestParam String avatarurl) {
        
        User userExisting = userServiceManager.readUser(username);
        if(userExisting == null){
            try {                
                User user = new User();
                user.setUsername(username);
                user.setFullname(fullname);
                user.setAvatarurl(avatarurl);
                user.setDisplayname(displayname);
                user.setEmailid(emailid);
                userServiceManager.createUser(user);    
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                String exceptionMsg = "Error occured while creating the user with username "+username;
                ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e); 
                return ResourceUtil.FAILURE_JSON_MSG;
            }
        }
        return ResourceUtil.SUCCESS_JSON_MSG;
    }
}