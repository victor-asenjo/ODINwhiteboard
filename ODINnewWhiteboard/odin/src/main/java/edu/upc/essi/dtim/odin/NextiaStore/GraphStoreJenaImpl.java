package edu.upc.essi.dtim.odin.NextiaStore;

import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.LocalGraph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.Queries.Query;
import edu.upc.essi.dtim.Queries.QueryResult;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDB;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class GraphStoreJenaImpl implements GraphStoreInterface {
    // Function to convert Jena Model to Graph object
    public static Graph convertJenaModelToGraph(Model jenaModel) {
        Graph graph = null;

        // Get the default graph name from Jena Model
        URI graphName = new URI(jenaModel.getGraph().toString());

        // Create a set to store triples in the Graph
        Set<Triple> triples = new HashSet<>();

        // Iterate through the statements in Jena Model
        ExtendedIterator<Statement> stmtIter = jenaModel.listStatements();
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
        graph = new LocalGraph(graphName, triples);

        return graph;
    }

    public static Model convertToJenaModel(Graph graph) {
        Model model = ModelFactory.createDefaultModel();

        return model;
    }

    /**
     * Retrieves all triples with the given subject.
     *
     * @param subject the URI of the subject to retrieve triples for
     * @param graph   the graph to retrieve triples from
     * @return a set of triples that have the given subject
     */
    @Override
    public Set<Triple> getTriplesWithSubject(URI subject, Graph graph) {
        Objects.requireNonNull(subject, "subject cannot be null");
        Objects.requireNonNull(graph, "graph cannot be null");

        // create an empty set of Triples
        Set<Triple> triples = new HashSet<>();
        // translate our graph into Jena's model
        Model model = convertToJenaModel(graph);
        // get an iterator over all resources with the given property
        ResIterator iter = model.listSubjectsWithProperty(
                ResourceFactory.createProperty(subject.toString()));
        // iterate over all the resources and their statements to get the Triples
        while (iter.hasNext()) {
            Resource res = iter.nextResource();
            StmtIterator stmtIter = res.listProperties();
            while (stmtIter.hasNext()) {
                Statement stmt = stmtIter.nextStatement();
                URI predicate = new URI(stmt.getPredicate().getURI());
                URI objectUri = new URI(stmt.getObject().asResource().getURI());
                Triple triple = new Triple(subject, predicate, objectUri);
                triples.add(triple);
            }
        }
        // return the set of Triples that have the given subject
        return triples;
    }

    /**
     * Retrieves all triples with the given predicate.
     *
     * @param predicate the URI of the predicate to retrieve triples for
     * @param graph     the graph to retrieve triples from
     * @return a set of triples that have the given predicate
     */
    @Override
    public Set<Triple> getTriplesWithPredicate(URI predicate, Graph graph) {
        Objects.requireNonNull(predicate, "predicate cannot be null");
        Objects.requireNonNull(graph, "graph cannot be null");

        Set<Triple> triples = new HashSet<>();
        Model model = convertToJenaModel(graph);
        StmtIterator iter = model.listStatements(
                null,
                ResourceFactory.createProperty(predicate.toString()),
                (RDFNode) null
        );
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            URI subject = new URI(stmt.getSubject().getURI());
            URI predicateUri = new URI(stmt.getPredicate().getURI());
            URI objectUri = new URI(stmt.getObject().asResource().getURI());
            Triple triple = new Triple(subject, predicateUri, objectUri);
            triples.add(triple);
        }
        return triples;
    }

    /**
     * Retrieves all triples with the given object.
     *
     * @param object the URI of the object to retrieve triples for
     * @param graph  the graph to retrieve triples from
     * @return a set of triples that have the given object
     */
    @Override
    public Set<Triple> getTriplesWithObject(URI object, Graph graph) {
        Objects.requireNonNull(object, "object cannot be null");
        Objects.requireNonNull(graph, "graph cannot be null");

        Set<Triple> triples = new HashSet<>();
        Model model = convertToJenaModel(graph);
        ResIterator iter = model.listResourcesWithProperty(
                ResourceFactory.createProperty(object.toString()));
        while (iter.hasNext()) {
            Resource res = iter.nextResource();
            StmtIterator stmtIter = res.listProperties();
            while (stmtIter.hasNext()) {
                Statement stmt = stmtIter.nextStatement();
                URI subject = new URI(stmt.getSubject().getURI());
                URI predicate = new URI(stmt.getPredicate().getURI());
                Triple triple = new Triple(subject, predicate, object);
                triples.add(triple);
            }
        }
        return triples;
    }

    /**
     * Retrieves all triples in the given graph.
     *
     * @param graph the graph to retrieve triples from
     */
    @Override
    public Set<Triple> getAllTriples(Graph graph) {
        return null;
    }

    /**
     * Retrieves all subjects in the given graph.
     *
     * @param graph the graph to retrieve subjects from
     */
    @Override
    public Set<URI> getSubjects(Graph graph) {
        return null;
    }

    /**
     * Retrieves all predicates in the given graph.
     *
     * @param graph the graph to retrieve predicates from
     */
    @Override
    public Set<URI> getPredicates(Graph graph) {
        return null;
    }

    /**
     * Retrieves all objects in the given graph.
     *
     * @param graph the graph to retrieve objects from
     */
    @Override
    public Set<URI> getObjects(Graph graph) {
        return null;
    }

    /**
     * Retrieves the graph with the given name.
     *
     * @param name the URI of the graph to retrieve
     */
    @Override
    public Graph getGraph(URI name) {
        // Create a Jena model for the given graph URI
        String graphURI = name.toString();
        Model model = ModelFactory.createDefaultModel();
        model.read(graphURI);

        // Convert Jena triples to our own Triple objects
        Set<Triple> triples = new HashSet<>();
        URI subject;
        URI predicate;
        URI object;
        StmtIterator stmtIterator = model.listStatements();
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.next();
            subject = new URI(stmt.getSubject().getURI());
            predicate = new URI(stmt.getPredicate().getURI());
            object = new URI(stmt.getObject().toString());
            Triple triple = new Triple(subject, predicate, object);
            triples.add(triple);
        }
        // Return a new Graph object containing the retrieved triples
        return new Graph(name, triples);
    }

    /**
     * Saves the given graph.
     *
     * @param graph the graph to save
     */
    @Override
    public void saveGraph(Graph graph) {
        // Create a new Jena Model object
        Model model = ModelFactory.createDefaultModel();

        // Add each triple from the Graph object to the Model object
        for (Triple triple : graph.getTriples()) {
            // create a Jena Resource object for the subject of the triple
            Resource subject = ResourceFactory.createResource(triple.getSubject().getURI());
            // create a Jena Property object for the predicate of the triple
            Property predicate = ResourceFactory.createProperty(triple.getPredicate().getURI());
            // create a Jena RDFNode object for the object of the triple
            RDFNode object;
            // check if the object of the triple is a literal or a resource
            if (triple.hasLiteralObject()) {
                // create a typed literal for the object of the triple if it is a literal
                object = ResourceFactory.createTypedLiteral(triple.getLiteralObjectValue(),
                        TypeMapper.getInstance().getSafeTypeByName(triple.getLiteralObjectDatatypeURI()));
            } else {
                // create a Jena Resource object for the object of the triple if it is a resource
                object = ResourceFactory.createResource(triple.getObject().toString());
            }
            // create a Jena statement from the subject, predicate, and object of the triple
            Statement statement = ResourceFactory.createStatement(subject, predicate, object);
            // add the statement to the Jena model object
            model.add(statement);
        }
        /*
        // Get a new Jena TDB Dataset object
        Dataset dataset = TDBFactory.createDataset("path/to/tdb");

        // Begin a new transaction
        dataset.begin(ReadWrite.WRITE);

        // Get a new Jena Model object from the Dataset object
        Model tdbModel = dataset.getDefaultModel();

        // Add the Model object to the TDB model
        tdbModel.add(model);

        // Commit the transaction and end it
        dataset.commit();
        dataset.end();
        */

    }

    /**
     * Deletes the graph with the given name.
     *
     * @param name the URI of the graph to delete
     */
    @Override
    public void deleteGraph(URI name) {

    }

    /**
     * Executes the given SPARQL query on the given graph and returns the results.
     *
     * @param query the SPARQL query to execute
     * @param graph the graph to execute the query on
     */
    @Override
    public QueryResult query(Query query, Graph graph) {
        try (
                QueryExecution qExec = QueryExecutionFactory.create(query.getQueryText(), convertToJenaModel(graph))
        ) {
            qExec.getContext().set(TDB.symUnionDefaultGraph, true);
            return (QueryResult) ResultSetFactory.copyResults(qExec.execSelect());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
