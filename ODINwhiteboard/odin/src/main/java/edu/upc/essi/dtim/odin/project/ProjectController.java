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
        System.out.println("POST PROJECT RECEIVED");
        System.out.println(project);
        Project savedProject = projectService.saveProject(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable String id) {
        Project project = projectService.findById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/deleteProjects")
    public ResponseEntity<Boolean> deleteProject(@RequestParam String id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ResponseEntity.ok(true);
        } else {
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
