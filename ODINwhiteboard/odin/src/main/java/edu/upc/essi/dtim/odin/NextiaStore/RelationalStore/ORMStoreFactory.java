package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import org.springframework.stereotype.Component;

@Component
public class ORMStoreFactory {
    private static ORMStoreInterface ormStoreInterfaceInstance = null;

    private ORMStoreFactory() {
        // Being private prevents the factory from being instantiated from outside the class
    }

    public static ORMStoreInterface getInstance() {
        if (ormStoreInterfaceInstance == null) {
            System.out.println("Creating new instance of JpaOrmImplementation");
            ormStoreInterfaceInstance = new JpaOrmImplementation();
        }
        return ormStoreInterfaceInstance;
    }

}
