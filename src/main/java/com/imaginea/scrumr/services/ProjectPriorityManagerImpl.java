package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.ProjectPriority;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.ProjectPriorityManager;

public class ProjectPriorityManagerImpl implements ProjectPriorityManager {

    private IDao<IEntity, Integer> genericDao;

    @Transactional
    public void createProjectPriority(ProjectPriority projectPriority) {
        if (projectPriority != null) {

            genericDao.save(projectPriority);

        }
    }

    public ProjectPriority readProjectPriority(Integer pkey) {

        return genericDao.find(ProjectPriority.class, pkey);

    }

    @Transactional
    public void updateProjectPriority(ProjectPriority projectPriority) {
        if (projectPriority != null) {

            genericDao.update(projectPriority);

        }
    }

    @Transactional
    public void deleteProjectPriority(ProjectPriority projectPriority) {
        if (projectPriority != null) {

            genericDao.delete(projectPriority);

        }
    }


    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

    public List<ProjectPriority> fetchAllProjectPrioritiesByProject(Integer projectId) {
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("projectid",projectId);
        return genericDao.getEntities(ProjectPriority.class, "projectpriorities.fetchAllPrioritiesByProjectId", ht);
    }

  /*  public List<ProjectPriority> searchAllProjectPrioritiesByProject(Integer pkey,
                                    String searchString) {
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("projectid",pkey);
        ht.put("searchstring", "%"+searchString+"%");
        return genericDao.getEntities(ProjectPriority.class, "projectpriorities.searchAllPrioritiesByProjectId", ht);
    }*/

}
