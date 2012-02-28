package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.ProjectLane;

public interface ProjectLaneManager {
	
	/* Project CRUD */
	
	void createProjectLane(ProjectLane projectLane);
	
	ProjectLane readProjectLane(Integer pkey);
	
	void updateProjectLane(ProjectLane projectLane);
	
	void deleteProjectLane(ProjectLane projectLane);	
	
	List<ProjectLane> fetchAllProjectLaneByProject(Integer pkey);

	
}
