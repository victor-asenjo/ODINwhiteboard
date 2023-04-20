package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.DataSources.Dataset;
import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStoreInterface;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BootstrappingController {

    @PostMapping("/api/bootstrap")
    public String bootstrap(@RequestBody BootstrapRequest request) {

        // Validate and authenticate access here
        if (!validateAccess(request)) {
            return "Access denied";
        }

        // Get datasource file and extract data
        Dataset datasource = extractData(request.getDatasourceFile());

        // Transform data to custom graph
        Graph graph = transformDataToGraph(datasource);

        // Save graph into database
        saveGraphToDatabase(graph);

        // Return success message
        return "Bootstrap successful";
    }

    /**
     * Validates access for the bootstrapping process.
     *
     * @param authentication the authentication object representing the authenticated user
     * @throws ResponseStatusException if the user does not have permission to perform the bootstrapping process
     */
    public boolean validateAccess(BootstrapRequest authentication) {

        /*
        // Extract the JWT token from the authentication object
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        // Extract the attributes (claims) from the JWT token
        Map<String, Object> attributes = token.getTokenAttributes();
        // Extract the username from the attributes
        String username = attributes.get("username").toString();
        // Extract the roles from the attributes
        List<String> roles = (List<String>) attributes.get("roles");
        // Check if the user has the 'ROLE_ADMIN' role
        boolean isAdmin = roles.contains("ROLE_ADMIN");
        // If the user is not an admin, throw an exception indicating that they don't have permission
        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User " + username + " does not have access to perform the bootstrapping process.");
        }
         */

        return true;

    }


    /**
     * Extracts data from a datasource file and returns it as a Dataset object.
     *
     * @param datasourceFile the filepath to the datasource file
     * @return the extracted data as a Dataset object
     */
    private Dataset extractData(String datasourceFile) {
        // TODO: Implement datasource extraction logic here
        // In this method, you should write code to read the datasource file and extract its data into a Dataset object
        // This could involve parsing the file's contents, validating its format, and constructing a Dataset object based on the data

        // For now, this method simply returns an empty Dataset object, to prevent errors in the rest of the code
        return new Dataset("Empty Dataset");
    }


    private Graph transformDataToGraph(Dataset datasource) {
        // Create a new Graph object to store the resulting RDF graph
        Graph graph = Graph.createDefaultGraph();

        // TODO: Implement data transformation logic here

        // For example, assuming that the dataset contains a list of objects
        //List<Object> objects = datasource.getData();

        List<Object> objects = new ArrayList<>();

        objects.add("John");
        objects.add(25);
        objects.add("johndoe@example.com");
        objects.add(false);

        // Iterate over the objects and create a new triple for each one
        for (Object object : objects) {
            // Create a new URI object for the subject, predicate, and object of the triple
            URI subject = new URI("http://example.org/subjects/" + object.toString());
            URI predicate = new URI("http://example.org/predicates/hasValue");
            URI objectURI = new URI("http://example.org/objects/" + object);

            // Create a new Triple object with the subject, predicate, and object URIs
            Triple triple = new Triple(subject, predicate, objectURI);

            // Add the new triple to the graph
            graph.addTriple(triple);
        }

        // Return the resulting RDF graph
        return graph;
    }

    private void saveGraphToDatabase(Graph graph) {
        GraphStoreInterface graphStore;
        try {
            graphStore = GraphStoreFactory.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        graphStore.saveGraph(graph);
    }

    @Getter
    @Setter
    public static class BootstrapRequest {

        private String datasourceFile;

        // Constructors, getters and setters

    }


}

