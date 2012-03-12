package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.StoryHistory;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.StoryHistoryManager;

public class StoryHistoryManagerImpl implements StoryHistoryManager {

    private IDao<IEntity, Integer> genericDao;
    private static final Logger logger = LoggerFactory.getLogger(StoryHistoryManagerImpl.class);

    @Transactional
    public void createStoryHistory(StoryHistory StoryHistory) {
        if (StoryHistory != null) {

            genericDao.save(StoryHistory);

        }
    }

    public StoryHistory readStoryHistory(Integer pkey) {

        return genericDao.find(StoryHistory.class, pkey);

    }

    @Transactional
    public void updateStoryHistory(StoryHistory StoryHistory) {
        if (StoryHistory != null) {

            genericDao.update(StoryHistory);

        }
    }

    @Transactional
    public void deleteStoryHistory(StoryHistory StoryHistory) {
        if (StoryHistory != null) {

            genericDao.delete(StoryHistory);

        }
    }

    public List<StoryHistory> fetchStoryHistory(Integer storyid, String stage) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("storyid", storyid);
        ht.put("stage", stage);

        return genericDao.getEntities(StoryHistory.class, "storyhistory.fetchStoryStoryHistory", ht);
    }

    public StoryHistory fetchUserStoryHistory(Integer storyid, String stage, String userid) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("storyid", storyid);
        ht.put("stage", stage);
        ht.put("userid", userid);

        return genericDao.getEntity(StoryHistory.class, "storyhistory.fetchUserStoryStoryHistory", ht);
    }

    @Transactional
    public String clearUsersByStage(Integer storyid, String stage) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("storyid", storyid);
        ht.put("stage", stage);
        // System.out.println("Deleting status");
        // return (String) genericDao.getResult("status.clearUsersByStage",ht);
        List<StoryHistory> statusObj = genericDao.getEntities(StoryHistory.class, "storyhistory.fetchStoryStoryHistory", ht);
        // System.out.println("Size of Array :"+statusObj.size());
        try {

            Iterator<StoryHistory> iterator = statusObj.iterator();
            while (iterator.hasNext()) {
                StoryHistory status = iterator.next();
                logger.info("Deleting StoryHistory Object :" + status.getUser().getFullname());
                genericDao.delete(status);

            }
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return "success";
    }

    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

}
