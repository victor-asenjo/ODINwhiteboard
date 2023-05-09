package edu.upc.essi.dtim.odin.project;

import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ORMProjectImplementation;
import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ORMStoreInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    /**
     * Adds a local graph to the specified project.
     *
     * @param projectId the ID of the project to add the local graph to
     * @param name the URI of the local graph to add
     * @throws IllegalArgumentException if the project with the given ID is not found
     */
    public void addLocalGraphToProject(String projectId, String name) {

        // Retrieve the project with the given ID
        Project project = findById(projectId);

        // If the project is not found, throw an exception
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        // Add the URI of the local graph to the project's list of local graph IDs
        project.getLocalGraphIDs().add(name);

        //saving the updated project
        saveProject(project);
    }

    public Project saveProject(Project project) {
        ORMStoreInterface<Project> ormProject = new ORMProjectImplementation();
        return ormProject.save(project);
    }

    public Project findById(String projectId) {
        ORMStoreInterface<Project> ormProject = new ORMProjectImplementation();
        return ormProject.findById(projectId);
    }

    public List<Project> getAllProjects() {
        ORMStoreInterface<Project> ormProject = new ORMProjectImplementation();
        return ormProject.getAll();
    }

    public boolean deleteProject(String id) {
        ORMStoreInterface<Project> ormProject = new ORMProjectImplementation();
        return ormProject.deleteOne(id);
    }

    public boolean deleteAllProjects() {
        ORMStoreInterface<Project> ormProject = new ORMProjectImplementation();
        return ormProject.deleteAll();
    }
}

