package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;
import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.odin.project.Project;

import java.util.List;

public class ORMDatasetImplementation implements ORMStoreInterface<Dataset>{

    @Override
    public Dataset findById(String id) {
        return null;
    }

    @Override
    public Dataset save(Project project) {
        return null;
    }

    @Override
    public List<Project> getAll() {
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
