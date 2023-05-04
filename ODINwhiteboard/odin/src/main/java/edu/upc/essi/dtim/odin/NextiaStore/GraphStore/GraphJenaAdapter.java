package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;

import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.LocalGraph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;


import java.util.HashSet;
import java.util.Set;

public class GraphJenaAdapter {

    public Graph adapt(Model model) {
        Graph graphAdapted = null;

        // Get the default graph name from Jena Model
        URI graphName = new URI(model.getGraph().toString());

        // Create a set to store triples in the Graph
        Set<Triple> triples = new HashSet<>();

        // Iterate through the statements in Jena Model
        ExtendedIterator<Statement> stmtIter = model.listStatements();
        while (stmtIter.hasNext()) {
            Statement stmt = stmtIter.next();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();
            Triple triple = new Triple(new URI(subject.toString()), new URI(predicate.toString()), new URI(object.toString()));
            triples.add(triple);
        }
        // Create a new Graph object with the name and triples
        //todo: review abstract class
        graphAdapted = new LocalGraph(graphName, triples);
        return graphAdapted;
    }

    public Model adapt(Graph graph) {
        Model modelAdapted = hardcodedModel(graph.getName().getURI());
        return modelAdapted;
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

