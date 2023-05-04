package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.odin.project.Project;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

public class ProjectEntityAdapter {

    public Project adapt(ProjectEntity entity) {
        Project projectAdapted = new Project(
                entity.getProjectId(),
                entity.getProjectName(),
                entity.getProjectDescription(), entity.getProjectPrivacy(),
                entity.getProjectColor(), entity.getCreatedBy(),
                entity.getLocalGraphIDs());
        return projectAdapted;
    }

    public ProjectEntity adapt(Project project) {
        ProjectEntity entity = new ProjectEntity(
                project.getProjectId(),
                project.getProjectName(),
                project.getProjectDescription(),
                project.getProjectPrivacy(),
                project.getProjectColor(),
                project.getCreatedBy(),
                project.getLocalGraphIDs());
        return entity;
    }

    Model hardcodedModel(String name){
        Model model = ModelFactory.createDefaultModel();

        // Crear propiedades y recursos
        Property hasTitle = model.createProperty("https://example.com/hasTitle");
        Resource book = model.createResource("https://example.com/"+name);
        Resource title = model.createResource("https://example.com/Title");

        // Crear declaraciones y agregar al modelo
        Statement stmt1 = model.createStatement(book, RDF.type, RDFS.Class);
        Statement stmt2 = model.createStatement(hasTitle, RDF.type, RDF.Property);
        Statement stmt3 = model.createStatement(book, hasTitle, title);

        model.add(stmt1);
        model.add(stmt2);
        model.add(stmt3);

        return model;
    }
}
