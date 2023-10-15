package pl.lodz.uni.biobank.foam.app.permission;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.sda.api.PermissionDeletedMessage;

@Component
public class DatasetPermissionDeletedListener {
    private final PermissionService service;

    public DatasetPermissionDeletedListener(PermissionService service) {
        this.service = service;
    }

    @EventListener
    public void handleEvent(PermissionDeletedMessage event) {
        service.handleEvent(event);
    }
}