package com.imaginea.scrumr.jobs;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.interfaces.CommentManager;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

class SprintThread extends TimerTask {

	@Autowired
	ProjectManager projectManager;

	@Autowired
	CommentManager commentManager;

	@Autowired
	StoryManager storyManager;

	@Autowired
	SprintManager sprintManager;

	@Autowired
	UserServiceManager userServiceManager;

	int count = 1;
	public void run() {
		count++;
		System.out.println("Running: "+count);
		List<Project> projects = projectManager.fetchAllProjects();
		if(projects != null){
			for(Project p: projects){
				Date start = p.getStart_date();
				Date end = p.getEnd_date();
				int duration = p.getSprint_duration();
				if(end.before(new Date())){
					p.setStatus("Finished");
				}else if(end.after(new Date())){
					Sprint old_sprint = sprintManager.readSprint(p.getCurrent_sprint());
					int current = getCurrentSprint(start, duration);
					System.out.println("Current: "+current);
					System.out.println("OLD: "+old_sprint);
					List<Story> stories = storyManager.fetchStoriesBySprint(old_sprint.getPkey());
					Sprint sprint = sprintManager.readSprint(current);
					System.out.println("Stoties: "+stories);
					if(stories != null && stories.size() > 0){
						System.out.println("Stoties Count: "+stories.size());
						for(Story us: stories){
							if(!us.getStatus().equalsIgnoreCase("Finished")){
								System.out.println("ST: "+us.getStatus());
								us.setSprint_id(sprint);
								us.setStatus("Not Started");
								storyManager.updateStory(us);
							}
						}
					}
					p.setCurrent_sprint(sprint.getId());
					p.setStatus("In Progress");
				}
				projectManager.updateProject(p);
			}
		}
	}

	private int getCurrentSprint(Date start, int duration){
		return (int) Math.ceil((((new Date().getTime() - start.getTime()) / (1000 * 60 * 60 * 24))/(7*duration))) + 1;
	}
}
