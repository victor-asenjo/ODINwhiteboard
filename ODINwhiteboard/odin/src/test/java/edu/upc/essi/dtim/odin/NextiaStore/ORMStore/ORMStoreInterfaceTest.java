package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.DataSources.dataset.CsvDataset;
import edu.upc.essi.dtim.DataSources.dataset.Dataset;
import edu.upc.essi.dtim.odin.project.Project;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ORMStoreInterfaceTest {

    private static ORMDatasetImplementation datasetStore;
    private static ORMProjectImplementation projectStore;

    @BeforeAll
    public static void setUp() {
        datasetStore = new ORMDatasetImplementation();
        projectStore = new ORMProjectImplementation();
    }

    @AfterAll
    public static void tearDown() {
        datasetStore.deleteAll();
        projectStore.deleteAll();
    }

    @Test
    void testSaveAndGetAll() {
        // Create a new project and save it
        Project project = new Project();
        project.setProjectName("testProject");
        projectStore.save(project);

        // Check that the project was saved successfully
        List<Project> allProjects = projectStore.getAll();
        assertEquals(1, allProjects.size());
        assertEquals("testProject", allProjects.get(0).getProjectName());

        // Create a new dataset and save it
        Dataset dataset = new CsvDataset(null, "testDataset", "testDatasetDescription", "testFile.csv");
        datasetStore.save(dataset);

        // Check that the dataset was saved successfully
        List<Dataset> allDatasets = datasetStore.getAll();
        assertEquals(1, allDatasets.size());
        assertEquals("testDataset", allDatasets.get(0).getName());
        assertEquals("testDatasetDescription", allDatasets.get(0).getDescription());
    }

    @Test
    void testFindById() {
        // Create a new project and save it
        Project project = new Project();
        project.setProjectName("testProject");
        Project savedProject = projectStore.save(project);

        // Retrieve the project by ID and check that it matches the saved project
        Project retrievedProject = projectStore.findById(savedProject.getProjectId());
        assertNotNull(retrievedProject);
        assertEquals(savedProject.getProjectId(), retrievedProject.getProjectId());
        assertEquals("testProject", retrievedProject.getProjectName());
    }

    @Test
    void testDeleteOne() {
        // Create a new project and save it
        Project project = new Project();
        project.setProjectName("testProject");
        Project savedProject = projectStore.save(project);

        // Delete the project and check that it was deleted successfully
        assertTrue(projectStore.deleteOne(savedProject.getProjectId()));
        assertNull(projectStore.findById(savedProject.getProjectId()));
    }

    @Test
    void testDeleteAll() {
        // Create some projects and save them
        Project project1 = new Project();
        project1.setProjectName("testProject1");
        Project savedProject1 = projectStore.save(project1);

        Project project2 = new Project();
        project2.setProjectName("testProject2");
        Project savedProject2 = projectStore.save(project2);

        // Delete all projects and check that they were deleted successfully
        assertTrue(projectStore.deleteAll());
        List<Project> allProjects = projectStore.getAll();
        assertEquals(0, allProjects.size());
    }
}