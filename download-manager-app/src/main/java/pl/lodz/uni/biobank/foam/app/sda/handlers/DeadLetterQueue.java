package pl.lodz.uni.biobank.foam.app.sda.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class DeadLetterQueue implements MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(DeadLetterQueue.class);
    private final RabbitTemplate template;

    public DeadLetterQueue(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public void setNext(MessageHandler handler) {

    }

    @Override
    public void handle(CegaMessageType type, String message) throws JsonProcessingException {
        template.convertAndSend("catch_all.dead", message);
        log.info("Handled unknown message and send to dead letter queue. The message {}", message);
    }
}
