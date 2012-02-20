package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.Task;

public interface TaskManager {

    void createTask(Task task);

    Task readTask(Integer pkey);

    void updateTask(Task task);

    void deleteTask(Task task);

    List<Task> fetchTasksByStory(Integer pkey);

    List<Task> fetchTasksByStatus(String status);

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
     * @param pageNumber
     * @param maxCount
     * @return
     */
    List<Task> fetchTaskStatusDetails(Integer projectId, Integer sprintId, Integer userId, Integer pageNumber, Integer maxCount);
}
