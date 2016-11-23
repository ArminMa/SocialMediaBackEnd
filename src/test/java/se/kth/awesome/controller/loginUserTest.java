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
import se.kth.awesome.model.User.UserEntity;
import se.kth.awesome.model.User.UserRepository;
import se.kth.awesome.model.ModelConverter;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.User.UserPojo;
import se.kth.awesome.model.role.UserRolePojo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.awesome.util.Util.nLin;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class loginUserTest {

	@Autowired
	private MockMvc mockMvc;

	private List<UserEntity> userEntities = new ArrayList<>();
	private List<UserPojo> userPojos = new ArrayList<>();

	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		System.out.println(nLin+nLin+"----------------- loginUserTest.setUp-start ----------------------------"+nLin+nLin);

		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
		userPojos.add(new UserPojo("loginUser", "loginUserTest@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userPojos.get(0).getRoles().add(new UserRolePojo(Role.MEMBER));
		userRepository.save((Collection<UserEntity>) ModelConverter.convert(userPojos));
		userRepository.flush();
		System.out.println(nLin+nLin+"----------------- loginUserTest.setUp-end ----------------------------"+nLin+nLin);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(nLin+nLin+"----------------- loginUserTest.tearDown-start ----------------------------"+nLin+nLin);
		assertThat(userEntities).isNotNull();
		userRepository.delete(userEntities);
		userRepository.flush();
		System.out.println(nLin+nLin+"----------------- loginUserTest.tearDown-end ----------------------------"+nLin+nLin);
	}


	@Test
	public void login() throws Exception {

		System.out.println(nLin+nLin+"----------------- loginUserTest.login.start ----------------------------"+nLin+nLin);

		UserEntity entitySaved = userRepository.findByEmail("loginUserTest@gmail.com");
		System.out.println(nLin+nLin+"------------------------ userSaved? -------------------------"+nLin);
		System.out.println(nLin+entitySaved.toString() + nLin);

		UserPojo userPojoTryToLogin = userPojos.get(0);
		userPojoTryToLogin.setPassword("PasswordHashed0");
		MockHttpServletResponse theResponse =  this.mockMvc.perform
				(
						post("/api/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userPojoTryToLogin.toString())
								.header("X-Requested-With","XMLHttpRequest")
//                                .header("Content-Type", "application/json")
								.header("Cache-Control","no-cache")
				)
				.andExpect(status().is(HttpStatus.OK.value()))
				.andReturn().getResponse();
		assertThat(theResponse).isNotNull();
		System.out.println("theRespone = " + theResponse.getContentAsString());

		System.out.println(nLin+nLin+"------------------------ TOKEN -------------------------");
		System.out.println(theResponse.getContentAsString() + nLin+nLin);

		System.out.println(nLin+nLin+"----------------- loginUserTest.login.end ----------------------------"+nLin+nLin);

	}
}
