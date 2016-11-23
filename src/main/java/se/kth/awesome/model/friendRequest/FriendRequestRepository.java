package se.kth.awesome.model.friendRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.kth.awesome.model.User.UserEntity;

/**
 * Created by Sys on 2016-11-07.
 */
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long>,
        JpaSpecificationExecutor<FriendRequest> {

    FriendRequest findBySenderAndReceiver(UserEntity sender, UserEntity receiver);
}
