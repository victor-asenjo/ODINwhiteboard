package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.DataSources.dataset.CsvDataset;
import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.DataSources.dataset.JsonDataset;
import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.LocalGraph;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;

class SourceServiceTest {
    @Mock
    private AppConfig appConfig;

    private final String jsonTestPath = "..\\ODINwhiteboard\\odin\\src\\test\\resources\\csvTestFile.json";


    @InjectMocks
    private SourceService sourceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(appConfig.getDiskPath()).thenReturn("src/test/resources");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testReconstructFile() throws IOException {
        // Create a test file
        Path filePath = Paths.get("src/test/resources/test.csv");
        String csvContent = "Name, Age, Gender\nJohn, 30, Male\nJane, 25, Female\n";
        Files.write(filePath, csvContent.getBytes());
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", Files.readAllBytes(filePath));

        // Call the method being tested
        String storedFilePath = sourceService.reconstructFile(file);
        System.out.println(storedFilePath);

        // Verify that the file was stored in the expected location
        Path storedFilePathAsPathType = Path.of(storedFilePath);
        Assertions.assertTrue(Files.exists(storedFilePathAsPathType));

        // Verify that the stored file path is correct

        Assertions.assertTrue(storedFilePath.replace("\\", "/").startsWith("src/test/resources"));
        Assertions.assertTrue(storedFilePath.endsWith("_test.csv"));

        // Clean up the test file
        Files.deleteIfExists(filePath);
        Files.deleteIfExists(storedFilePathAsPathType);
    }

    @Test
    void testExtractData_csv() {
        // Prepare the input data
        String filePath = "..\\ODINwhiteboard\\odin\\src\\test\\resources\\csvTestFile.csv";
        String datasetName = "test csv dataset";
        String datasetDescription = "test csv description";

        // Call the method under test
        Dataset dataset = sourceService.extractData(filePath, datasetName, datasetDescription);

        // Verify that the dataset object was created correctly
        Assertions.assertNotNull(dataset);
        Assertions.assertTrue(dataset instanceof CsvDataset);
        Assertions.assertEquals(filePath, dataset.getPath());
        Assertions.assertEquals(datasetName, dataset.getName());
        Assertions.assertEquals(datasetDescription, dataset.getDescription());
    }

    @Test
    void testExtractData_json() {
        // Prepare the input data
        String filePath = jsonTestPath;
        String datasetName = "test json dataset";
        String datasetDescription = "test json description";

        // Call the method under test
        Dataset dataset = sourceService.extractData(filePath, datasetName, datasetDescription);

        // Verify that the dataset object was created correctly
        Assertions.assertNotNull(dataset);
        Assertions.assertTrue(dataset instanceof JsonDataset);
        Assertions.assertEquals(filePath, dataset.getPath());
        Assertions.assertEquals(datasetName, dataset.getName());
        System.out.println(datasetDescription);
        System.out.println(dataset.getDescription());
        Assertions.assertEquals(datasetDescription, dataset.getDescription());
    }

    @Test
    public void testTransformToGraphCsv() {
        //Dataset csvDataset = new CsvDataset("id", "name", "description", csvTestPath);

        //TODO: check core implementation
        /*
        Graph graph = sourceService.transformToGraph(csvDataset);
        Assertions.assertNotNull(graph);
        Assertions.assertTrue(graph instanceof LocalGraph);
        Assertions.assertEquals("name", graph.getName().getURI());
        Assertions.assertEquals(4, graph.getTriples().size());
        */
    }

    @Test
    public void testTransformToGraphJson() throws IOException {
        Dataset jsonDataset = new JsonDataset("id", "name", "description", jsonTestPath);
        Graph graph = sourceService.transformToGraph(jsonDataset);
        //Assertions.assertNotNull(graph);
        Assertions.assertTrue(graph instanceof LocalGraph);
        //Assertions.assertEquals("name", graph.getName().getURI());
//        Assertions.assertEquals(3, graph.getTriples().size());
    }
}