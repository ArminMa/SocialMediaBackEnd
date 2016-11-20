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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.model.modelConverter.ModelConverter;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.pojos.TokenPojo;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.pojos.UserRolePojo;
import se.kth.awesome.util.GsonX;
import se.kth.awesome.util.MediaTypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.awesome.util.Util.nLin;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class userAuthTest {

	@Autowired
	private MockMvc mockMvc;

	private List<UserPojo> userPojos = new ArrayList<>();

	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		System.out.println(nLin+nLin+"----------------- userAuthTest.setUp-start ----------------------------"+nLin+nLin);
		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
		userPojos.add(new UserPojo("testUser", "test@test.test", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userPojos.get(0).getRoles().add(new UserRolePojo(Role.MEMBER));
		userRepository.save((Collection<UserEntity>) ModelConverter.convert(userPojos));
		userRepository.flush();
		System.out.println(nLin+nLin+"----------------- userAuthTest.setUp-end ----------------------------"+nLin+nLin);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("\n\n----------------- userAuthTest.tearDown-start ----------------------------"+nLin+nLin);
		userRepository.deleteAll();
		userRepository.flush();
		System.out.println("\n\n----------------- userAuthTest.tearDown-end ----------------------------"+nLin+nLin);
	}

	@Test
	public void verifyUserToken() throws Exception {

//		X-Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdmxhZGFAZ21haWwuY29tIiwic2NvcGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1BSRU1JVU1fTUVNQkVSIl0sImlzcyI6Imh0dHA6Ly9zdmxhZGEuY29tIiwiaWF0IjoxNDcyMzkwMDY1LCJleHAiOjE0NzIzOTA5NjV9.Y9BR7q3f1npsSEYubz-u8tQ8dDOdBcVPFN7AIfWwO37KyhRugVzEbWVPO1obQlHNJWA0Nx1KrEqHqMEjuNWo5w
//		Cache-Control: no-cache
		UserPojo userPojoTryToLogin = userPojos.get(0);
		userPojoTryToLogin.setPassword("PasswordHashed0");
		System.out.println("\n\n----------------- userAuthTest.testGetUser.start ----------------------------"+nLin+nLin);
		String tokenJson =  this.mockMvc.perform
				(
						post("/api/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userPojoTryToLogin.toString())
								.header("X-Requested-With","XMLHttpRequest")
//                                .header("Content-Type", "application/json")
								.header("Cache-Control","no-cache")
				)
				.andExpect(status().is(HttpStatus.OK.value()))
				.andReturn().getResponse().getContentAsString();
		assertThat(tokenJson).isNotNull();

		TokenPojo tokenPojo = GsonX.gson.fromJson(tokenJson, TokenPojo.class);
		System.out.println(nLin+ "token = " + tokenPojo.toString() + nLin);

		this.mockMvc.perform(get("/api/me").
				accept(MediaTypes.JsonUtf8)
				.header("X-Authorization", "Bearer " + tokenPojo.getToken())
				.header("Cache-Control", "no-cache"))

				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.JsonUtf8))
				.andReturn().getResponse().getContentAsString();

		System.out.println("\n\n----------------- userAuthTest.testGetUser.end ----------------------------"+nLin+nLin);
	}
}
