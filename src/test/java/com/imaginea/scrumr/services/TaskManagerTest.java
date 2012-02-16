package com.imaginea.scrumr.services;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.imaginea.scrumr.entities.Task;
import com.imaginea.scrumr.interfaces.TaskManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TaskManagerTest {

    @Autowired
    private TaskManager taskManager;

    @Test
    public void testFetchTaskLoad() {
        Integer projectId = 1;
        Integer sprintId = 1;
        List tasks = taskManager.fetchTeamStatusBySprint(projectId, sprintId);
        assertNotNull(tasks);
        debug(tasks);
    }

    private void debug(List tasks) {
        System.out.println(tasks);
        for (Iterator iterator = tasks.iterator(); iterator.hasNext();) {
            Object[] object = (Object[]) iterator.next();
            // Object object = (Object)iterator.next();
            System.out.println(object.getClass().getSimpleName());
           // Task task = (Task) object[0];
            // Task task = (Task) object;
            // System.out.println(object[1].getClass().getSimpleName());
            Long sum = (Long) object[1];
            // System.out.println(object.getClass().getSimpleName());
            /*
             * System.out.println(task.getUser().getDisplayname() + "createdBy:" + " Days: " +
             * task.getTimeInDays() + " Primary Key" + task.getPkey() + "content" +
             * task.getContent());
             */
            System.out.println("Sum:" + sum);
        }

    }
}
