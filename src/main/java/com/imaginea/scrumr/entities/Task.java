package com.imaginea.scrumr.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.EnumType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "tasks")
// TODO: for dynamic queries- either use criteria api, or just get the raw query from NamedQuery and
// add the appropriate orderby clause
// this is a quick way for reports
@NamedQueries({
        @NamedQuery(name = "tasks.fetchTasksByStory", query = "SELECT instance from Task instance where instance.story.id=:storyid"),
        @NamedQuery(name = "tasks.fetchTasksByAssignee", query = "SELECT instance from Task instance where instance.user.id=:userid"),
        @NamedQuery(name = "tasks.fetchUserTaskBySprint", query="SELECT instance from Task instance where instance.project=:project and instance.sprint = :sprint" ),
        @NamedQuery(name = "tasks.fetchTasksByStatus", query = "SELECT instance from Task instance where instance.story.pkey=:storyid and instance.status =:status"),
        @NamedQuery(name = "tasks.fetchAllUserTaskStory", query = "SELECT instance from Task instance where instance.story.pkey=:storyid and instance.user.pkey =:pkey"),
        @NamedQuery(name = "tasks.fetchTeamStatusSummaryBySprint", query = "SELECT instance,  count(instance) as total_tasks, sum(instance.timeInDays) as time_in_days from Task instance where instance.story.project.id=:projectId and instance.story.id in (select story.id from Story as story where story.sprint_id.id=:sprintId) group by instance.user"),
        @NamedQuery(name = "tasks.fetchTeamStatusSummaryByProject", query = "SELECT instance, count(instance) as total_tasks, sum(instance.timeInDays) as time_in_days from Task instance where instance.story.project.id=:projectId group by instance.user.id"),
        @NamedQuery(name = "tasks.fetchAssignedUserTaskBySprint", query = "SELECT instance.user.username, instance.user.displayname, count(instance) from Task instance where instance.project=:project and instance.sprint = :sprint and status = 0 group by user"),
        @NamedQuery(name = "tasks.fetchInProgressUserTaskBySprint", query = "SELECT instance.user.username, instance.user.displayname, count(instance) from Task instance where instance.project=:project and instance.sprint = :sprint and status = 1 group by user"),
        @NamedQuery(name = "tasks.fetchCompletedUserTaskBySprint", query = "SELECT instance.user.username, instance.user.displayname, count(instance) from Task instance where instance.project=:project and instance.sprint = :sprint and status = 2 group by user"),
        @NamedQuery(name = "tasks.fetchUnAssignedTaskBySprint", query = "SELECT instance from Task instance where instance.sprint.id=:sprintid and instance.status=0"),
        @NamedQuery(name = "tasks.fetchAssignedTaskByCurrentUser", query = "SELECT instance from Task instance where instance.sprint.id=:sprintid and user.username=:userid") })
@XmlRootElement
public class Task extends AbstractEntity implements IEntity, Serializable {

    private String content;

    // TODO - get rid of this
    private String milestonePeriod;

    private int timeInDays;

    private User createdByUser;

    private User user;

    private Story story;
    
    private Sprint sprint;
    
    private Project project;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    public static final String FETCH_TEAM_STATUS_DETAILS_BY_SPRINT = "SELECT instance from Task instance where instance.story.project.id=:projectId and instance.story.id in (select story.id from Story as story where story.sprint_id.id=:sprintId) ";

    public static final String FETCH_TEAM_STATUS_DETAILS_BY_USER = "SELECT instance from Task instance where instance.story.project.id=:projectId and instance.story.id in (select story.id from Story as story where story.sprint_id.id=:sprintId) and instance.user.id=:userId";

    public static final String FETCH_TEAM_STATUS_DETAILS_BY_PROJECT = "SELECT instance from Task instance where instance.story.project.id=:projectId ";

    public enum TaskStatus {
        ASSIGNED, NOT_YET_ASSIGNED, IN_PROGRESS, COMPLETED;
    }

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "projectid", nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
    @ManyToOne
    @JoinColumn(name = "userid", nullable = true)
    public User getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    @ManyToOne
    @JoinColumn(name = "sprintid", nullable = false)
    public Sprint getSprint() {
        return sprint;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    /**
     * @return the commentContent
     */
    @Column(name = "tcontent")
    public String getContent() {
        return content;
    }

    /**
     * @param commentContent
     *            the commentContent to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the commentLogDate
     */
    @Column(name = "tmilestoneperiod")
    public String getMilestonePeriod() {
        return milestonePeriod;
    }

    /**
     * @param commentLogDate
     *            the commentLogDate to set
     */
    public void setMilestonePeriod(String milestonePeriod) {
        this.milestonePeriod = milestonePeriod;
    }

    /**
     * @return the story
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "storyid", nullable = false)
    public Story getStory() {
        return story;
    }

    /**
     * @param story
     *            the story to set
     */
    public void setStory(Story story) {
        this.story = story;
    }

    @Column(name = "tdays")
    public int getTimeInDays() {
        return timeInDays;
    }

    public void setTimeInDays(int timeInDays) {
        this.timeInDays = timeInDays;
    }
    
    @Column(name = "status")
    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @OneToOne
    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }
}
