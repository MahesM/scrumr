package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PROJECTS")
@XmlRootElement
public class ProjectEntity implements Serializable {
	public ProjectEntity(){
		this.title = null;
		this.description = null;
		this.start_date = null;
		this.end_date = null;
		this.no_of_sprints = 0;
		this.sprint_duration = 0;
		this.current_sprint = 0;
		this.createdby = null;
		this.last_updated = null;
		this.creation_date = null;
		this.status = null;
		this.last_updatedby = null;
	}
	
	private Long id;
	private String title;
	private String description;
	private Date start_date;
	private Date end_date;
	private int no_of_sprints;
	private int sprint_duration;
	private int current_sprint;
	private String createdby;
	private Set<UserEntity> assignees;
    private Date last_updated;
    private Date creation_date;
    private String status;
    private String last_updatedby;
    
    @Id
    @GeneratedValue
    @Column(name = "pid")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ptitle", nullable = false, length = 500)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "pdescription", nullable = false, length = 600)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "pstartdate", nullable = true)
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	
	@Column(name = "penddate", nullable = true)
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	
	@Column(name = "pnoofsprints", nullable = true)
	public int getNo_of_sprints() {
		return no_of_sprints;
	}
	
	public void setNo_of_sprints(int no_of_sprints) {
		this.no_of_sprints = no_of_sprints;
	}
	
	@Column(name = "psprintduration", nullable = true)
	public int getSprint_duration() {
		return sprint_duration;
	}
	public void setSprint_duration(int sprint_duration) {
		this.sprint_duration = sprint_duration;
	}
	
	@Column (name="pcurrentsprint", nullable = false)
	public int getCurrent_sprint() {
		return current_sprint;
	}
	public void setCurrent_sprint(int current_sprint) {
		this.current_sprint = current_sprint;
	}
	
	@Column(name = "pcreator", nullable = false, length = 100)
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
	@ManyToMany(fetch= FetchType.EAGER, targetEntity=UserEntity.class)
	@JoinTable(name = "PROJECT_USERS", joinColumns = { @JoinColumn(name = "ppid") }, inverseJoinColumns = { @JoinColumn(name = "puserid") })
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
	
	@Column(name = "plastupdated", nullable = false, length = 100)
	public Date getLast_updated() {
		return last_updated;
	}
	public void setLast_updated(Date last_updated) {
		this.last_updated = last_updated;
	}
	
	@Column(name = "pcreation", nullable = false)
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	
	@Column(name = "pstatus", nullable = false, length = 100)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "pupdatedby", nullable = false, length = 100)
	public String getLast_updatedby() {
		return last_updatedby;
	}
	public void setLast_updatedby(String last_updatedby) {
		this.last_updatedby = last_updatedby;
	}
	
}