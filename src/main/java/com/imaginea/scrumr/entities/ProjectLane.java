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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

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

    @Column(name = "color", nullable = false)
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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
