package pl.lodz.uni.biobank.foam.app.sda

import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

//@SpringBootTest
//@Testcontainers
class SdaListenerTest extends Specification {

    @Shared
    RabbitMQContainer broker = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management-alpine"))
            .withVhost("sda")
            .withExchange("sda", "sda", "topic")
            .withQueue("sda", "sda_unknown_schema")
            .withBinding("sda", "sda", "sda_unknown_schema", new HashMap<String, Object>(), "sda_unknown_schema", "queue")


//    @Autowired
    RabbitTemplate rt

//TODO
    def "Publish message to sda_unknown_schema queue"() {
//        given: "Create RabbitTemplate"
//        def rt = new RabbitTemplate(getConnectionFactory())

        when: "Publish message"
        rt.send("sda_unknown_schema", "sda_unknown_schema", new Message("Placki".getBytes()))

        then: ""
        sleep(1000)
        rt.addListener()
    }

//    @Autowired
//    private  PermissionListener listener
//
//    def "when context is loaded then all expected beans are created"() {
//        expect: "the WebController is created"
//        listener
//    }


//    CachingConnectionFactory getConnectionFactory() {
//        def ccf = new CachingConnectionFactory(broker.getHost(), broker.getAmqpPort())
//        ccf.setUsername(broker.getAdminUsername())
//        ccf.setPassword(broker.getAdminPassword())
//        return ccf;
//    }
}
