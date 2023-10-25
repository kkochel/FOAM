package pl.lodz.uni.biobank.foam.app.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.sda.api.PermissionDeletedMessage;

@Component
public class DatasetPermissionDeletedListener {
    private static final Logger log = LoggerFactory.getLogger(DatasetPermissionDeletedListener.class);

    private final PermissionService service;

    public DatasetPermissionDeletedListener(PermissionService service) {
        this.service = service;
    }

    @EventListener
    public void handleEvent(PermissionDeletedMessage event) {
        log.info("Handle PermissionDeletedMessage: {}", event);
        service.handleEvent(event);
    }
}