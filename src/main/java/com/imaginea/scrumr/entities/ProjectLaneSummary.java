package com.imaginea.scrumr.entities;

/**
 *  ProjectLaneSummary - represent a lane information for a given project
 *
 */
public class ProjectLaneSummary{

    private int color;

    private int rank;

    private int projectid;

    private String description;
    
    private String type;
    
    private int laneid;

    public void setColor(int color) {
		this.color = color;
	}
    
    public void setRank(int rank) {
		this.rank = rank;
	}
    
    public void setProjectid(int projectid) {
		this.projectid = projectid;
	}
    
    public void setDescription(String description) {
		this.description = description;
	}
    
    public void setType(String type) {
		this.type = type;
	}
    
    public void setLaneid(int laneid) {
		this.laneid = laneid;
	}
    
    public int getColor() {
        return color;
    }
    
    public int getRank() {
        return rank;
    }
    
    public int getProjectid() {
        return projectid;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getType() {
        return type;
    }
    
    public int getLaneid() {
        return laneid;
    }
}
