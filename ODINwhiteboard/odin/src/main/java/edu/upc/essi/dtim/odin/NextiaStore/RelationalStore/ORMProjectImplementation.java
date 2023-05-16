package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.odin.project.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class ORMProjectImplementation implements ORMStoreInterface<Project>{
    /**
     * Helper method to retrieve a project by ID.
     *
     * @param id the ID of the project to retrieve
     * @return the project with the given ID, or null if not found
     */
    @Override
    public Project findById(String id) {
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

    @Override
    public Project save(Project object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        Project savedProject = null;
        try {
            em.getTransaction().begin();
            if (object.getProjectId() == null) {
                // New project, persist it
                em.persist(object);
                savedProject = object;
            } else {
                // Existing project, merge it
                savedProject = em.merge(object);
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

    @Override
    public List<Project> getAll() {
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
    @Override
    public boolean deleteOne(String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            Project project = em.find(Project.class, id);
            if (project != null) {
                //Deleting the relations with project
                List<Dataset> datasetsIds = project.getDatasets();
                for (Dataset datasetToRemove : datasetsIds){
                    // Initializing ORMStoreInterface
                    ORMStoreInterface<Dataset> ormDataset = null;
                    try {
                        ormDataset = ORMStoreFactory.getInstance(Dataset.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    ormDataset.deleteOne(datasetToRemove.getDatasetId());
                }

                //Finally we remove the project object
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

    @Override
    public boolean deleteAll() {
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
