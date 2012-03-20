package com.imaginea.scrumr.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.entities.UserTaskReport;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.TaskManager;

public class TaskManagerImpl implements TaskManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(TaskManagerImpl.class);

    private IDao<IEntity, Integer> genericDao;

    public enum TaskStatus {ASSIGNED, IN_PROGRESS, COMPLETED} ;
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

    
    public List<UserTaskReport> fetchUserTaskBySprint(Project project, Sprint sprint) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("project", project);
        ht.put("sprint", sprint);

         List userAssignedTaskCount = genericDao.getResults("tasks.fetchAssignedUserTaskBySprint", ht);
         List userInProgressTaskCount = genericDao.getResults("tasks.fetchInProgressUserTaskBySprint", ht);
         List userCompletedTaskCount = genericDao.getResults("tasks.fetchCompletedUserTaskBySprint", ht);
         
         HashMap<String, UserTaskReport> users = new HashMap<String, UserTaskReport>();
         prepareTaskReport(userAssignedTaskCount, users, TaskStatus.ASSIGNED );
         prepareTaskReport(userInProgressTaskCount, users, TaskStatus.IN_PROGRESS );
         prepareTaskReport(userCompletedTaskCount, users, TaskStatus.COMPLETED );
         
         List<UserTaskReport> userTaskReport = new ArrayList<UserTaskReport>();
         Iterator<Map.Entry<String,UserTaskReport>> usersIterator = users.entrySet().iterator();
         while(usersIterator.hasNext()){
             Entry<String, UserTaskReport> entry = usersIterator.next();
             userTaskReport.add(entry.getValue());
         }
         return userTaskReport;
    }
    
    public void prepareTaskReport(List userTaskCount,HashMap<String, UserTaskReport> users, TaskStatus status){
        if(!userTaskCount.isEmpty()){
            for(Object user:userTaskCount){
                Object[] task = (Object[])user;
                String userId = (String) task[0];
                UserTaskReport userTask = users.get(userId);                
                if( userTask == null){
                    userTask = new UserTaskReport();
                    userTask.setUserid(userId);
                    userTask.setUserName((String)task[1]);
                    users.put(userId, userTask);                 
                }
                
                if(status == TaskStatus.ASSIGNED)
                    userTask.setAssignedTask((Long)task[2]);
                else if(status == TaskStatus.IN_PROGRESS)
                    userTask.setInProgressTask((Long)task[2]);
                else if(status == TaskStatus.COMPLETED)
                    userTask.setCompletedTask((Long)task[2]); 
            }
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
    
    public List<Task> fetchAssignedTaskByCurrentUser(int sprintId, String userId) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("sprintid", sprintId);
        ht.put("userid", userId);
        return genericDao.getEntities(Task.class, "tasks.fetchAssignedTaskByCurrentUser", ht);
    }
    
    public List<Task> fetchUnAssignedTaskBySprint(int sprintId) {

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        ht.put("sprintid", sprintId);
        return genericDao.getEntities(Task.class, "tasks.fetchUnAssignedTaskBySprint", ht);
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

    public List<Task> fetchTaskStatusDetails(Integer projectId, Integer sprintId, Integer userId,
                                    String orderBy, Integer pageNumber, Integer maxCount) {
        String queryName = Task.FETCH_TEAM_STATUS_DETAILS_BY_PROJECT;
        Hashtable<String, Object> criteria = new Hashtable<String, Object>();
        // projectId is mandatory
        criteria.put("projectId", projectId);
        if (sprintId != null) {
            criteria.put("sprintId", sprintId);
            queryName = Task.FETCH_TEAM_STATUS_DETAILS_BY_SPRINT;
        }
        if (userId != null) {
            criteria.put("userId", userId);
            queryName = Task.FETCH_TEAM_STATUS_DETAILS_BY_USER;
        }
        return genericDao.getResults(queryName, criteria, orderBy, pageNumber, maxCount);
    }

    /* Getters and Setters */

    public IDao<IEntity, Integer> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(IDao<IEntity, Integer> genericDao) {
        this.genericDao = genericDao;
    }

    public List<Task> getTasksByUser(int pkey, int storyid) {
        String queryName = "tasks.fetchAllUserTaskStory";
        Hashtable<String, Object> criteria = new Hashtable<String, Object>();
        criteria.put("pkey", pkey);
        criteria.put("storyid", storyid);
        return genericDao.getResults(queryName, criteria);
    }
}
