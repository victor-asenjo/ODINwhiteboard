package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.CsvDataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.JsonDataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.URI;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreInterface;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * The controller class for managing datasources in a project.
 */
@RestController
public class SourceController {
    private static final Logger logger = LoggerFactory.getLogger(SourceController.class);

    private final SourceService sourceService;

    /**
     * Constructs a new instance of SourceController.
     *
     * @param sourceService the SourceService dependency for performing datasource operations
     */
    private SourceController( @Autowired SourceService sourceService) {
        this.sourceService = sourceService;
    }

    /**
     * Performs a bootstrap operation by creating a datasource, transforming it into a graph, and saving it to the database.
     *
     * @param projectId          The ID of the project.
     * @param datasetName        The name of the dataset.
     * @param datasetDescription The description of the dataset.
     * @param attach_file        The attached file representing the datasource.
     * @return A ResponseEntity object containing the saved dataset or an error message.
     */
    @PostMapping(value="/project/{id}")//, consumes = {"multipart/form-data"})
    public ResponseEntity<?> bootstrap(@PathVariable("id") String projectId,
                                            @RequestPart String datasetName,
                                            @RequestPart String datasetDescription,
                                            @RequestPart MultipartFile attach_file) {
        try{
            logger.info("POST DATASOURCE RECEIVED FOR BOOTSTRAP");
            // Validate and authenticate access here
            if (!validateAccess(projectId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Reconstruct file from Multipart file
            String filePath = sourceService.reconstructFile(attach_file);

            // Extract data from datasource file
            Dataset datasource = sourceService.extractData(filePath, datasetName, datasetDescription);

            //Saving dataset to assign an id
            Dataset savedDataset = sourceService.saveDataset(datasource);

            // Transform datasource into graph
            GraphModelPair graph = sourceService.transformToGraph(savedDataset);

            //Generating visual schema for frontend
            String visualSchema = sourceService.generateVisualSchema(graph);

            // Save graph into database
            boolean isSaved = true;
                    //sourceService.saveGraphToDatabase(graph.getGraph());

            if (isSaved) {
                graph.getGraph().setGraphicalSchema(visualSchema);
                savedDataset.setLocalGraph(graph.getGraph());

                //Create the relation with project adding the datasetId
                sourceService.addDatasetIdToProject(projectId, savedDataset);
            }

            // Return success message
            return new ResponseEntity<>(savedDataset, HttpStatus.OK);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data source not created sucessfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Data source not created sucessfully");
        }
    }

    /**
     * Saves a dataset object and associates it with a specific project. NOT USED JUST FOR TESTING
     *
     * @param datasetName        The name of the dataset.
     * @param datasetDescription The description of the dataset.
     * @param path               The path of the dataset.
     * @param projectId          The ID of the project to associate the dataset with.
     * @return A ResponseEntity object containing the saved dataset or an error message.
     */
    @PostMapping("/project/{projectId}/datasources")
    public ResponseEntity<Dataset> savingDatasetObject(
            @RequestParam("datasetName") String datasetName,
            @RequestParam("datasetDescription") String datasetDescription,
            @RequestParam("datasetPath") String path,
            @PathVariable String projectId) {
        try {
            logger.info("POST A DATASOURCE RECEIVED: "+projectId);
            Dataset dataset;

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
            sourceService.addDatasetIdToProject(projectId, savedDataset);

            return new ResponseEntity<>(savedDataset, HttpStatus.OK);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data source not created sucessfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Data source not created sucessfully");
        }
    }

    /**
     * Deletes a datasource from a specific project.
     *
     * @param projectId The ID of the project from which to delete the datasource.
     * @param id        The ID of the datasource to delete.
     * @return A ResponseEntity object containing a boolean indicating if the deletion was successful or not.
     */
    @DeleteMapping("/project/{projectId}/datasource/{id}")
    public ResponseEntity<Boolean> deleteDatasource(@PathVariable("projectId") String projectId,
                                                 @PathVariable("id") String id) {
        // Print a message to indicate that the delete request was received
        logger.info("DELETE A DATASOURCE from project: " +projectId);
        logger.info("DELETE A DATASOURCE RECEIVED: " +id);

        boolean deleted = false;

        //Check if the dataset is part of that project
        if(sourceService.projectContains(projectId, id)){
            // Delete the relation with project
            sourceService.deleteDatasetFromProject(projectId, id);

            // Call the projectService to delete the project and get the result
            //this is not necessary since we have the cascade all call in relation one-to-many
            //deleted = sourceService.deleteDatasource(id);
            deleted = true;
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

    /**
     * Retrieves all datasources from a specific project.
     *
     * @param id The ID of the project to retrieve datasources from.
     * @return A ResponseEntity object containing the list of datasets or an error message.
     */
    @GetMapping("/project/{id}/datasources")
    public ResponseEntity<?> getDatasourcesFromProject(@PathVariable String id) {
        try {
            logger.info("GET ALL DATASOURCE FROM PROJECT " +id);
            List<Dataset> datasets = sourceService.getDatasetsOfProject(id);

            if (datasets.isEmpty()) {
                return new ResponseEntity<>("No datasets found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(datasets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves all datasources.
     *
     * @return A ResponseEntity object containing the list of datasets or an error message.
     */
    @GetMapping("/datasources")
    public ResponseEntity<?> getAllDatasource() {
        try {
            logger.info("GET ALL DATASOURCE RECEIVED");
            List<Dataset> datasets = sourceService.getDatasets();

            if (datasets.isEmpty()) {
                return new ResponseEntity<>("No datasets found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(datasets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a graph based on the given graph ID.
     *
     * @param graphId The ID of the graph to retrieve.
     * @return The graph corresponding to the provided ID.
     */
    @GetMapping("/getGraph")
    public Graph getGraph(@RequestParam("graphId") String graphId) {
        GraphStoreInterface graphStore;
        try {
            graphStore = GraphStoreFactory.getInstance(new AppConfig());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return graphStore.getGraph(new URI(graphId));
    }




    /**
     * Validates access for the bootstrapping process.
     *
     * @param authentication the authentication object representing the authenticated user
     * @throws ResponseStatusException if the user does not have permission to perform the bootstrapping process
     */
    private boolean validateAccess(String authentication) {
        logger.info(authentication);
        //TODO: IMPLEMENTATION
        return true;

    }
}

