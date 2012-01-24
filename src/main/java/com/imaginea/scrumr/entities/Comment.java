package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="comments")
@NamedQueries({
	@NamedQuery(name="comments.fetchCommentsByStory", query="SELECT instance from Comment instance where instance.story=:story" )
})
@XmlRootElement
public class Comment extends AbstractEntity implements IEntity, Serializable {

	private String content;
	private Date logDate;
	private User user;
	private Story story;
	
	
	@ManyToOne(cascade = CascadeType.ALL)	
//	@JoinColumn (name="uid", nullable = false)
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
	 * @return the commentContent
	 */
	@Column(name="ccontent")
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
	@Column(name="clogdate")
	public Date getLogDate() {
		return logDate;
	}
	/**
	 * @param commentLogDate the commentLogDate to set
	 */
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	/**
	 * @return the story
	 */
	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn (name="stid", nullable = false)
	public Story getStory() {
		return story;
	}
	/**
	 * @param story the story to set
	 */
	public void setStory(Story story) {
		this.story = story;
	}
}
