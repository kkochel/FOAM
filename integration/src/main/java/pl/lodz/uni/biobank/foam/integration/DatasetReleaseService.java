package pl.lodz.uni.biobank.foam.integration;

import org.springframework.stereotype.Service;

@Service
public class DatasetReleaseService {

    private final SdaQueryRepository repository;
    private final FoamDatasetSender sender;
    private final I2B2Sender i2B2Sender;

    public DatasetReleaseService(SdaQueryRepository repository, FoamDatasetSender sender, I2B2Sender i2B2Sender) {
        this.repository = repository;
        this.sender = sender;
        this.i2B2Sender = i2B2Sender;
    }

    public void handle(DatasetRelease message) {
        sender.handleSend(repository.getDatasetWithFiles(message.datasetId()));
        i2B2Sender.handleSend(repository.getI2B2Files(message.datasetId()));
    }
}
