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
@Table(name = "stories")
@NamedQueries({
	@NamedQuery(name="stories.fetchStoriesByProject", query="SELECT instance from Story instance where instance.project=:project" ),
	@NamedQuery(name="stories.fetchStoriesByStatus", query="SELECT instance from Story instance where instance.sprint_id=:sprint and instance.status=:status" ),
	@NamedQuery(name="stories.fetchUnfinishedStories", query="SELECT instance from Story instance where instance.sprint_id=:sprint and instance.status !=:status" ),
	@NamedQuery(name="stories.fetchStoriesBySprint", query="SELECT instance from Story instance where instance.sprint_id=:sprint" ),
	@NamedQuery(name="stories.fetchStoriesBySprintProject", query="SELECT instance from Story instance where instance.project=:project and instance.sprint_id=:sprint" ),
	@NamedQuery(name="stories.fetchUnAssignedStories", query="SELECT instance from Story instance where instance.project=:project and instance.sprint_id is null" )
})
@XmlRootElement
public class Story extends AbstractEntity implements IEntity, Serializable {

	private Project project;
	private String title;
	private int priority;
	private Sprint sprint_id;
	private Date creation_date;
	private String creator;
	private Date last_updated;
	private String last_updatedby;
	private String status;
	private Set<User> assignees;
	private List<Comment> comments;
	private List<Task> todos;
	private int view_count;
	
	private List<Status> statusList; 


	@ManyToOne()
	@JoinColumn (name="stpid", nullable = false)
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}

	@Column(name = "sttitle")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "stpriority", nullable = false)
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@ManyToOne
	@JoinColumn (name="stsprint", nullable = true)
	public Sprint getSprint_id() {
		return sprint_id;
	}
	public void setSprint_id(Sprint sprint_id) {
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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
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
	@JoinTable(name = "story_users", joinColumns = { @JoinColumn(name = "stpid") }, inverseJoinColumns = { @JoinColumn(name = "stuserid") })
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

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY,  mappedBy= "story")
	@JsonIgnore
	//	@JoinColumn (name="stid", nullable = false)
	public List<Comment> getComments() {
		return comments;
	}
	/**
	 * @param story the story to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	@OneToMany(cascade =CascadeType.ALL, fetch=FetchType.LAZY,  mappedBy= "story")
	@JsonIgnore
	public List<Status> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<Status> statusList) {
		this.statusList = statusList;
	}
	
	@OneToMany(cascade =CascadeType.ALL, fetch=FetchType.LAZY,  mappedBy= "story")
	@JsonIgnore
	public List<Task> getTodos() {
		return todos;
	}
	public void setTodos(List<Task> todos) {
		this.todos = todos;
	}
	

}
