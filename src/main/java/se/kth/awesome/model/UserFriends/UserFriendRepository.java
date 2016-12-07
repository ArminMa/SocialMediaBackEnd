package se.kth.awesome.model.UserFriends;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface UserFriendRepository extends
		JpaRepository<UserFriend, UserFriendID>,
		JpaSpecificationExecutor<UserFriend> {



	@Modifying
	@Transactional
	@Query(value = "delete from UserFriend FR " +
			"where FR.pk.requester.id = :userID or FR.pk.accepter.id = :userID" )
	void deleteAllFriendsBuUserId(@Param("userID") Long userId);


}
