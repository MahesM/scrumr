package com.imaginea.scrumr.resources;

import org.junit.Before;
import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;

import com.imaginea.scrumr.interfaces.TaskManager;

public class TaskResourceTest {

    // @Mock TaskManager taskManager;

    @Before
    public void setup() {
        // MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTeamStatusBySprint() {
        TaskResource taskResource = new TaskResource();
        taskResource.fetchTaskStatusSummary("1", "1");
    }
}
