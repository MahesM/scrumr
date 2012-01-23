package com.imaginea.scrumr.utilities;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.imaginea.scrumr.entities.ProjectEntity;
import com.imaginea.scrumr.entities.SprintEntity;
import com.imaginea.scrumr.entities.UserStoryEntity;

class SprintThread extends TimerTask {

	int count = 1;
	public void run() {
		count++;
		System.out.println("Running: "+count);
		DatabaseHandler data = new DatabaseHandler();
		List<ProjectEntity> projects = data.getAllProjects();
		if(projects != null){
			for(ProjectEntity p: projects){
				Date start = p.getStart_date();
				Date end = p.getEnd_date();
				int duration = p.getSprint_duration();
				if(end.before(new Date())){
					p.setStatus("Finished");
				}else if(end.after(new Date())){
					SprintEntity old_sprint = data.getSprint(p.getCurrent_sprint());
					int current = getCurrentSprint(start, duration);
					System.out.println("Current: "+current);
					System.out.println("OLD: "+old_sprint);
					List<UserStoryEntity> stories = data.getAllUserStoryEntities(p.getId(), old_sprint);
					SprintEntity sprint = data.getSprint(current);
					System.out.println("Stoties: "+stories);
					if(stories != null && stories.size() > 0){
						System.out.println("Stoties Count: "+stories.size());
						for(UserStoryEntity us: stories){
							if(!us.getStatus().equalsIgnoreCase("Finished")){
								System.out.println("ST: "+us.getStatus());
								us.setSprint_id(sprint);
								us.setStatus("Not Started");
								data.updateUserStory(us);
							}
						}
					}
					p.setCurrent_sprint(sprint.getId());
					p.setStatus("In Progress");
				}
				data.updateProject(p);
			}
		}
	}
	
	private int getCurrentSprint(Date start, int duration){
		return (int) Math.ceil((((new Date().getTime() - start.getTime()) / (1000 * 60 * 60 * 24))/(7*duration))) + 1;
	}
}
