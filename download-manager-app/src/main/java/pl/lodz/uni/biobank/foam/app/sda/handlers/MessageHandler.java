package pl.lodz.uni.biobank.foam.app.sda.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessageHandler {
    void setNext(MessageHandler handler);

    void handle(CegaMessageType type, String message) throws JsonProcessingException;


}
