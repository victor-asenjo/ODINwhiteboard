package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class ORMDatasetImplementation implements ORMStoreInterface<Dataset>{

    @Override
    public Dataset findById(String id) {
        return null;
    }

    @Override
    public Dataset save(Dataset object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        Dataset savedDataset = null;
        try {
            em.getTransaction().begin();
            if (object.getDatasetId() == null) {
                // New project, persist it
                em.persist(object);
                savedDataset = object;
            } else {
                // Existing project, merge it
                savedDataset = em.merge(object);
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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        List<Dataset> datasets = null;
        try {
            Query CSVquery = em.createQuery("SELECT p FROM CsvDataset p");
            Query JSONquery = em.createQuery("SELECT p FROM JsonDataset p");
            datasets= CSVquery.getResultList();
            datasets.addAll(JSONquery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return datasets;
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
