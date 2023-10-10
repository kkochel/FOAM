package pl.lodz.uni.biobank.foam.app.sda;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.lodz.uni.biobank.foam.app.sda.handlers.MessageHandlerService;

@Service
public class SdaListener {

    private final MessageHandlerService handlerService;

    public SdaListener(MessageHandlerService handlerService) {
        this.handlerService = handlerService;
    }

    @RabbitListener(queues = "unknown_schema", errorHandler = "sdaListenerErrorHandler")
    public void receiveMessage(String message) throws ParseException, JsonProcessingException {
        handlerService.handle(message);
    }
}
