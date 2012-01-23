package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.SprintManager;

public class SprintManagerImpl implements SprintManager {
	
	public static final Logger LOGGER = Logger.getLogger(SprintManagerImpl.class);
	
	private IDao<IEntity, Integer> genericDao;
	
	@Transactional
	public void createSprint(Sprint sprint) {
		if(sprint != null) {
			
			genericDao.save(sprint);
			
		}
	}

	public Sprint readSprint(Integer pkey) {
		
		return genericDao.find(Sprint.class, pkey);
		
	}

	@Transactional
	public void updateSprint(Sprint sprint) {
		if(sprint != null) {

			genericDao.update(sprint);
			
		}
	}

	@Transactional
	public void deleteSprint(Sprint sprint) {
		if(sprint != null) {
			
			genericDao.delete(sprint);
			
		}
	}
	
	public Sprint selectSprintByProject(Project project,Integer sprint_number){
		
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put("project", project);
		ht.put("sprint_num", sprint_number);
		
		return genericDao.getEntity(Sprint.class, "sprints.selectSprintByProject",ht);
	}

	public List<Sprint> selectSprintsByProject(Project project){
		
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put("project", project);
		
		return genericDao.getEntities(Sprint.class, "sprints.selectSprintsByProject",ht);
	}
	
	public List<Sprint> selectFinishedSprints(Project project){
		
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put("enddate", project.getEnd_date());
		
		return genericDao.getEntities(Sprint.class, "sprints.selectFinishedSprints",ht);
	}

	/* Getters and Setters */
	
	public IDao<IEntity, Integer> getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(IDao<IEntity, Integer> genericDao) {
		this.genericDao = genericDao;
	}

}
