package pl.lodz.uni.biobank.foam.c4ghfs;

import org.apache.commons.codec.DecoderException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MessageListener {
    private final String crypt4ghPrivateKeyPath;
    private final String crypt4ghPrivateKeyPasswordPath;

    private final C4ghService service;

    public MessageListener(C4ghService service, String crypt4ghPrivateKeyPath, String crypt4ghPrivateKeyPasswordPath) {
        this.service = service;
        this.crypt4ghPrivateKeyPath = crypt4ghPrivateKeyPath;
        this.crypt4ghPrivateKeyPasswordPath = crypt4ghPrivateKeyPasswordPath;
    }

    @RabbitListener(queues = "outbox_export", errorHandler = "sdaListenerErrorHandler")
    public void receiveMessage(C4ghExportTask task) throws DecoderException, GeneralSecurityException, IOException {
        service.encryptionWithReceiverPublicKey(task, crypt4ghPrivateKeyPath, crypt4ghPrivateKeyPasswordPath);
    }
}
