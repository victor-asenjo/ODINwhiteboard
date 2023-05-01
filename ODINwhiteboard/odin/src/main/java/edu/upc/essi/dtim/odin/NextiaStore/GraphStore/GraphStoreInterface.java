package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;

import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.Queries.Query;
import edu.upc.essi.dtim.Queries.QueryResult;

import java.util.Set;

public interface GraphStoreInterface {
    /**
     * Retrieves all triples with the given subject.
     *
     * @param subject the URI of the subject to retrieve triples for
     * @param graph the graph to retrieve triples from
     */
    Set<Triple> getTriplesWithSubject(URI subject, Graph graph);

    /**
     * Retrieves all triples with the given predicate.
     *
     * @param predicate the URI of the predicate to retrieve triples for
     * @param graph the graph to retrieve triples from
     */
    Set<Triple> getTriplesWithPredicate(URI predicate, Graph graph);

    /**
     * Retrieves all triples with the given object.
     *
     * @param object the URI of the object to retrieve triples for
     * @param graph the graph to retrieve triples from
     */
    Set<Triple> getTriplesWithObject(URI object, Graph graph);

    /**
     * Retrieves all triples in the given graph.
     *
     * @param graph the graph to retrieve triples from
     */
    Set<Triple> getAllTriples(Graph graph);

    /**
     * Retrieves all subjects in the given graph.
     *
     * @param graph the graph to retrieve subjects from
     */
    Set<URI> getSubjects(Graph graph);

    /**
     * Retrieves all predicates in the given graph.
     *
     * @param graph the graph to retrieve predicates from
     */
    Set<URI> getPredicates(Graph graph);

    /**
     * Retrieves all objects in the given graph.
     *
     * @param graph the graph to retrieve objects from
     */
    Set<URI> getObjects(Graph graph);

    /**
     * Retrieves the graph with the given name.
     *
     * @param name the URI of the graph to retrieve
     */
    Graph getGraph(URI name);

    /**
     * Saves the given graph.
     *
     * @param graph the graph to save
     */
    void saveGraph(Graph graph);

    /**
     * Deletes the graph with the given name.
     *
     * @param name the URI of the graph to delete
     */
    void deleteGraph(URI name);

    /**
     * Executes the given SPARQL query on the given graph and returns the results.
     *
     * @param query the SPARQL query to execute
     * @param graph the graph to execute the query on
     */
    QueryResult query(Query query, Graph graph);
}
