package com.imaginea.scrumr.entities;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "projects")
@NamedQueries({
	@NamedQuery(name="projects.fetchAllProjects", query="SELECT instance from Project instance" )
})
@XmlRootElement
public class Project extends AbstractEntity implements IEntity, Serializable {

	private String title;
	private String description;
	private Date start_date;
	private Date end_date;
	private Integer no_of_sprints;
	private Integer sprint_duration;
	private Integer current_sprint;
	private String createdby;
	private Set<User> assignees;
	private Date last_updated;
	private Date creation_date;
	private String status;
	private String last_updatedby;
	
	


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
	public Integer getNo_of_sprints() {
		return no_of_sprints;
	}

	public void setNo_of_sprints(Integer no_of_sprints) {
		this.no_of_sprints = no_of_sprints;
	}

	@Column(name = "psprintduration", nullable = true)
	public Integer getSprint_duration() {
		return sprint_duration;
	}
	public void setSprint_duration(Integer sprint_duration) {
		this.sprint_duration = sprint_duration;
	}

	@Column (name="pcurrentsprint", nullable = false)
	public Integer getCurrent_sprint() {
		return current_sprint;
	}
	public void setCurrent_sprint(Integer current_sprint) {
		this.current_sprint = current_sprint;
	}

	@Column(name = "pcreator", nullable = false, length = 100)
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	@ManyToMany(fetch= FetchType.LAZY, cascade = CascadeType.ALL, targetEntity=User.class)
	@JoinTable(name = "project_users", joinColumns = { @JoinColumn(name = "ppid") }, inverseJoinColumns = { @JoinColumn(name = "puserid") })
	public Set<User> getAssignees() {
		return assignees;
	}
	public void setAssignees(Set<User> assignees) {
		this.assignees = assignees;
	}

	public void addAssignees(User assignee) {
		this.assignees.add(assignee);
	}

	public void removeAssignees(User assignee) {
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