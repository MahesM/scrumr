package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.Status;

public interface StatusManager {
	
	void createStatus(Status Status);
	
	Status readStatus(Integer pkey);
	
	void updateStatus(Status Status);
	
	void deleteStatus(Status Status);
	
	List<Status> fetchStoryStatus(Integer storyid, String stage);
	
	String clearUsersByStage(Integer storyid, String stage);
	
	Status fetchUserStoryStatus(Integer storyid, String stage, String userid);
	
	
}
