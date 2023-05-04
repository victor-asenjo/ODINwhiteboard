package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;

import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.LocalGraph;
import edu.upc.essi.dtim.Graph.Triple;
import edu.upc.essi.dtim.Graph.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class GraphStoreInterfaceTest {
    private GraphStoreInterface graphStore;
    private Graph graph;

    @BeforeEach
    void setUp() throws Exception {
        // Get the instance of GraphStoreInterface from GraphStoreFactory singleton
        graphStore = GraphStoreFactory.getInstance();
        graph = createTestGraph();
    }

    @AfterEach
    void tearDown() {
        // Clean up any resources after each test if needed
    }

    @Test
    void getGraph() {
        URI graphURI = new URI("http://example.com/graph1");
        //Graph retrievedGraph = graphStore.getGraph(graphURI);
        //assertNotNull(retrievedGraph);
    }

    @Test
    void saveGraph() {
    }

    @Test
    void deleteGraph() {
    }
    
    private Graph createTestGraph() {
        Set<Triple> triples = new HashSet<>();
        Graph testGraph = new LocalGraph(new URI("testGraph"), triples);

        Triple testTriple1 = new Triple(new URI("http://example.com/subject1"),
                new URI("http://example.com/predicate1"),
                new URI("http://example.com/object1"));
        Triple testTriple2 = new Triple(new URI("http://example.com/subject2"),
                new URI("http://example.com/predicate2"),
                new URI("http://example.com/object1"));
        Triple testTriple3 = new Triple(new URI("http://example.com/subject1"),
                new URI("http://example.com/predicate1"),
                new URI("http://example.com/object2"));

        testGraph.addTriple(testTriple1);
        testGraph.addTriple(testTriple2);
        testGraph.addTriple(testTriple3);

        return testGraph;
    }
}