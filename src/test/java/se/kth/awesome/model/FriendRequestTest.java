package se.kth.awesome.model;

import java.util.ArrayList;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FriendRequestTest {


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FriendRequestRepository friendRequestRepo;



    List<UserEntity> userEntities = new ArrayList<>();
    List<FriendRequest> friendRequests = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        System.out.println("\n\n----------------- FriendRequestTest.setUp-start ----------------------------\n\n");
        userEntities.add(new UserEntity("testEmail0@gmail.com", "testUser0", "PasswordHashed0")); // userEntities 0
        userEntities.add(new UserEntity("testEmail1@gmail.com", "testUser1", "PasswordHashed1")); // userEntities 1
        userEntities.add(new UserEntity("testEmail2@gmail.com", "testUser2", "PasswordHashed2")); // userEntities 2

        userEntities = userRepo.save(userEntities);
        userRepo.flush();
        assertThat(userEntities).isNotNull();
        assertThat(userEntities.size()).isEqualTo(3);

        friendRequests.add(new FriendRequest(userEntities.get(0),userEntities.get(1))); // friendRequests 0
        friendRequests.add(new FriendRequest(userEntities.get(0),userEntities.get(2))); // friendRequests 1
        friendRequests.add(new FriendRequest(userEntities.get(1),userEntities.get(2))); // friendRequests 2

// userEntities 1 får en

        friendRequests = friendRequestRepo.save(friendRequests);
        friendRequestRepo.flush();
        assertThat(friendRequests).isNotNull();
        assertThat(friendRequests.size()).isEqualTo(3);


//        userEntities.get(1).getFriendRequests().add(friendRequests.get(0));
        friendRequestRepo.save(friendRequests);
        friendRequestRepo.flush();


//        userEntities.get(2).getFriendRequests().add(friendRequests.get(1));
//        userEntities.get(2).getFriendRequests().add(friendRequests.get(2));
        friendRequestRepo.save(friendRequests);
        friendRequestRepo.flush();
        System.out.println("\n\n----------------- FriendRequestTest.setUp-end ----------------------------\n\n");
    }

    @After // TODO remove a friendRequest without removing all the users first
    public void tearDown() throws Exception {
        System.out.println("\n\n----------------- FriendRequestTest.tearDown-start ----------------------------\n\n");
        FriendRequest friendRequestFromUser0ToUser1 =
                friendRequestRepo.findBySenderAndReceiver(userEntities.get(0), userEntities.get(1));
        assertThat(friendRequestFromUser0ToUser1).isNotNull();

        userRepo.deleteFriendsByReceiverAndSenderID(userEntities.get(0).getId() , userEntities.get(1).getId());
        userRepo.flush();

        friendRequestFromUser0ToUser1 =
                friendRequestRepo.findBySenderAndReceiver(userEntities.get(0), userEntities.get(1));
        assertThat(friendRequestFromUser0ToUser1).isNull();


        // no friendRequest to delete after the users are deleted
        assertThat(userEntities).isNotNull();
        userRepo.delete(userEntities);
        userRepo.flush();

        System.out.println("\n\n----------------- FriendRequestTest.tearDown-end ----------------------------\n\n");
    }

    @Test
    public void A_friendRequestTest(){
        System.out.println("\n\n----------------- FriendRequestTest.friendRequestTest-start ----------------------------\n\n");

        assertThat(userEntities).isNotNull();
        assertThat(userEntities.size()).isGreaterThan(2);
        assertThat(friendRequests).isNotNull();
        assertThat(friendRequests.size()).isGreaterThan(0);

        UserEntity user0 = userRepo.findByUsername("testUser1");

        FriendRequest user0FriendRequest = user0.getFriendRequests().first();
        UserEntity receivingUser = user0FriendRequest.getReceiver();
        assertThat(receivingUser.getId()).isEqualTo(userEntities.get(1).getId());







        System.out.println("\n\n----------------- FriendRequestTest.friendRequestTest-end ----------------------------\n\n");
    }


}