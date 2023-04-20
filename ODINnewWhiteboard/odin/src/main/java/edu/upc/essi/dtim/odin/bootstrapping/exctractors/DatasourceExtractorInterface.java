package edu.upc.essi.dtim.odin.bootstrapping.exctractors;

import edu.upc.essi.dtim.DataSources.Dataset;

public interface DatasourceExtractorInterface {
    Dataset extractData(String datasourceFile);
}
