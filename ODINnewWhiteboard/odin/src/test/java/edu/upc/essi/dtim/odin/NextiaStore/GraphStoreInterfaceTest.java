package edu.upc.essi.dtim.odin.NextiaStore;

import edu.upc.essi.dtim.Graph.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void getTriplesWithSubject() {
        URI subject = new URI("http://example.com/subject1");
        Set<Triple> triples = graphStore.getTriplesWithSubject(subject, graph);
        assertNotNull(triples);
        assertEquals(1, triples.size());
    }

    @Test
    void getTriplesWithPredicate() {
        URI predicate = new URI("http://example.com/predicate1");
        Set<Triple> triples = graphStore.getTriplesWithPredicate(predicate, graph);
        assertNotNull(triples);
        assertEquals(1, triples.size());
    }

    @Test
    void getTriplesWithObject() {
        URI object = new URI("http://example.com/object1");
        //insertar
        Set<Triple> triples = graphStore.getTriplesWithObject(object, graph);
        assertNotNull(triples);
        assertEquals(1, triples.size());
    }

    @Test
    void getAllTriples() {
        Set<Triple> triples = graphStore.getAllTriples(graph);
        assertNotNull(triples);
        assertEquals(3, triples.size());
    }

    @Test
    void getSubjects() {
        Set<URI> subjects = graphStore.getSubjects(graph);
        assertNotNull(subjects);
        assertEquals(2, subjects.size());
    }

    @Test
    void getPredicates() {
        Set<URI> predicates = graphStore.getPredicates(graph);
        assertNotNull(predicates);
        assertEquals(2, predicates.size());
    }

    @Test
    void getObjects() {
        Set<URI> objects = graphStore.getObjects(graph);
        assertNotNull(objects);
        assertEquals(2, objects.size());
    }

    @Test
    void getGraph() {
        URI graphURI = new URI("http://example.com/graph1");
        Graph retrievedGraph = graphStore.getGraph(graphURI);
        assertNotNull(retrievedGraph);
    }

    @Test
    void saveGraph() {
    }

    @Test
    void deleteGraph() {
    }

    @Test
    void query() {
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