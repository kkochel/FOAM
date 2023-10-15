package pl.lodz.uni.biobank.foam.app.sda.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.context.ApplicationEventPublisher;
import pl.lodz.uni.biobank.foam.app.sda.api.PermissionDeletedMessage;

public class PermissionDeleted implements MessageHandler {
    private final ApplicationEventPublisher eventPublisher;
    private MessageHandler nextHandler;

    public PermissionDeleted(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void setNext(MessageHandler handler) {
        this.nextHandler = handler;
    }

    //permission.deleted
    @Override
    public void handle(CegaMessageType type, String message) throws JsonProcessingException {
        if (CegaMessageType.PERMISSION_DELETED.equals(type)) {
            ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            PermissionDeletedMessage pm = mapper.readValue(message, PermissionDeletedMessage.class);

            eventPublisher.publishEvent(pm);
        } else {
            this.nextHandler.handle(type, message);
        }
    }
}
