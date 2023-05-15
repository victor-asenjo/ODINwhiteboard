package edu.upc.essi.dtim.odin.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    private final ProjectService projectService;
    ProjectController(@Autowired ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        System.out.println("################### POST PROJECT RECEIVED ################### ");
        Project savedProject = projectService.saveProject(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") String id) {
        System.out.println("################### GET PROJECT RECEIVED ################### " + id);
        Project project = projectService.findById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        System.out.println("################### GET ALL PROJECTS RECEIVED ################### ");
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable("id") String id) {
        // Print a message to indicate that the delete request was received
        System.out.println("################### DELETE A PROJECT RECEIVED ################### " + id);

        // Call the projectService to delete the project and get the result
        boolean deleted = projectService.deleteProject(id);

        // Check if the project was deleted successfully
        if (deleted) {
            // Return a ResponseEntity with HTTP status 200 (OK) and the boolean value true
            return ResponseEntity.ok(true);
        } else {
            // Return a ResponseEntity with HTTP status 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/projects/delete")
    public ResponseEntity<Boolean> deleteAllProjects() {
        boolean deleted = projectService.deleteAllProjects();
        if (deleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
