package edu.upc.essi.dtim.odin.project;

import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntity;
import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntityAdapter;
import edu.upc.essi.dtim.odin.NextiaStore.ORMStore.ProjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
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
        System.out.println("--------------------CREATING " + project);
        ProjectEntityAdapter adapter = new ProjectEntityAdapter();
        ProjectEntity projectEntity = adapter.adapt(project);
        System.out.println("--------------------adapted " + projectEntity);
        Project entity = projectRepository.save(projectEntity); // get the ProjectEntity object from somewhere
        entity.setProjectId(entity.getProjectId()); // set the ID in the project object
        System.out.println("--------------------SAVED " + entity);

        return entity;
    }


    public void saveProject(Project project) {
        ProjectEntityAdapter adapter = new ProjectEntityAdapter();
        ProjectEntity projectEntity = adapter.adapt(project);
        Project entity = projectRepository.save(projectEntity); // get the ProjectEntity object from somewhere
    }


    public List<Project> getAllProjects() {
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        System.out.println(projectEntities);

        ProjectEntityAdapter adapter = new ProjectEntityAdapter();
        List<Project> projects = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntities) {
            Project project = adapter.adapt(projectEntity);
            projects.add(project);
        }

        return projects;
    }

    public boolean deleteAllProjects() {
        EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        Query query = entityManager.createNativeQuery("DELETE FROM projects");
        int rowsDeleted = query.executeUpdate();
        entityManager.getTransaction().commit();
        return rowsDeleted == 0;
    }

    public Project findById(String projectId) {
        Optional<ProjectEntity> projectEntityOptional = projectRepository.findById(projectId);
        if (projectEntityOptional.isPresent()) {
            ProjectEntity projectEntity = projectEntityOptional.get();
            return new Project(
                    projectEntity.getProjectId(),
                    projectEntity.getProjectName(),
                    projectEntity.getProjectDescription(),
                    projectEntity.getProjectPrivacy(),
                    projectEntity.getProjectColor(),
                    projectEntity.getCreatedBy(),
                    projectEntity.getLocalGraphIDs()
            );
        } else {
            // Handle case where project with given id does not exist
            return null;
        }
    }
}

