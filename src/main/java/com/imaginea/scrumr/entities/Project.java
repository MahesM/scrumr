package com.imaginea.scrumr.entities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

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
    private Set<Sprint> sprints;
    private Set<Story> stories;
    private List<ProjectStage> projectStages;
    private Set<ProjectPriority> projectPriorities;
    private ProjectPreferences projectPreferences;
    private int projectStoryCount;
    private int currentSprintStoryCount;
    private int currentSprintTaskCount;
    private int completedProjectStoryCount;
    private int completedCurrentSprintStoryCount;
    private int completedCurrentSprintTaskCount;
    private int maxRankStageId;
    private int minRankStageId;

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

    @Column(name = "pcreator", nullable = false)
    public String getCreatedby() {
        return createdby;
    }
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    @ManyToMany(fetch= FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity=User.class)
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
    
    @OneToOne(cascade=CascadeType.ALL, mappedBy="project")
    public ProjectPreferences getProjectPreferences() {
        if(projectPreferences == null){
            getDefaultProjectPreferences();
        }
        return projectPreferences;
    }
    
    private void getDefaultProjectPreferences() {
        ProjectPreferences projectPreferences = new ProjectPreferences();

        projectPreferences.setProject(this);            
        projectPreferences.setStorypriorityEnabled(true);

        projectPreferences.setStoryPointType(0);
        projectPreferences.setStoryPointLimit(18);
        projectPreferences.setStoryPointEnabled(true);            

        projectPreferences.setTaskMileStoneEnabled(true);
        projectPreferences.setMileStoneType(0);
        projectPreferences.setMileStoneRange(40); 

        this.projectPreferences = projectPreferences;
    }
    
    public void setProjectPreferences(ProjectPreferences projectPreferences) {
        if(projectPreferences != null){
            this.projectPreferences = projectPreferences;
        }        
    }

    @Column(name = "pstatus", nullable = false, length = 100)
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "pupdatedby", nullable = false)
    public String getLast_updatedby() {
        return last_updatedby;
    }
    public void setLast_updatedby(String last_updatedby) {
        this.last_updatedby = last_updatedby;
    }

    @OneToMany(cascade=CascadeType.ALL, mappedBy="project")
    public Set<Sprint> getSprints() {
        return sprints;
    }
    public void setSprints(Set<Sprint> sprints) {
        this.sprints = sprints;
    }

    @JsonIgnore
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy="project")
    public Set<Story> getStories() {
        return stories;
    }
    public void setStories(Set<Story> stories) {
        this.stories = stories;
        this.projectStoryCount = 0;
        this.currentSprintStoryCount = 0;
        this.currentSprintTaskCount = 0;
        this.completedProjectStoryCount = 0;
        this.completedCurrentSprintStoryCount = 0;
        this.completedCurrentSprintTaskCount = 0;
        
        if(this.stories != null &&  !this.stories.isEmpty()){
            this.projectStoryCount = stories.size();
            Sprint currentSprint = null;
            for(Story story:stories){
                ProjectStage stage = story.getStstage();
                if(stage != null && stage.getPkey() == this.maxRankStageId){
                    completedProjectStoryCount++;
                }
                Sprint sprint = story.getSprint_id();
                if(sprint != null){
                    Integer SprintId = sprint.getId();
                    if(sprint!= null && SprintId != null && SprintId.equals(current_sprint)){
                        currentSprint = sprint;
                        if(stage != null && stage.getPkey() == this.maxRankStageId){
                            completedCurrentSprintStoryCount++;
                        }
                        this.currentSprintStoryCount++;                
                    } 
                }
                
            }
            if(currentSprint != null) {
                this.currentSprintTaskCount = currentSprint.getTaskCount();
                this.completedCurrentSprintTaskCount = currentSprint.getCompletedTasks();
            }                            
        }	   
    }

    @OneToMany(cascade=CascadeType.REMOVE, mappedBy="project")
    @OrderBy("rank ASC")
    public List<ProjectStage> getProjectStages(){
        if(projectStages == null){
            this.projectStages = fetchDefaultProjectStages();
        }
        return projectStages;

    }

    public void setProjectStages(List<ProjectStage> projectStages){
        if(projectStages != null){
            Collections.sort((projectStages));
            this.projectStages = projectStages;
            setStageMaxRank();
        }

    }
    
    private void setStageMaxRank() {
        int maxStage = 0, minRange = Integer.MAX_VALUE;
        this.maxRankStageId = 4;
        this.minRankStageId = 1;
        for(ProjectStage projectStage:projectStages){
            if(projectStage.getPkey() == null)
                return;
            int rank = projectStage.getRank();
            if(rank > maxStage){
               maxStage = rank;
               this.maxRankStageId = projectStage.getPkey();
           }
            if(rank < minRange){
                minRange = rank;
                this.minRankStageId = projectStage.getPkey();
            }
        }
    }
    @Transient
    public int getMaxRankStageId() {
        return maxRankStageId;
    }
    
    public void setMaxRankStageId(int maxRankStageId) {
        this.maxRankStageId = maxRankStageId;
    }
    
    @Transient
    public int getMinRankStageId() {
        return minRankStageId;
    }
    
    public void setMinRankStageId(int minRankStageId) {
        this.minRankStageId = minRankStageId;
    }
    
    @Transient
	public int getCurrentSprintTaskCount() {
        return currentSprintTaskCount;
    }

	public void setCurrentSprintTaskCount(int currentSprintTaskCount) {
        this.currentSprintTaskCount = currentSprintTaskCount;
    }

	@Transient
	public int getProjectStoryCount() {
        return projectStoryCount;
    }

	public void setProjectStoryCount(int projectStoryCount) {
        this.projectStoryCount = projectStoryCount;
    }

	@Transient
	public int getCurrentSprintStoryCount(){
	    return currentSprintStoryCount;
	}

	public void setCurrentSprintStoryCount(int currentSprintStoryCount) {
        this.currentSprintStoryCount = currentSprintStoryCount;
    }

    @OneToMany(cascade=CascadeType.REMOVE, mappedBy="project")
    @OrderBy("rank ASC")
    public Set<ProjectPriority> getProjectPriorities(){
        if(projectPriorities == null){
            this.projectPriorities = fetchDefaultProjectPriorities();
        }
        return projectPriorities;

    }

    public void setProjectPriorities(Set<ProjectPriority> projectPriorities){
        if(projectPriorities != null){
            this.projectPriorities = projectPriorities;
        }	       
    }

    private List<ProjectStage> fetchDefaultProjectStages() {
        ProjectStage.DefaultProjectStages[] defaultStages = ProjectStage.DefaultProjectStages.values();
        ProjectStage projectStage;
        List<ProjectStage> projectStages = new ArrayList<ProjectStage>();
        for(ProjectStage.DefaultProjectStages defaultStage: defaultStages){
            projectStage = createDefaultProjectStage(this,defaultStage.getTitle(),defaultStage.getDescription(),defaultStage.getRank(),defaultStage.getImageUrlIndex());
            projectStages.add(projectStage);
        }
        return projectStages;
    }

    private ProjectStage createDefaultProjectStage(Project project, String title, String description, int rank,int imageUrlIndex) {
        ProjectStage projectStage = new ProjectStage();
        projectStage.setProject(project);
        projectStage.setTitle(title);
        projectStage.setDescription(description);
        projectStage.setImageUrlIndex(imageUrlIndex);
        projectStage.setRank(rank);        
        return projectStage;
    }

    private Set<ProjectPriority> fetchDefaultProjectPriorities() {
        ProjectPriority.DefaultPriority[] priorities = ProjectPriority.DefaultPriority.values();
        ProjectPriority projectPriority;
        Set<ProjectPriority> projectPriorities = new HashSet<ProjectPriority>();
        for(ProjectPriority.DefaultPriority priority: priorities){
            projectPriority = createProjectPriority(this,priority.getDescription(),priority.getColor(), priority.getPKey(), priority.getRank());
            projectPriorities.add(projectPriority);
        }
        return projectPriorities;
    }

    private ProjectPriority createProjectPriority(Project project, String description, String color, int pkey, int rank) {
        ProjectPriority projectPriority = new ProjectPriority();
        projectPriority.setColor(color);
        projectPriority.setProject(project);
        projectPriority.setRank(rank);
        projectPriority.setDescription(description);
        return projectPriority;        
    }

    @Transient
    public int getCompletedProjectStoryCount() {
        return completedProjectStoryCount;
    }
    
    public void setCompletedProjectStoryCount(int completedProjectStoryCount) {
        this.completedProjectStoryCount = completedProjectStoryCount;
    }
    
    @Transient
    public int getCompletedCurrentSprintStoryCount() {
        return completedCurrentSprintStoryCount;
    }
    
    public void setCompletedCurrentSprintStoryCount(int completedCurrentSprintStoryCount) {
        this.completedCurrentSprintStoryCount = completedCurrentSprintStoryCount;
    }
    
    @Transient
    public int getCompletedCurrentSprintTaskCount() {
        return completedCurrentSprintTaskCount;
    }
    
    public void setCompletedCurrentSprintTaskCount(int completedCurrentSprintTaskCount) {
        this.completedCurrentSprintTaskCount = completedCurrentSprintTaskCount;
    }
}