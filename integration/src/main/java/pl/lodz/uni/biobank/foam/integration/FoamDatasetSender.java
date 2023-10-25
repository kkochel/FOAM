package pl.lodz.uni.biobank.foam.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FoamDatasetSender {
    private static final Logger log = LoggerFactory.getLogger(FoamDatasetSender.class);

    @Value("${app.foam-dataset.routing-key}")
    private String routingKey;
    private final RabbitTemplate template;

    public FoamDatasetSender(RabbitTemplate template) {
        this.template = template;
    }

    public void handleSend(DatasetData data) {
        template.convertAndSend(routingKey, data);
        log.info("Dataset with id {} has been send to FOAM with routing key {}", data.stableId(), routingKey);
    }
}
