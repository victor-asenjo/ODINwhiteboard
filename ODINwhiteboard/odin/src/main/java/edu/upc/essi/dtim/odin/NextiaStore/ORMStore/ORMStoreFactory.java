package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.DataSources.Tuple;
import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.odin.project.Project;
import org.springframework.stereotype.Component;

@Component
public class ORMStoreFactory {
    private static ORMStoreInterface<Project> ormProjectInstance = null;
    private static ORMStoreInterface<Dataset> ormDatasetInstance = null;
    private static ORMStoreInterface<Tuple> ormTupleInstance = null;

    private ORMStoreFactory() {
        // Being private prevents the factory from being instantiated from outside the class
    }

    public static ORMStoreInterface getInstance(Class<?> ormClass) throws Exception {
        System.out.println("Creating new instance of ORMStoreInterface");
        if (ormClass.equals(Project.class)) {
            if (ormProjectInstance == null) ormProjectInstance = new ORMProjectImplementation();
            return ormProjectInstance;
        } else if (ormClass.equals(Dataset.class)) {
            if (ormDatasetInstance == null) ormDatasetInstance = new ORMDatasetImplementation();
            return ormDatasetInstance;
        } else if (ormClass.equals(Tuple.class)) {
            if (ormTupleInstance == null) ormTupleInstance = new ORMTupleImplementation();
            return ormDatasetInstance;
        } else {
            throw new Exception("The class that you are trying to store does not have ORM implementation.");
        }
    }

}
