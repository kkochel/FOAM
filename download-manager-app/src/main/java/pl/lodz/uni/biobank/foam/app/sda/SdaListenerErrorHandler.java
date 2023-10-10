package pl.lodz.uni.biobank.foam.app.sda;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

import java.nio.charset.StandardCharsets;


public class SdaListenerErrorHandler implements RabbitListenerErrorHandler {

    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) {
        throw new AmqpRejectAndDontRequeueException("Error Handler converted exception to fatal" + new String(amqpMessage.getBody(), StandardCharsets.UTF_8), exception);
    }
}
