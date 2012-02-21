package com.imaginea.scrumr.entities;

/**
 *  ProjectSummaryReport - represent a task summary report for the project
 *
 */
public class ProjectSummaryReport {

    private Task task;

    private int totalTasks;

    private int totalTime;

    private int timeWorked;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTimeWorked() {
        return timeWorked;
    }

    public void setTimeWorked(int timeWorked) {
        this.timeWorked = timeWorked;
    }
}
