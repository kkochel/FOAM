package pl.lodz.uni.biobank.foam.app.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.lodz.uni.biobank.foam.app.dataset.DatasetWithFilesResponse;

@RestController
@RequestMapping("api/export")
public class ExportController {
    private static final Logger log = LoggerFactory.getLogger(ExportController.class);

    private final ExportService service;

    public ExportController(ExportService service) {
        this.service = service;
    }

    @PostMapping("datasets/{stableId}")
    ResponseEntity<DatasetWithFilesResponse> exportDataset(@PathVariable String stableId, Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("datasets/{stableId}/files")
    ResponseEntity<HttpStatus> exportFile(@PathVariable String stableId, @RequestBody ExportRequest request, Authentication authentication) {
        String userName = (String) authentication.getPrincipal();
        log.info("User: {} ordered the export of the file: {}", userName, request.stableId());

        service.exportFile(stableId, request.stableId(), (String) authentication.getPrincipal());
        return ResponseEntity.ok().build();
    }

}
