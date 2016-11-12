package se.kth.awesome.model;

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
    UserEntity findByUserID (@Param("userId") Long userID);

    @Query(value = "select U FROM UserEntity U WHERE U.userName = :theUserName")
    UserEntity findByUsername (@Param("theUserName") String userName);

    @Modifying
    @Transactional
    @Query(value = "delete from FriendRequest FR " +
            "where FR.sender.id = :senderId and FR.receiver.id = :receiverId" )
    void deleteFriendsByReceiverAndSenderID(@Param("senderId") Long senderID, @Param("receiverId") Long receiverID);


    UserEntity  findOneByUserNameOrEmail(String userName, String email);
//    UserEntity findOneUserByEmailOrUsername(String email, String userName);
}
