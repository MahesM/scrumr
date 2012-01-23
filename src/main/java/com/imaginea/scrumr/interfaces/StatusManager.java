package com.imaginea.scrumr.interfaces;

import com.imaginea.scrumr.entities.Status;

public interface StatusManager {
	
	void createStatus(Status Status);
	
	Status readStatus(Integer pkey);
	
	void updateStatus(Status Status);
	
	void deleteStatus(Status Status);
	
}
