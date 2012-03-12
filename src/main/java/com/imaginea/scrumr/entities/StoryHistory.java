package com.imaginea.scrumr.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "story_history")
@NamedQueries({
	@NamedQuery(name="storyhistory.fetchStoryStatus", query="SELECT instance from StoryHistory instance where instance.story.id=:storyid and instance.stage=:stage" ),
	@NamedQuery(name="storyhistory.fetchUserStoryStatus", query="SELECT instance from StoryHistory instance where instance.user.username =:userid and instance.story.id=:storyid and instance.stage=:stage" ),
	@NamedQuery(name="storyhistory.clearUsersByStage", query="DELETE from StoryHistory instance where instance.story.id=:storyid and instance.stage=:stage" )
})
public class StoryHistory extends AbstractEntity implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private User user;
	private Story story;
	private ProjectStage stage;

	/**
	 * @return the user
	 */
	@ManyToOne
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
	@ManyToOne
	@JoinColumn (name="storyid", nullable = true)
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
	@ManyToOne
	@JoinColumn(name="stageid", nullable = false)
	public ProjectStage getStage() {
		return stage;
	}
	/**
	 * @param stage the stage to set
	 */
	public void setStage(ProjectStage stage) {
		this.stage = stage;
	}
}
