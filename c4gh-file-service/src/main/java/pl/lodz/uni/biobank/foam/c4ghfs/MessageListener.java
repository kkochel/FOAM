package pl.lodz.uni.biobank.foam.c4ghfs;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MessageListener {
    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    private final String crypt4ghPrivateKeyPath;
    private final String crypt4ghPrivateKeyPasswordPath;

    private final C4ghService service;

    public MessageListener(C4ghService service, String crypt4ghPrivateKeyPath, String crypt4ghPrivateKeyPasswordPath) {
        this.service = service;
        this.crypt4ghPrivateKeyPath = crypt4ghPrivateKeyPath;
        this.crypt4ghPrivateKeyPasswordPath = crypt4ghPrivateKeyPasswordPath;
    }

    @RabbitListener(queues = "outbox_export", errorHandler = "sdaListenerErrorHandler")
    public void handleEvent(C4ghExportTask event) throws DecoderException, GeneralSecurityException, IOException {
        log.info("Handle export task with id: {}", event.taskId());

        service.encryptionWithReceiverPublicKey(event, crypt4ghPrivateKeyPath, crypt4ghPrivateKeyPasswordPath);
    }
}
