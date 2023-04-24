package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.DataSources.dataset.CsvDataset;
import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.DataSources.dataset.JsonDataset;
import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.LocalGraph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStoreInterface;
import edu.upc.essi.dtim.odin.config.AppConfig;
import edu.upc.essi.dtim.odin.project.ProjectService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;

@Service
public class SourceService {

    private final AppConfig appConfig;

    public SourceService(@Autowired AppConfig appConfig) {
        this.appConfig = appConfig;
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
            Path destinationFile = diskPath.resolve(Paths.get(filename)).normalize().toAbsolutePath();

            // Perform a security check to ensure that the destination file is within the disk path
            if (!destinationFile.getParent().equals(diskPath.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            // Copy the input stream of the multipart file to the destination file
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFile,StandardCopyOption.REPLACE_EXISTING);
            }

            // Return the absolute path of the destination file as a string
            //return destinationFile.toString();

            // Construct a relative path from the disk path and the generated filename
            Path relativePath = diskPath.relativize(destinationFile);
            return relativePath.toString();
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
        System.out.println(extension);
        System.out.println(filePath);

        Dataset dataset;

        // Create a new dataset object with the extracted data
        switch (extension.toLowerCase()){
            case "csv":
                dataset = new CsvDataset(filePath, datasetName, datasetDescription, filePath);
                break;
            case "json":
                dataset = new JsonDataset(filePath, datasetName, datasetDescription, filePath);
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
        String datasetName = dataset.getName();
        if (datasetName == null) datasetName = "DatasetNameIsEmpty";
        try {
            // Try to convert the dataset to a graph
            return dataset.convertToGraph(dataset.getDatasetId(), datasetName, dataset.getPath());
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
        ProjectService projectService = new ProjectService();
        projectService.addLocalGraphToProject(projectId, name);
    }
}


