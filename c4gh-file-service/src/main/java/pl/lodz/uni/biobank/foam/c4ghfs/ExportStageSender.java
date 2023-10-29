package pl.lodz.uni.biobank.foam.c4ghfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

public class ExportStageSender {
    private static final Logger log = LoggerFactory.getLogger(ExportStageSender.class);

    @Value("${application.export-file-stage.routing-key}")
    private String routingKey;
    private final RabbitTemplate template;

    public ExportStageSender(RabbitTemplate template) {
        this.template = template;
    }

    public void handleSend(FileExportEvent message) {
        template.convertAndSend(routingKey, message);

        log.info("The export stage: {} has been sent wit routing key: {}", message, routingKey);
    }
}
