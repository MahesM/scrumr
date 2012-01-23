package com.imaginea.scrumr.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Comment;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.interfaces.CommentManager;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;

public class CommentManagerImpl implements CommentManager {
	
	public static final Logger LOGGER = Logger.getLogger(CommentManagerImpl.class);
	
	private IDao<IEntity, Integer> genericDao;
	
	@Transactional
	public void createComment(Comment comment) {
		if(comment != null) {
			
			genericDao.save(comment);
			
		}
	}

	public Comment readComment(Integer pkey) {
		
		return genericDao.find(Comment.class, pkey);
		
	}

	@Transactional
	public void updateComment(Comment comment) {
		if(comment != null) {

			genericDao.update(comment);
			
		}
	}

	@Transactional
	public void deleteComment(Comment comment) {
		if(comment != null) {
			
			genericDao.delete(comment);
			
		}
	}
	
	public List<Comment> fetchCommentsByStory(Integer pkey){
		
		Story story = genericDao.find(Story.class, pkey);
		return story.getComments();
	}
	
	
	/* Getters and Setters */
	
	public IDao<IEntity, Integer> getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(IDao<IEntity, Integer> genericDao) {
		this.genericDao = genericDao;
	}

}
