package com.imaginea.scrumr.entities;


/**
 *  ProjectSummaryReport - represent a task summary report for the project
 *
 */
public class SearchStoryParameters {

    private String type;
    private String value;

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
