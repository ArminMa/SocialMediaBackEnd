package se.kth.awesome.model.friendRequest;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestFk>,
		JpaSpecificationExecutor<FriendRequest> {




	@Modifying
	@Transactional
	@Query(value = "delete from FriendRequest FR " +
			"where FR.pk.requestFrom.id = :fromUserId AND FR.pk.requestTo.id = :toUserId" )
	void deleteToAndFromByUserId(@Param("fromUserId") Long userFromId, @Param("toUserId") Long UserToId);

	@Modifying
	@Transactional
	@Query(value = "delete from FriendRequest FR " +
			"where FR.pk.requestFrom.id = :userID or FR.pk.requestTo.id = :userID" )
	void deleteToOrFromByUserId(@Param("userID") Long userId);

	@Modifying
	@Transactional
	@Query(value = "delete from FriendRequest FR where FR.pk.requestTo.id = :toUserId" )
	void deleteToUserId(@Param("toUserId") Long UserToId);

	@Modifying
	@Transactional
	@Query(value = "delete from FriendRequest FR where FR.pk.requestFrom.id = :fromUserId" )
	void deleteFromByUserId(@Param("fromUserId") Long userFromId);

	@Query(value = "SELECT FR " +
			"FROM FriendRequest FR where FR.pk.requestFrom.id =:requester")
	List<FriendRequest> findAllFriendRequestFromUserByUserId(@Param("requester") Long fromUserId);

	@Query(value = "SELECT FR FROM FriendRequest FR " +
			"where FR.pk.requestTo.id =:receiver_id")
	List<FriendRequest> findAllFriendRequestToUserByUserId(@Param("receiver_id") Long toUserId);


}
