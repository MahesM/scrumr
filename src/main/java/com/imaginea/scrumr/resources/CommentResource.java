package com.imaginea.scrumr.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.imaginea.scrumr.entities.Comment;
import com.imaginea.scrumr.interfaces.CommentManager;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;
import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

@Controller
@RequestMapping("/comments")
public class CommentResource {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    CommentManager commentManager;

    @Autowired
    StoryManager storyManager;

    @Autowired
    UserServiceManager userServiceManager;
    
    private static final Logger logger = LoggerFactory.getLogger(CommentResource.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Comment> fetchComment(@PathVariable("id") String id) {
        
        int commentId = ResourceUtil.stringToIntegerConversion("comment_id", id);
        List<Comment> comments = new ArrayList<Comment>();
        try{
            Comment comment = commentManager.readComment(commentId);
            comments.add(comment);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured during reading the comment (Pkey) "+id ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return comments;
    }

    @RequestMapping(value = "/story/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Comment> fetchCommentsByStory(@PathVariable("id") String id) {
        int storyId = ResourceUtil.stringToIntegerConversion("story_id", id);
        List<Comment> comments = null;
        try{
            comments = commentManager.fetchCommentsByStory(storyId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured during reading the comments of the story (Pkey) "+id ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        
        return comments;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    List<Comment> createComment(@RequestParam String logdate, @RequestParam String user,
                                    @RequestParam String content, @RequestParam String storyid) {
        
        List<Comment> result = new ArrayList<Comment>();
        int storyId = ResourceUtil.stringToIntegerConversion("story_id", storyid);
        
        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date logDate = null;

        try {
            logDate = dateFormat.parse(logdate);
            
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setLogDate(logDate);
            comment.setUser(userServiceManager.readUser(user));
            comment.setStory(storyManager.readStory(storyId));
            commentManager.createComment(comment);
            result.add(comment);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured during creating the comment of the story (Pkey) "+storyid+" with content "+content ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return result;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String deleteComment(@PathVariable("id") String id) {
        int commentId = ResourceUtil.stringToIntegerConversion("comment_id", id);
        
        try{
            Comment comment = commentManager.readComment(commentId);
            commentManager.deleteComment(comment);
            return ResourceUtil.SUCCESS_JSON_MSG;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured during deleting the comment(Pkey) "+id;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
            return ResourceUtil.FAILURE_JSON_MSG;
        }
        
        
    }

}
