package edu.upc.essi.dtim.odin.project;

import org.springframework.stereotype.Service;

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
        Project project = getProjectById(projectId);
        project = new Project();

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
        // TODO: Implement logic to retrieve the project from the database or some other source

        return null;
    }
}
