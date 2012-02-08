package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.Project;

public interface ProjectManager {
	
	/* Project CRUD */
	
	void createProject(Project project);
	
	Project readProject(Integer pkey);
	
	void updateProject(Project project);
	
	void deleteProject(Project project);
	
	
	List<Project> fetchProjectsByUser(Integer pkey);
	List<Project> fetchAllProjects();
	
}
