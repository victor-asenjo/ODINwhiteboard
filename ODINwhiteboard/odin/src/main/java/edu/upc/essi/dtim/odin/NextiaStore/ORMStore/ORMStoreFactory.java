package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.odin.project.Project;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ORMStoreFactory {
    private static ORMStoreInterface instance = null;
    private static ORMStoreInterface<Project> ormProjectInstance = null;
    private static ORMStoreInterface<Dataset> ormDatasetInstance = null;

    private ORMStoreFactory() {
        // Being private prevents the factory from being instantiated from outside the class
    }

    public static ORMStoreInterface getInstance(Class<?> ormClass) throws Exception {
        System.out.println("Creating new instance of ORMStoreInterface");

        // Mapa que asocia una clase ORM con su implementación correspondiente
        Map<Class<?>, ORMStoreInterface> instances = new HashMap<>();
        instances.put(Project.class, new ORMProjectImplementation());
        instances.put(Dataset.class, new ORMDatasetImplementation());

        // Busca la instancia correspondiente en el mapa
        ORMStoreInterface instance = instances.get(ormClass);

        // Si no se encontró una instancia, lanza una excepción
        if (instance == null) {
            throw new Exception("The class that you are trying to store does not have ORM implementation.");
        }

        System.out.println("Returning instance of ORMStoreInterface: "+ instance);
        return instance;
    }

}
