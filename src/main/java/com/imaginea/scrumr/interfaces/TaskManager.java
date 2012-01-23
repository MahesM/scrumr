package com.imaginea.scrumr.interfaces;

import com.imaginea.scrumr.entities.Task;

public interface TaskManager {
	
	void createTask(Task task);
	
	Task readTask(Integer pkey);
	
	void updateTask(Task task);
	
	void deleteTask(Task task);

}
