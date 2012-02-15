package com.imaginea.scrumr.entities;

import java.io.Serializable;

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
@Table(name = "tasks")
@NamedQueries({ @NamedQuery(name = "tasks.fetchTasksByStory", query = "SELECT instance from Task instance where instance.story.id=:storyid") })
@XmlRootElement
public class Task extends AbstractEntity implements IEntity, Serializable {

    private String content;

    private String milestonePeriod;

    private User user;

    private Story story;

    private String status;

    @ManyToOne
    public User getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the commentContent
     */
    @Column(name = "tcontent")
    public String getContent() {
        return content;
    }

    /**
     * @param commentContent
     *            the commentContent to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the commentLogDate
     */
    @Column(name = "tmilestonemeriod")
    public String getMilestonePeriod() {
        return milestonePeriod;
    }

    /**
     * @param commentLogDate
     *            the commentLogDate to set
     */
    public void setMilestonePeriod(String milestonePeriod) {
        this.milestonePeriod = milestonePeriod;
    }

    /**
     * @return the story
     */
    @ManyToOne
    public Story getStory() {
        return story;
    }

    /**
     * @param story
     *            the story to set
     */
    public void setStory(Story story) {
        this.story = story;
    }

}
