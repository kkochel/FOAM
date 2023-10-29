package pl.lodz.uni.biobank.foam.app.permission;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.dataset.NewDatasetPermissionEvent;

@Component
public class DatasetPermissionEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public DatasetPermissionEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void handle(String username, String datasetId) {
        eventPublisher.publishEvent(new NewDatasetPermissionEvent(username, datasetId));
    }
}
