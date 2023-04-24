package edu.upc.essi.dtim.odin.bootstrapping;

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

        // Verify that the stored file path is correct
        Assertions.assertTrue(storedFilePath.startsWith("C:\\Users\\victo\\Documents\\GitHub\\ODINwhiteboard\\ODINnewWhiteboard\\odin\\"+"src\\test\\resources\\"));
        Assertions.assertTrue(storedFilePath.endsWith("_test.csv"));

        // Clean up the test file
        Files.deleteIfExists(filePath);
    }

    @Test
    void extractData() {

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