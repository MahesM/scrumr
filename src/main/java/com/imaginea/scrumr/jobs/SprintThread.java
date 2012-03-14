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
			for(Project project: projects){
			    logger.debug("Updating project:" + project.getPkey());
			    updateProject(project);
			}
		}
	}

	private void updateProject(Project project) {
	    Date projectEndDate = project.getEnd_date();
        if (projectEndDate !=null){
            if(projectEndDate.before(new Date())){
                logger.debug("Project end date is before the current date ");
                project.setStatus("Finished");
            }else if(projectEndDate.after(new Date())){
                logger.debug("Project end date is after the current date ");
                updateForInProgressProject(project);
            }
        }else{
            logger.debug("Project didn't have end date ");
            updateProjectWithoutEndDate(project);
        }
        projectManager.updateProject(project);
    }

    private void updateProjectWithoutEndDate(Project project) {
        Sprint currentSprint = null;        
        List<Story> unfinishedStoriesLastSprint = null;
        List<Story> storiesInBackLog = null;
        
        currentSprint = sprintManager.selectSprintByProject(project, project.getCurrent_sprint());
        logger.debug("Project's current sprint before updation is "+project.getCurrent_sprint());
        
        Date sprintEndDate = currentSprint.getEnddate();
        if(sprintEndDate.before(new Date())){
            logger.debug("Current sprint's end date is before the current date");
           // unfinishedStoriesLastSprint = getUnfinishedStoriesAndUpdateSprint(project, currentSprint);
            storiesInBackLog = storyManager.fetchUnAssignedStories(project.getPkey());
        }

        if((unfinishedStoriesLastSprint != null && unfinishedStoriesLastSprint.size() > 0) || (storiesInBackLog != null && storiesInBackLog.size() > 0)){
            Sprint newSprint = createNewSprint(project,currentSprint);            
            
            project.setCurrent_sprint(newSprint.getId());
            project.setStatus("In Progress");          

            // MOVE ALL UNFINISHED STORIES TO NEWLY CREATED SPRINT
            //updateStories(unfinishedStoriesLastSprint,newSprint); 
            
        }else{
            logger.debug("All the stories in the current sprint are completed and there are no stories in the project backlog ");
            project.setStatus("Finished");
        }
        
    }

    private void updateStories(List<Story> unfinishedStoriesLastSprint, Sprint newSprint) {
        if(unfinishedStoriesLastSprint != null && unfinishedStoriesLastSprint.size() > 0){
            for(Story story: unfinishedStoriesLastSprint){
                story.setSprint_id(newSprint);
                logger.debug("The sprint id of the story with id "+ story.getPkey() +" is updated to "+newSprint.getPkey());
                
                story.setStstage(projectStageManager.readProjectStage(story.getProject().getMinRankStageId()));
                storyManager.updateStory(story);
                logger.debug("The stage id of the story with id "+ story.getPkey() +" is updated to "+story.getProject().getMinRankStageId());
                
            }
        }
        
    }

    private Sprint createNewSprint(Project project, Sprint currentSprint) {
        Sprint newSprint = null;
        newSprint = new Sprint();
        newSprint.setId(project.getCurrent_sprint()+1);
        newSprint.setStartdate(new Date(currentSprint.getEnddate().getTime()+3600000));
        newSprint.setEnddate(new Date(currentSprint.getEnddate().getTime()+ 3600000 + ((7*project.getSprint_duration())*86400000L) - 3600000L));
        newSprint.setStatus(statusSetter(newSprint.getStartdate(), newSprint.getEnddate()));
        newSprint.setProject(project);
        sprintManager.createSprint(newSprint);
        logger.debug("New sprint is created with id: "+newSprint.getId());
        
        return newSprint;
    }

    private void updateForInProgressProject(Project project) {
        Sprint currentSprint = null;
        Sprint nextSprint = null;
        List<Story> unfinishedStoriesLastSprint = null;

        currentSprint = sprintManager.selectSprintByProject(project, project.getCurrent_sprint());
        Date sprintEndDate = currentSprint.getEnddate();
        if(sprintEndDate.before(new Date())){
            logger.debug("Current sprint's end date is before the current date");
            
            nextSprint = sprintManager.selectSprintByProject(project, project.getCurrent_sprint()+1);
            if(nextSprint !=null){
                project.setCurrent_sprint(nextSprint.getId());
            }else{
                project.setCurrent_sprint(createNewSprint(project, currentSprint).getId());
            }
          //  unfinishedStoriesLastSprint = getUnfinishedStoriesAndUpdateSprint(project, currentSprint);
        }


        // MOVE ALL UNFINISHED STORIES TO NEXT SPRINT
        //updateStories(unfinishedStoriesLastSprint,nextSprint);
        
        project.setStatus("In Progress");        
    }

    private List<Story> getUnfinishedStoriesAndUpdateSprint(Project project, Sprint currentSprint) {
        String stageMaxRank = projectStageManager.fetchMaxRankByProjectId(project.getPkey());
        List<Story> unfinishedStories = storyManager.fetchUnfinishedStories(currentSprint.getPkey(), stageMaxRank);
        
        currentSprint.setStatus("Finished");
        sprintManager.updateSprint(currentSprint);
        logger.debug("The status of the sprint with pKey "+currentSprint.getPkey()+" is updated to finished");
        
        return unfinishedStories;
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
		    logger.debug("Start date is after the current date");
		    return "Not Started";
		}else{
			if(end.before(today)){
			    logger.debug("End date is before the current date");
			    return "Finished";
			}else{
			    logger.debug("Start date is before the current date and end date is after the current date");
				return "In Progress";
			}
		}
	}
}
