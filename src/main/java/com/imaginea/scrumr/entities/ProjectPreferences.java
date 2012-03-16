package com.imaginea.scrumr.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Transient;
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
    private int storySizeLowRangeIndex;
    private int storySizeHighRangeIndex;
    private int mileStoneType;
    private int mileStoneRange;
    private boolean storypriorityEnabled;
    
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
  
        this.storySizeLowRangeIndex = calculateRangeIndex(storyPointLimit, false);
        this.storySizeHighRangeIndex = calculateRangeIndex(storyPointLimit, true);
     
    }
    
    private int calculateRangeIndex(int storyPointLimit, boolean isUpperLimit) {
        int index = 0;
        boolean rangeBitNotSet = (storyPointLimit & 1)!= 1;
        while(rangeBitNotSet || isUpperLimit){
            if(!rangeBitNotSet){
                isUpperLimit = false;
            }
            index ++;
            storyPointLimit = storyPointLimit >> 1;
            rangeBitNotSet = (storyPointLimit & 1)!= 1;
        }
        return index;
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
 
    @Transient
    public int getStorySizeHighRangeIndex() {
        return storySizeHighRangeIndex;
    }
    
    public void setStorySizeHighRangeIndex(int storySizeHighRangeIndex) {
        this.storySizeHighRangeIndex = storySizeHighRangeIndex;
    }
    
    @Transient
    public int getStorySizeLowRangeIndex() {
        return storySizeLowRangeIndex;
    }
    
    public void setStorySizeLowRangeIndex(int storySizeLowRangeIndex) {
        this.storySizeLowRangeIndex = storySizeLowRangeIndex;
    }
    

}
