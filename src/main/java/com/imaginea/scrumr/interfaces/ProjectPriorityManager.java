package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.ProjectPriority;

public interface ProjectPriorityManager {
	
	/* Project CRUD */
	
	void createProjectPriority(ProjectPriority projectPriority);
	
	ProjectPriority readProjectPriority(Integer pkey);
	
	void updateProjectPriority(ProjectPriority projectPriority);
	
	void deleteProjectPriority(ProjectPriority projectPriority);	
	
	List<ProjectPriority> fetchAllProjectPrioritiesByProject(Integer pkey);

   // List<ProjectPriority> searchAllProjectPrioritiesByProject(Integer pkey, String searchString);

	
}
