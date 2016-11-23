package se.kth.awesome;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.awesome.model.friendRequest.FriendRequest;
import se.kth.awesome.model.friendRequest.FriendRequestRepository;
import se.kth.awesome.model.User.UserEntity;
import se.kth.awesome.model.User.UserRepository;
import se.kth.awesome.model.chatMessage.ChatMessageRepository;
import se.kth.awesome.model.role.UserRoleRepository;
import se.kth.awesome.model.User.UserPojo;
import se.kth.awesome.util.gson.GsonX;
import se.kth.awesome.util.MediaTypes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.awesome.util.Util.nLin;

@Component
public class PreTest {

	public PreTest() {
	}

	public MockMvc mockMvc;

	public List<UserPojo> userPojoList = new ArrayList<>();
	public List<UserEntity> faceUserList = new ArrayList<>();
	public UserEntity user;


	public FriendRequest friendRequest;
	public List<FriendRequest> friendRequests = new ArrayList<>();

	public UserRoleRepository userReceivedMailRepo;
	public ChatMessageRepository chatMessageRepo;
	public UserRepository userRepo;
	public FriendRequestRepository friendRequestRepo;

	public PreTest(
			MockMvc mockMvc,
			UserRoleRepository userReceivedMailRepo,
			ChatMessageRepository chatMessageRepo,
			UserRepository userRepo,
			FriendRequestRepository friendRequestRepo) {

		this.mockMvc = mockMvc;
		this.userReceivedMailRepo = userReceivedMailRepo;
		this.chatMessageRepo = chatMessageRepo;
		this.userRepo = userRepo;
		this.friendRequestRepo = friendRequestRepo;
	}

	public PreTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}



	public void registerUser(List<UserPojo> userPojos) throws Exception {

		System.out.println(nLin+nLin+"----------------- PreTest.registerUser-start ----------------------------\n\n");
		for(UserPojo U: userPojos){
			this.mockMvc.perform
					(
							post("/social/register")
									.contentType(MediaTypes.JsonUtf8)
									.content(GsonX.gson.toJson(U))
					)
					.andExpect(status().is(HttpStatus.CREATED.value()))
					.andExpect(content().contentType(MediaTypes.JsonUtf8));
		}

		System.out.println(nLin+nLin+"----------------- PreTest.registerUser-end ----------------------------\n\n");
	}

}
