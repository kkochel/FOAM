package pl.lodz.uni.biobank.foam.app.export;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.lodz.uni.biobank.foam.app.cega_user.CegaUserService;
import pl.lodz.uni.biobank.foam.app.dataset.Dataset;
import pl.lodz.uni.biobank.foam.app.dataset.DatasetAccessDeniedException;
import pl.lodz.uni.biobank.foam.app.dataset.DatasetService;
import pl.lodz.uni.biobank.foam.shared.FileData;

import java.util.UUID;

@Service
public class ExportService {
    private final ExportMessageSender sender;
    private final DatasetService datasetService;
    private final CegaUserService userService;
    private final FileExportLogService logService;
    private final UserExportFileService exportFileService;

    public ExportService(ExportMessageSender sender, DatasetService datasetService, CegaUserService userService, FileExportLogService logService, UserExportFileService exportFileService) {
        this.sender = sender;
        this.datasetService = datasetService;
        this.userService = userService;
        this.logService = logService;
        this.exportFileService = exportFileService;
    }

    public void tryExportFile(String datasetId, String fileId, String username) {
        UUID uuid = UUID.randomUUID();
        try {
            exportFile(datasetId, fileId, username, uuid);
        } catch (DatasetAccessDeniedException e) {
            logService.save(new FileExportLog(fileId, username, uuid, ExportStage.REVOKED));
            exportFileService.updateFileStage(fileId, username, ExportStage.REVOKED);

            throw new AccessDeniedException(String.format("User %s lost permission to export file %s", username, fileId), e);
        }
    }

    public void tryExportDataset(String datasetId, String username) {
        try {
            Dataset dataset = datasetService.getDataset(datasetId, username);
            dataset.getFiles().forEach(file -> tryExportFile(datasetId, file.getStableId(), username));
        } catch (DatasetAccessDeniedException e) {
            throw new AccessDeniedException(String.format("User %s lost permission to export dataset %s", username, datasetId));
        }
    }

    private void exportFile(String datasetId, String fileId, String username, UUID uuid) throws DatasetAccessDeniedException {
        FileData file = datasetService.getFileById(datasetId, fileId, username);
        String key = getCegaUserKey(username, file.stableId(), uuid);
        logService.save(new FileExportLog(file.stableId(), username, uuid, ExportStage.ACCEPTED));
        exportFileService.updateFileStage(file.stableId(), username, ExportStage.ACCEPTED);

        sender.handleSend(new C4ghExportTask(uuid, file.header(), file.archiveFilePath(), file.fileName(), key, username, file.stableId(), datasetId));
    }

    private String getCegaUserKey(String username, String fileId, UUID uuid) {
        try {
            return userService.getPublicC4ghKey(username).get(0).getKey();
        } catch (RuntimeException e) {
            logService.save(new FileExportLog(fileId, username, uuid, ExportStage.ACCEPTED));
            exportFileService.updateFileStage(fileId, username, ExportStage.ACCEPTED);

            throw new MissingC4ghUserPublicKeyException(e);
        }

    }
}
