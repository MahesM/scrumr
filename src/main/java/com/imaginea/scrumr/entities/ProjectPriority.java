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
@Table(name = "projectpriority")
@NamedQueries({
    @NamedQuery(name = "projectpriorities.fetchAllPrioritiesByProjectId", query = "SELECT instance from ProjectPriority instance where instance.project.id=:projectid") })
@XmlRootElement
public class ProjectPriority extends AbstractEntity implements IEntity, Serializable {
    private String color; 
    private String description;
    private Project project;

    public static enum DefaultPriority {
        PRIORITY_1 ("Priority 1", "#FC5F5F"),
        PRIORITY_2 ("Priority 2", "#7395DC"),
        PRIORITY_3 ("Priority 3", "#EBA143");

        private final String description;
        private final String color;
        DefaultPriority(String description, String color) {
            this.description = description;
            this.color = color;
        }
        public String getDescription() { return description; }
        public String getColor() { return color; }
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

    @ManyToOne()
    @JoinColumn(name = "projectid", nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


}
