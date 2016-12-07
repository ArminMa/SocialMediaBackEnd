package se.kth.awesome.model.friendRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import se.kth.awesome.model.UserFriends.UserFriend;
import se.kth.awesome.model.UserFriends.UserFriendRepository;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FriendRequestTest {

	private static String nLin = System.lineSeparator();
	@Autowired
	private UserRepository userRepo;
	List<UserEntity> userList = new ArrayList<>();

	@Autowired
	private FriendRequestRepository friendRequestRepo;
	private FriendRequest friendRequest;
	private List<FriendRequest> friendRequests = new ArrayList<>();


	@Autowired
	private UserFriendRepository userFriendRepo;
	private UserFriend userFriend;
	List<UserFriend> fromUser3 = new ArrayList<>();

	@Before
	public void setUp() throws Exception {

		System.out.println("\n\n-----------------UserFriendRepoTest.setUpUserControllerTest-start----------------------------\n\n");

		//------------ creating Users -----------
		userList.add(new UserEntity("FriendRequest0", "FriendRequest0@gmail.com", "PasswordHashed2"));
		userList.add(new UserEntity("FriendRequest1", "FriendRequest1@gmail.com", "PasswordHashed2"));
		userList.add(new UserEntity("FriendRequest2", "FriendRequest2@gmail.com", "PasswordHashed2"));
		userList.add(new UserEntity("FriendRequest3", "FriendRequest3@gmail.com", "PasswordHashed2"));
		userList.add(new UserEntity("FriendRequest4", "FriendRequest4@gmail.com", "PasswordHashed2"));
		//if the this return set or SortedSet dow cast to ArrayList while fix this problem
		userList = userRepo.save( userList);
		userRepo.flush();
		Assert.notNull(userList);
		Assert.isTrue(userList.size() == 5);

		//******************** creating friend request and friends *********************

		//------------ friend request from user 0 to user 1 -----------
		friendRequests.add(new FriendRequest(userList.get(0), userList.get(1), new Date()) );
		//------------ friend request from user 0 to user 1 -----------
		friendRequests.add(new FriendRequest(userList.get(0), userList.get(2), new Date()));
		//------------ friend request from user 1 to user 2 -----------
		friendRequests.add(new FriendRequest(userList.get(1), userList.get(2), new Date()));

		friendRequests = friendRequestRepo.save(friendRequests);
		friendRequestRepo.flush();
		Assert.notNull(friendRequests);
		Assert.isTrue(friendRequests.size() == 3);



		//------------- user 2 and 3 are friends------------
		userFriend = new UserFriend(userList.get(2), userList.get(3), new Date());
		userFriendRepo.save(userFriend);
		userFriendRepo.flush();


		//------------- user 4 and 3 are friends------------
		userFriend = new UserFriend(userList.get(4), userList.get(3), new Date() );
		userFriend = userFriendRepo.save( userFriend  ) ;
		userFriendRepo.flush();
		assertThat( userFriend ).isNotNull();
		Assert.isTrue( userFriend.getPk().getAccepter().getId().equals(userList.get(4).getId() ) );


		//------------- user 3 is friends with user 0,1 ------------
		fromUser3.add( new UserFriend(userList.get(3), userList.get(0), new Date()) );
		fromUser3.add( new UserFriend(userList.get(3), userList.get(1), new Date()) );
		fromUser3 = userFriendRepo.save(fromUser3);
		userFriendRepo.flush();
		Assert.notEmpty(fromUser3);
		Assert.notNull(fromUser3);
		Assert.isTrue(fromUser3.size() == 2);



		System.out.println("\n\n-----------------UserFriendRepoTest.setUpUserControllerTest-End----------------------------\n\n");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("\n\n-----------------UserFriendRepoTest.tearDown-start----------------------------\n\n");

		// this wokrs but we want to remove one by one
//		friendRequestRepo.deleteAllInBatch();
//		friendRequestRepo.flush();
//		userRepo.deleteAllInBatch();
//		userRepo.flush();


		friendRequestRepo.deleteToOrFromByUserId(userList.get(1).getId());
		friendRequestRepo.flush();
		userFriendRepo.deleteAllFriendsBuUserId(userList.get(1).getId());
		userFriendRepo.flush();
		userRepo.delete(userList.get(1).getId());
		userRepo.flush();

		friendRequestRepo.deleteToOrFromByUserId(userList.get(2).getId());
		friendRequestRepo.flush();
		userFriendRepo.deleteAllFriendsBuUserId(userList.get(2).getId());
		userFriendRepo.flush();
		userRepo.delete(userList.get(2).getId());
		userRepo.flush();

		friendRequestRepo.deleteToOrFromByUserId(userList.get(3).getId());
		friendRequestRepo.flush();
		userFriendRepo.deleteAllFriendsBuUserId(userList.get(3).getId());
		userFriendRepo.flush();
		userRepo.delete(userList.get(3).getId());
		userRepo.flush();

		friendRequestRepo.deleteToOrFromByUserId(userList.get(0).getId());
		friendRequestRepo.flush();
		userFriendRepo.deleteAllFriendsBuUserId(userList.get(0).getId());
		userFriendRepo.flush();
		userRepo.delete(userList.get(0).getId());
		userRepo.flush();

		userRepo.delete(userList.get(4).getId());
		userRepo.flush();





		System.out.println("\n\n-----------------UserFriendRepoTest.tearDown-End----------------------------\n\n");
	}


	@Test
	public void friendRequestTest() throws Exception {

		System.out.println("\n\n-----------------UserFriendRepoTest.friendRequestTest-Start----------------------------\n\n");
		//********************* friend request test and friend test *********************

		List<FriendRequest> friendRequestFromUser1List = friendRequestRepo.findAllFriendRequestFromUserByUserId(userList.get(0).getId());
		Assert.isTrue(friendRequestFromUser1List.get(0).getRequestFrom().getId().equals(userList.get(0).getId()));
		Assert.notNull(friendRequestFromUser1List);
		Assert.isTrue(!friendRequestFromUser1List.isEmpty());
		Assert.isTrue( friendRequestFromUser1List.size() == 2);


		//fiend all request to user 2.
		friendRequests = friendRequestRepo.findAllFriendRequestToUserByUserId(userList.get(2).getId() ) ;
		Assert.notNull(friendRequests);
		Assert.notEmpty(friendRequests);
		Assert.isTrue( friendRequests.size() == 2 );     //2 request was sent to user 2


		// user 2 should have no friends
		List<UserEntity> userAndFriendsForUser2 = userRepo.findAllFriendsWithUserByUserId1(userList.get(2).getId());
		assertThat(userAndFriendsForUser2).isNotNull();
		assertThat(userAndFriendsForUser2).hasSize(1);
		//user 2:s only friend is user 3, lets assert that That's true
		assertThat(userAndFriendsForUser2.get(0).getId()).isEqualTo(userList.get(3).getId());

		// remove friendship between users 2 and 3
		userRepo.deleteFriendshipBetweenTheseUsers(userList.get(2).getId(), userList.get(3).getId());
		userRepo.flush();

		userAndFriendsForUser2 = userRepo.findAllFriendsWithUserByUserId1(userList.get(2).getId());
		assertThat(userAndFriendsForUser2).isNotNull();
		assertThat(userAndFriendsForUser2).isEmpty();


		// user 3 should have 3 friends

		List<UserEntity> userAndFriendsForUser3 = userRepo.findAllFriendsWithUserByUserId1(userList.get(3).getId());
		assertThat(userAndFriendsForUser3).isNotEmpty();
		assertThat(userAndFriendsForUser3).hasSize(3);

		// a sorted List with all user 3:s friend should be returned
		assertThat(userAndFriendsForUser3.get(0).getId()).isEqualTo(userList.get(0).getId());
		assertThat(userAndFriendsForUser3.get(1).getId()).isEqualTo(userList.get(1).getId());
		assertThat(userAndFriendsForUser3.get(2).getId()).isEqualTo(userList.get(4).getId());


//		assertThat(userAndFriends.contains(userList.get(1))).isTrue();
//		assertThat(userAndFriends.contains(userList.get(2))).isFalse();
//		assertThat(userAndFriends.contains(userList.get(3))).isFalse();
//		assertThat(userAndFriends.contains(userList.get(4))).isTrue();



		System.out.println("\n-----------------UserFriendRepoTest.friendRequestTest-End----------------------------\n\n");
	}

}