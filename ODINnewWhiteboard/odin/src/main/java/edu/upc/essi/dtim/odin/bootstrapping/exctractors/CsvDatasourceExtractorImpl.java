package edu.upc.essi.dtim.odin.bootstrapping.exctractors;

import edu.upc.essi.dtim.DataSources.Dataset;

public class CsvDatasourceExtractorImpl implements DatasourceExtractorInterface {
    @Override
    public Dataset extractData(String datasourceFile) {
        // Implementation for CSV extraction
        return new Dataset("CSV");
    }
}
