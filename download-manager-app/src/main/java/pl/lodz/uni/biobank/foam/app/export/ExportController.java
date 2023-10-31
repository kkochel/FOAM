package pl.lodz.uni.biobank.foam.app.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.lodz.uni.biobank.foam.app.dataset.DatasetResponse;

import java.util.List;

@RestController
@RequestMapping("api/export")
public class ExportController {
    private static final Logger log = LoggerFactory.getLogger(ExportController.class);

    private final ExportService exportService;
    private final FileExportLogService logService;
    private final UserExportFileService exportFileService;


    public ExportController(ExportService service, FileExportLogService logService, UserExportFileService exportFileService) {
        this.exportService = service;
        this.logService = logService;
        this.exportFileService = exportFileService;
    }

    @PostMapping("datasets/{stableId}")
    ResponseEntity<DatasetResponse> exportDataset(@PathVariable String stableId, Authentication authentication) {
        String userName = (String) authentication.getPrincipal();
        log.info("User: {} ordered the export of the dataset: {}", userName, stableId);
        exportService.tryExportDataset(stableId, userName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("datasets/{datasetId}/files")
    ResponseEntity<List<UserFileResponse>> getFiles(@PathVariable String datasetId, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return ResponseEntity.ok(exportFileService.getFiles(datasetId, username));
    }

    @GetMapping("datasets/{datasetId}/files/{fileId}/history")
    ResponseEntity<List<FileExportHistoryItemResponse>> getFileExportLog(@PathVariable String datasetId, @PathVariable String fileId, Authentication authentication) {
        String userName = (String) authentication.getPrincipal();
        List<FileExportHistoryItemResponse> history = logService.getFileHistory(fileId, userName);
        return ResponseEntity.ok(history);
    }

    @PostMapping("datasets/{datasetId}/files")
    ResponseEntity<HttpStatus> exportFile(@PathVariable String datasetId, @RequestBody ExportRequest request, Authentication authentication) {
        String userName = (String) authentication.getPrincipal();
        log.info("User: {} ordered the export of the file: {}", userName, request.stableId());

        exportService.tryExportFile(datasetId, request.stableId(), userName);
        return ResponseEntity.ok().build();
    }

}
