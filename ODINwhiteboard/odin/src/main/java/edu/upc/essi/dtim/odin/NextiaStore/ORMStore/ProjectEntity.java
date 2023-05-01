package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.odin.project.Project;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
public class ProjectEntity extends Project {
    // Unique identifier for the project
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "project_id", unique = true, nullable = false)
    private String projectId;


    // Name of the project
    @Column(name = "project_name")
    private String projectName;

    // Description of the project
    @Column(name = "project_description")
    private String projectDescription;

    // Privacy level of the project (e.g. private, public)
    @Column(name = "project_privacy")
    private String projectPrivacy;

    // Color associated with the project (not sure what this is used for)
    @Column(name = "project_color")
    private String projectColor;

    // Username of the user who created the project
    @Column(name = "created_by")
    private String createdBy;

    // List of local graph IDs associated with the project (not sure what this is used for)
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    @CollectionTable(name = "local_graph_ids", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "local_graph_id")
    private List<String> localGraphIDs;

    public ProjectEntity(){
        super();
    }

    public ProjectEntity(String projectId, String projectName, String projectDescription, String projectPrivacy, String projectColor, String createdBy, List<String> localGraphIDs) {
        super(projectId, projectName, projectDescription, projectPrivacy, projectColor, createdBy, localGraphIDs);
        if(projectId != null) this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectPrivacy = projectPrivacy;
        this.projectColor = projectColor;
        this.createdBy = createdBy;
        this.localGraphIDs = localGraphIDs;
    }
}

