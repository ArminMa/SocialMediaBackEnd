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
public class SendPostTest {


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
	private List<Post> posts = new ArrayList<>();
	private List<UserEntity> userEntities = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		System.out.println(nLin+nLin+"----------------- SendPostTest.setUp-start ----------------------------"+nLin+nLin);

		List<UserRoleEntity> userRoleEntities = new ArrayList<>();
		userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
		userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
		userRoleEntities = userRoleRepo.save(userRoleEntities);
		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
		userEntities.add(new UserEntity("testUserPost1", "testUserPost1@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userEntities.add(new UserEntity("testUserPost2", "testUserPost2@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userEntities.get(0).getAuthorities().add(userRoleEntities.get(0));
		userEntities.get(1).getAuthorities().add(userRoleEntities.get(1));
		userEntities = userRepo.save(userEntities);
		userRepo.flush();
		assertThat(userEntities).isNotNull();
		assertThat(userEntities).isNotEmpty();
		userPojos = (List<UserPojo>) ModelConverter.convert(userEntities);
		userPojos.get(0).setPassword("PasswordHashed0");
		userPojos.get(1).setPassword("PasswordHashed0");

		assertThat(userPojos).isNotNull();
		assertThat(userPojos).isNotEmpty();

		String tokenJson =  this.mockMvc.perform
				(
						post("/api/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userPojos.get(0).toString())
								.header("X-Requested-With","XMLHttpRequest")
								.header("Cache-Control","no-cache")
				)
				.andExpect(status().is(HttpStatus.OK.value()))
				.andReturn().getResponse().getContentAsString();
		assertThat(tokenJson).isNotNull();

		tokenPojo = GsonX.gson.fromJson(tokenJson, TokenPojo.class);
		System.out.println(nLin+nLin+"----------------- SendPostTest.setUp-end ----------------------------"+nLin+nLin);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(nLin+nLin+"----------------- SendPostTest.tearDown-start ----------------------------"+nLin+nLin);

		Collection<Post> posts = postRepository.findAll();
		assertThat(posts).isNotNull();
		assertThat(posts).isNotEmpty();

		postRepository.deleteAll();
		postRepository.flush();
		assertThat(userPojos).isNotNull();
		//check that the user is not deleted
		UserEntity userEntity = userRepo.findByEmail(userPojos.get(0).getEmail());
		assertThat(userEntity).isNotNull();


		userRepo.deleteAll();
		userRepo.flush();
		System.out.println(nLin+nLin+"-----------------   SendPostTest.tearDown-end ----------------------------"+nLin+nLin);
	}


	@Test
	public void sendPost() throws Exception {
		System.out.println(nLin+nLin+"----------------- SendPostTest.sendPost.start ----------------------------"+nLin+nLin);
		PostPojo postPojo = new PostPojo("P1", userPojos.get(0),userPojos.get(1));
		assertThat(tokenPojo).isNotNull();
		assertThat(userPojos).isNotNull();
		MockHttpServletResponse theResponse =  this.mockMvc.perform
				(
						post("/api/sendPost")
								.contentType(MediaType.APPLICATION_JSON)
								.content(GsonX.gson.toJson(postPojo))
								.header("X-Authorization", "Bearer " + tokenPojo.getToken())
								.header("Cache-Control", "no-cache")
				)
				.andExpect(status().is(HttpStatus.CREATED.value()))
				.andReturn().getResponse();
		assertThat(theResponse).isNotNull();

		System.out.println(nLin+nLin+"-----------------   SendPostTest.sendPost.end ----------------------------"+nLin+nLin);
	}
}
