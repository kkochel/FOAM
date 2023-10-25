package pl.lodz.uni.biobank.foam.app.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NewDatasetListener {
    private static final Logger log = LoggerFactory.getLogger(NewDatasetListener.class);

    private final DatasetService service;

    public NewDatasetListener(DatasetService service) {
        this.service = service;
    }

    @RabbitListener(queues = "foam_dataset")
    public void handleEvent(DatasetData event) {
        log.info("Handle DatasetData: {}", event);
        service.handleMessage(event);
    }
}
