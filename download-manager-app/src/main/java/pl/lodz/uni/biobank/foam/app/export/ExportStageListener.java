package pl.lodz.uni.biobank.foam.app.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.nio.charset.StandardCharsets;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import pl.lodz.uni.biobank.foam.app.inbox.InboxService;

@Component
public class ExportStageListener {
    private static final Logger log = LoggerFactory.getLogger(ExportStageListener.class);
    private final FileExportLogService service;
    private final InboxService inboxService;

    public ExportStageListener(FileExportLogService service, InboxService inboxService) {
        this.service = service;
        this.inboxService = inboxService;
    }

    @RabbitListener(queues = "export_stage_log", errorHandler = "sdaListenerErrorHandler")
    public void handleMessage(FileExportMessage message, Message rawMessage) {
        log.info("Handle FileExportEvent: {}", message);
        String messageId = InboxService.messageId(rawMessage);
        try {
            if (inboxService.isDuplicate(messageId, "export_stage_log", new String(rawMessage.getBody(), StandardCharsets.UTF_8))) {
                return;
            }
        } catch (DataIntegrityViolationException e) {
            log.info("Concurrent duplicate message on export_stage_log, skipping. messageId={}", messageId);
            return;
        }
        service.handleEvent(message);
    }
}
