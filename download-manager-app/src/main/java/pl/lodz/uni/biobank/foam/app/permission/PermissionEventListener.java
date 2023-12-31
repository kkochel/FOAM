package pl.lodz.uni.biobank.foam.app.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.sda.api.PermissionMessage;

@Component
public class PermissionEventListener {
    private static final Logger log = LoggerFactory.getLogger(PermissionDeletedEventListener.class);

    private final PermissionService service;

    public PermissionEventListener(PermissionService service) {
        this.service = service;
    }

    @EventListener
    public void handleEvent(PermissionMessage event) {
        log.info("Handle PermissionMessage: {}", event);
        service.handleEvent(event);
    }
}