package se.kth.awesome.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.awesome.model.ModelConverter;
import se.kth.awesome.model.TokenPojo;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.model.user.UserRepository;
import se.kth.awesome.model.post.Post;
import se.kth.awesome.model.post.PostPojo;
import se.kth.awesome.model.post.PostRepository;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.role.UserRoleEntity;
import se.kth.awesome.model.role.UserRoleRepository;
import se.kth.awesome.util.gsonX.GsonX;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.awesome.util.Util.nLin;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeletePostTest {


	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;

	private TokenPojo tokenPojo;
	private List<UserPojo> userPojos = new ArrayList<>();
	private List<PostPojo> postPojos = new ArrayList<>();
	private List<Post> postsEntity = new ArrayList<>();
	private List<UserEntity> userEntities = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		System.out.println(nLin+nLin+"----------------- DeletePostTest.setUp-start ----------------------------"+nLin+nLin);


		List<UserRoleEntity> userRoleEntities = new ArrayList<>();
		userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
		userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
		userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
		userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
		userRoleEntities = userRoleRepo.save(userRoleEntities);

		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
		userEntities.add(new UserEntity("getPostTest1", "getPostTest1@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userEntities.add(new UserEntity("getPostTest2", "getPostTest2@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userEntities.add(new UserEntity("getPostTest3", "getPostTest4@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userEntities.add(new UserEntity("getPostTest4", "getPostTest5@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userEntities.get(0).getAuthorities().add(userRoleEntities.get(0));
		userEntities.get(1).getAuthorities().add(userRoleEntities.get(1));
		userEntities.get(2).getAuthorities().add(userRoleEntities.get(2));
		userEntities.get(3).getAuthorities().add(userRoleEntities.get(3));
		userEntities = userRepo.save(userEntities);
		userRepo.flush();
		assertThat(userEntities).isNotNull();
		assertThat(userEntities).isNotEmpty();
		userPojos = (List<UserPojo>) ModelConverter.convert(userEntities);
		userPojos.get(0).setPassword("PasswordHashed0");
		userPojos.get(1).setPassword("PasswordHashed0");
		userPojos.get(2).setPassword("PasswordHashed0");
		userPojos.get(3).setPassword("PasswordHashed0");

//String messageContent, String topic, UserEntity sender, UserEntity receiver)
		postPojos.add(new PostPojo("P1", userPojos.get(0),userPojos.get(0)));
		postPojos.add(new PostPojo("P2", userPojos.get(0),userPojos.get(1)));
		postPojos.add(new PostPojo("P3", userPojos.get(0),userPojos.get(2)));
		postPojos.add(new PostPojo("P4", userPojos.get(0),userPojos.get(3)));
		postPojos.add(new PostPojo("P5", userPojos.get(1),userPojos.get(0)));
		postPojos.add(new PostPojo("P6", userPojos.get(2),userPojos.get(3)));
		postsEntity = postRepository.save((Collection<Post>) ModelConverter.convert(postPojos));
		postRepository.flush();
		assertThat(postsEntity).isNotNull();
		assertThat(postsEntity).isNotEmpty();
		assertThat(postsEntity.size()).isEqualTo(postPojos.size());

		postPojos = (List<PostPojo>) ModelConverter.convert(postsEntity);



		String tokenJson =  this.mockMvc.perform
				(
						post("/api/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(GsonX.gson.toJson(userPojos.get(0)))
								.header("X-Requested-With","XMLHttpRequest")
								.header("Cache-Control","no-cache")
				)
				.andExpect(status().is(HttpStatus.OK.value()))
				.andReturn().getResponse().getContentAsString();
		assertThat(tokenJson).isNotNull();
		tokenPojo = GsonX.gson.fromJson(tokenJson, TokenPojo.class);
		System.out.println(nLin+nLin+"----------------- DeletePostTest.setUp-end ----------------------------"+nLin+nLin);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(nLin+nLin+"----------------- DeletePostTest.tearDown-start ----------------------------"+nLin+nLin);

		Collection<Post> posts = postRepository.findAll();
		assertThat(posts).isNotNull();
		assertThat(posts).isNotEmpty();

		postRepository.delete(postsEntity);
		postRepository.flush();
		assertThat(userPojos).isNotNull();
		//check that the user is not deleted
		UserEntity userEntity = userRepo.findByEmail(userPojos.get(0).getEmail());
		assertThat(userEntity).isNotNull();


		userRepo.delete(userEntities);
		userRepo.flush();
		System.out.println(nLin+nLin+"-----------------   DeletePostTest.tearDown-end ----------------------------"+nLin+nLin);
	}


	@Test
	public void deletePost() throws Exception {
		System.out.println(nLin+nLin+"----------------- DeletePostTest.sendPost.start ----------------------------"+nLin+nLin);
		assertThat(tokenPojo).isNotNull();
		assertThat(userPojos).isNotNull();
		assertThat(postPojos).isNotNull();

		MockHttpServletResponse theResponse =  this.mockMvc.perform
				(
						post("/api/deleteLogMessage")
								.contentType(MediaType.APPLICATION_JSON)
								.content(GsonX.gson.toJson(postPojos.get(0)))
								.header("X-Authorization", "Bearer " + tokenPojo.getToken())
								.header("Cache-Control", "no-cache")
				)
				.andExpect(status().is(HttpStatus.OK.value()))
				.andReturn().getResponse();
		assertThat(theResponse).isNotNull();

		System.out.println(nLin+nLin+"-----------------   DeletePostTest.sendPost.end ----------------------------"+nLin+nLin);
	}

}
