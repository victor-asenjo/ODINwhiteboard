package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;


import edu.upc.essi.dtim.Graph.Graph;
import edu.upc.essi.dtim.Graph.URI;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb2.TDB2Factory;


public class GraphStoreJenaImpl implements GraphStoreInterface {

    /**
     * Saves the given graph.
     *
     * @param graph the graph to save
     */
    @Override
    public void saveGraph(Graph graph) {
        GraphJenaAdapter adapter = new GraphJenaAdapter();
        Model modelToSave = adapter.adapt(graph);

        // Crear una base de datos TDB en la carpeta "tdb"
        String directory = ".\\odin\\dbFiles\\jenaFiles\\jenaDB";
        Dataset dataset = TDB2Factory.connectDataset(directory);
        dataset.begin(ReadWrite.WRITE);
        try {
            // Perform database operation here
            // Obtener el modelo del conjunto de modelos
            String modelName = graph.getName().getURI();
            Model model = dataset.getNamedModel(modelName);

            // Agregar el modelo a la base de datos
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
        // Obtener el modelo asociado al grafo
        String modelName = name.getURI();
        //Model model = dataset.getNamedModel(modelName);

        // Verificar si el modelo existe y eliminarlo
        //if (model != null) {
          //  dataset.removeNamedModel(modelName);
        //}

        // Guardar los cambios
        //dataset.commit();
        //dataset.end();
    }
}
