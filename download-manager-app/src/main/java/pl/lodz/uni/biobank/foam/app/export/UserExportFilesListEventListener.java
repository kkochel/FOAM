package pl.lodz.uni.biobank.foam.app.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserExportFilesListEventListener {
    private static final Logger log = LoggerFactory.getLogger(UserExportFilesListEventListener.class);

    private final UserExportFileService service;

    public UserExportFilesListEventListener(UserExportFileService service) {
        this.service = service;
    }


    @EventListener
    public void handleEvent(UserExportFilesEventList event) {
        log.info("Handle NewDatasetPermissionEvent: {}", event);
        service.handleEvent(event);
    }
}
