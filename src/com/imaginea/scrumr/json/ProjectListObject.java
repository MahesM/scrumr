package com.imaginea.scrumr.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.imaginea.scrumr.entities.ProjectEntity;
import com.sun.jersey.api.json.JSONConfiguration;

@XmlRootElement
public class ProjectListObject{
	private int count;
	private String nextPage;
	private String prevPage;
	private String error;
	
	@XmlElement(name="projects")
	List<ProjectEntity> projects; 
	
	public ProjectListObject(){
		count = 0;
		nextPage = null;
		prevPage = null;
		projects = null;
		error = null;
	}
	
	public ProjectListObject(int count, String nextPage,String prevPage, List<ProjectEntity> projects){
		this.count = count;
		this.nextPage = nextPage;
		this.prevPage = prevPage;
		this.projects = projects;
	}
	
	public void addProject(ProjectEntity proj){
		projects.add(proj);
	}
	
	public void setProjects(ArrayList<ProjectEntity> projs){
		projects = projs;
	}
	
	public List<ProjectEntity> getProjects(){
		return projects;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	public int getCount(){
		return count;
	}

	public void setNextPage(String nextPage){
		this.nextPage = nextPage;
	}
	
	public void setPrevPage(String prevPage){
		this.prevPage = prevPage;
	}
	
	public String getNextPage(){
		return nextPage;
	}
	
	public String getPrevPage(){
		return prevPage;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}