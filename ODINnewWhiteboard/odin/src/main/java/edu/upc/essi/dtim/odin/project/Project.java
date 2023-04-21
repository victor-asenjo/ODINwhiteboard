package edu.upc.essi.dtim.odin.project;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    // Constructor for the Project class
    public Project(){
        // Generate a unique ID for the project using UUID
        this.projectId = UUID.randomUUID().toString().replace("-", "");

        // Set the default privacy level to "private"
        this.projectPrivacy = "private";

        // Set the default project description to an empty string
        this.projectDescription = "";

        // Initialize the list of localGraphs empty by default
        this.localGraphIDs = new ArrayList<String>();
    }
}
