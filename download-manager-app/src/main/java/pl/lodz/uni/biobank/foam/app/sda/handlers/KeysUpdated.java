package pl.lodz.uni.biobank.foam.app.sda.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.context.ApplicationEventPublisher;
import pl.lodz.uni.biobank.foam.app.sda.api.KeysUpdatedMessage;

public class KeysUpdated implements MessageHandler {
    private final ApplicationEventPublisher eventPublisher;
    private MessageHandler nextHandler;

    public KeysUpdated(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void setNext(MessageHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public void handle(CegaMessageType type, String message) throws JsonProcessingException {
        if (CegaMessageType.KEYS_UPDATED.equals(type)) {
            ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            KeysUpdatedMessage kum = mapper.readValue(message, KeysUpdatedMessage.class);

            eventPublisher.publishEvent(kum);
        } else {
            this.nextHandler.handle(type, message);
        }
    }
}
