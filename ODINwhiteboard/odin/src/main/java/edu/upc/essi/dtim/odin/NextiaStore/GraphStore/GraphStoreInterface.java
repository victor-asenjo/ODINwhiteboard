package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import edu.upc.essi.dtim.NextiaCore.graph.URI;

public interface GraphStoreInterface {
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

    Graph getGraph(URI name);
}
