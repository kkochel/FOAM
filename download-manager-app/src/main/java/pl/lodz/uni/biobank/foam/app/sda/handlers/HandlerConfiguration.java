package pl.lodz.uni.biobank.foam.app.sda.handlers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    MessageHandlerService handlerService (ApplicationEventPublisher eventPublisher, RabbitTemplate template){
        Permission permission = new Permission(eventPublisher);
        PermissionDeleted permissionDeleted = new PermissionDeleted(eventPublisher);
        permission.setNext(permissionDeleted);

        PasswordUpdated passwordUpdated = new PasswordUpdated(eventPublisher);
        permissionDeleted.setNext(passwordUpdated);

        KeysUpdated keysUpdated = new KeysUpdated(eventPublisher);
        passwordUpdated.setNext(keysUpdated);

        DeadLetterQueue deadLetterQueue = new DeadLetterQueue(template);
        keysUpdated.setNext(deadLetterQueue);


        return new MessageHandlerService(permission);
    }

}
