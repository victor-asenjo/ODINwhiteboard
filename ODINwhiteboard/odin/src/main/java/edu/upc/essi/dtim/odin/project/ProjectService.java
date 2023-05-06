package edu.upc.essi.dtim.odin.project;

import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;

    public ProjectService(@Autowired ProjectRepository projectRepository, @Autowired EntityManagerFactory entityManagerFactory) {
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

    @Transactional
    public Project createProject(Project project) {
        System.out.println("--------------------CREATING " + project);

        Project tmp = projectRepository.save(project); // get the ProjectEntity object from somewhere
        return tmp;
    }
    @Transactional
    public void saveProject(Project project) {
        if (projectRepository.existsById(project.getProjectId())) {
            Optional<Project> existingProjectEntity = projectRepository.findById(project.getProjectId());
            // update the fields of the existing project entity

            existingProjectEntity.get().setLocalGraphIDs(project.getLocalGraphIDs());
            // update any other fields as needed
            projectRepository.save(existingProjectEntity.get());

        } else {
            projectRepository.save(project);
        }
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        System.out.println(projects);


        return projects;
    }

    @Transactional
    public boolean deleteAllProjects() {
        //projectRepository.deleteAll();
        //projectRepository.flush();

        return projectRepository.findAll().isEmpty();
    }

    /**
     * Helper method to retrieve a project by ID.
     *
     * @param projectId the ID of the project to retrieve
     * @return the project with the given ID, or null if not found
     */
    @Transactional
    public Project findById(String projectId) {
        Optional<Project> projectEntityOptional = projectRepository.findById(projectId);
        if (true) {
            Project project = projectEntityOptional.get();

            return project;
        } else {
            // Handle case where project with given id does not exist
            return null;
        }
    }
}

