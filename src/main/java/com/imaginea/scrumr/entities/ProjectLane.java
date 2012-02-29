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

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "projectlane")
@NamedQueries({
    @NamedQuery(name = "projectlanes.fetchAllProjectLanesByProjectId", query = "SELECT instance from ProjectLane instance where instance.project.id=:projectid") })
@XmlRootElement
public class ProjectLane extends AbstractEntity implements IEntity, Serializable {
    private int color; 
    private String description;
    private Project project;
    private int rank;
    private String type;

    public static enum DefaultProjectLanes {
        LANE_1 ("BackLog", 0, "BACKLOG"),
        LANE_2 ("Development", 1, null),
        LANE_3 ("QA", 2, null),
        LANE_4 ("Completed", 3, "FINISHED");

        private final String description;
        private final String type;
        private final int rank;
        
        DefaultProjectLanes(String description,int rank, String type) {
            this.description = description;
            this.rank = rank;
            this.type = type;
        }
        public String getDescription() { return description; }
        public int getRank() { return rank; }
        public String getType() { return type; }
    }

    
    
    @Column(name = "color", nullable = false)
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @ManyToOne()
    @JoinColumn(name = "projectid", nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


}
