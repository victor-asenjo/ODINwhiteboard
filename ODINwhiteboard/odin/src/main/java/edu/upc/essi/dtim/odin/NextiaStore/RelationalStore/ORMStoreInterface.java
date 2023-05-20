package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import java.util.List;

public interface ORMStoreInterface<T> {
    /**
     * Saves the object in the relational store.
     *
     * @param object The object to save.
     * @return The saved object.
     */
    T save(T object);

    /**
     * Finds an object by its ID in the relational store.
     *
     * @param entityClass The class of the object to find.
     * @param id          The ID of the object to find.
     * @return The found object, or null if not found.
     */
    T findById(Class<T> entityClass, String id);

    /**
     * Retrieves all objects of the specified class from the relational store.
     *
     * @param entityClass The class of the objects to retrieve.
     * @return A list of all objects of the specified class.
     */
    List<T> getAll(Class<T> entityClass);

    /**
     * Deletes an object with the specified ID from the relational store.
     *
     * @param id The ID of the object to delete.
     * @return true if the object was successfully deleted, false otherwise.
     */
    boolean deleteOne(String id);

    /**
     * Deletes all objects of the specified class from the relational store.
     *
     * @param entityClass The class of the objects to delete.
     * @return true if all objects were successfully deleted, false otherwise.
     */
    boolean deleteAll(Class<T> entityClass);
}
