package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "sprints")
@NamedQueries({
	@NamedQuery(name="sprints.selectSprintsByProject", query="SELECT instance from Sprint instance where instance.project=:project" ),
	@NamedQuery(name="sprints.selectSprintCountForProject", query="SELECT count(instance) from Sprint instance where instance.project=:project" ),
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
	private Set<Task> taskList;
	private Set<Story> storyList;
	private int taskCount;
	private int completedTasks;
    private List<StoryStageInfo> storyStageDetails = new ArrayList<StoryStageInfo>();
    private List<StorySizeInfo> storySizeDetails = new ArrayList<StorySizeInfo>();
    private Date projectStartDate;
    private Date projectEndDate;
    private String projectStatus;
    
	@Column(name = "spid")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@JsonIgnore
	@Embedded
	@ManyToOne
	@JoinColumn (name="sppid", nullable = false)
	public Project getProject() {
		return project;		
	}
	public void setProject(Project project) {
	    this.projectStartDate = project.getStart_date();
        this.projectEndDate = project.getEnd_date();
        this.projectStatus = project.getStatus();
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
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="sprint_id")
    public Set<Story> getStoryList() {
	    return storyList;
    }
    
    public void setStoryList(Set<Story> storyList) {
        this.storyList = storyList;
    }

    @Transient
    public List<StoryStageInfo> getStoryStageDetails() {
        return storyStageDetails;
    }
    
    public void setStoryStageDetails(List<StoryStageInfo> storyStageDetails) {
        this.storyStageDetails = storyStageDetails;
    }
    
	@Column(name = "spstatus", nullable = false)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="sprint")
	public Set<Task> getTaskList() {
        return taskList;
    }
	
	public void setTaskList(Set<Task> taskList) {
        this.taskList = taskList;
        this.taskCount = 0;
        this.completedTasks = 0;
        if(taskList != null && !(taskList.isEmpty())){
            this.taskCount = taskList.size();
            setCompletedTaskCount();
        }
        
    }
	
	private void setCompletedTaskCount() {
	    this.completedTasks = 0;
	    for(Task task:taskList){
            if(task.getStatus()!=null && task.getStatus().equals("2")){
                this.completedTasks++;
            }
        }
    }
    @Transient
	public int getTaskCount() {
        return taskCount;
    }
	
	public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
	
	@Transient
	public int getCompletedTasks() {
        return completedTasks;
    }
	
	public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }
	
    @Transient
    public Date getProjectStartDate() {
        return projectStartDate;
    }
    
    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }
    
    @Transient
    public Date getProjectEndDate() {
        return projectEndDate;
    }
    
    public void setProjectEndDate(Date projectEndDate) {
        this.projectEndDate = projectEndDate;
    }
    
    @Transient
    public String getProjectStatus() {
        return projectStatus;
    }
    
    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
    
    @Transient
    public List<StorySizeInfo> getStorySizeDetails() {
        return storySizeDetails;
    }
    
    public void setStorySizeDetails(List<StorySizeInfo> storySizeDetails) {
        this.storySizeDetails = storySizeDetails;
    }
}
