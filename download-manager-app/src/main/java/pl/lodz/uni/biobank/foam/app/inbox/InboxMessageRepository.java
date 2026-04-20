package pl.lodz.uni.biobank.foam.app.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxMessageRepository extends JpaRepository<InboxMessage, Long> {

    boolean existsByMessageIdAndQueueName(String messageId, String queueName);
}
