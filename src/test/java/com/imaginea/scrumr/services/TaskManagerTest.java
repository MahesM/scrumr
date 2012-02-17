package com.imaginea.scrumr.services;

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
import com.imaginea.scrumr.interfaces.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TaskManagerTest {

    @Autowired
    private TaskManager taskManager;

    private static final Logger logger = LoggerFactory.getLogger(TaskManagerTest.class);

    @Test
    public void testStatusUpdate() {
        int taskId = 1;
        // status update
        String status = "IN_PROGRESS";
        Task task = taskManager.readTask(taskId);
        task.setStatus(TaskStatus.valueOf(status));
        taskManager.updateTask(task);

        Task updatedTask = taskManager.readTask(taskId);
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus());
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
