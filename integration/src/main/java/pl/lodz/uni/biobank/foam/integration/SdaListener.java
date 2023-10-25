package pl.lodz.uni.biobank.foam.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SdaListener {
    private static final Logger log = LoggerFactory.getLogger(SdaListener.class);

    private final DatasetReleaseService service;

    public SdaListener(DatasetReleaseService service) {
        this.service = service;
    }

    @RabbitListener(queues = "foam_integration")
    public void handleEvent(DatasetRelease event) {
        log.info("Handle DatasetRelease: {}", event);
        service.handle(event);
    }
}
