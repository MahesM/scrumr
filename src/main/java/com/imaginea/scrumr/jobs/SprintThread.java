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

	ProjectManager projectManager;

	StoryManager storyManager;

	SprintManager sprintManager;

	public void run() {
		System.out.println("Triggered Job");
		List<Project> projects = projectManager.fetchAllProjects();
		if(projects != null){
			for(Project p: projects){
				Date end = p.getEnd_date();
				if(end.before(new Date())){
					p.setStatus("Finished");
				}else if(end.after(new Date())){
					Sprint old_sprint = null;
					Sprint current = null;
					List<Story> stories = null;
					if(p.getCurrent_sprint() != 0){
						old_sprint = sprintManager.selectSprintByProject(p, p.getCurrent_sprint());
						Date spend = old_sprint.getEnddate();
						if(spend.before(new Date())){
							current = sprintManager.selectSprintByProject(p, p.getCurrent_sprint()+1);
							stories = storyManager.fetchStoriesBySprint(old_sprint.getPkey());
						}
					}
					
					if(stories != null && stories.size() > 0){
						for(Story us: stories){
							if(!us.getStatus().equalsIgnoreCase("finished")){
								if(current != null){
									us.setSprint_id(current);
								}else{
									us.setSprint_id(null);
								}
								us.setStatus("notstarted");
								storyManager.updateStory(us);
							}
						}
						p.setCurrent_sprint(current.getId());
					}
					p.setStatus("In Progress");
				}
				projectManager.updateProject(p);
			}
		}
	}

	public ProjectManager getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}
	public StoryManager getStoryManager() {
		return storyManager;
	}
	public void setStoryManager(StoryManager storyManager) {
		this.storyManager = storyManager;
	}
	public SprintManager getSprintManager() {
		return sprintManager;
	}
	public void setSprintManager(SprintManager sprintManager) {
		this.sprintManager = sprintManager;
	}
}
