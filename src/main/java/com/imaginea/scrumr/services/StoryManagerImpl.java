package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.StoryManager;

public class StoryManagerImpl implements StoryManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(StoryManagerImpl.class);

    private IDao<IEntity, Integer> genericDao;

    @Transactional
    public void createStory(Story story) {
        if (story != null) {

            genericDao.save(story);

        }
    }

    public Story readStory(Integer pkey) {

        return genericDao.find(Story.class, pkey);

    }

    @Transactional
    public void updateStory(Story story) {
        if (story != null) {

            genericDao.update(story);

        }
    }

    @Transactional
    public void deleteStory(Story story) {
        if (story != null) {

            LOGGER.info("Deleting story");
            genericDao.delete(story);

        }
    }

    public List<Story> fetchStoriesByProject(Integer pkey) {

        Project project = genericDao.find(Project.class, pkey);
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("project", project);

        return genericDao.getEntities(Story.class, "stories.fetchStoriesByProject", ht);
    }

    public List<Story> fetchStoriesByStatus(Integer pkey, String status) {

        Sprint sprint = genericDao.find(Sprint.class, pkey);
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("sprint", sprint);
        ht.put("status", status);

        return genericDao.getEntities(Story.class, "stories.fetchStoriesByStatus", ht);
    }

    public List<Story> fetchStoriesBySprint(Integer pkey) {

        Sprint sprint = genericDao.find(Sprint.class, pkey);
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("sprint", sprint);

        return genericDao.getEntities(Story.class, "stories.fetchStoriesBySprint", ht);
    }

    public List<Story> fetchUnAssignedStories(Integer pkey) {

        Project project = genericDao.find(Project.class, pkey);
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("project", project);
        return genericDao.getEntities(Story.class, "stories.fetchUnAssignedStories", ht);
    }

    public List<Story> fetchUnfinishedStories(Integer pkey, String status) {

        Sprint sprint = genericDao.find(Sprint.class, pkey);
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("sprint", sprint);
        ht.put("status", status);
        return genericDao.getEntities(Story.class, "stories.fetchUnfinishedStories", ht);
    }

    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

    public List<Object> fetchAllStoryPointsByProject(Integer pkey) {
        String queryName = "stories.fetchDistinctStoryPointsByProject";
        Hashtable<String, Object> criteria = new Hashtable<String, Object>();
        criteria.put("projectid", pkey);
        return genericDao.getResults(queryName, criteria);
    }
    
    public List<Object> searchAllStoryPointsByProject(Integer pkey, String searchString) {
        String queryName = "stories.searchStoryPointsByProject";
        Hashtable<String, Object> criteria = new Hashtable<String, Object>();
        criteria.put("projectid", pkey);
        int searchPoint = 0;
        try{
            searchPoint = Integer.parseInt(searchString);
        }catch(NumberFormatException e){
            searchPoint = 0;
        }
        criteria.put("searchstring", searchPoint);
        return genericDao.getResults(queryName, criteria);
    }
    
    public List<Object> searchAllStoryTagsByProject(Integer pkey, String searchString) {
        String queryName = "stories.searchStoryTagsByProject";
        Hashtable<String, Object> criteria = new Hashtable<String, Object>();
        criteria.put("projectid", pkey);
        criteria.put("searchstring", "%"+searchString+"%");
        return genericDao.getResults(queryName, criteria);
    }

}
