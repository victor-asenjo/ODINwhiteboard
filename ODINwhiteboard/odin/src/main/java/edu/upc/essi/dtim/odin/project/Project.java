package edu.upc.essi.dtim.odin.project;

import lombok.Data;

import java.util.List;

@Data
public class Project {

    // Unique identifier for the project
    private String projectId;

    // Name of the project
    private String projectName;

    // Description of the project
    private String projectDescription;

    // Privacy level of the project (e.g. private, public)
    private String projectPrivacy;

    // Color associated with the project (not sure what this is used for)
    private String projectColor;

    // Username of the user who created the project
    private String createdBy;

    // List of local graph IDs associated with the project (not sure what this is used for)
    private List<String> localGraphIDs;


    // Constructor for the Project class with id
    public Project(String projectName, String projectDescription, String projectPrivacy,
                   String projectColor, String createdBy, List<String> localGraphIDs) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectPrivacy = projectPrivacy;
        this.projectColor = projectColor;
        this.createdBy = createdBy;
        this.localGraphIDs = localGraphIDs;
    }

    public Project(){}
}
