package edu.upc.essi.dtim.odin.project;

import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntity;
import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntityAdapter;
import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectEntityRepository projectRepository;

    public ProjectService(@Autowired ProjectEntityRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Adds a local graph to the specified project.
     *
     * @param projectId the ID of the project to add the local graph to
     * @param name the URI of the local graph to add
     * @throws IllegalArgumentException if the project with the given ID is not found
     */
    public void addLocalGraphToProject(String projectId, String name) {

        // Retrieve the project with the given ID
        Project project = getProjectById(projectId);

        // If the project is not found, throw an exception
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        // Add the URI of the local graph to the project's list of local graph IDs
        project.getLocalGraphIDs().add(name);
    }


    /**
     * Helper method to retrieve a project by ID.
     *
     * @param projectId the ID of the project to retrieve
     * @return the project with the given ID, or null if not found
     */
    private Project getProjectById(String projectId) {
        return null;
    }

    public Project createProject(Project project) {
        ProjectEntityAdapter adapter = new ProjectEntityAdapter();
        ProjectEntity projectEntity = adapter.adapt(project);
        Project entity = projectRepository.save(projectEntity); // get the ProjectEntity object from somewhere

        return entity;
    }


    public void saveProject(Project project) {
        ProjectEntityAdapter adapter = new ProjectEntityAdapter();
        ProjectEntity projectEntity = adapter.adapt(project);
        Project entity = projectRepository.save(projectEntity); // get the ProjectEntity object from somewhere
    }


    public List<Project> getAllProjects() {
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        ProjectEntityAdapter adapter = new ProjectEntityAdapter();
        return projectEntities.stream()
                .map(adapter::adapt)
                .collect(Collectors.toList());
    }
}

