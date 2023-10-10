package pl.lodz.uni.biobank.foam.app.dataset;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatasetService {

    private final DatasetRepository repository;

    public DatasetService(DatasetRepository repository) {
        this.repository = repository;
    }

    public void handle(DatasetData datasetData) {
        List<DatasetFile> filesList = datasetData.datasetFiles().stream().map(DatasetFile::new).toList();
        Dataset dataset = new Dataset(datasetData);
        filesList.forEach(dataset::addFile);

        repository.save(dataset);
    }
}
