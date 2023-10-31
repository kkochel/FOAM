package pl.lodz.uni.biobank.foam.app.export;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.lodz.uni.biobank.foam.app.cega_user.CegaUserKey;
import pl.lodz.uni.biobank.foam.app.cega_user.CegaUserService;
import pl.lodz.uni.biobank.foam.app.dataset.DatasetService;
import pl.lodz.uni.biobank.foam.app.dataset.FileData;
import pl.lodz.uni.biobank.foam.app.permission.PermissionService;
import pl.lodz.uni.biobank.foam.app.permission.PermissionStatus;

import java.util.UUID;

@Service
public class ExportService {
    private final ExportMessageSender sender;
    private final PermissionService permissionService;
    private final DatasetService datasetService;
    private final CegaUserService userService;
    private final FileExportLogService logService;
    private final UserExportFileService exportFileService;

    public ExportService(ExportMessageSender sender, PermissionService permissionService, DatasetService datasetService, CegaUserService userService, FileExportLogService logService, UserExportFileService exportFileService) {
        this.sender = sender;
        this.permissionService = permissionService;
        this.datasetService = datasetService;
        this.userService = userService;
        this.logService = logService;
        this.exportFileService = exportFileService;
    }

    //TODO refactor to remove coupling
    public void exportFile(String datasetId, String fileId, String username) {
        PermissionStatus datasetStatus = permissionService.datasetStatusForUser(datasetId, username);

        if (PermissionStatus.REVOKED.equals(datasetStatus)) {
            throw new AccessDeniedException(String.format("User %s lost permission to export dataset %s", username, fileId));
        }

        UUID uuid = UUID.randomUUID();
        FileData file = datasetService.getFileById(fileId);
        String key = getCegaUserKey(username, file.stableId(), uuid);
        logService.save(new FileExportLog(file.stableId(), username, uuid, ExportStage.ACCEPTED));
        exportFileService.updateFileStage(file.stableId(), username, ExportStage.ACCEPTED);

        sender.handleSend(new C4ghExportTask(uuid, file.header(), file.archiveFilePath(), file.fileName(), key, username, file.stableId()));
    }

    private String getCegaUserKey(String username, String fileId, UUID uuid) {
        try {
            return userService.getPublicC4ghKey(username).getKey();
        } catch (RuntimeException e) {
            logService.save(new FileExportLog(fileId, username, uuid, ExportStage.ACCEPTED));
            exportFileService.updateFileStage(fileId, username, ExportStage.ACCEPTED);

            throw new MissingC4ghUserPublicKeyException(e);
        }

    }
}
