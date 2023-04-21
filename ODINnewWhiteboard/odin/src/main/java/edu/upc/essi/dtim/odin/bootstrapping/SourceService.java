package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.DataSources.dataset.CsvDataset;
import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.DataSources.dataset.JsonDataset;
import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.LocalGraph;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStoreInterface;
import edu.upc.essi.dtim.odin.project.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Service
public class SourceService {
    /**
     * Recibe un MultipartFile y retorna la ruta donde se ha guardado el archivo.
     * @param multipartFile El archivo multipart a reconstruir
     * @return La ruta donde se ha guardado el archivo
     * @throws IOException Si ocurre un error durante la escritura del archivo
     */
    public String reconstructFile(MultipartFile multipartFile) throws IOException {
        // Generate a unique file name using a UUID
        String fileName = UUID.randomUUID().toString() + "." + multipartFile.getOriginalFilename();
        System.out.println(fileName);
        // Create a File object with the generated file name in the upload directory
        File file = new File( "/" + fileName);

        // Write the contents of the MultipartFile to the File
        multipartFile.transferTo(file);

        // Return the absolute path of the uploaded file
        return file.getAbsolutePath();
    }


    /**
     * Recibe una ruta de archivo, lee los metadatos y retorna un objeto Dataset con los datos extraídos del archivo.
     * @param filePath La ruta del archivo del que se extraerán los datos
     * @return Un objeto Dataset con los datos extraídos del archivo
     */
    public Dataset extractData(String filePath) {
        // Extract the extension of the file from the file path
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        System.out.println(extension);

        Dataset dataset = null;

        try {
            // Read the metadata from the file
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            Map<String, String> metadata = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    metadata.put(parts[0].trim(), parts[1].trim());
                }
            }
            reader.close();

            // Create a new dataset object with the extracted data
            if (extension.equals("csv")) {
                String id = metadata.get("id");
                String name = metadata.get("name");
                String delimiter = metadata.get("delimiter");
                dataset = new CsvDataset(id, name, filePath, delimiter);
            } else if (extension.equals("json")) {
                String id = metadata.get("id");
                String name = metadata.get("name");
                dataset = new JsonDataset(id, name, filePath);
            } else {
                throw new IllegalArgumentException("Unsupported file format: " + extension);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    /**
     * Recibe un objeto Dataset y retorna un objeto Graph que representa los datos del Dataset transformados a RDF.
     * @param dataset Un objeto Dataset con los datos a transformar
     * @return Un objeto Graph con los datos transformados a RDF
     */
    public Graph transformToGraph(Dataset dataset) throws IOException {
        // Create a new Graph object to store the resulting RDF graph
        Graph graph = new LocalGraph(new URI(dataset.getName()), new HashSet<>());
        //graph = dataset.convertToGraph(dataset.getDatasetId(),dataset.getName(), dataset.getPath() );

        // Return the resulting RDF graph
        return graph;
    }

    /**
     * Recibe un objeto Dataset y retorna un objeto Graph que representa los datos del Dataset transformados a RDF.
     * @param dataset Un objeto Dataset con los datos a transformar
     * @return Un objeto Graph con los datos transformados a RDF
     */
    public Graph readMetadata(Dataset dataset) {
        // TODO: Implementar la lógica para leer los metadatos del objeto Dataset y construir
        // un objeto Graph que los represente.
        return null;
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


