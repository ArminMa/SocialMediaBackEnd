package se.kth.awesome.controller;

import org.assertj.core.util.Compatibility;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.pojos.ping.PingPojo;
import se.kth.awesome.util.GsonX;
import se.kth.awesome.util.MediaTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sebastian on 2016-11-12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc ;
    //	private MockRestServiceServer mockServer;
    @Autowired
    private UserRepository userRepository;
    List<UserEntity> userEntities = new ArrayList<>();
    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        UserEntity user = new UserEntity();
        user.setUserName("test");
        user.setPassword("test");
        user.setEmail("test@test.test");
        userEntities.add(user);
        userEntities = userRepository.save(userEntities);
        userRepository.flush();
    }

//    @After
//    public void tearDown() throws Exception {
//
//    }

    @After
    public void tearDown() throws Exception {
        System.out.println("\n\n----------------- UserRepositoryTest.tearDown-start ----------------------------\n\n");
        assertThat(userEntities).isNotNull();
        userRepository.delete(userEntities);
        userRepository.flush();
        System.out.println("\n\n----------------- UserRepositoryTest.tearDown-end ----------------------------\n\n");
    }

    @Test
    public void getUserByEmail() throws Exception {
        System.out.println("\n\n----------------- UserControllerTest.testGetUser.start ----------------------------\n\n");
        UserPojo userPojo = GsonX.gson.fromJson(
                this.mockMvc.perform(get("/getEmail/test@test.test").accept(MediaTypes.JsonUtf8))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.JsonUtf8))
                        .andReturn().getResponse().getContentAsString()
                , UserPojo.class);
        assertThat(userPojo).isNotNull();
//        UserPojo ping = new UserPojo();
//        ping.setEmail("test@test.test");
//        ping.setPassword("test");
        assertThat(userPojo.getEmail()).isEqualTo("test@test.test");
        System.out.println("this is how userpojo looks like "+ System.lineSeparator() + userPojo.toString());
        System.out.println("\n\n----------------- UserControllerTest.testGetUser.end ----------------------------\n\n");
    }

    /**
     * Assumes there is a user named test@test.test name test pass test in database
     * @throws Exception
     */
    @Test
    public void login() throws Exception {
        System.out.println("\n\n----------------- UserControllerTest.login.start ----------------------------\n\n");
        String result = GsonX.gson.fromJson(
                this.mockMvc.perform(get("/login/test/test").accept(MediaTypes.JsonUtf8))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.JsonUtf8))
                        .andReturn().getResponse().getContentAsString()
                , String.class);
        assertThat(result).isNotNull();
        assertThat(result.equals("success"));
        System.out.println("\n\n----------------- UserControllerTest.login.end ----------------------------\n\n");
    }

    /**
     * Assumes there is a user with a name containing e in the database
     * @throws Exception
     **/
    @Test
    public void searchUsersByString() throws Exception {
        System.out.println("\n\n----------------- UserControllerTest.searchUsersByString.start ----------------------------\n\n");
        Collection<UserEntity> result = (Collection<UserEntity>)GsonX.gson.fromJson(
                this.mockMvc.perform(get("/userSearch/e").accept(MediaTypes.JsonUtf8))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.JsonUtf8))
                        .andReturn().getResponse().getContentAsString()
                , Collection.class);
        assertThat(result).isNotNull();
        assertThat(!result.isEmpty());

        System.out.println("\n\n----------------- UserControllerTest.searchUsersByString.end ----------------------------\n\n");
    }

}