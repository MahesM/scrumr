package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.Story;

public interface StoryManager {
	
	void createStory(Story story);
	
	Story readStory(Integer pkey);
	
	void updateStory(Story story);
	
	void deleteStory(Story story);
	
	List<Story> fetchStoriesByProject(Integer pkey);
	
	List<Story> fetchStoriesBySprint(Integer pkey);
	
	List<Story> fetchStoriesByStatus(Integer pkey, String status);
	
	List<Story> fetchUnAssignedStories(Integer pkey);
	
	List<Story> fetchUnfinishedStories(Integer pkey, String status);

    List<Object> fetchAllStoryPointsByProject(Integer pkey);
    
    List<Object> searchAllStoryTagsByProject(Integer pkey, String searchString);

    List<Object> searchAllStoryPointsByProject(Integer pkey, String searchString);
	
}
