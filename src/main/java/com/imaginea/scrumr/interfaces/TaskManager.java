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

    List<Object> fetchTeamStatusBySprint(Integer projectId, Integer sprintId);
}
