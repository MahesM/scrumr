package com.imaginea.scrumr.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SprintUpdateJob extends QuartzJobBean {

	private SprintThread sprintThred;
	 
	public void setSprintThread(SprintThread sprintThred) {
		this.sprintThred = sprintThred;
	}
 
	protected void executeInternal(JobExecutionContext context)
	throws JobExecutionException {
 
		sprintThred.run();
 
	}
	
	
}
