package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.DataSources.dataset.CsvDataset;
import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.DataSources.dataset.JsonDataset;
import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStoreInterface;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;

class SourceServiceTest {
    @Mock
    private AppConfig appConfig;

    @Mock
    private GraphStoreInterface graphStore;

    @InjectMocks
    private SourceService sourceService;

    @Captor
    private ArgumentCaptor<Graph> graphCaptor;

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

        // Verify that the file was stored in the expected location
        Assertions.assertTrue(Files.exists(Path.of(storedFilePath)));

        // Verify that the stored file path is correct
        Assertions.assertTrue(storedFilePath.startsWith("C:\\Users\\victo\\Documents\\GitHub\\ODINwhiteboard\\ODINnewWhiteboard\\odin\\"+"src\\test\\resources\\"));
        Assertions.assertTrue(storedFilePath.endsWith("_test.csv"));

        // Clean up the test file
        Files.deleteIfExists(filePath);
    }

    @Test
    void testExtractData_csv() {
        // Prepare the input data
        String filePath = "ODINnewWhiteboard/odin/src/test/resources/csvTestFile.csv";
        String datasetName = "test csv dataset";
        String datasetDescription = "test csv description";

        // Call the method under test
        Dataset dataset = sourceService.extractData(filePath, datasetName, datasetDescription);

        // Verify that the dataset object was created correctly
        Assertions.assertNotNull(dataset);
        Assertions.assertTrue(dataset instanceof CsvDataset);
        //Assertions.assertEquals(filePath, dataset.getPath());
        Assertions.assertEquals(datasetName, dataset.getName());
        Assertions.assertEquals(datasetDescription, dataset.getDescription());
    }

    @Test
    void testExtractData_json() {
        // Prepare the input data
        String filePath = "ODINnewWhiteboard/odin/src/test/resources/jsonTestFile.json";
        String datasetName = "test json dataset";
        String datasetDescription = "test json description";

        // Call the method under test
        Dataset dataset = sourceService.extractData(filePath, datasetName, datasetDescription);

        // Verify that the dataset object was created correctly
        Assertions.assertNotNull(dataset);
        Assertions.assertTrue(dataset instanceof JsonDataset);
        //Assertions.assertEquals(filePath, dataset.getPath());
        Assertions.assertEquals(datasetName, dataset.getName());
        Assertions.assertEquals(datasetDescription, dataset.getDescription());
    }

    @Test
    void transformToGraph() {
    }

    @Test
    void saveGraphToDatabase() {
    }

    @Test
    void addLocalGraphToProject() {
    }
}