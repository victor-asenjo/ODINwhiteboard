package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import edu.upc.essi.dtim.NextiaCore.graph.Graph;

import java.util.List;

public class ORMGraphImplementation implements ORMStoreInterface<Graph>{

    @Override
    public Graph findById(String id) {
        return null;
    }

    @Override
    public Graph save(Graph object) {
        return null;
    }

    @Override
    public List<Graph> getAll() {
        return null;
    }

    @Override
    public boolean deleteOne(String id) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }
}
