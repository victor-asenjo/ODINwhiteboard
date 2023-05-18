package edu.upc.essi.dtim.odin.bootstrapping;

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
     */
    @PostMapping(value="/project/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> bootstrap(@PathVariable("id") String projectId,
                                            @RequestPart String datasetName,
                                            @RequestPart String datasetDescription,
                                            @RequestPart MultipartFile attach_file) {
        System.out.println("################### POST DATASOURCE RECEIVED FOR BOOTSTRAP###################");
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

        String visualSchema = sourceService.generateVisualSchema(graph);
        System.out.println(visualSchema);
        System.out.println("-----------------------------------> VISUAL SCHEMA INSERTED");
        // Save graph into database
        boolean isSaved = sourceService.saveGraphToDatabase(graph);

        String graphId = graph.getName().getURI();

        if (isSaved) {
            // Add the local graph to the project's list of local graph IDs if it was saved
            sourceService.addLocalGraphToProject(projectId, graphId);

            if(datasource.getClass() == CsvDataset.class) savingDatasetObject(datasource.getName(), datasource.getDescription(), ((CsvDataset) datasource).getPath(), projectId);
            else if (datasource.getClass() == JsonDataset.class) savingDatasetObject(datasource.getName(), datasource.getDescription(), ((JsonDataset) datasource).getPath(), projectId);
        }



        // Return success message
        return ResponseEntity.ok(graphId);
    }

    @PostMapping("/project/{projectId}/datasources")
    public ResponseEntity<?> savingDatasetObject(
            @RequestParam("datasetName") String datasetName,
            @RequestParam("datasetDescription") String datasetDescription,
            @RequestParam("datasetPath") String path,
            @PathVariable String projectId) {
        try {
            System.out.println("################### POST A DATASOURCE RECEIVED ################### " + projectId);
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
    public Graph getGraph(@RequestParam("graphId") String graphId) {
        GraphStoreInterface graphStore;
        try {
            graphStore = GraphStoreFactory.getInstance();
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
        System.out.println(authentication);
        //TODO: IMPLEMENTATION
        return true;

    }
}

