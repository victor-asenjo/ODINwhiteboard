package edu.upc.essi.dtim.odin.bootstrapping;

import edu.upc.essi.dtim.DataSources.Dataset;
import edu.upc.essi.dtim.odin.bootstrapping.exctractors.CsvDatasourceExtractorImpl;
import edu.upc.essi.dtim.odin.bootstrapping.exctractors.DatasourceExtractorInterface;
import edu.upc.essi.dtim.odin.bootstrapping.exctractors.JsonDatasourceExtractorImpl;
import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.Map;

public class BootstrappingService {

    private final Map<String, DatasourceExtractorInterface> extractors;

    public BootstrappingService() {
        extractors = new HashMap<>();
        extractors.put("csv", new CsvDatasourceExtractorImpl());
        extractors.put("json", new JsonDatasourceExtractorImpl());
    }

    private Dataset extractData(String datasourceFile) {
        String extension = FilenameUtils.getExtension(datasourceFile);
        if (!extractors.containsKey(extension)) {
            throw new UnsupportedOperationException("Unsupported datasource type: " + extension);
        }
        DatasourceExtractorInterface extractor = extractors.get(extension);
        return extractor.extractData(datasourceFile);
    }

    // other methods
}

