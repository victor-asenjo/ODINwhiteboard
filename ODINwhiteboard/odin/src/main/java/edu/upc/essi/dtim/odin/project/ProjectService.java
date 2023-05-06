package edu.upc.essi.dtim.odin.project;

import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntity;
import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntityAdapter;
import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final LocalContainerEntityManagerFactoryBean entityManagerFactory;
    private ProjectEntityRepository projectRepository;

    public ProjectService(@Autowired ProjectEntityRepository projectRepository, @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        this.projectRepository = projectRepository;
        this.entityManagerFactory = entityManagerFactory;
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
        ProjectEntityAdapter adapter = new ProjectEntityAdapter();
        ProjectEntity projectEntity = adapter.adapt(project);
        System.out.println("--------------------adapted " + projectEntity);
        Project tmp = projectRepository.saveAndFlush(projectEntity); // get the ProjectEntity object from somewhere
        return tmp;
    }
    @Transactional
    public void saveProject(Project project) {
        ProjectEntityAdapter adapter = new ProjectEntityAdapter();
        ProjectEntity projectEntity = adapter.adapt(project);
        if (projectRepository.existsById(projectEntity.getProjectId())) {
            Optional<Project> existingProjectEntity = projectRepository.findById(projectEntity.getProjectId());
            // update the fields of the existing project entity
            if(existingProjectEntity.isPresent()){
                existingProjectEntity.get().setLocalGraphIDs(projectEntity.getLocalGraphIDs());
                // update any other fields as needed
                projectRepository.saveAndFlush(existingProjectEntity.get());
            }
        } else {
            projectRepository.saveAndFlush(projectEntity);
        }
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        projectRepository.flush();
        List<Project> projects = projectRepository.findAll();
        System.out.println(projects);


        return projects;
    }

    @Transactional
    public boolean deleteAllProjects() {
        projectRepository.deleteAll();
        projectRepository.flush();

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
        if (projectEntityOptional.isPresent()) {
            Project projectEntity = projectEntityOptional.get();
            Project project = new Project(
                    projectEntity.getProjectId(),
                    projectEntity.getProjectName(),
                    projectEntity.getProjectDescription(),
                    projectEntity.getProjectPrivacy(),
                    projectEntity.getProjectColor(),
                    projectEntity.getCreatedBy(),
                    projectEntity.getLocalGraphIDs()
            );
            return project;
        } else {
            // Handle case where project with given id does not exist
            return null;
        }
    }
}

