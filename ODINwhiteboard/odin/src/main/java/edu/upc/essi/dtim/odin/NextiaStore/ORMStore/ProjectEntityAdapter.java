package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.odin.project.Project;

public class ProjectEntityAdapter {

    public Project adapt(ProjectEntity entity) {
        Project projectAdapted = new Project(
                entity.getProjectName(),
                entity.getProjectDescription(), entity.getProjectPrivacy(),
                entity.getProjectColor(), entity.getCreatedBy(),
                entity.getLocalGraphIDs());
        projectAdapted.setProjectId(entity.getProjectId());
        return projectAdapted;
    }

    public ProjectEntity adapt(Project project) {
        ProjectEntity entity = new ProjectEntity(
                project.getProjectName(),
                project.getProjectDescription(),
                project.getProjectPrivacy(),
                project.getProjectColor(),
                project.getCreatedBy(),
                project.getLocalGraphIDs());
        return entity;
    }
}
