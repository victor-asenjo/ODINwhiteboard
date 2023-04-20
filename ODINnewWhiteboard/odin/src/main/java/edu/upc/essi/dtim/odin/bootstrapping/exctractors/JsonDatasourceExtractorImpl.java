package edu.upc.essi.dtim.odin.bootstrapping.exctractors;

import edu.upc.essi.dtim.DataSources.Dataset;

public class JsonDatasourceExtractorImpl implements DatasourceExtractorInterface {
    @Override
    public Dataset extractData(String datasourceFile) {
        // Implementation for JSON extraction
        return new Dataset("JSON");
    }
}
