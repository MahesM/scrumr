package com.imaginea.scrumr.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "project_preferences")
@NamedQueries({
    // @NamedQuery(name = "projectpriorities.fetchAllPrioritiesByProjectId", query = "SELECT instance from ProjectPriority instance where instance.project.id=:projectid"),
    //@NamedQuery(name = "projectpriorities.fetchMaxPriorityIdByProjectId", query = "SELECT max(instance.priorityid) from ProjectPriority instance where instance.project.id=:projectid")
})
@XmlRootElement
public class ProjectPreferences extends AbstractEntity implements IEntity, Serializable {

    private Project project;
    private int storyPointType;
    private int storyPointLimit;
    private int mileStoneType;
    private int mileStoneRange;
    private boolean storypriorityEnabled;
    private boolean storyPointEnabled;
    private boolean taskMileStoneEnabled;
    private boolean storyPointMandatory;
    private boolean taskMileStoneMandatory;
    
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "projectid", nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(name = "storypointtype")
    public int getStoryPointType() {
        return storyPointType;
    }

    public void setStoryPointType(int storyPointType) {
        this.storyPointType = storyPointType;
    }

    @Column(name = "storypointlimit")
    public int getStoryPointLimit() {
        return storyPointLimit;
    }
    
    public void setStoryPointLimit(int storyPointLimit) {
        this.storyPointLimit = storyPointLimit;
    }
    
    @Column(name = "milestonerange")
    public int getMileStoneRange() {
        return mileStoneRange;
    }
    
    public void setMileStoneRange(int mileStoneRange) {
        this.mileStoneRange = mileStoneRange;
    }
    
    @Column(name = "milestonetype")
    public int getMileStoneType() {
        return mileStoneType;
    }
    
    public void setMileStoneType(int mileStoneType) {
        this.mileStoneType = mileStoneType;
    }
    
    @Column(name = "storypriorityenabled")
    public boolean isStorypriorityEnabled() {
        return storypriorityEnabled;
    }
    
    public void setStorypriorityEnabled(boolean storypriorityEnabled) {
        this.storypriorityEnabled = storypriorityEnabled;
    }
    
    @Column(name = "storysizeenabled")
    public boolean isStoryPointEnabled() {
        return storyPointEnabled;
    }
    
    public void setStoryPointEnabled(boolean setStoryPointEnabled) {
        this.storyPointEnabled = setStoryPointEnabled;
    }

    @Column(name = "taskmilestoneenabled")
    public boolean isTaskMileStoneEnabled() {
        return taskMileStoneEnabled;
    }

    public void setTaskMileStoneEnabled(boolean taskMileStoneEnabled) {
        this.taskMileStoneEnabled = taskMileStoneEnabled;
    }

    @Column(name = "storypointmandatory")
    public boolean isStoryPointMandatory() {
        return storyPointMandatory;
    }

    public void setStoryPointMandatory(boolean storyPointMandatory) {
        this.storyPointMandatory = storyPointMandatory;
    }

    @Column(name = "taskmilestonemandatory")
    public boolean isTaskMileStoneMandatory() {
        return taskMileStoneMandatory;
    }

    public void setTaskMileStoneMandatory(boolean taskMileStoneMandatory) {
        this.taskMileStoneMandatory = taskMileStoneMandatory;
    }

}
