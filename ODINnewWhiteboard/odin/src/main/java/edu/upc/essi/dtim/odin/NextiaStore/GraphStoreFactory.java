package edu.upc.essi.dtim.odin.NextiaStore;

import edu.upc.essi.dtim.odin.config.AppConfig;
import org.springframework.stereotype.Component;

@Component
public class GraphStoreFactory {
    private static GraphStoreInterface instance = null;

    private static final AppConfig appConfig;

    static {
        // Initialize appConfig here
        appConfig = new AppConfig();
    }

    // Private constructor prevents instantiation from outside the class
    private GraphStoreFactory() {
        // Being private prevents the factory from being instantiated from outside the class
    }

    // The synchronized keyword is used to ensure thread safety of the singleton instance creation.
    public static synchronized GraphStoreInterface getInstance() throws Exception {
        if (instance == null) {
            // https://www.digitalocean.com/community/tutorials/spring-value-annotation
            switch (appConfig.getDBTypeProperty()){
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
        return instance;
    }
}
