package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.ProjectManager;

public class ProjectManagerImpl implements ProjectManager {

    private IDao<IEntity, Integer> genericDao;

    @Transactional
    public void createProject(Project project) {
        if (project != null) {

            genericDao.save(project);

        }
    }

    public Project readProject(Integer pkey) {

        return genericDao.find(Project.class, pkey);

    }

    @Transactional
    public void updateProject(Project project) {
        if (project != null) {

            genericDao.update(project);

        }
    }

    @Transactional
    public void deleteProject(Project project) {
        if (project != null) {

            genericDao.delete(project);

        }
    }

    public List<Project> fetchProjectsByUser(Integer pkey) {

        User user = genericDao.find(User.class, pkey);
        return user.getProjects();
    }

    public List<Project> fetchAllProjects() {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        return genericDao.getEntities(Project.class, "projects.fetchAllProjects", ht);
    }

    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

}
