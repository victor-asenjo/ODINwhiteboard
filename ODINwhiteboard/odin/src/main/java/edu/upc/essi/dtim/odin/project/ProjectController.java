package edu.upc.essi.dtim.odin.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class ProjectController {
    private ProjectService projectService;

    private ProjectController( @Autowired ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        System.out.println("--------------------POST " + project);
        project.setProjectId(UUID.randomUUID().toString());
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.created(URI.create("/projects/" + createdProject.getProjectId()))
                .body(createdProject);
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @DeleteMapping("/projects/delete")
    public boolean deleteAllProjects()
    {
        return projectService.deleteAllProjects();
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable String projectId) {
        Project project = projectService.findById(projectId);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
