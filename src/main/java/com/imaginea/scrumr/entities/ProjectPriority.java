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
@Table(name = "project_priority")
@NamedQueries({
    @NamedQuery(name = "projectpriorities.fetchAllPrioritiesByProjectId", query = "SELECT instance from ProjectPriority instance where instance.project.id=:projectid")
  //  ,@NamedQuery(name = "projectpriorities.searchAllPrioritiesByProjectId", query = "SELECT instance from ProjectPriority instance where instance.project.id=:projectid and instance.description like :searchstring")
    
    //@NamedQuery(name = "projectpriorities.fetchMaxPriorityIdByProjectId", query = "SELECT max(instance.priorityid) from ProjectPriority instance where instance.project.id=:projectid")
    })
@XmlRootElement
public class ProjectPriority extends AbstractEntity implements IEntity, Serializable {
    private String color; 
    private String description;
    private int rank;
    private Project project;

    public static enum DefaultPriority {
        PRIORITY_1 ("Priority 1", "#FC5F5F",1,0),
        PRIORITY_2 ("Priority 2", "#7395DC",2,1),
        PRIORITY_3 ("Priority 3", "#EBA143",3,2);

        private final String description;
        private final String color;
        private final int PKey;
        private final int rank;
        DefaultPriority(String description, String color, int pKey, int rank) {
            this.description = description;
            this.color = color;
            this.rank = rank;
            this.PKey = pKey;
        }
        public String getDescription() { return description; }
        public String getColor() { return color; }
        public int getPKey() { return PKey; }
        public int getRank() { return rank; }
    }
    
    @Column(name = "rank", nullable = false)
    public int getRank(){
        return rank;
    }
    
    public void setRank(int rank){
        this.rank = rank;
    }
    
    @Column(name = "color", nullable = false)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name = "description", length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


}
