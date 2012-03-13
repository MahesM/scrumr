package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.util.Date;
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
    private int[] storyCountByStages = new int[5];
    private String[] stageImageUrl = new String[5];
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
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="sprint_id")
    public Set<Story> getStoryList() {
        return storyList;
    }
    
    public void setStoryList(Set<Story> storyList) {
        this.storyList = storyList;
        if(this.storyList != null){
            for(Story story:storyList){
                ProjectStage stage = story.getStstage();
                if(stage != null){
                    int rank = stage.getRank();
                    int currentCount = this.storyCountByStages[rank];
                    currentCount++;
                    this.storyCountByStages[rank] = currentCount;
                    this.stageImageUrl[rank] = stage.getUrl();
                }
                         
            } 
        }
    }

	@Column(name = "spstatus", nullable = false)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="sprint")
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
            if(task.getStatus().equals("2")){
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
    public int[] getStoryCountByStages() {
        return storyCountByStages;
    }
    
    public void setStoryCountByStages(int[] storyCountByStages) {
        this.storyCountByStages = storyCountByStages;
    }
    
    @Transient
    public String[] getStageImageUrl() {
        return stageImageUrl;
    }
    
    public void setStageImageUrl(String[] stageImageUrl) {
        this.stageImageUrl = stageImageUrl;
    }
}
