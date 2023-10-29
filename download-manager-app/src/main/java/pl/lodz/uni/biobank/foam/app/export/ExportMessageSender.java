package pl.lodz.uni.biobank.foam.app.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExportMessageSender {
    private static final Logger log = LoggerFactory.getLogger(ExportMessageSender.class);

    @Value("${application.export-file.routing-key}")
    private String routingKey;
    private final RabbitTemplate template;

    public ExportMessageSender(RabbitTemplate template) {
        this.template = template;
    }

    public void handleSend(C4ghExportTask message) {
        template.convertAndSend(routingKey, message);

        log.info("The export order: {} has been sent wit routing key: {}", message, routingKey);
    }
}
