package pl.lodz.uni.biobank.foam.integration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FoamDatasetSender {
    @Value("${app.foam-dataset.routing-key}")
    private String routingKey;
    private final RabbitTemplate template;

    public FoamDatasetSender(RabbitTemplate template) {
        this.template = template;
    }

    public void handleSend(DatasetData data) {
        template.convertAndSend(routingKey, data);
    }
}
