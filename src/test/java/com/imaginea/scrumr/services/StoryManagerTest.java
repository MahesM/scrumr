package com.imaginea.scrumr.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.entities.Story;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.SprintManager;
import com.imaginea.scrumr.interfaces.StoryManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class StoryManagerTest {

    @Autowired
    private StoryManager storyManager;

    @Autowired
    private SprintManager sprintManager;

    @Autowired
    private ProjectManager projectManager;

    private static final Logger logger = LoggerFactory.getLogger(StoryManagerTest.class);

    Integer projectId = 1;

    Integer storyId = 1;

    Integer sprintNum = 1;

    @Test
    public void testAddToSprint() {
        try {

            Story story = storyManager.readStory(storyId);
            Sprint toSprint = sprintManager.selectSprintByProject(projectManager.readProject(projectId), sprintNum);
            story.setSprint_id(toSprint);
            logger.debug(toSprint.toString());
            storyManager.updateStory(story);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testAddToBacklog() {
        try {
            int backlogId = 0; // 0 is used for backlog
            Story story = storyManager.readStory(storyId);
            Sprint toSprint = sprintManager.selectSprintByProject(projectManager.readProject(projectId), backlogId);
            story.setSprint_id(toSprint);
            logger.debug(toSprint.toString());
           // story.setStatus("");
            storyManager.updateStory(story);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
