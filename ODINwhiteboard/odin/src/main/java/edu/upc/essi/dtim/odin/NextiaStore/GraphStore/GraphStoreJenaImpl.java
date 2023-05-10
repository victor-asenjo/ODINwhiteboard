package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;


import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.LocalGraph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.ModelCom;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GraphStoreJenaImpl implements GraphStoreInterface {
    private final Dataset dataset;


    public GraphStoreJenaImpl(@Autowired AppConfig appConfig) {
        String directory = appConfig.getJenaPath();

        //Open TDB Dataset
        dataset = TDBFactory.createDataset(directory);
    }

    /**
     * Saves the given graph.
     *
     * @param graph the graph to save
     */
    @Override
    public void saveGraph(Graph graph) {
        Model modelToSave = adapt(graph);
        dataset.begin(ReadWrite.WRITE);
        try {
            String modelName = graph.getName().getURI();
            dataset.addNamedModel(modelName, modelToSave);
            dataset.commit();
            System.out.println("Graph "+ modelName +" saved successfully");
        } catch (final Exception ex) {
            dataset.abort();
            throw ex;
        }
    }


    /**
     * Deletes the graph with the given name.
     *
     * @param name the URI of the graph to delete
     */
    @Override
    public void deleteGraph(URI name) {
        dataset.begin(ReadWrite.WRITE);
        try {
            String modelName = name.toString();
            if (dataset.containsNamedModel(modelName)) {
                dataset.removeNamedModel(modelName);
            } else {
                throw new IllegalArgumentException("Graph " + name + " not found");
            }
            dataset.commit();
        } catch (final Exception ex) {
            dataset.abort();
            throw ex;
        }
    }

    /**
     * Retrieves the graph with the given name.
     *
     * @param name the URI of the graph to retrieve
     * @return the retrieved graph
     */
    @Override
    public Graph getGraph(URI name) {
        dataset.begin(ReadWrite.READ);
        try {
            //Retrieve Named Graph from Dataset, or use Default Graph.
            String modelName = name.getURI();
            Model model = dataset.getNamedModel(modelName);
            if (model.isEmpty()) {
                throw new IllegalArgumentException("Graph " + name + " is empty");
            } else {
                System.out.println(model);
                return adapt(model, name);
            }
        } finally {
            dataset.end();
        }
    }

    private Graph adapt(Model model, URI name) {
        Set<Triple> triples = new HashSet<>();

        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            triples.add(new Triple(
                    new URI(stmt.getSubject().getURI()),
                    new URI(stmt.getPredicate().getURI()),
                    new URI(stmt.getObject().asResource().getURI())
            ));
        }

        Graph graph = new LocalGraph(name, triples);
        for(Triple t : triples){
            System.out.println();
            System.out.println(t.getSubject().getURI());
            System.out.println(t.getPredicate().getURI());
            System.out.println(t.getObject());
            System.out.println();
        }
        return graph;
    }

    private Model adapt(Graph graph) {
        Model model = ModelFactory.createDefaultModel();
        for (Triple triple : graph.getTriples()) {
            Resource subject = ResourceFactory.createResource(triple.getSubject().getURI());
            Property predicate = ResourceFactory.createProperty(triple.getPredicate().getURI());
            Statement statement = null;
            if (true /*triple.getObject().isURI()*/) {
                Resource object = ResourceFactory.createResource(triple.getObject().toString());
                statement = new StatementImpl(subject, predicate, object);
            } else {
                org.apache.jena.datatypes.RDFDatatype datatype = null;
                if (true /*triple.getObject().getLiteralDatatypeURI() != null*/) {
                    datatype = org.apache.jena.datatypes.TypeMapper.getInstance().getSafeTypeByName(triple.getObject().toString());
                }
                statement = new StatementImpl(subject, predicate, (RDFNode) triple.getObject(), (ModelCom) datatype);
            }
            model.add(statement);
        }
        return model;
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

