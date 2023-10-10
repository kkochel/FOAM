package pl.lodz.uni.biobank.foam.integration;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SdaListener {

    private final DatasetReleaseService service;

    public SdaListener(DatasetReleaseService service) {
        this.service = service;
    }

    @RabbitListener(queues = "foam_integration")
    public void receiveMessage(DatasetRelease message) {
        service.handle(message);
    }
}
