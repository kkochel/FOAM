package pl.lodz.uni.biobank.foam.app.export;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ExportStageListener {
    private static final Logger log = LoggerFactory.getLogger(ExportStageListener.class);
    private final FileExportLogService service;

    public ExportStageListener(FileExportLogService service) {
        this.service = service;
    }


    @RabbitListener(queues = "export_stage_log", errorHandler = "sdaListenerErrorHandler")
    public void handleMessage(FileExportMessage message) {
        log.info("Handle FileExportEvent: {}", message);
        service.handleEvent(message);
    }
}
