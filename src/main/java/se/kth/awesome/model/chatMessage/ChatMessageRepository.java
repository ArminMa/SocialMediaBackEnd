package se.kth.awesome.model.chatMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.chatMessage.ChatMessage;

import java.util.List;
import java.util.SortedSet;

/**
 * Created by Sys on 2016-11-07.
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>,
        JpaSpecificationExecutor<ChatMessage> {


    @Query(value = "select urm from ChatMessage urm where urm.pk.receivingUser.id = :userId or urm.pk.author = :userId")
    SortedSet<ChatMessage> findChatMessagesByUserId(@Param("userId") Long userID);

    @Query(value = "select urm from ChatMessage urm where urm.pk.receivingUser.id = :userId")
    SortedSet<ChatMessage> findChatMessageToUserByUserId(@Param("userId") Long userID);

    @Query(value = "select urm from ChatMessage urm where urm.pk.receivingUser.id = :userId")
    SortedSet<ChatMessage> findReceivedChatMessageByUserId(@Param("userId") Long userID);

    @Query(value = "select urm from ChatMessage urm where urm.pk.receivingUser.id = :userId")
    SortedSet<ChatMessage> findSentChatMessageByUserId(@Param("userId") Long userID);

    @Query(value = "select urm from ChatMessage urm where urm.pk.author.id = :senderId and urm.pk.receivingUser.id = :receiverId")
    List<ChatMessage> findBySenderAndReceiver(@Param("senderId") Long senderID, @Param("receiverId") Long receiverID);


    @Modifying
    @Transactional
    @Query(value = "delete from ChatMessage FR where FR.pk.receivingUser.id = :userId or FR.pk.author.id = :userId" )
    void deleteChatMessageByUserId(@Param("userId") Long userID);

    @Modifying
    @Transactional
    @Query(value = "delete from ChatMessage FR where FR.pk.receivingUser.id = :userId" )
    void deleteReceivedChatMessageByUserId(@Param("userId") Long userID);

    @Modifying
    @Transactional
    @Query(value = "delete from ChatMessage FR where FR.pk.author.id = :userId" )
    void deleteSentChatMessageByUserId(@Param("userId") Long userID);
}
