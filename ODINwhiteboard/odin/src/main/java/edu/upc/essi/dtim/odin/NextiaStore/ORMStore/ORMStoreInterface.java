package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import java.util.List;

public interface ORMStoreInterface<T> {

    T findById(String id);

    T save(T object);

    List<T> getAll();

    boolean deleteOne(String id);

    boolean deleteAll();
}
