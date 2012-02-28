package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.ProjectLane;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.ProjectLaneManager;
import com.imaginea.scrumr.interfaces.ProjectManager;

public class ProjectLaneManagerImpl implements ProjectLaneManager {

    private IDao<IEntity, Integer> genericDao;

    @Transactional
    public void createProjectLane(ProjectLane projectLane) {
        if (projectLane != null) {

            genericDao.save(projectLane);

        }
    }

    public ProjectLane readProjectLane(Integer pkey) {

        return genericDao.find(ProjectLane.class, pkey);

    }

    @Transactional
    public void updateProjectLane(ProjectLane projectLane) {
        if (projectLane != null) {

            genericDao.update(projectLane);

        }
    }

    @Transactional
    public void deleteProjectLane(ProjectLane projectLane) {
        if (projectLane != null) {

            genericDao.delete(projectLane);

        }
    }


    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

	public List<ProjectLane> fetchAllProjectLaneByProject(Integer projectId) {
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put("projectid",projectId);
        return genericDao.getEntities(ProjectLane.class, "projectlanes.fetchAllProjectLanesByProjectId", ht);
	}

}
