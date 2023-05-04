package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;


import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.URI;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb2.TDB2Factory;
import org.springframework.stereotype.Component;

@Component
public class GraphStoreJenaImpl implements GraphStoreInterface {
    private final Dataset dataset;
    private final GraphJenaAdapter adapter;


    public GraphStoreJenaImpl(AppConfig appConfig) {
        this.adapter = new GraphJenaAdapter();
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
        Model modelToSave = adapter.adapt(graph);
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
                return adapter.adapt(model);
            }
        } finally {
            dataset.end();
        }
    }



}

