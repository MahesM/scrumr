package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "stories")
@NamedQueries({
    @NamedQuery(name = "stories.fetchStoriesByProject", query = "SELECT instance from Story instance where instance.project=:project"),
    @NamedQuery(name = "stories.fetchStoriesByStatus", query = "SELECT instance from Story instance where instance.sprint_id=:sprint and instance.ststage=:stage"),
    @NamedQuery(name = "stories.fetchUnfinishedStories", query = "SELECT instance from Story instance where instance.sprint_id=:sprint and instance.ststage !=:projectstage"),
    @NamedQuery(name = "stories.fetchStoriesBySprint", query = "SELECT instance from Story instance where instance.sprint_id=:sprint"),
    @NamedQuery(name = "stories.fetchStoriesBySprintProject", query = "SELECT instance from Story instance where instance.project=:project and instance.sprint_id=:sprint"),
    @NamedQuery(name = "stories.fetchDistinctStoryPointsByProject", query = "SELECT distinct(instance.storyPoint) from Story instance where instance.project.id=:projectid"),
    @NamedQuery(name = "stories.fetchUnAssignedStories", query = "SELECT instance from Story instance where instance.project=:project and instance.sprint_id is null") })
@XmlRootElement
public class Story extends AbstractEntity implements IEntity, Serializable {

    private Project project;

    private String title;

    private String description;

    private ProjectPriority priority;

    private Sprint sprint_id;

    private Date creationDate;

    private String creator;

    private Date lastUpdated;

    private String lastUpdatedby;

    private ProjectStage ststage;

    private Set<User> assignees;

    private List<Comment> comments;

    private List<Task> todos;

    private List<StoryHistory> statusList;

    private int storyPoint;

    private int mileStone;

    private Set<Task> taskList;

    private int unCompletedTask;

    private int totalTask;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "stpid", nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(name = "sttitle", length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "stdescription", length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "stpriorityid", nullable = true)
    public ProjectPriority getPriority() {
        return priority;
    }

    public void setPriority(ProjectPriority priority) {
        this.priority = priority;
    }
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "stsprint", nullable = true)
    public Sprint getSprint_id() {
        return sprint_id;
    }

    public void setSprint_id(Sprint sprint_id) {
        this.sprint_id = sprint_id;
    }

    @Column(name = "stcreation", nullable = false)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creation_date) {
        this.creationDate = creation_date;
    }

    @Column(name = "stcreator", nullable = false, length = 100)
    public String getCreator() {
        return creator;
    }


    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name = "storypoint", nullable = true)
    public int getStoryPoint() {
        return storyPoint;
    }

    public void setStoryPoint(int storyPoint) {
        this.storyPoint = storyPoint;
    }

    @Column(name = "stlastupdated", nullable = false)
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date last_updated) {
        this.lastUpdated = last_updated;
    }

    @Column(name = "stupdatedby", nullable = false, length = 100)
    public String getLastUpdatedby() {
        return lastUpdatedby;
    }

    public void setLastUpdatedby(String last_updatedby) {
        this.lastUpdatedby = last_updatedby;
    }

    @ManyToOne
    @JoinColumn(name = "ststageid", nullable = true)
    public ProjectStage getStstage() {
        return ststage;
    }

    public void setStstage(ProjectStage ststage) {
        this.ststage = ststage;
    }

    @ManyToMany
    @JoinTable(name = "story_users", joinColumns = { @JoinColumn(name = "stpid") }, inverseJoinColumns = { @JoinColumn(name = "stuserid") })
    public Set<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<User> assignees) {
        this.assignees = assignees;
    }

    public void addAssignees(User assignee) {
        this.assignees.add(assignee);
    }

    public void removeAssignees(User assignee) {
        this.assignees.remove(assignee);
    }

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "story")
    @JsonIgnore
    // @JoinColumn (name="stid", nullable = false)
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * @param story
     *            the story to set
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "story")
    @JsonIgnore
    public List<StoryHistory> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<StoryHistory> statusList) {
        this.statusList = statusList;
    }

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "story")
    @JsonIgnore
    public List<Task> getTodos() {
        return todos;
    }

    public void setTodos(List<Task> todos) {
        this.todos = todos;
    }

    @Column(name = "milestone")
    public int getMileStone() {
        return mileStone;
    }

    public void setMileStone(int mileStone) {
        this.mileStone = mileStone;
    }
    @JsonIgnore
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy="story")
    public Set<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(Set<Task> taskList) {
        this.taskList = taskList;
        this.totalTask = 0;
        this.unCompletedTask = 0;
        
        if(this.taskList != null && !(this.taskList.isEmpty())){
            this.totalTask = this.taskList.size();
            this.unCompletedTask = getUncompletedTask(taskList); 
        }

    }
    private int getUncompletedTask(Set<Task> taskList) {
        int uncompletedTask = 0;
        for(Task task:taskList){
            if(!("COMPLETED".equals(task.getStatus()))){
                uncompletedTask++;
            }
        }
        return uncompletedTask;
    }

    @Transient
    public int getUnCompletedTask() {
        return unCompletedTask;
    }

    public void setUnCompletedTask(int unCompletedTask) {
        this.unCompletedTask = unCompletedTask;
    }
    @Transient
    public int getTotalTask() {
        return totalTask;
    }

    public void setTotalTask(int totalTask) {
        this.totalTask = totalTask;
    }

}
