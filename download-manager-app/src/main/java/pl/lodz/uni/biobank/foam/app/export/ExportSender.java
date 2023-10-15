package pl.lodz.uni.biobank.foam.app.export;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExportSender {
    @Value("${application.export-file.routing-key}")
    private String routingKey;
    private final RabbitTemplate template;

    public ExportSender(RabbitTemplate template) {
        this.template = template;
    }

    public void handleSend(C4ghExportTask message) {
        template.convertAndSend(routingKey, message);
    }
}
