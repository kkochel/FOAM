package pl.lodz.uni.biobank.foam.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class FileStatusSender {
    private static final Logger log = LoggerFactory.getLogger(FileStatusSender.class);

    @Value("${application.export-file-stage.routing-key}")
    private String routingKey;

    private final RabbitTemplate template;

    public FileStatusSender(RabbitTemplate template) {
        this.template = template;
    }

    public void handleSend(FileExportMessage data) {
        template.convertAndSend(routingKey, data);
        log.info("Message {} published to with routing key {}", data, routingKey);
    }
}
