package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.odin.project.Project;
import org.springframework.stereotype.Component;

@Component
public class ORMStoreFactory {
    private static ORMStoreInterface<Project> ormProjectInstance = null;
    private static ORMStoreInterface<Dataset> ormDatasetInstance = null;
    private static ORMStoreInterface<Graph> ormGraphInstance = null;

    private ORMStoreFactory() {
        // Being private prevents the factory from being instantiated from outside the class
    }

    public static ORMStoreInterface getInstance(Class<?> ormClass) throws Exception {
        System.out.println("Getting ORMStoreInterface" + ormClass.toString());
        if (ormClass.equals(Project.class)) {
            if (ormProjectInstance == null) {
                System.out.println("Creating new instance of ormProjectInstance");
                ormProjectInstance = new ORMProjectImplementation();
            }
            return ormProjectInstance;
        } else if (ormClass.equals(Dataset.class)) {
            if (ormDatasetInstance == null){
                System.out.println("Creating new instance of ormDatasetInstance");
                ormDatasetInstance = new ORMDatasetImplementation();
            }
            return ormDatasetInstance;
        } else if (ormClass.equals(Graph.class)) {
            if (ormGraphInstance == null) {
                System.out.println("Creating new instance of ormTupleInstance");
                ormGraphInstance = new ORMGraphImplementation();
            }
            return ormDatasetInstance;
        } else {
            throw new Exception("The class that you are trying to store does not have ORM implementation.");
        }
    }

}
