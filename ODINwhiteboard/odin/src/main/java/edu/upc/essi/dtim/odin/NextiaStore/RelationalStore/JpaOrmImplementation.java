package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class JpaOrmImplementation<T> implements ORMStoreInterface<T> {
    @Override
    public T save(T object) {
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
    public T findById(Class<T> entityClass, String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        T object = null;
        try {
            // Find the object with the given id in the entity
            object = (T) em.find(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return object;
    }

    @Override
    public List<T> getAll(Class<T> entityClass) {
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
    public boolean deleteOne(String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        EntityManager em = emf.createEntityManager();
        boolean success = false;
        try {
            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPAL LOBYYYY ALGUIEN");
            em.getTransaction().begin();

            Dataset datasetToRemove = em.find(Dataset.class, id);
            if (datasetToRemove != null) {
                System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPAL LOBYYYY DATASET");
                em.remove(datasetToRemove);
                success = true;
            } else {
                System.out.println("NNNNNNNNNNNNNOOO SE HA ELIMINADOO LOBYYYY FAIL DATASET");
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
    public boolean deleteAll(Class<T> entityClass) {
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
