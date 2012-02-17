package com.imaginea.scrumr.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.entities.Task.TaskStatus;
import com.imaginea.scrumr.entities.User;
import com.imaginea.scrumr.interfaces.StoryManager;
import com.imaginea.scrumr.interfaces.TaskManager;
import com.imaginea.scrumr.interfaces.UserServiceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TaskManagerTest {

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private UserServiceManager userServiceManager;

    @Autowired
    private StoryManager storyManager;

    private static final Logger logger = LoggerFactory.getLogger(TaskManagerTest.class);

    @Test
    public void testStatusUpdate() {
        int taskId = 1;
        // status update
        String status = "IN_PROGRESS";
        Task task = taskManager.readTask(taskId);
        task.setStatus(TaskStatus.valueOf(status));
        User createdBy = userServiceManager.readUser(1);
        task.setCreatedByUser(createdBy);
        taskManager.updateTask(task);

        Task updatedTask = taskManager.readTask(taskId);
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    public void testTaskCreate() {

        String storyId = "1";
        String username = "201008025088";
        String assignee = null;
        String timeInDays = "1";
        String milestonePeriod = "1";
        // fetch the task count and compare in the end of it succeeds- no method currently to get
        // the count or taskId
        Task task = new Task();
        List<Task> result = new ArrayList<Task>();
        try {

            User createdBy = userServiceManager.readUser(username);
            task.setContent("teste");
            task.setCreatedByUser(createdBy);
            // if(milestonePeriod != null)
            task.setMilestonePeriod(milestonePeriod);
            // if(timeInDays != null)
            task.setTimeInDays(Integer.parseInt(timeInDays));

            User assigneeUser = userServiceManager.readUser(assignee);
            if (assigneeUser != null)
                task.setUser(assigneeUser);

            // independent task support is ok
            if (storyId != null)
                task.setStory(storyManager.readStory(Integer.parseInt(storyId)));
            taskManager.createTask(task);
        } catch (Exception e) {
            e.printStackTrace();
            // return result;
        }

    }

    @Test
    public void testFetchTaskStatusSummary() {
        // by project
        Integer projectId = 1;
        Integer sprintId = null;
        List tasks = taskManager.fetchTaskStatusSummary(projectId, sprintId);
        assertNotNull(tasks);
        // debug(tasks);

        // by sprint
        sprintId = 1;
        tasks = taskManager.fetchTaskStatusSummary(projectId, sprintId);
        assertNotNull(tasks);
        // debug(tasks);
    }

    @Test
    public void testFetchTaskStatusDetails() {

        Integer projectId = 1;
        Integer sprintId = 1;
        Integer userId = null;
        // by project Id and sprintId
        List<Task> tasks = taskManager.fetchTaskStatusDetails(projectId, sprintId, userId);
        assertNotNull(tasks);
        debug(tasks);

        // by user
        userId = 1;
        tasks = taskManager.fetchTaskStatusDetails(projectId, sprintId, userId);
        assertNotNull(tasks);
        debug(tasks);
    }

    private void debug(List tasks) {
        logger.debug("tasks", tasks);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();) {
            // Object[] object = (Object[]) iterator.next();
            Object object = (Object) iterator.next();
            // System.out.println(object.getClass().getSimpleName());
            // Task task = (Task) object[0];
            Task task = (Task) object;
            // System.out.println(object[1].getClass().getSimpleName());
            // Long sum = (Long) object[1];

            // System.out.println(object.getClass().getSimpleName());

            logger.debug(task.getUser().getDisplayname() + "createdBy:" + " Days: "
                                            + task.getTimeInDays() + " Primary Key"
                                            + task.getPkey() + "content" + task.getContent()
                                            + "status:" + task.getStatus());

            // System.out.println("Sum:" + sum);
            // System.out.println("Status" + (TaskStatus) object[3]);
        }

    }
}
