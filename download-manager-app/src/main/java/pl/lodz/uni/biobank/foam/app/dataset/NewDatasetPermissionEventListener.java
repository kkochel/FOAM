package pl.lodz.uni.biobank.foam.app.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NewDatasetPermissionEventListener {
    private static final Logger log = LoggerFactory.getLogger(NewDatasetPermissionEventListener.class);

    private final DatasetService service;

    public NewDatasetPermissionEventListener(DatasetService service) {
        this.service = service;
    }

    @EventListener
    public void handleEvent(NewDatasetPermissionEvent event) {
        log.info("Handle NewDatasetPermissionEvent: {}", event);
        service.handleNewDatasetPermission(event);
    }
}
