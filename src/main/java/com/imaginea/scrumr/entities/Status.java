package com.imaginea.scrumr.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status extends AbstractEntity implements Serializable{

	private User user;
	private Story story;
	private String stage;

	/**
	 * @return the user
	 */
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn (name="userid", nullable = false)
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the story
	 */
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn (name="storyid", nullable = false)
	public Story getStory() {
		return story;
	}
	/**
	 * @param story the story to set
	 */
	public void setStory(Story story) {
		this.story = story;
	}
	/**
	 * @return the stage
	 */
	
	@Column(name="stage")
	public String getStage() {
		return stage;
	}
	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}
}
