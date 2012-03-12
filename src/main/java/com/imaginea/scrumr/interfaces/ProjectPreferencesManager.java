package com.imaginea.scrumr.interfaces;

import com.imaginea.scrumr.entities.ProjectPreferences;

public interface ProjectPreferencesManager {
	
	/* Project CRUD */
	
	void createProjectPreferences(ProjectPreferences projectPreferences);
	
	ProjectPreferences readProjectPreferences(Integer pkey);
	
	void updateProjectPreferences(ProjectPreferences projectPreferences);
	
	void deleteProjectPreferences(ProjectPreferences projectPreferences);	

}
