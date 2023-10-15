package pl.lodz.uni.biobank.foam.app.permission;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/datasets")
public class PermittedDatasetsController {
    private final PermissionService service;

    public PermittedDatasetsController(PermissionService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity<PermittedDatasetsResponse> getFullUsername(Authentication authentication) {
        PermittedDatasetsResponse response = new PermittedDatasetsResponse(service.getDatasets((String) authentication.getPrincipal()));
        return ResponseEntity.ok(response);
    }

}
