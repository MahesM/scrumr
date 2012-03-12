package com.imaginea.scrumr.entities;

/**
 *  ProjectSummaryReport - represent a task summary report for the project
 *
 */
public class UserTaskReport {

    private String userid;
    
    private String userName;

    private long assignedTask;

    private long inProgressTask;

    private long completedTask;

    public String getUserid() {
        return userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public long getAssignedTask() {
        return assignedTask;
    }
    
    public void setAssignedTask(long assignedTask) {
        this.assignedTask = assignedTask;
    }
    
    public long getInProgressTask() {
        return inProgressTask;
    }
    
    public void setInProgressTask(long inProgressTask) {
        this.inProgressTask = inProgressTask;
    }
    
    public long getCompletedTask() {
        return completedTask;
    }
    
    public void setCompletedTask(long completedTask) {
        this.completedTask = completedTask;
    }   
    
}
