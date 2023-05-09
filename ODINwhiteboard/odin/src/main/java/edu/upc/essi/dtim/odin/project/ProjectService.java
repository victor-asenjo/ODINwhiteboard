package edu.upc.essi.dtim.odin.project;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
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

    /**
     * Helper method to retrieve a project by ID.
     *
     * @param projectId the ID of the project to retrieve
     * @return the project with the given ID, or null if not found
     */
    public Project findById(String projectId) {
        return null;
    }

    public Project saveProject(Project project) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        Project savedProject = null;
        try {
            em.getTransaction().begin();
            if (project.getProjectId() == null) {
                // New project, persist it
                em.persist(project);
                savedProject = project;
            } else {
                // Existing project, merge it
                savedProject = em.merge(project);
            }
            em.getTransaction().commit();
            System.out.println("Project saved successfully");

        } catch (Exception e) {
            System.out.println("Error saving project: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return savedProject;
    }


    public Project getProject(String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        Project project = null;
        try {
            project = em.find(Project.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return project;
    }

    public List<Project> getAllProjects() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        List<Project> projects = null;
        try {
            Query query = em.createQuery("SELECT p FROM Project p");
            projects = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return projects;
    }

    public boolean deleteProject(String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            Project project = em.find(Project.class, id);
            if (project != null) {
                em.remove(project);
                success = true;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return success;
    }

    public boolean deleteAllProjects() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Project");
            int deletedCount = query.executeUpdate();
            em.getTransaction().commit();
            success = deletedCount > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return success;
    }
}

