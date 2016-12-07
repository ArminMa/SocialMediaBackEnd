package se.kth.awesome.model.user;

import java.util.Collection;
import java.util.List;
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

	@Query(value = "select distinct UE FROM UserEntity UE " +
			"where UE.email = :eMmail or UE.username = :userNName" )
	UserEntity findOneUserByEmailOrUsername(
			@Param("eMmail") String email,
			@Param("userNName") String userName);


//	@Modifying
//    @Transactional
//    @Query(value = "delete from FriendRequest FR " +
//            "where FR.pk.requestFrom.id = :senderId and FR.pk.requestTo.id = :receiverId" )
//    void deleteFriendsByReceiverAndSenderID(@Param("senderId") Long senderID, @Param("receiverId") Long receiverID);


    @Modifying
    @Transactional
    @Query(value = "delete from UserFriend FR " +
            "where " +
            "FR.pk.accepter.id = :userId1 and FR.pk.requester.id = :userId2 " +
            "or " +
            "FR.pk.requester.id = :userId1 and FR.pk.accepter.id = :userId2" )
    void deleteFriendshipBetweenTheseUsers(@Param("userId1") Long userID1, @Param("userId2") Long userID2);

    @Query(value = "select U from UserEntity U, UserFriend UF " +
            "where U.id = UF.pk.requester.id and UF.pk.accepter.id = :userId or " +
            "U.id = UF.pk.accepter.id and UF.pk.requester.id = :userId" )
    List<UserEntity> findAllFriendsWithUserByUserId1(@Param("userId") Long userID);

    @Query(value = "select UF FROM UserFriend UF " +
            "where :userId = UF.pk.accepter.id or  :userId = UF.pk.requester.id" )
    List<UserEntity> findAllFriendsWithUserByUserId2(@Param("userId") Long userID);
}
