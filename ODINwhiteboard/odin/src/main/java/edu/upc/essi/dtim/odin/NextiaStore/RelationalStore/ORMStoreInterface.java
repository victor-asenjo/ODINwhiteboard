package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import java.util.List;

public interface ORMStoreInterface<T> {
    T save(T object);

    T findById(Class<T> entityClass, String id);

    List<T> getAll(Class<T> entityClass);

    boolean deleteOne(String id);

    boolean deleteAll(Class<T> entityClass);
}
