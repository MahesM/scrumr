package com.imaginea.scrumr.entities;

public class StorySizeInfo {

    private int totalStories;
    private int completedStories;
    private String value;
    
    public int getTotalStories() {
        return totalStories;
    }
    
    public void setTotalStories(int totalStories) {
        this.totalStories = totalStories;
    }
    
    public int getCompletedStories() {
        return completedStories;
    }
    
    public void setCompletedStories(int completedStories) {
        this.completedStories = completedStories;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
