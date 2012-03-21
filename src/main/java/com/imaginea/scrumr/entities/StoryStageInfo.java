package com.imaginea.scrumr.entities;

public class StoryStageInfo {

    private int id;
    private int imageUrlIndex;
    private int storyCount;
    private int sprintId;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getImageUrlIndex() {
        return imageUrlIndex;
    }
    
    public void setImageUrlIndex(int imageUrlIndex) {
        this.imageUrlIndex = imageUrlIndex;
    }
    
    public int getStoryCount() {
        return storyCount;
    }
    
    public void setStoryCount(int storyCount) {
        this.storyCount = storyCount;
    }
    
    public int getSprintId() {
        return sprintId;
    }
    
    public void setSprintId(int sprintId) {
        this.sprintId = sprintId;
    }
}
