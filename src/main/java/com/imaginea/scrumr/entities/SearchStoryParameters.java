package com.imaginea.scrumr.entities;

import java.util.List;

/**
 *  ProjectSummaryReport - represent a task summary report for the project
 *
 */
public class SearchStoryParameters {

    private List<ProjectPriority> projectPrioritiesList;
    private List<Object> storyPoints;
    
    public List<ProjectPriority> getProjectPrioritiesList() {
        return projectPrioritiesList;
    }
    
    public void setProjectPrioritiesList(List<ProjectPriority> projectPrioritiesList) {
        this.projectPrioritiesList = projectPrioritiesList;
    }
    
    public List<Object> getStoryPoints() {
        return storyPoints;
    }
    
    public void setStoryPoints(List<Object> storyPoints) {
        this.storyPoints = storyPoints;
    }    
}
