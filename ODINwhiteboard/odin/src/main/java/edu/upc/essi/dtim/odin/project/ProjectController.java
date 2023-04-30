package edu.upc.essi.dtim.odin.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {
    private ProjectService projectService;

    private ProjectController( @Autowired ProjectService projectService) {
        this.projectService = projectService;
        // Being private prevents the factory from being instantiated from outside the class
    }


}
