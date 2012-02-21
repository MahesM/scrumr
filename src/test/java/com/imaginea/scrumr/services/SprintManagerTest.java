package com.imaginea.scrumr.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;
import com.imaginea.scrumr.interfaces.ProjectManager;
import com.imaginea.scrumr.interfaces.SprintManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SprintManagerTest {

    @Autowired
    private SprintManager sprintManager;

    @Autowired
    private ProjectManager projectManager;

    private static final Logger logger = LoggerFactory.getLogger(SprintManagerTest.class);

    @Test
    public void testCreateSprint() {
        Sprint sprint = new Sprint();
        String start_date = "02/22/2012";
        String end_date = "02/29/2012";
        String projectId = "1";
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date startdate = format.parse(start_date);
            sprint.setStartdate(startdate);
            Date enddate = format.parse(end_date);
            sprint.setEnddate(enddate);
            Project project = projectManager.readProject(Integer.parseInt(projectId));
            sprint.setProject(project);
            sprintManager.createSprint(sprint);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
