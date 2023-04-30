package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.odin.project.Project;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Project")
public class ProjectEntity extends Project {

    @Id
    private Long project_id;

    public void setProject_id(Long projectId) {
        this.project_id = projectId;
    }

    public Long getProject_id() {
        return project_id;
    }
}
