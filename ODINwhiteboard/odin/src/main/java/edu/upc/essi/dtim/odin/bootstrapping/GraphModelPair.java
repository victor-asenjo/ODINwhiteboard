package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;
import org.apache.jena.rdf.model.Model;

public class GraphModelPair {
    private Graph graph;
    private Model model;

    public GraphModelPair(Graph graph, Model model) {
        this.graph = graph;
        this.model = model;
    }

    public Graph getGraph() {
        return graph;
    }

    public Model getModel() {
        return model;
    }
}
