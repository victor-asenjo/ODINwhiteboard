package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.datasources.Tuple;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.CsvDataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.NextiaCore.datasources.dataset.JsonDataset;
import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.LocalGraph;
import edu.upc.essi.dtim.NextiaCore.graph.Triple;
import edu.upc.essi.dtim.NextiaCore.graph.URI;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreInterface;
import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreInterface;
import edu.upc.essi.dtim.odin.config.AppConfig;
import edu.upc.essi.dtim.odin.project.ProjectService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SourceService {

    private ProjectService projectService;

    private final AppConfig appConfig;

    public SourceService(@Autowired AppConfig appConfig,
                         @Autowired ProjectService projectService
    ) {
        this.appConfig = appConfig;
        this.projectService = projectService;
    }

    /**
     * Stores a multipart file in the specified disk path and returns the absolute path of the file.
     *
     * @param multipartFile The multipart file to store.
     * @return The absolute path of the stored file.
     */
    public String reconstructFile(MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            String filename = RandomStringUtils.randomAlphanumeric(16) +"_"+ multipartFile.getOriginalFilename();

            // Get the disk path from the app configuration
            Path diskPath = Path.of(appConfig.getDiskPath());

            // Resolve the destination file path using the disk path and the generated filename
            Path destinationFile = diskPath.resolve(Paths.get(filename));
            System.out.println(destinationFile);
            System.out.println(destinationFile.getParent());
            System.out.println(destinationFile.getParent().equals(diskPath.toAbsolutePath()));
            // Perform a security check to ensure that the destination file is within the disk path
            if (!destinationFile.getParent().equals(diskPath)) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            // Copy the input stream of the multipart file to the destination file
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFile,StandardCopyOption.REPLACE_EXISTING);
            }

            // Return the absolute path of the destination file as a string
            //return destinationFile.toString();

            // Construct a relative path from the disk path and the generated filename
            //Path relativePath = diskPath.relativize(destinationFile);
            return destinationFile.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }



    /**
     * Recibe una ruta de archivo, lee los metadatos y retorna un objeto Dataset con los datos extraídos del archivo.
     *
     * @param filePath    La ruta del archivo del que se extraerán los datos
     * @param datasetName The name of the dataset
     * @param datasetDescription The description of the dataset
     * @return Un objeto Dataset con los datos extraídos del archivo
     */
    public Dataset extractData(String filePath, String datasetName, String datasetDescription) {
        // Extract the extension of the file from the file path
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);

        Dataset dataset;

        // Create a new dataset object with the extracted data
        switch (extension.toLowerCase()){
            case "csv":
                dataset = new CsvDataset(filePath, datasetName, datasetDescription, filePath, null);
                break;
            case "json":
                dataset = new JsonDataset(filePath, datasetName, datasetDescription, filePath, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported file format: " + extension);
        }

        return dataset;
    }

    /**
     * Recibe un objeto Dataset y retorna un objeto Graph que representa los datos del Dataset transformados a RDF.
     * @param dataset Un objeto Dataset con los datos a transformar
     * @return Un objeto Graph con los datos transformados a RDF
     */
    public Graph transformToGraph(Dataset dataset) throws IOException {
        //todo: call NextiaBS
        String datasetName = dataset.getName();
        if (datasetName == null) datasetName = "DatasetNameIsEmpty";
        try {
            // Try to convert the dataset to a graph
            //return dataset.convertToGraph(dataset.getDatasetId(), datasetName, dataset.getPath());
            //todo: here goes the transformation call to our localGraph
            Graph graph = hardcodedGraph(datasetName);
            return graph;
        } catch (UnsupportedOperationException e) {
            // If the dataset format is not supported, return an error graph
            Graph errorGraph = new LocalGraph(new URI(datasetName), new HashSet<>());
            errorGraph.addTriple(new Triple(
                    new URI(dataset.getDatasetId()),
                    new URI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
                    new URI("http://example.org/error#UnsupportedDatasetFormat")
            ));
            return errorGraph;
        }
    }

    public String generateVisualSchema(Graph graph) {
        //TODO: generate the visual schema from graph
        /* EXAMPLE
        {
          "nodes": [
            {
              "iri": "http://www.example.com/resource1",
              "id": "Class1",
              "label": "resource1",
              "iriType": "http://www.w3.org/2002/07/owl#Class",
              "shortType": "owl:Class",
              "type": "class"
            },
            {
              "iri": "http://www.example.com/property1",
              "id": "Property1",
              "label": "property1",
              "type": "property",
              "domain": "http://www.example.com/resource1",
              "range": "http://www.example.com/resource2"
            },
            {
              "iri": "http://www.example.com/resource2",
              "id": "Class2",
              "label": "resource2",
              "iriType": "http://www.w3.org/2002/07/owl#Class",
              "shortType": "owl:Class",
              "type": "class"
            }
          ],
          "links": [
            {
              "id": "Link1",
              "nodeId": "Property1",
              "source": "Class1",
              "target": "Class2",
              "label": "property1"
            }
          ]
        }

         */
        return "{'nodes': [{'iri': 'http://www.example.com/resource1','id': 'Class1','label': 'resource1','iriType': 'http://www.w3.org/2002/07/owl#Class','shortType': 'owl:Class','type': 'class'},{'iri': 'http://www.example.com/property1','id': 'Property1','label': 'property1','type': 'property','domain': 'http://www.example.com/resource1','range': 'http://www.example.com/resource2'}],'links': [{'id': 'Link1','nodeId': 'Property1','source': 'Class1','target': 'Class2','label': 'property1'}]}";
    }

    public boolean saveGraphToDatabase(Graph graph) {
        GraphStoreInterface graphStore;
        try {
            graphStore = GraphStoreFactory.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        graphStore.saveGraph(graph);
        return true;
    }

    public void addLocalGraphToProject(String projectId, String name) {
        projectService.addLocalGraphToProject(projectId, name);
    }
    public void addDatasetIdToProject(String projectId, String datasetId) {
        projectService.addDatasetIdToProject(projectId, datasetId);
    }

    public void deleteDatasetIdFromProject(String projectId, String datasetId) {
        projectService.deleteDatasetIdFromProject(projectId, datasetId);
    }

    public Tuple saveTuple(Tuple tuple) {
        ORMStoreInterface<Tuple> ormProject = null;
        try {
            ormProject = ORMStoreFactory.getInstance(Tuple.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ormProject.save(tuple);
    }

    public Dataset saveDataset(Dataset dataset) {
        ORMStoreInterface<Dataset> ormDataset = null;
        try {
            ormDataset = ORMStoreFactory.getInstance(Dataset.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ormDataset.save(dataset);
    }

    public List<Dataset> getDatasets() {
        ORMStoreInterface<Dataset> ormDataset = null;
        try {
            ormDataset = ORMStoreFactory.getInstance(Dataset.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ormDataset.getAll();
    }

    public boolean deleteDatasource(String id) {
        ORMStoreInterface<Dataset> ormDataset = null;
        try {
            ormDataset = ORMStoreFactory.getInstance(Dataset.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ormDataset.deleteOne(id);
    }

    public boolean projectContains(String projectId, String id) {
        return projectService.projectContains(projectId, id);
    }

    public List<Dataset> getDatasetsOfProject(String id) {
        // Initializing ORMStoreInterface
        ORMStoreInterface<Dataset> ormDataset = null;
        try {
            ormDataset = ORMStoreFactory.getInstance(Dataset.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Getting a list of dataset IDs associated with the project ID
        List<String> datasetIds = projectService.getDatasetIds(id);

        // Initializing the list to store datasets of the project
        List<Dataset> datasetsOfProject = new ArrayList<>();

        // Retrieving and adding datasets to the list using ORM
        for (String datasetId : datasetIds) {
            datasetsOfProject.add(ormDataset.findById(datasetId));
        }

        // Returning the list of datasets associated with the project
        return datasetsOfProject;
    }








    private Graph hardcodedGraph(String graphName) {
        Set<Triple> triples = new HashSet<>();

        triples.add(new Triple(
                new URI("http://somewhere/cat"),
                new URI("http://www.w3.org/2001/vcard-rdf/3.0#TYPE"),
                new URI("http://www.w3.org/2001/vcard-rdf/3.0#Animal")
        ));
        triples.add(new Triple(
                new URI("http://somewhere/cat"),
                new URI("http://www.w3.org/2001/vcard-rdf/3.0#FN"),
                new URI("tail")
        ));
        triples.add(new Triple(
                new URI("http://somewhere/dog"),
                new URI("http://somewhere/has"),
                new URI("paws")
        ));
        triples.add(new Triple(
                new URI("http://somewhere/bird"),
                new URI("http://somewhere/can"),
                new URI("fly")
        ));
        triples.add(new Triple(
                new URI("http://somewhere/fish"),
                new URI("http://somewhere/lives"),
                new URI("in water")
        ));

        Graph graph = new LocalGraph(new URI(graphName), triples);
        return graph;
    }

    Model hardcodedModel(String name){
        Model model = ModelFactory.createDefaultModel();

        // Crear propiedades y recursos
        Property hasTitle = model.createProperty("https://example.com/hasTitle");
        Resource book = model.createResource("https://example.com/"+name);
        Resource title = model.createResource("https://example.com/Title");

        // Crear declaraciones y agregar al modelo
        Statement stmt1 = model.createStatement(book, RDF.type, RDFS.Class);
        Statement stmt2 = model.createStatement(hasTitle, RDF.type, RDF.Property);
        Statement stmt3 = model.createStatement(book, hasTitle, title);

        model.add(stmt1);
        model.add(stmt2);
        model.add(stmt3);

        return model;
    }
}


