package pl.lodz.uni.biobank.foam.app.permission;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.lodz.uni.biobank.foam.app.sda.api.PermissionDeletedMessage;
import pl.lodz.uni.biobank.foam.app.sda.api.PermissionMessage;

import java.util.List;

@Service
public class PermissionService {
    private final DatasetPermissionRepository repository;

    public PermissionService(DatasetPermissionRepository repository) {
        this.repository = repository;
    }

    public void handleEvent(PermissionMessage event) {
        String username = event.user().username();
        String datasetId = event.datasetId();
        List<Permission> permissions = repository.getPermission(username, datasetId);

        permissionValidation(permissions, username, datasetId);

        if (permissions.isEmpty()) {
            repository.persist(new Permission(username, datasetId));
        } else {
            permissions.get(0).makeAvailable();
            repository.merge(permissions.get(0).makeAvailable());
        }
    }

    public void handleEvent(PermissionDeletedMessage event) {
        String username = event.user();
        String datasetId = event.datasetId();
        List<Permission> permissions = repository.getPermission(username, datasetId);

        permissionValidation(permissions, username, datasetId);

        if (!permissions.isEmpty()) {
            repository.merge(permissions.get(0).revoke());
        }
    }

    public List<String> getDatasets(String username) {
        return repository.getDatasetsId(username).stream().sorted().toList();
    }

    public PermissionStatus datasetStatusForUser(String datasetId, String username) {
        List<Permission> permission = repository.getPermission(username, datasetId);

        permissionValidation(permission, username, datasetId);

        if (permission.isEmpty()) {
            throw new AccessDeniedException(String.format("Unauthorized user: %s access to dataset: %s attempted", username, datasetId));
        }

        return permission.get(0).getStatus();
    }

    private static void permissionValidation(List<Permission> permissions, String username, String datasetId) {
        if (permissions.size() > 1) {
            throw new RuntimeException(String.format("Query return to many elements %s for user: %s and datasetId: %s ", permissions.size(), username, datasetId));
        }
    }
}
