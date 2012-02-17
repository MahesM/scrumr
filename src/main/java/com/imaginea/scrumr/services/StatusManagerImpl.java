package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Status;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.StatusManager;

public class StatusManagerImpl implements StatusManager {

    private IDao<IEntity, Integer> genericDao;

    @Transactional
    public void createStatus(Status Status) {
        if (Status != null) {

            genericDao.save(Status);

        }
    }

    public Status readStatus(Integer pkey) {

        return genericDao.find(Status.class, pkey);

    }

    @Transactional
    public void updateStatus(Status Status) {
        if (Status != null) {

            genericDao.update(Status);

        }
    }

    @Transactional
    public void deleteStatus(Status Status) {
        if (Status != null) {

            genericDao.delete(Status);

        }
    }

    public List<Status> fetchStoryStatus(Integer storyid, String stage) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("storyid", storyid);
        ht.put("stage", stage);

        return genericDao.getEntities(Status.class, "status.fetchStoryStatus", ht);
    }

    public Status fetchUserStoryStatus(Integer storyid, String stage, String userid) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("storyid", storyid);
        ht.put("stage", stage);
        ht.put("userid", userid);

        return genericDao.getEntity(Status.class, "status.fetchUserStoryStatus", ht);
    }

    @Transactional
    public String clearUsersByStage(Integer storyid, String stage) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("storyid", storyid);
        ht.put("stage", stage);
        // System.out.println("Deleting status");
        // return (String) genericDao.getResult("status.clearUsersByStage",ht);
        List<Status> statusObj = genericDao.getEntities(Status.class, "status.fetchStoryStatus", ht);
        // System.out.println("Size of Array :"+statusObj.size());
        try {

            Iterator<Status> iterator = statusObj.iterator();
            while (iterator.hasNext()) {
                Status status = iterator.next();
                System.out.println("Deleting Status Object :" + status.getUser().getFullname());
                genericDao.delete(status);

            }
        } catch (Exception e) {
            System.out.println(e.toString());
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
