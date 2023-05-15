package edu.upc.essi.dtim.odin.project;

import java.util.List;

public class Project {

    // Unique identifier for the project
    private String projectId;

    // Name of the project
    private String projectName;

    // Description of the project
    private String projectDescription;

    // Privacy level of the project (e.g. private, public)
    private String projectPrivacy;

    // Color associated with the project
    private String projectColor;

    // Username of the user who created the project
    private String createdBy;

    // List of local graph IDs associated with the project
    private List<String> localGraphIDs;

    // List of dataset IDs associated with the project
    private List<String> datasetIDs;

    public List<String> getDatasetIDs() {
        return datasetIDs;
    }

    public void setDatasetIDs(List<String> datasetIDs) {
        this.datasetIDs = datasetIDs;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectPrivacy() {
        return projectPrivacy;
    }

    public void setProjectPrivacy(String projectPrivacy) {
        this.projectPrivacy = projectPrivacy;
    }

    public String getProjectColor() {
        return projectColor;
    }

    public void setProjectColor(String projectColor) {
        this.projectColor = projectColor;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getLocalGraphIDs() {
        return localGraphIDs;
    }

    public void setLocalGraphIDs(List<String> localGraphIDs) {
        this.localGraphIDs = localGraphIDs;
    }
}
