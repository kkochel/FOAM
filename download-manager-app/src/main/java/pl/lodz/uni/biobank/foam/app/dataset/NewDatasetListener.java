package pl.lodz.uni.biobank.foam.app.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.nio.charset.StandardCharsets;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.uni.biobank.foam.app.inbox.InboxService;

@Service
public class NewDatasetListener {
    private static final Logger log = LoggerFactory.getLogger(NewDatasetListener.class);

    private final DatasetService service;
    private final InboxService inboxService;

    public NewDatasetListener(DatasetService service, InboxService inboxService) {
        this.service = service;
        this.inboxService = inboxService;
    }

    @RabbitListener(queues = "foam_dataset")
    public void handleEvent(DatasetData event, Message rawMessage) {
        log.info("Handle DatasetData: {}", event);
        String messageId = InboxService.messageId(rawMessage);
        try {
            if (inboxService.isDuplicate(messageId, "foam_dataset", new String(rawMessage.getBody(), StandardCharsets.UTF_8))) {
                return;
            }
        } catch (DataIntegrityViolationException e) {
            log.info("Concurrent duplicate message on foam_dataset, skipping. stableId={}", event.stableId());
            return;
        }
        service.handleMessage(event);
    }
}
