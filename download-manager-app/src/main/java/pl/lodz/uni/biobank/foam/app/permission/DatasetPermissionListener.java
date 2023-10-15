package pl.lodz.uni.biobank.foam.app.permission;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.sda.api.PermissionMessage;

@Component
public class DatasetPermissionListener {
    private final PermissionService service;

    public DatasetPermissionListener(PermissionService service) {
        this.service = service;
    }

    @EventListener
    public void handleEvent(PermissionMessage event) {
        service.handleEvent(event);
    }
}