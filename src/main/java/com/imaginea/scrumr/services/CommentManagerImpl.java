package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Comment;
import com.imaginea.scrumr.interfaces.CommentManager;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;

public class CommentManagerImpl implements CommentManager {

    private IDao<IEntity, Integer> genericDao;

    private static final Logger logger = LoggerFactory.getLogger(CommentManagerImpl.class);
    
    @Transactional
    public void createComment(Comment comment) {
        if (comment != null) {

            genericDao.save(comment);

        }
    }

    public Comment readComment(Integer pkey) {

        return genericDao.find(Comment.class, pkey);

    }

    @Transactional
    public void updateComment(Comment comment) {
        if (comment != null) {

            genericDao.update(comment);

        }
    }

    @Transactional
    public void deleteComment(Comment comment) {
        if (comment != null) {

            genericDao.delete(comment);

        }
    }

    public List<Comment> fetchCommentsByStory(Integer pkey) {

        // Story story = genericDao.find(Story.class, pkey);
        // return story.getComments();
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        logger.info("Story id :" + pkey);
        ht.put("story_id", pkey);
        return genericDao.getEntities(Comment.class, "comments.fetchCommentsByStory", ht);
    }

    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

}
