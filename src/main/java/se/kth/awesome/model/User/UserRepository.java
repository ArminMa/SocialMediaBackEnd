package se.kth.awesome.model.User;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

    @Query(value = "select U FROM UserEntity U WHERE U.email = :theEmail")
    UserEntity findByEmail(@Param("theEmail") String email);

    @Query(value = "select U FROM UserEntity U WHERE U.id = :userId")
    UserEntity findByUserID(@Param("userId") Long userID);

    @Query(value = "select U FROM UserEntity U WHERE U.username = :theUserName")
    UserEntity findByUsername(@Param("theUserName") String userName);

    @Query(value = "select U FROM UserEntity U WHERE U.username LIKE CONCAT('%',:searchString,'%') ORDER BY U.username DESC")
    Collection<UserEntity> searchUsersResemblingByUsername(@Param("searchString") String searchString);

    @Modifying
    @Transactional
    @Query(value = "delete from FriendRequest FR " +
            "where FR.receiver.id = :senderId and FR.receiver.id = :receiverId" )
    void deleteFriendsByReceiverAndSenderID(@Param("senderId") Long senderID, @Param("receiverId") Long receiverID);


    @Query(value = "select distinct UE FROM UserEntity UE " +
            "where UE.email = :eMmail or UE.username = :userNName" )
    UserEntity findOneUserByEmailOrUsername(
		    @Param("eMmail") String email,
		    @Param("userNName") String userName);
}
