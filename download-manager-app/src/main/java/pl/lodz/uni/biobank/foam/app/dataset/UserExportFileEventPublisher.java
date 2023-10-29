package pl.lodz.uni.biobank.foam.app.dataset;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.export.UserExportFile;
import pl.lodz.uni.biobank.foam.app.export.UserExportFilesEventList;

import java.util.List;

@Component
public class UserExportFileEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public UserExportFileEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void handle(List<UserExportFile> files) {
        eventPublisher.publishEvent(new UserExportFilesEventList(files));
    }
}
