package se.kth.awesome.model.mailMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Am on 11/22/2016.
 */
public interface MailMessageRepository extends JpaRepository<MailMessage, Long>,
        JpaSpecificationExecutor<MailMessage> {

    @Query(value = "select MM from MailMessage MM where MM.pk.sender.id = :senderId and MM.pk.receiver.id = :receiverId")
    List<MailMessage> findBySenderAndReceiver(@Param("senderId") Long senderID, @Param("receiverId") Long receiverID);

}
