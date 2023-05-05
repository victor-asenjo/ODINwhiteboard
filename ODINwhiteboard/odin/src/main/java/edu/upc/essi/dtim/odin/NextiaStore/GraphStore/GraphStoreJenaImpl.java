package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;


import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.LocalGraph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GraphStoreJenaImpl implements GraphStoreInterface {
    private final Dataset dataset;


    public GraphStoreJenaImpl(AppConfig appConfig) {
        String directory = appConfig.getJenaPath();
        dataset = TDB2Factory.connectDataset(directory);
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
            Model model = dataset.getNamedModel(modelName);
            if (model.isEmpty()) {
                model.add(modelToSave);
                dataset.addNamedModel(modelName, model);
            } else {
                model.add(modelToSave);
            }
            dataset.commit();
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
            String modelName = name.getURI();
            Model model = dataset.getNamedModel(modelName);
            if (model.isEmpty()) {
                throw new IllegalArgumentException("Graph " + name + " is empty");
            } else {
                return adapt(model);
            }
        } finally {
            dataset.end();
        }
    }

    public static Graph adapt(Model model) {
        Set<Triple> triples = new HashSet<>();
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();
            if (object.isLiteral()) {
                Literal literal = object.asLiteral();
                RDFDatatype datatype = literal.getDatatype();
                if (datatype == null) {
                    datatype = XSDDatatype.XSDstring;
                }
                triples.add(new Triple(new URI(subject.getURI()), new URI(predicate.getURI()), new URI(object.asResource().getURI())));
            } else {
                triples.add(new Triple(new URI(subject.getURI()), new URI(predicate.getURI()), new URI(object.asResource().getURI())));
            }
        }
        return new LocalGraph(new URI(model.getGraph().toString()), triples);
    }

    private Model adapt(Graph graph) {
        Set<Triple> triples = graph.getTriples();
        Model model = ModelFactory.createDefaultModel();
        String graphUri = graph.getName().toString();
        model.setNsPrefix("", graphUri + "#"); // set default namespace
        for (Triple triple : triples) {
            Resource subject = ResourceFactory.createResource(triple.getSubject().getURI());
            Property predicate = ResourceFactory.createProperty(triple.getPredicate().getURI());
            RDFNode object;
            if (triple.hasLiteralObject()) {
                object = ResourceFactory.createPlainLiteral(triple.getObject().toString().replace(" ",""));
            } else {
                object = ResourceFactory.createResource(triple.getObject().toString());
            }
            Statement statement = ResourceFactory.createStatement(subject, predicate, object);
            model.add(statement);
        }
        System.out.println("++++++++++++++++  "+model.getGraph().getPrefixMapping()+" ---------------------------");
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

