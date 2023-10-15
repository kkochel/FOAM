package pl.lodz.uni.biobank.foam.app.dataset;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/datasets")
public class DatasetController {
    private final DatasetService service;

    public DatasetController(DatasetService service) {
        this.service = service;
    }

    @GetMapping("{datasetId}")
    ResponseEntity<DatasetWithFilesResponse> getFullUsername(@PathVariable String datasetId, Authentication authentication) {
        return ResponseEntity.ok(service.getDatasetWithFiles(datasetId, (String) authentication.getPrincipal()));
    }

}
