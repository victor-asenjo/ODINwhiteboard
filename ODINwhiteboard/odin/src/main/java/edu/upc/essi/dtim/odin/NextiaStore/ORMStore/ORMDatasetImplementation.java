package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.DataSources.dataset.Dataset;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ORMDatasetImplementation implements ORMStoreInterface<Dataset>{

    @Override
    public Dataset findById(String id) {
        return null;
    }

    @Override
    public Dataset save(Dataset dataset) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        Dataset savedDataset = null;
        try {
            em.getTransaction().begin();
            if (dataset.getDatasetId() == null) {
                // New project, persist it
                em.persist(dataset);
                savedDataset = dataset;
            } else {
                // Existing project, merge it
                savedDataset = em.merge(dataset);
            }
            em.getTransaction().commit();
            System.out.println("Dataset saved successfully");

        } catch (Exception e) {
            System.out.println("Error saving dataset: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return savedDataset;
    }

    @Override
    public List<Dataset> getAll() {
        return null;
    }

    @Override
    public boolean deleteOne(String id) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }
}
