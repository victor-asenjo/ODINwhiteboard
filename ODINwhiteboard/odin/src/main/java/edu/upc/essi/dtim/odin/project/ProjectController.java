package edu.upc.essi.dtim.odin.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    private final ProjectService projectService;
    ProjectController(@Autowired ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    public Project saveProject(@RequestBody Project project)
    {
        return projectService.saveProject(project);
    }

    @GetMapping("/projects/{id}")
    public Project getProject(@PathVariable String id)
    {
        return projectService.findById(id);
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects()
    {
        return projectService.getAllProjects();
    }

    @PostMapping("/deleteProjects")
    public boolean deleteProject(@RequestParam String id){
        return projectService.deleteProject(id);
    }

    @DeleteMapping("projects/delete")
    public boolean deleteAllProjects(){
        return projectService.deleteAllProjects();
    }

}
