package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "USERSTORIES")
@XmlRootElement
public class UserStoryEntity implements Serializable {
	private Long id;
	private ProjectEntity project;
	private String title;
	private String description;
	private int priority;
	private SprintEntity sprint_id;
	private Date creation_date;
	private String createdby;
    private Date last_updated;
    private String last_updatedby;
    private String status;
    private Set<UserEntity> assignees;
    private int view_count;
    
    @Id
    @GeneratedValue
    @Column(name = "stid")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn (name="stpid", nullable = false)
	public ProjectEntity getProject() {
		return project;
	}
	public void setProject(ProjectEntity project) {
		this.project = project;
	}
	
	@Column(name = "sttitle")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "stdescription", nullable = false, length = 500)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "stpriority", nullable = false)
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn (name="stsprint", nullable = true)
	public SprintEntity getSprint_id() {
		return sprint_id;
	}
	public void setSprint_id(SprintEntity sprint_id) {
		this.sprint_id = sprint_id;
	}
	
	@Column(name = "stcreation", nullable = false)
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	
	@Column(name = "stcreator", nullable = false, length = 100)
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
	@Column(name = "stlastupdated", nullable = false)
	public Date getLast_updated() {
		return last_updated;
	}
	public void setLast_updated(Date last_updated) {
		this.last_updated = last_updated;
	}
	
	@Column(name = "stupdatedby", nullable = false, length = 100)
	public String getLast_updatedby() {
		return last_updatedby;
	}
	public void setLast_updatedby(String last_updatedby) {
		this.last_updatedby = last_updatedby;
	}
	
	@Column(name = "ststatus", nullable = false, length = 100)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "stviewcount", nullable = false)
	public int getView_count() {
		return view_count;
	}
	public void setView_count(int view_count) {
		this.view_count = view_count;
	}
	
	@ManyToMany
	@JoinTable(name = "STORY_USERS", joinColumns = { @JoinColumn(name = "stpid") }, inverseJoinColumns = { @JoinColumn(name = "stuserid") })
	public Set<UserEntity> getAssignees() {
		return assignees;
	}
	public void setAssignees(Set<UserEntity> assignees) {
		this.assignees = assignees;
	}
	
	public void addAssignees(UserEntity assignee) {
		this.assignees.add(assignee);
	}
	
	public void removeAssignees(UserEntity assignee) {
		this.assignees.remove(assignee);
	}
	
    
}
