package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
@Table(name = "sprints")
@NamedQueries({
	@NamedQuery(name="sprints.selectSprintsByProject", query="SELECT instance from Sprint instance where instance.project=:project" ),
	@NamedQuery(name="sprints.selectSprintByProject", query="SELECT instance from Sprint instance where instance.project=:project and instance.id=:sprint_num" ),
	@NamedQuery(name="sprints.selectFinishedSprints", query="SELECT instance from Sprint instance where instance.enddate<:enddate" )
})

@XmlRootElement
public class Sprint extends AbstractEntity implements IEntity, Serializable {

	private Integer id;
	private Project project;
	private Date startdate;
	private Date enddate;
	private String status;

	@Column(name = "spid")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Embedded
	@ManyToOne
	@JoinColumn (name="sppid", nullable = false)
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
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
