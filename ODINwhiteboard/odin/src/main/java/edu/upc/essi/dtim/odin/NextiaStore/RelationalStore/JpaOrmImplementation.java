package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class JpaOrmImplementation implements ORMStoreInterface {
    @Override
    public <T> T save(T object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        T savedObject = null;
        try {
            em.getTransaction().begin();

            savedObject = em.merge(object);

            em.getTransaction().commit();
            System.out.println("Object "+ object.getClass() +" saved successfully");

        } catch (Exception e) {
            System.out.println("Error saving object" + object.getClass() + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return savedObject;
    }

    @Override
    public <T> T findById(Class<T> entityClass, String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        T object = null;
        try {
            // Find the object with the given id in the entity
            object = em.find(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return object;
    }

    @Override
    public <T> List<T> getAll(Class<T> entityClass) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        List<T> objects = null;
        try {
            Query datasetsOfDB = em.createQuery("SELECT d FROM "+entityClass.getSimpleName()+" d");
            objects= datasetsOfDB.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return objects;
    }

    @Override
    public <T> boolean deleteOne(Class<T> entityClass, String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        boolean success = false;
        try {
            System.out.println("-------------> STARTING DELETE PROCESS");
            em.getTransaction().begin();

            T objectToRemove = em.find(entityClass, id);
            if (objectToRemove != null) {
                System.out.println(entityClass.getSimpleName()+" DELETED");
                em.remove(objectToRemove);
                success = true;
            } else {
                System.out.println("!!!!!!!!!!!!!!!!!!!!! ERROR DELETING " + entityClass.getSimpleName());
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
    public boolean deleteAll(Class<?> entityClass) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM "+entityClass.getSimpleName());
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
