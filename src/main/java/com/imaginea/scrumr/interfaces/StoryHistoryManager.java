package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.StoryHistory;

public interface StoryHistoryManager {
	
	void createStoryHistory(StoryHistory StoryHistory);
	
	StoryHistory readStoryHistory(Integer pkey);
	
	void updateStoryHistory(StoryHistory StoryHistory);
	
	void deleteStoryHistory(StoryHistory StoryHistory);
	
	List<StoryHistory> fetchStoryHistory(Integer storyid, String stage);
	
	String clearUsersByStage(Integer storyid, String stage);
	
	StoryHistory fetchUserStoryHistory(Integer storyid, String stage, String userid);
	
	
}
