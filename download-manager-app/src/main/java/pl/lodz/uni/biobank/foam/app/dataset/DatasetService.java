package pl.lodz.uni.biobank.foam.app.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.lodz.uni.biobank.foam.app.export.UserExportFile;
import pl.lodz.uni.biobank.foam.app.permission.PermissionService;
import pl.lodz.uni.biobank.foam.app.permission.PermissionStatus;

import java.util.List;

@Service
public class DatasetService {
    private static final Logger log = LoggerFactory.getLogger(DatasetService.class);

    private final DatasetFileRepository fileRepository;
    private final DatasetRepository repository;
    private final PermissionService permissionService;
    private final UserExportFileEventPublisher userExportFileEventPublisher;

    public DatasetService(DatasetFileRepository fileRepository, DatasetRepository repository, PermissionService permissionService, UserExportFileEventPublisher userExportFileEventPublisher) {
        this.fileRepository = fileRepository;
        this.repository = repository;
        this.permissionService = permissionService;
        this.userExportFileEventPublisher = userExportFileEventPublisher;
    }

    public void handleMessage(DatasetData event) {
        List<DatasetFile> filesList = event.datasetFiles().stream().map(DatasetFile::new).toList();
        Dataset dataset = new Dataset(event);
        filesList.forEach(dataset::addFile);

        repository.save(dataset);
        log.info("Dataset event processed correctly. StableId: {} ", event.stableId());
    }


    public DatasetResponse getDatasets(String datasetId, String username) {
        PermissionStatus status = permissionService.datasetStatusForUser(datasetId, username);
        Dataset dataset = repository.getWithFilesBy(datasetId);
        return new DatasetResponse(dataset.getStableId(), dataset.getTitle(), dataset.getDescription(), status.label);
    }

    public FileData getFileById(String datasetID, String fileId, String username) throws DatasetAccessDeniedException {
        checkPermission(datasetID, username);
        DatasetFile file = fileRepository.getFile(fileId);
        return new FileData(file.getStableId(), file.getFileName(), file.getArchiveFilePath(), file.getArchiveFileSize(), file.getDecryptedFileSize(), file.getHeader());
    }

    public void handleNewDatasetPermission(NewDatasetPermissionEvent event) {
        List<UserExportFile> list = repository.getWithFilesBy(event.datasetId())
                .getFiles().stream()
                .map(f -> new UserExportFile(event.datasetId(), f.getStableId(), event.username()))
                .toList();

        userExportFileEventPublisher.handle(list);
    }

    public Dataset getDataset(String datasetID, String username) throws DatasetAccessDeniedException {
        checkPermission(datasetID, username);
        return repository.getWithFilesBy(datasetID);
    }

    private void checkPermission(String datasetId, String username) throws DatasetAccessDeniedException {
        PermissionStatus permissionStatus = permissionService.datasetStatusForUser(datasetId, username);
        if (PermissionStatus.REVOKED.equals(permissionStatus)) {
            throw new DatasetAccessDeniedException(String.format("User: %s try get access to dataset: %s", username, datasetId));
        }
    }

}
