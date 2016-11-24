package se.kth.awesome.model.post;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>,
        JpaSpecificationExecutor<Post> {

    @Query(value = "select PM from Post PM where PM.pk.sender.id = :senderId and PM.pk.receiver.id = :receiverId")
    List<Post> findBySenderAndReceiver(@Param("senderId") Long senderID, @Param("receiverId") Long receiverID);

    @Query(value = "select PM from Post PM where PM.pk.sender.username = :userName or PM.pk.receiver.username = :userName")
    List<Post> getAllSentAndReceivedMailByUserName(@Param("userName") String username);

    @Query(value = "select PM from Post PM where PM.pk.receiver.username = :userName")
    Collection<Post> getAllReceivedPostsByUserName(@Param("userName") String username);

    @Query(value = "select PM from Post PM where PM.id = :iD")
    Post getPost(@Param("iD")Long ID);
}
