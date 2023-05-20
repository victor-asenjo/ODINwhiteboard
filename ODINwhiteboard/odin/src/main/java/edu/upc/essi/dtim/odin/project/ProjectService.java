package edu.upc.essi.dtim.odin.project;

import edu.upc.essi.dtim.NextiaCore.datasources.dataset.Dataset;
import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.RelationalStore.ORMStoreInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    ORMStoreInterface<Project> ormProject;

    public ProjectService() {
        try {
            this.ormProject = ORMStoreFactory.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Adds a local graph to the specified project.
     *
     * @param projectId the ID of the project to add the local graph to
     * @param name the URI of the local graph to add
     * @throws IllegalArgumentException if the project with the given ID is not found
     */
    public void addLocalGraphToProject(String projectId, String name) {

        // Retrieve the project with the given ID
        Project project = findById(projectId);

        // If the project is not found, throw an exception
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        // Add the URI of the local graph to the project's list of local graph IDs
        project.getLocalGraphIDs().add(name);

        //saving the updated project
        saveProject(project);
    }

    public void addDatasetIdToProject(String projectId, Dataset dataset) {
        // Retrieve the project with the given ID
        Project project = findById(projectId);

        // If the project is not found, throw an exception
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        // Add the URI of the local graph to the project's list of local graph IDs
        project.getDatasets().add(dataset);

        //saving the updated project
        saveProject(project);
    }

    public void deleteDatasetFromProject(String projectId, String datasetId) {
        // Retrieve the project with the given ID
        Project project = findById(projectId);

        // If the project is not found, throw an exception
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }
        System.out.println("--------------------------->"+project.getDatasets());

        List<Dataset> datasetsOfProjectToUpload = project.getDatasets();
        for (Dataset datasetInProject : datasetsOfProjectToUpload) {
            if (datasetId.equals(datasetInProject.getDatasetId())) {
                datasetsOfProjectToUpload.remove(datasetInProject);
                project.setDatasets(datasetsOfProjectToUpload);
                break; // Rompemos el bucle después de eliminar el objeto
            }
        }
        System.out.println("--------------------------->"+project.getDatasets().toString());

        //saving the updated project
        saveProject(project);
    }

    public Project saveProject(Project project) {
        return ormProject.save(project);
    }

    public Project findById(String projectId) {
        return ormProject.findById(Project.class, projectId);
    }

    public List<Project> getAllProjects() {
        return ormProject.getAll(Project.class);
    }

    public boolean deleteProject(String id) {
        return ormProject.deleteOne(id);
    }

    public boolean deleteAllProjects() {
        return ormProject.deleteAll(Project.class);
    }

    public boolean projectContains(String projectId, String datasetId) {
        Project project = ormProject.findById(Project.class, projectId);
        for (Dataset datasetInProject : project.getDatasets()) {
            String comp = datasetInProject.getDatasetId();
            System.out.println("------------------>" + comp);
            System.out.println("------------------>" + datasetId);
            if(datasetId.equals(comp)) return true;
        }
        return false;
    }

    public List<Dataset> getDatasetsOfProject(String id) {
        Project project = ormProject.findById(Project.class, id);
        return project.getDatasets();
    }
}

