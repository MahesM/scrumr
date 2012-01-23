package com.imaginea.scrumr.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="TODO")
@XmlRootElement
public class TodoEntity {

	private long todoID;	
	private String content;
	private String milestonePeriod;
	private UserEntity user;
	private UserStoryEntity story;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name="tId")
	public long getTodoID() {
		return todoID;
	}
	/**
	 * @param id the id to set
	 */
	public void setTodoID(long todoID) {
		this.todoID = todoID;
	}		
	
	@ManyToOne(cascade = CascadeType.ALL)	
//	@JoinColumn (name="uid", nullable = false)
	public UserEntity getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(UserEntity user) {
		this.user = user;
	}
	/**
	 * @return the commentContent
	 */
	@Column(name="tContent")
	public String getContent() {
		return content;
	}
	/**
	 * @param commentContent the commentContent to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the commentLogDate
	 */
	@Column(name="cMilestonePeriod")
	public String getMilestonePeriod() {
		return milestonePeriod;
	}
	/**
	 * @param commentLogDate the commentLogDate to set
	 */
	public void setMilestonePeriod(String milestonePeriod) {
		this.milestonePeriod = milestonePeriod;
	}
	/**
	 * @return the story
	 */
	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn (name="stid", nullable = false)
	public UserStoryEntity getStory() {
		return story;
	}
	/**
	 * @param story the story to set
	 */
	public void setStory(UserStoryEntity story) {
		this.story = story;
	}
}
