package pl.lodz.uni.biobank.foam.integration;

import org.springframework.stereotype.Service;

@Service
public class DatasetReleaseService {

    private final SdaQueryRepository repository;
    private final FoamDatasetSender sender;

    public DatasetReleaseService(SdaQueryRepository repository, FoamDatasetSender sender) {
        this.repository = repository;
        this.sender = sender;
    }

    public void handle(DatasetRelease message) {
        sender.handleSend(repository.getDatasetWithFiles(message.datasetId()));
    }
}
