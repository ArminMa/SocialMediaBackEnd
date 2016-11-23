package se.kth.awesome.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.util.gson.GsonX;
import se.kth.awesome.util.MediaTypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class registerUserTest {
	private static String nLin = System.lineSeparator();

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MockMvc mockMvc ;
	private List<UserPojo> userPojos = new ArrayList<>();


	@After
	public void tearDown() throws Exception {
		System.out.println(nLin+nLin+"----------------- registerUserTest.tearDown-start ----------------------------"+nLin+nLin);
		userRepository.deleteAll();
		userRepository.flush();
		System.out.println(nLin+nLin+"----------------- registerUserTest.tearDown-end ----------------------------"+nLin+nLin);
	}



	@Test
	public void registerTest() throws Exception {
		System.out.println("----------------- registerUserTest.registerTest-start ----------------------------");
		userPojos.add(new UserPojo("loginUser", "loginUserTest@gmail.com", "PasswordHashed0"));
		System.out.println("\nGsonX.gson.toJson(userPojos.get(0)) = " + GsonX.gson.toJson(userPojos.get(0)) + nLin);
		HttpServletRequest httpServletRequest = null;
		HttpServletResponse httpServletResponse = null;
		MockHttpServletResponse theResponse =  this.mockMvc.perform
				(
						post("/social/register")
								.contentType(MediaTypes.JsonUtf8)
								.content(GsonX.gson.toJson(userPojos.get(0)))
								.header("keyUserServer","my token should be here")
				)
				.andExpect(status().is(HttpStatus.CREATED.value()))
				.andExpect(content().contentType(MediaTypes.JsonUtf8))
				.andReturn().getResponse();

		assertThat(theResponse).isNotNull();
		System.out.println("\ntheResponse.getStatus = " + theResponse.getStatus() + nLin);

		System.out.println("----------------- registerUserTest.registerTest-end ----------------------------");
	}
}
