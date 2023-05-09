package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.odin.project.Project;

import java.util.List;

public interface ORMStoreInterface<T> {

    T findById(String id);

    T save(Project project);

    List<Project> getAll();

    boolean deleteOne(String id);

    boolean deleteAll();
}
