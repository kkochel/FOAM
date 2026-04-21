package pl.lodz.uni.biobank.foam.app.inbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class InboxService {
    private static final Logger log = LoggerFactory.getLogger(InboxService.class);

    private final InboxMessageRepository repository;

    public InboxService(InboxMessageRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isDuplicate(String messageId, String queueName, String payload) {
        boolean duplicate = repository.existsByMessageIdAndQueueName(messageId, queueName);
        if (duplicate) {
            log.info("Duplicate message received. messageId={}, queue={}", messageId, queueName);
        }
        repository.save(new InboxMessage(messageId, queueName, payload, duplicate));
        return duplicate;
    }

    public static String messageId(Message message) {
        String id = message.getMessageProperties().getMessageId();
        if (id != null) {
            return id;
        }
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(message.getBody());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
