package com.imaginea.scrumr.entities;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "SPRINTS")
@XmlRootElement
public class SprintEntity {
	private Long id;
	private ProjectEntity project;
	private Date startdate;
	private Date enddate;
    private String status;
    
	@Id
    @GeneratedValue
    @Column(name = "spid")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn (name="sppid", nullable = false)
	public ProjectEntity getProject() {
		return project;
	}
	public void setProject(ProjectEntity project) {
		this.project = project;
	}
	
	@Column(name = "spstart", nullable = true)
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	
	@Column(name = "spend", nullable = true)
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
	@Column(name = "spstatus", nullable = false)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
