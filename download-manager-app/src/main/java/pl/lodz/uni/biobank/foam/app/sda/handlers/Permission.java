package pl.lodz.uni.biobank.foam.app.sda.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.context.ApplicationEventPublisher;
import pl.lodz.uni.biobank.foam.app.sda.api.PermissionMessage;

public class Permission implements MessageHandler {
    private MessageHandler nextHandler;
    private final ApplicationEventPublisher eventPublisher;

    public Permission(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void setNext(MessageHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public void handle(CegaMessageType type, String message) throws JsonProcessingException {
        if (CegaMessageType.PERMISSION.equals(type)) {
            ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            PermissionMessage permissionMessage = mapper.readValue(message, PermissionMessage.class);

            eventPublisher.publishEvent(permissionMessage.user());
            eventPublisher.publishEvent(permissionMessage);


        } else {
            this.nextHandler.handle(type, message);
        }
    }
}
