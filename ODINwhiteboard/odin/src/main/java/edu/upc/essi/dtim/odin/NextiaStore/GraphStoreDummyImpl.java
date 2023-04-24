package edu.upc.essi.dtim.odin.NextiaStore;


import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.Queries.Query;
import edu.upc.essi.dtim.Queries.QueryResult;

import java.util.HashSet;
import java.util.Set;

public class GraphStoreDummyImpl implements GraphStoreInterface{
    /**
     * Retrieves all triples with the given subject.
     *
     * @param subject the URI of the subject to retrieve triples for
     * @param graph the graph to retrieve triples from
     */
    @Override
    public Set<Triple> getTriplesWithSubject(URI subject, Graph graph) {
        return null;
    }

    /**
     * Retrieves all triples with the given predicate.
     *
     * @param predicate the URI of the predicate to retrieve triples for
     * @param graph the graph to retrieve triples from
     */
    @Override
    public Set<Triple> getTriplesWithPredicate(URI predicate, Graph graph) {
        return null;
    }

    /**
     * Retrieves all triples with the given object.
     *
     * @param object the URI of the object to retrieve triples for
     * @param graph the graph to retrieve triples from
     */
    @Override
    public Set<Triple> getTriplesWithObject(URI object, Graph graph) {
        return null;
    }

    /**
     * Retrieves all triples in the given graph.
     *
     * @param graph the graph to retrieve triples from
     */
    @Override
    public Set<Triple> getAllTriples(Graph graph) {
        return graph.getTriples();
    }

    /**
     * Retrieves all subjects in the given graph.
     *
     * @param graph the graph to retrieve subjects from
     */
    @Override
    public Set<URI> getSubjects(Graph graph) {
        Set<URI> uris = new HashSet<>();
        Set<Triple> triples = graph.getTriples();
        for (Triple triple : triples) {
            uris.add((URI) triple.getObject());
        }
        return uris;
    }

    /**
     * Retrieves all predicates in the given graph.
     *
     * @param graph the graph to retrieve predicates from
     */
    @Override
    public Set<URI> getPredicates(Graph graph) {
        Set<URI> uris = new HashSet<>();
        Set<Triple> triples = graph.getTriples();
        for (Triple triple : triples) {
            uris.add((URI) triple.getObject());
        }
        return uris;
    }

    /**
     * Retrieves all objects in the given graph.
     *
     * @param graph the graph to retrieve objects from
     */
    @Override
    public Set<URI> getObjects(Graph graph) {
        Set<URI> uris = new HashSet<>();
        Set<Triple> triples = graph.getTriples();
        for (Triple triple : triples) {
            uris.add((URI) triple.getObject());
        }
        return uris;
    }

    /**
     * Retrieves the graph with the given name.
     *
     * @param name the URI of the graph to retrieve
     */
    @Override
    public Graph getGraph(URI name) {
        return null;
    }

    /**
     * Saves the given graph.
     *
     * @param graph the graph to save
     */
    @Override
    public void saveGraph(Graph graph) {

    }

    /**
     * Deletes the graph with the given name.
     *
     * @param name the URI of the graph to delete
     */
    @Override
    public void deleteGraph(URI name) {}

    /**
     * Executes the given SPARQL query on the given graph and returns the results.
     *
     * @param query the SPARQL query to execute
     * @param graph the graph to execute the query on
     */
    @Override
    public QueryResult query(Query query, Graph graph) {
        return null;
    }
}
