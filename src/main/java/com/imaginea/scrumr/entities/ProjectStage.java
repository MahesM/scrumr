package com.imaginea.scrumr.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "project_stage")
@NamedQueries({
    @NamedQuery(name = "projectstages.fetchAllProjectStagesByProjectId", query = "SELECT instance from ProjectStage instance where instance.project.id=:projectid"),
    @NamedQuery(name = "projectstages.fetchMaxRankByProjectId", query = "SELECT max(instance.rank) from ProjectStage instance where instance.project.id=:projectid")})
@XmlRootElement
public class ProjectStage extends AbstractEntity implements IEntity, Serializable, Comparable<ProjectStage> {
    private String description;
    private Project project;
    private int rank;
    private int imageUrlIndex;
    private String title;
    

    public static enum DefaultProjectStages {
        STAGE_1 ("BackLog","BackLog", 0,1,0),
        STAGE_2 ("Development","Development", 1,2,1),
        STAGE_3 ("QA","QA", 2, 3,2),
        STAGE_4 ("Completed","Completed", 3, 4,3);

        private final String description;
        private final int rank;
        private final int pKey;
        private final String title;
        private int imageUrlIndex;
        
        DefaultProjectStages(String title, String description,int rank, int pKey, int imageUrlIndex) {
            this.description = description;
            this.rank = rank;
            this.pKey = pKey;
            this.imageUrlIndex = imageUrlIndex;
            this.title = title;
        }
        public String getDescription() { return description; }
        public int getRank() { return rank; }
        public int getPKey() { return pKey; }
        public int getImageUrlIndex() { return imageUrlIndex; }
        public String getTitle() { return title; }
    }

    @Column(name = "description", length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "title", length = 100)
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    
    @Column(name = "rank")
    @OrderBy("rank ASC")
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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
    
    @Column(name = "imageurlindex")
    public int getImageUrlIndex() {
        return imageUrlIndex;
    }
    
    public void setImageUrlIndex(int imageUrlIndex) {
        this.imageUrlIndex = imageUrlIndex;
    }

    public int compareTo(ProjectStage stage) {
          return this.rank -  stage.rank; 
    }

}
