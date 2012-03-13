package com.imaginea.scrumr.jobs;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.ProjectStageManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.StoryManager;

class SprintThread extends TimerTask {

	ProjectManager projectManager;

	StoryManager storyManager;

	SprintManager sprintManager;
	
	ProjectStageManager projectStageManager;
	
	private static Logger logger = LoggerFactory.getLogger(SprintThread.class);

	public void run() {
	    logger.debug("Triggered Job");
		List<Project> projects = projectManager.fetchAllProjects();
		if(projects != null){
			for(Project p: projects){
				Date projectEndDate = p.getEnd_date();
				if (projectEndDate !=null){
					if(projectEndDate.before(new Date())){
						p.setStatus("Finished");
					}else if(projectEndDate.after(new Date())){
						Sprint old_sprint = null;
						Sprint current = null;
						List<Story> stories = null;
						if(p.getCurrent_sprint() != 1){
							old_sprint = sprintManager.selectSprintByProject(p, p.getCurrent_sprint());
							Date spend = old_sprint.getEnddate();
							if(spend.before(new Date())){
								current = sprintManager.selectSprintByProject(p, p.getCurrent_sprint()+1);
								if(current !=null){
									p.setCurrent_sprint(current.getId());
								}
								stories = storyManager.fetchStoriesBySprint(old_sprint.getPkey());
							}
						}

						// MOVE ALL UNFINISHED STORIES TO NEXT SPRINT
						/*if(stories != null && stories.size() > 0){
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
						}*/
						p.setStatus("In Progress");
						projectManager.updateProject(p);
					}
				}else{
					Sprint old_sprint = null;
					Sprint current = null;
					List<Story> storiesLastSprint = null;
					List<Story> storiesInBackLog = null;
					if(p.getCurrent_sprint() != 0){
						old_sprint = sprintManager.selectSprintByProject(p, p.getCurrent_sprint());
						Date spend = old_sprint.getEnddate();
						if(spend.before(new Date())){
							String status = projectStageManager.fetchMaxRankByProjectId(p.getPkey());
						    storiesLastSprint = storyManager.fetchUnfinishedStories(old_sprint.getPkey(), status);
							storiesInBackLog = storyManager.fetchUnAssignedStories(p.getPkey());
							old_sprint.setStatus("Finished");
							sprintManager.updateSprint(old_sprint);
						}
					}
					if((storiesLastSprint != null && storiesLastSprint.size() > 0) || (storiesInBackLog != null && storiesInBackLog.size() > 0)){
						current = new Sprint();
						current.setId(p.getCurrent_sprint()+1);
						current.setStartdate(new Date(old_sprint.getEnddate().getTime()+3600000));
						current.setEnddate(new Date(old_sprint.getEnddate().getTime()+ 3600000 + ((7*p.getSprint_duration())*86400000L) - 3600000L));
						current.setStatus(statusSetter(current.getStartdate(), current.getEnddate()));
						p.setCurrent_sprint(current.getId());
						p.setStatus("In Progress");
						projectManager.updateProject(p);
						current.setProject(p);
						sprintManager.createSprint(current);

						// MOVE ALL UNFINISHED STORIES TO NEWLY CREATED SPRINT
						/*if(storiesLastSprint != null && storiesLastSprint.size() > 0){
							for(Story us: storiesLastSprint){
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
						}*/
						
					}else{
						p.setStatus("Finished");
						projectManager.updateProject(p);
					}
				}
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
	
	public ProjectStageManager getProjectStageManager() {
        return projectStageManager;
    }
    public void setProjectStageManager(ProjectStageManager projectStageManager) {
        this.projectStageManager = projectStageManager;
    }
	
	public String statusSetter(Date start, Date end){
		Date today = new Date();
		if(start.after(today)){
			return "Not Started";
		}else{
			if(end.before(today)){
				return "Finished";
			}else{
				return "In Progress";
			}
		}
	}
}
