package com.imaginea.scrumr.services;

import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.TaskManager;

public class TaskManagerImpl implements TaskManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(TaskManagerImpl.class);

    private IDao<IEntity, Integer> genericDao;

    @Transactional
    public void createTask(Task task) {
        if (task != null) {
            genericDao.save(task);
        }
    }

    public Task readTask(Integer pkey) {
        return genericDao.find(Task.class, pkey);
    }

    @Transactional
    public void updateTask(Task task) {
        if (task != null) {
            genericDao.update(task);
        }
    }

    @Transactional
    public void deleteTask(Task task) {
        if (task != null) {
            genericDao.delete(task);
        }
    }

    public List<Task> fetchTasksByStory(Integer pkey) {

        // Story story = genericDao.find(Story.class, pkey);
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("storyid", pkey);
        List<Task> tasks = genericDao.getEntities(Task.class, "tasks.fetchTasksByStory", ht);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Tasks :" + tasks);

        return tasks;
    }

    public List<Task> fetchTasksByStatus(String status) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("status", status);

        return genericDao.getEntities(Task.class, "tasks.fetchTasksByStatus", ht);
    }

    public List<Object> fetchTaskStatusSummary(Integer projectId, Integer sprintId) {
        String queryName = "tasks.fetchTeamStatusSummaryByProject";
        Hashtable<String, Object> criteria = new Hashtable<String, Object>();
        criteria.put("projectId", projectId);
        if (sprintId != null) {
            criteria.put("sprintId", sprintId);
            queryName = "tasks.fetchTeamStatusSummaryBySprint";
        }

        return genericDao.getResults(queryName, criteria);
    }

    public List<Task> fetchTaskStatusDetails(Integer projectId, Integer sprintId, Integer userId, Integer pageNumber, Integer maxCount) {
        String queryName = "tasks.fetchTeamStatusDetailsBySprint";
        Hashtable<String, Object> criteria = new Hashtable<String, Object>();
        // projectId is mandatory
        criteria.put("projectId", projectId);
        if (sprintId != null) {
            criteria.put("sprintId", sprintId);
            queryName = "tasks.fetchTeamStatusDetailsBySprint";
        }
        if (userId != null) {
            criteria.put("userId", userId);
            queryName = "tasks.fetchTeamStatusDetailsByUser";
        }
        return genericDao.getResults(queryName, criteria, pageNumber, maxCount);
    }

    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }
}
