package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.Tuple;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.CsvDataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.JsonDataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.URI;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
public class SourceController {
    private final SourceService sourceService;

    private SourceController( @Autowired SourceService sourceService) {
        this.sourceService = sourceService;
    }

    /**
     * Bootstrap a new datasource into the project.
     *
     * @param projectId The ID of the project.
     * @param attach_file The Multipart file containing the datasource.
     * @param datasetName The name of the dataset.
     * @param datasetDescription The description of the dataset.
     * @return A ResponseEntity with a success message if the bootstrap was successful, or an error message if it failed.
     * @throws IOException If there is an error with the file handling.
     */
    @PostMapping(value="/project/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> bootstrap(@PathVariable("id") String projectId,
                                            @RequestPart String datasetName,
                                            @RequestPart String datasetDescription,
                                            @RequestPart MultipartFile attach_file) throws IOException {
        System.out.println("################### POST DATASOURCE RECIVED ###################");
        // Validate and authenticate access here
        if (!validateAccess(projectId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        // Reconstruct file from Multipart file
        String filePath = sourceService.reconstructFile(attach_file);

        // Extract data from datasource file
        Dataset datasource = sourceService.extractData(filePath, datasetName, datasetDescription);

        // Transform datasource into graph
        Graph graph = sourceService.transformToGraph(datasource);

        // Save graph into database
        boolean isSaved = sourceService.saveGraphToDatabase(graph);

        String graphId = graph.getName().getURI();

        if (isSaved) {
            // Add the local graph to the project's list of local graph IDs if it was saved
            sourceService.addLocalGraphToProject(projectId, graphId);

            if(datasource.getClass() == CsvDataset.class) savingDatasetObject(datasource.getName(), datasource.getDescription(), ((CsvDataset) datasource).getPath(), projectId);
            else if (datasource.getClass() == CsvDataset.class) savingDatasetObject(datasource.getName(), datasource.getDescription(), ((JsonDataset) datasource).getPath(), projectId);
        }

        // Return success message
        return ResponseEntity.ok(graphId);
    }

    @PostMapping("/tuple")
    public Tuple savingTupleObject(@RequestParam("tupleId") String datasetId,
                                  @RequestParam("tupleName") String tupleName,
                                  @RequestParam("tupleDescription") String tupleDescription){
        Tuple tuple = new Tuple();
        tuple.setTupleName(tupleName);
        tuple.setTupleDescription(tupleDescription);
        return sourceService.saveTuple(tuple);
    }

    @PostMapping("/project/{projectId}/datasources")
    public ResponseEntity<?> savingDatasetObject(
            @RequestParam("datasetName") String datasetName,
            @RequestParam("datasetDescription") String datasetDescription,
            @RequestParam("datasetPath") String path,
            @PathVariable String projectId) {
        try {
            System.out.println("################### POST A DATASOURCE RECEIVED ################### " + projectId);
            Dataset dataset = null;

            String extension = "";
            int dotIndex = path.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < path.length() - 1) {
                extension = path.substring(dotIndex + 1);
            }

            switch (extension.toLowerCase()) {
                case "csv":
                    dataset = new CsvDataset(null, datasetName, datasetDescription, path);
                    break;
                case "json":
                    dataset = new JsonDataset(null, datasetName, datasetDescription, path);
                    break;
                default:
                    throw new UnsupportedOperationException("Dataset type not supported: " + extension);
            }

            Dataset savedDataset = sourceService.saveDataset(dataset);

            //Create the relation with project adding the datasetId
            sourceService.addDatasetIdToProject(projectId, savedDataset.getDatasetId());

            return new ResponseEntity<>(savedDataset, HttpStatus.OK);
        } catch (UnsupportedOperationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/project/{projectId}/datasource/{id}")
    public ResponseEntity<Boolean> deleteDatasource(@PathVariable("projectId") String projectId,
                                                 @PathVariable("id") String id) {
        // Print a message to indicate that the delete request was received
        System.out.println("################### DELETE A DATASOURCE from project ################### " + projectId);
        System.out.println("################### DELETE A DATASOURCE RECEIVED ################### " + id);

        boolean deleted = false;

        //Check if the dataset is part of that project
        if(sourceService.projectContains(projectId, id)){
            // Delete the relation with project
            sourceService.deleteDatasetIdFromProject(projectId, id);

            // Call the projectService to delete the project and get the result
            deleted = sourceService.deleteDatasource(id);
        }

        // Check if the project was deleted successfully
        if (deleted) {
            // Return a ResponseEntity with HTTP status 200 (OK) and the boolean value true
            return ResponseEntity.ok(true);
        } else {
            // Return a ResponseEntity with HTTP status 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/project/{id}/datasources")
    public ResponseEntity<?> getDatasourcesFromProject(@PathVariable String id) {
        try {
            System.out.println("################### GET ALL DATASOURCE FROM PROJECT ################### " + id);
            List<Dataset> datasets = sourceService.getDatasetsOfProject(id);

            if (datasets.isEmpty()) {
                return new ResponseEntity<>("No datasets found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(datasets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/datasources")
    public ResponseEntity<?> getAllDatasource() {
        try {
            System.out.println("################### GET ALL DATASOURCE RECEIVED ################### ");
            List<Dataset> datasets = sourceService.getDatasets();

            if (datasets.isEmpty()) {
                return new ResponseEntity<>("No datasets found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(datasets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getGraph")
    public Graph getGraph(@RequestParam("graphId") String graphId) throws IOException {
        GraphStoreInterface graphStore = null;
        try {
            graphStore = GraphStoreFactory.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Graph graph = graphStore.getGraph(new URI(graphId));


        return graph;
    }




    /**
     * Validates access for the bootstrapping process.
     *
     * @param authentication the authentication object representing the authenticated user
     * @throws ResponseStatusException if the user does not have permission to perform the bootstrapping process
     */
    private boolean validateAccess(String authentication) {
        System.out.println(authentication);
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

