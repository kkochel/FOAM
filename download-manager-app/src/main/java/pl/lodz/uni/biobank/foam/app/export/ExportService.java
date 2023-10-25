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
    private final ExportSender sender;
    private final PermissionService permissionService;
    private final DatasetService datasetService;
    private final CegaUserService userService;

    public ExportService(ExportSender sender, PermissionService permissionService, DatasetService datasetService, CegaUserService userService) {
        this.sender = sender;
        this.permissionService = permissionService;
        this.datasetService = datasetService;
        this.userService = userService;
    }

    public void exportFile(String datasetId, String fileId, String username) {
        PermissionStatus datasetStatus = permissionService.datasetStatusForUser(datasetId, username);

        if (PermissionStatus.REVOKED.equals(datasetStatus)) {
            throw new AccessDeniedException(String.format("User %s lost permission to export dataset %s", username, fileId));
        }

        FileData file = datasetService.getFileById(fileId);
        CegaUserKey key = userService.getPublicC4ghKey(username);

        sender.handleSend(new C4ghExportTask(UUID.randomUUID(), file.header(), file.archiveFilePath(), file.fileName(), key.getKey(), username));
    }
}
