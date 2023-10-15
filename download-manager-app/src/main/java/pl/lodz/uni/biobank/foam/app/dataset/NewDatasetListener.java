package pl.lodz.uni.biobank.foam.app.dataset;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NewDatasetListener {

    private final DatasetService service;

    public NewDatasetListener(DatasetService service) {
        this.service = service;
    }

    @RabbitListener(queues = "foam_dataset")
    public void receiveMessage(DatasetData dataset) {
        service.handleMessage(dataset);
    }
}
