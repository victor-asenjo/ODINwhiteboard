package edu.upc.essi.dtim.odin.NextiaStore;

import edu.upc.essi.dtim.odin.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphStoreFactory {
    private static GraphStoreInterface instance = null;

    @Autowired
    private static AppConfig appConfig;

    // Private constructor prevents instantiation from outside the class
    private GraphStoreFactory() {
        // Being private prevents the factory from being instantiated from outside the class
    }

    // The synchronized keyword is used to ensure thread safety of the singleton instance creation.
    public static synchronized GraphStoreInterface getInstance() throws Exception {
        if (instance == null) {
            if(appConfig != null) {
                // https://www.digitalocean.com/community/tutorials/spring-value-annotation
                System.out.println("Creating new instance of GraphStoreFactory");
                switch (appConfig.getDBTypeProperty()) {
                    case "Jena":
                        instance = new GraphStoreJenaImpl();
                        break;
                    case "Dummy":
                        instance = new GraphStoreDummyImpl();
                        break;
                    default:
                        throw new Exception("Error with DB type");
                }
            }
            else instance = new GraphStoreJenaImpl();
        }
        System.out.println("Returning instance of GraphStoreFactory: "+ instance);
        return instance;
    }
}
