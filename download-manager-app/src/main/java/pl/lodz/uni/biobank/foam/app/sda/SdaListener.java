package pl.lodz.uni.biobank.foam.app.sda;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.json.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.nio.charset.StandardCharsets;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.uni.biobank.foam.app.inbox.InboxService;
import pl.lodz.uni.biobank.foam.app.sda.handlers.MessageHandlerService;

@Service
public class SdaListener {
    private static final Logger log = LoggerFactory.getLogger(SdaListener.class);

    private final MessageHandlerService handlerService;
    private final InboxService inboxService;

    public SdaListener(MessageHandlerService handlerService, InboxService inboxService) {
        this.handlerService = handlerService;
        this.inboxService = inboxService;
    }

    @RabbitListener(queues = "unknown_schema", errorHandler = "sdaListenerErrorHandler")
    public void receiveMessage(Object message) throws ParseException, JsonProcessingException {
        Message m = (Message) message;
        String messageId = InboxService.messageId(m);
        try {
            if (inboxService.isDuplicate(messageId, "unknown_schema", new String(m.getBody(), StandardCharsets.UTF_8))) {
                return;
            }
        } catch (DataIntegrityViolationException e) {
            log.info("Concurrent duplicate message on unknown_schema, skipping. messageId={}", messageId);
            return;
        }
        handlerService.handle(message);
    }
}
