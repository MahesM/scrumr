package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.TaskManager;

public class TaskManagerImpl implements TaskManager {
	
	public static final Logger LOGGER = Logger.getLogger(TaskManagerImpl.class);
	
	private IDao<IEntity, Integer> genericDao;
	
	@Transactional
	public void createTask(Task task) {
		if(task != null) {
			
			genericDao.save(task);
			
		}
	}

	public Task readTask(Integer pkey) {
		
		return genericDao.find(Task.class, pkey);
		
	}

	@Transactional
	public void updateTask(Task task) {
		if(task != null) {

			genericDao.update(task);
			
		}
	}

	@Transactional
	public void deleteTask(Task task) {
		if(task != null) {
			
			genericDao.delete(task);
			
		}
	}
	
	public List<Task> fetchTasksByStory(Integer pkey){
		
		//Story story = genericDao.find(Story.class, pkey);
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put("storyid", pkey);
		System.out.println("--------------->>> Pkey of story:"+pkey);
		List<Task>tasks =genericDao.getEntities(Task.class, "tasks.fetchTasksByStory",ht);
		System.out.println("-------------------->>>>>> "+tasks);
		return tasks;
	}
	
	public List<Task> fetchTasksByStatus(String status){
		
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put("status", status);
		
		return genericDao.getEntities(Task.class, "tasks.fetchTasksByStatus",ht);
	}
	
	
	/* Getters and Setters */
	
	public IDao<IEntity, Integer> getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(IDao<IEntity, Integer> genericDao) {
		this.genericDao = genericDao;
	}

}
