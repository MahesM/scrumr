package com.imaginea.scrumr.json;

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

@XmlRootElement
public class ProjectObject {
	public ProjectObject(){
		this.title = null;
		this.description = null;
		this.start_date = null;
		this.end_date = null;
		this.no_of_sprints = 0;
		this.sprint_duration = 0;
		this.current_sprint = null;
		this.createdby = null;
		this.last_updated = null;
		this.creation_date = null;
		this.status = null;
		this.view_count = 0;
		this.last_updatedby = null;
	}
	
	public ProjectObject(Long id, String title, String description,
			Date start_date, Date end_date, int no_of_sprints,
			int sprint_duration, String current_sprint, String createdby,
			Date last_updated,
			Date creation_date, String status, int view_count,
			String last_updatedby) {
		super();
		this.title = title;
		this.description = description;
		this.start_date = start_date;
		this.end_date = end_date;
		this.no_of_sprints = no_of_sprints;
		this.sprint_duration = sprint_duration;
		this.current_sprint = current_sprint;
		this.createdby = createdby;
		this.last_updated = last_updated;
		this.creation_date = creation_date;
		this.status = status;
		this.view_count = view_count;
		this.last_updatedby = last_updatedby;
	}
	private Long id;
	private String title;
	private String description;
	private Date start_date;
	private Date end_date;
	private int no_of_sprints;
	private int sprint_duration;
	private String current_sprint;
	private String createdby;
	private Set<String> assignees;
	private Set<String> stories;
	private Date last_updated;
    private Date creation_date;
    private String status;
    private int view_count;
    private String last_updatedby;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	
	public Set<String> getStories() {
		return stories;
	}

	public void setStories(Set<String> stories) {
		this.stories = stories;
	}
	
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	
	public int getNo_of_sprints() {
		return no_of_sprints;
	}
	
	public void setNo_of_sprints(int no_of_sprints) {
		this.no_of_sprints = no_of_sprints;
	}
	
	public int getSprint_duration() {
		return sprint_duration;
	}
	public void setSprint_duration(int sprint_duration) {
		this.sprint_duration = sprint_duration;
	}
	
	public String getCurrent_sprint() {
		return current_sprint;
	}
	public void setCurrent_sprint(String current_sprint) {
		this.current_sprint = current_sprint;
	}
	
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
	public Set<String> getAssignees() {
		return assignees;
	}
	public void setAssignees(Set<String> assignees) {
		this.assignees = assignees;
	}
	
	public Date getLast_updated() {
		return last_updated;
	}
	public void setLast_updated(Date last_updated) {
		this.last_updated = last_updated;
	}
	
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getView_count() {
		return view_count;
	}
	public void setView_count(int view_count) {
		this.view_count = view_count;
	}
	
	public String getLast_updatedby() {
		return last_updatedby;
	}
	public void setLast_updatedby(String last_updatedby) {
		this.last_updatedby = last_updatedby;
	}
	
}