package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.Graph.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class SourceController {
    private final SourceService sourceService;

    private SourceController( @Autowired SourceService sourceService) {
        this.sourceService = sourceService;
    }

    /**
     * Bootstrap a new datasource into the project.
     * @param projectId The ID of the project.
     * @param projectDescription Additional metadata about the datasource.
     * @param file The Multipart file containing the datasource.
     * @return A success message if the bootstrap was successful, or an error message if it failed.
     */
    @PostMapping("/api/addSource")
    public String bootstrap(@RequestParam("projectId") String projectId,
                            @RequestParam("datasetName") String datasetName,
                            @RequestParam("datasetDescription") String datasetDescription,
                            @RequestParam("file") MultipartFile file) throws IOException {

        // Validate and authenticate access here
        if (!validateAccess(projectId)) {
            return "Access denied";
        }

        // Reconstruct file from Multipart file
        String filePath = sourceService.reconstructFile(file);

        // Extract data from datasource file
        Dataset datasource = sourceService.extractData(filePath, datasetName, datasetDescription);

        // Transform datasource into graph
        Graph graph = sourceService.transformToGraph(datasource);

        // Save graph into database
        boolean isSaved = sourceService.saveGraphToDatabase(graph);

        String graphId = graph.getName().getURI();

        // Add the local graph to the project's list of local graph IDs if it was saved
        if (isSaved) {
            sourceService.addLocalGraphToProject(projectId, graphId);
        }

        // Return success message
        return graphId;
    }

    /**
     * Validates access for the bootstrapping process.
     *
     * @param authentication the authentication object representing the authenticated user
     * @throws ResponseStatusException if the user does not have permission to perform the bootstrapping process
     */
    public boolean validateAccess(String authentication) {

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
}

