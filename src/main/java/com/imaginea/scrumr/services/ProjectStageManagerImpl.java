package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.ProjectStage;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.ProjectStageManager;

public class ProjectStageManagerImpl implements ProjectStageManager {

    private IDao<IEntity, Integer> genericDao;

    @Transactional
    public void createProjectStage(ProjectStage projectStage) {
        if (projectStage != null) {

            genericDao.save(projectStage);

        }
    }

    public ProjectStage readProjectStage(Integer pkey) {

        return genericDao.find(ProjectStage.class, pkey);

    }

    @Transactional
    public void updateProjectStage(ProjectStage projectStage) {
        if (projectStage != null) {

            genericDao.update(projectStage);

        }
    }

    @Transactional
    public void deleteProjectStage(ProjectStage projectStage) {
        if (projectStage != null) {

            genericDao.delete(projectStage);

        }
    }


    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

	public List<ProjectStage> fetchAllProjectStageByProject(Integer projectId) {
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put("projectid",projectId);
        return genericDao.getEntities(ProjectStage.class, "projectstages.fetchAllProjectStagesByProjectId", ht);
	}

    public String fetchMaxRankByProjectId(Integer projectId) {
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("projectid",projectId);
        return genericDao.getResult("projectStages.fetchMaxRankByProjectId", ht).toString();
    }

}
