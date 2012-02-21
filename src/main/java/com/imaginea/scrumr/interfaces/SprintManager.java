package com.imaginea.scrumr.interfaces;

import java.util.List;

import com.imaginea.scrumr.entities.Project;
import com.imaginea.scrumr.entities.Sprint;

public interface SprintManager {

    void createSprint(Sprint sprint);

    Sprint readSprint(Integer pkey);

    void updateSprint(Sprint sprint);

    void deleteSprint(Sprint sprint);

    List<Sprint> selectSprintsByProject(Project project);

    Integer getSprintCountForProject(Project project);

    List<Sprint> selectFinishedSprints(Project project);

    Sprint selectSprintByProject(Project project, Integer sprint_number);
}
