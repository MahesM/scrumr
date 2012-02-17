package com.imaginea.scrumr.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.EnumType;
import javax.xml.bind.annotation.XmlRootElement;

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
        @NamedQuery(name = "tasks.fetchTeamStatusSummaryBySprint", query = "SELECT tsk.user.displayname as displayname, count(tsk) as total_tasks, sum(tsk.timeInDays) as total_tasks, tsk.status as status from Task tsk where tsk.story.project.id=:projectId and tsk.story.id in (select story.id from Story as story where story.sprint_id.id=:sprintId) group by tsk.user.id"),
        @NamedQuery(name = "tasks.fetchTeamStatusSummaryByProject", query = "SELECT tsk.user.displayname as displayname, count(tsk) as total_tasks, sum(tsk.timeInDays) as total_tasks, tsk.status as status from Task tsk where tsk.story.project.id=:projectId group by tsk.user.id"),
        @NamedQuery(name = "tasks.fetchTeamStatusDetailsBySprint", query = "SELECT tsk from Task tsk where tsk.story.project.id=:projectId and tsk.story.id in (select story.id from Story as story where story.sprint_id.id=:sprintId) order by tsk.user.id"),
        @NamedQuery(name = "tasks.fetchTeamStatusDetailsByUser", query = "SELECT tsk from Task tsk where tsk.story.project.id=:projectId and tsk.story.id in (select story.id from Story as story where story.sprint_id.id=:sprintId) and tsk.user.id=:userId") })
@XmlRootElement
public class Task extends AbstractEntity implements IEntity, Serializable {

    private String content;

    // TODO - get rid of this
    private String milestonePeriod;

    private int timeInDays;

    private User createdByUser;

    private User user;

    private Story story;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    public enum TaskStatus {
        CREATED, IN_PROGRESS, COMPLETED;
    }

    public Task() {
        this.status = TaskStatus.CREATED;
    }

    @ManyToOne
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
    @Column(name = "tmilestonemeriod")
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
    @ManyToOne
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
