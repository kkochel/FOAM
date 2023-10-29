package pl.lodz.uni.biobank.foam.app.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/datasets")
public class DatasetController {
    private static final Logger log = LoggerFactory.getLogger(DatasetController.class);

    private final DatasetService service;

    public DatasetController(DatasetService service) {
        this.service = service;
    }

    @GetMapping("{datasetId}")
    ResponseEntity<DatasetResponse> getDatasets(@PathVariable String datasetId, Authentication authentication) {
        String userName = (String) authentication.getPrincipal();
        log.info("User: {} query for dataset: {}", userName, datasetId);

        return ResponseEntity.ok(service.getDatasets(datasetId, userName));
    }

}
