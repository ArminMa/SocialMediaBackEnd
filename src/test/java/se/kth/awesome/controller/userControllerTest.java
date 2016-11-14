package se.kth.awesome.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;

import se.kth.awesome.pojos.UserPojo;

import se.kth.awesome.util.GsonX;
import se.kth.awesome.util.MediaTypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class userControllerTest {


    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc ;

    private List<UserEntity> userEntities = new ArrayList<>();
    private List<UserPojo> userPojos = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("\n\n----------------- userControllerTest.tearDown-start ----------------------------\n\n");
        assertThat(userEntities).isNotNull();

        userRepository.delete(userEntities);
        userRepository.flush();
        System.out.println("\n\n----------------- userControllerTest.tearDown-end ----------------------------\n\n");
    }

    @Test
    public void registerTest() throws Exception {
        System.out.println("----------------- userControllerTest.registerTest-start ----------------------------");
        userPojos.add(new UserPojo("TestUserController0@gmail.com", "TestUserController0", "PasswordHashed0"));
        System.out.println("\nGsonX.gson.toJson(userPojos.get(0)) = " + GsonX.gson.toJson(userPojos.get(0)) + "\n");
        HttpServletRequest httpServletRequest = null;
        HttpServletResponse httpServletResponse = null;
        MockHttpServletResponse theResponse =  this.mockMvc.perform
                (
                        post("/register")
                                .contentType(MediaTypes.JsonUtf8)
                                .content(GsonX.gson.toJson(userPojos.get(0)))
                                .header("keyUserServer","my token should be here")
                )
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().contentType(MediaTypes.JsonUtf8))
                .andReturn().getResponse();

        assertThat(theResponse).isNotNull();
        System.out.println("\ntheResponse.getStatus = " + theResponse.getStatus() + "\n");

        System.out.println("----------------- userControllerTest.registerTest-end ----------------------------");

    }

}