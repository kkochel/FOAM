package pl.lodz.uni.biobank.foam.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FoamDatasetSender {
    private static final Logger log = LoggerFactory.getLogger(FoamDatasetSender.class);

    private final String routingKey;
    private final RabbitTemplate template;

    public FoamDatasetSender(RabbitTemplate template, @Value("${app.foam-dataset.routing-key}") String routingKey) {
        this.template = template;
        this.routingKey = routingKey;
    }

    public void handleSend(DatasetData data) {
        template.convertAndSend(routingKey, data);
        log.info("Dataset with id {} has been send to FOAM with routing key {}", data.stableId(), routingKey);
    }
}
