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
@Table(name="COMMENTS")
@XmlRootElement
public class CommentEntity {

	private long commentID;	
	private String content;
	private Date logDate;
	private UserEntity user;
	private UserStoryEntity story;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name="cId")
	public long getCommentID() {
		return commentID;
	}
	/**
	 * @param id the id to set
	 */
	public void setCommentID(long commentID) {
		this.commentID = commentID;
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
	@Column(name="cContent")
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
	@Column(name="cLogDate")
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
