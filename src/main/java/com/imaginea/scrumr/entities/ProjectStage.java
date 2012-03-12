package com.imaginea.scrumr.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
public class ProjectStage extends AbstractEntity implements IEntity, Serializable {
    private String description;
    private Project project;
    private int rank;
    private String url;
    

    public static enum DefaultProjectStages {
        STAGE_1 ("BackLog", 0,1,"url"),
        STAGE_2 ("Development", 1,2,"url"),
        STAGE_3 ("QA", 2, 3,"url"),
        STAGE_4 ("Completed", 3, 4,"url");

        private final String description;
        private final int rank;
        private final int pKey;
        private String url;
        
        DefaultProjectStages(String description,int rank, int pKey, String url) {
            this.description = description;
            this.rank = rank;
            this.pKey = pKey;
            this.url = url;
        }
        public String getDescription() { return description; }
        public int getRank() { return rank; }
        public int getPKey() { return pKey; }
        public String getUrl() { return url; }
    }

    @Column(name = "description", length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "rank")
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
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

}
