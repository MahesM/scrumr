package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.ProjectPreferences;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.ProjectPreferencesManager;

public class ProjectPreferencesManagerImpl implements ProjectPreferencesManager {

    private IDao<IEntity, Integer> genericDao;

    @Transactional
    public void createProjectPreferences(ProjectPreferences projectPreferences) {
        if (projectPreferences != null) {

            genericDao.save(projectPreferences);

        }
    }

    public ProjectPreferences readProjectPreferences(Integer pkey) {

        return genericDao.find(ProjectPreferences.class, pkey);

    }

    @Transactional
    public void updateProjectPreferences(ProjectPreferences projectPreferences) {
        if (projectPreferences != null) {

            genericDao.update(projectPreferences);

        }
    }

    @Transactional
    public void deleteProjectPreferences(ProjectPreferences projectPreferences) {
        if (projectPreferences != null) {

            genericDao.delete(projectPreferences);

        }
    }
    
    public ProjectPreferences getPreferencesByProject(int projectId) {
        Hashtable<String, Object> criteria = new Hashtable<String, Object>();
        criteria.put("projectid", projectId);
        return genericDao.getEntity(ProjectPreferences.class, "ProjectPreferences.fetchPreferencesByProject", criteria);
    }


    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

}
