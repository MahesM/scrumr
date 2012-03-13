package com.imaginea.scrumr.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SprintUpdateJob extends QuartzJobBean {

	private SprintThread sprintThread;
	
	public SprintThread getSprintThread() {
        return sprintThread;
    }
	
	public void setSprintThread(SprintThread sprintThred) {
		this.sprintThread = sprintThred;
	}
 
	protected void executeInternal(JobExecutionContext context)
	throws JobExecutionException {
	    Thread thread = new Thread(sprintThread);
		thread.start(); 
	}	
}
