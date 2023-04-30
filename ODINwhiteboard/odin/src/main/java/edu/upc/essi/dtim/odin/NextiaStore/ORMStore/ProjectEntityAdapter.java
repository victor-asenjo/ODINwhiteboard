package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.odin.project.Project;

public class ProjectEntityAdapter {

    public Project adapt(ProjectEntity entity) {
        return new Project(entity.getProjectId(), entity.getProjectName(),
                entity.getProjectDescription(), entity.getProjectPrivacy(),
                entity.getProjectColor(), entity.getCreatedBy(),
                entity.getLocalGraphIDs());
    }

    public ProjectEntity adapt(Project project) {
        ProjectEntity entity = new ProjectEntity(
                project.getProjectId(),
                project.getProjectName(),
                project.getProjectDescription(),
                project.getProjectPrivacy(),
                project.getProjectColor(),
                project.getCreatedBy(),
                project.getLocalGraphIDs());
        return entity;
    }
}
