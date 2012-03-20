package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.entities.UserTaskReport;

public interface TaskManager {

    void createTask(Task task);

    Task readTask(Integer pkey);

    void updateTask(Task task);

    void deleteTask(Task task);

    List<Task> fetchTasksByStory(Integer pkey);

    List<Task> fetchTasksByStatus(String status);
    
    List<UserTaskReport> fetchUserTaskBySprint(Project project, Sprint sprint);
    
    List<Task> fetchAssignedTaskByCurrentUser(int sprintId, String userId);
    
    List<Task> fetchUnAssignedTaskBySprint(int sprintId);
    
    /**
     * fetchTaskStatusSummary
     * 
     * @param projectId
     * @param sprintId
     * @return
     */
    List<Object> fetchTaskStatusSummary(Integer projectId, Integer sprintId);

    /**
     * fetchTaskStatusDetails, returns details of a task for a specific project if provided,
     * specific sprint if provided, and specific user if provided. If one or more are provided, the
     * appropriate tasks for those combinations are returned
     * 
     * @param projectId
     * @param sprintId
     * @param userId
     * @param orderBy
     * @param pageNumber
     * @param maxCount
     * @return
     */
    List<Task> fetchTaskStatusDetails(Integer projectId, Integer sprintId, Integer userId, String orderBy, Integer pageNumber, Integer maxCount);

    List<Task> getTasksByUser(int userPkey, int storyid);
}
