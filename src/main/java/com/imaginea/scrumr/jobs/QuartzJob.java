package com.imaginea.scrumr.jobs;

import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.springframework.scheduling.quartz.JobDetailBean;

/**
 * Created by IntelliJ IDEA.
 * User: pramati
 * Date: Sep 29, 2010
 * Time: 2:16:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuartzJob extends JobDetailBean{
    /**
     * Creates a Quartz Job assigned to the 'Default' job group.
     *
     * @param inJobName  -- A unique name for this job.  Only one job with this name can execute at a time.
     * @param inJobClass -- The class that defines the job.  Must implement org.quartz.Job.
     */
    public QuartzJob(String inJobName, Class inJobClass) {
        this(inJobName, Scheduler.DEFAULT_GROUP, inJobClass);
    }

    /**
     * Creates a Quartz assigned user defined 'JobName, JobGroup, and JobClass'.
     *
     * @param inJobName  -- A unique name for this job.  Only one job with this name can execute at a time.
     * @param inJobGroup -- A group to be associated with this job.
     * @param inJobClass -- The class that defines the job.  Must implement org.quartz.Job.
     */
    public QuartzJob(String inJobName, String inJobGroup, Class inJobClass) {
        setName(inJobName);
        setGroup(inJobGroup);
        setJobClass(inJobClass);
    }

    /**
     * Convenience method to store 'state information' to the job instance. This information is add to the 'JobDataMap'
     * of the JobDetails.
     *
     * @param inParmId    -- key identifier.
     * @param inParmValue -- Object value.
     * @see org.quartz.JobDataMap
     * @see org.quartz.JobDetail
     */
    public void setJobParm(String inParmId, Object inParmValue) {
        if (getJobDataMap() == null) {
            setJobDataMap(new JobDataMap());
        }
        getJobDataMap().put(inParmId, inParmValue);
    }

    /**
     * Returns the 'JobDataMap' of the JobDetails.
     *
     * @return Map -- underlying JobDataMap.
     */
    public Map getJobParm() {
        return getJobDataMap();
    }

}
