package pl.lodz.uni.biobank.foam.app.inbox;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(schema = "inbox", name = "inbox_messages")
public class InboxMessage {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "inbox_generator")
    @SequenceGenerator(name = "inbox_generator", sequenceName = "inbox_seq", schema = "inbox", allocationSize = 1)
    private Long id;

    @Column(name = "message_id", nullable = false)
    private String messageId;

    @Column(name = "queue_name", nullable = false)
    private String queueName;

    @Column(name = "payload", nullable = false, columnDefinition = "text")
    private String payload;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    protected InboxMessage() {
    }

    public InboxMessage(String messageId, String queueName, String payload) {
        this.messageId = messageId;
        this.queueName = queueName;
        this.payload = payload;
        this.receivedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getPayload() {
        return payload;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }
}
