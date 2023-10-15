package pl.lodz.uni.biobank.foam.app.sda.handlers;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

@Service
public class MessageHandlerService {
    private final MessageHandler handler;

    public MessageHandlerService(ApplicationEventPublisher eventPublisher) {
        this.handler = new Permission(eventPublisher);

        PermissionDeleted permissionDeleted = new PermissionDeleted(eventPublisher);

        this.handler.setNext(permissionDeleted);
        permissionDeleted.setNext(null);
    }

    public void handle(Object message) throws ParseException, JsonProcessingException {
        Message m = (Message) message;
        String body = new String(m.getBody(), StandardCharsets.UTF_8);
        LinkedHashMap<String, Object> object = new JSONParser(body).object();
        String type = (String) object.get("type");

        CegaMessageType messageType = CegaMessageType.findByLabel(type);

        handler.handle(messageType, body);
    }
}
