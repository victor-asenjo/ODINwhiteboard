package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import edu.upc.essi.dtim.NextiaCore.datasources.Tuple;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ORMTupleImplementation implements ORMStoreInterface<Tuple>{

    @Override
    public Tuple findById(String id) {
        return null;
    }

    @Override
    public Tuple save(Tuple object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        Tuple savedTuple = null;
        try {
            em.getTransaction().begin();
            if (object.getTupleId() == null) {
                // New project, persist it
                em.persist(object);
                savedTuple = object;
            } else {
                // Existing project, merge it
                savedTuple = em.merge(object);
            }
            em.getTransaction().commit();
            System.out.println("Tuple saved successfully");

        } catch (Exception e) {
            System.out.println("Error saving tuple: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return savedTuple;
    }

    @Override
    public List<Tuple> getAll() {
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
