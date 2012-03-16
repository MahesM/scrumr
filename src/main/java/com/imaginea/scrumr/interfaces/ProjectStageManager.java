package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.ProjectStage;

public interface ProjectStageManager {
	
	/* Project CRUD */
	
	void createProjectStage(ProjectStage projectStage);
	
	ProjectStage readProjectStage(Integer pkey);
	
	void updateProjectStage(ProjectStage projectStage);
	
	void deleteProjectStage(ProjectStage projectStage);	
	
	List<ProjectStage> fetchAllProjectStageByProject(Integer pkey);

    String fetchMaxRankByProjectId(Integer pkey);
	
}
