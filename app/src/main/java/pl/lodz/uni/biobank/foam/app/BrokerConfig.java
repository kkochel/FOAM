package pl.lodz.uni.biobank.foam.app;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lodz.uni.biobank.foam.app.sda.SdaListenerErrorHandler;

@Configuration
@EnableRabbit
public class BrokerConfig {

    @Bean
    SdaListenerErrorHandler sdaListenerErrorHandler() {
        return new SdaListenerErrorHandler();
    }

}

