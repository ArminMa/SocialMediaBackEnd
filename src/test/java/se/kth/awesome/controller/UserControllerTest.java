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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.model.modelConverter.ModelConverter;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.pojos.UserRolePojo;
import se.kth.awesome.util.GsonX;
import se.kth.awesome.util.MediaTypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.awesome.util.Util.nLin;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    private List<UserPojo> userPojos = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

	@SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- UserControllerTest.setUp-start ----------------------------"+nLin+nLin);
		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
        userPojos.add(new UserPojo("testUser", "test@test.test", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
	    userPojos.get(0).getRoles().add(new UserRolePojo(Role.MEMBER));
	    userRepository.save((Collection<UserEntity>) ModelConverter.convert(userPojos));
		userRepository.flush();
        System.out.println(nLin+nLin+"----------------- UserControllerTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- UserControllerTest.tearDown-start ----------------------------"+nLin+nLin);
        userRepository.deleteAll();
        userRepository.flush();
        System.out.println(nLin+nLin+"----------------- UserControllerTest.tearDown-end ----------------------------"+nLin+nLin);
    }

    @Test
    public void getUserByEmail() throws Exception {
        System.out.println(nLin+nLin+"----------------- UserControllerTest.getUserByEmail.start ----------------------------"+nLin+nLin);
        UserPojo userPojo = GsonX.gson.fromJson(
                this.mockMvc.perform(get("/social/getEmail/test@test.test").accept(MediaTypes.JsonUtf8))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.JsonUtf8))
                        .andReturn().getResponse().getContentAsString()
                , UserPojo.class);
        assertThat(userPojo).isNotNull();
        assertThat(userPojo.getEmail()).isEqualTo("test@test.test");
        System.out.println("this is how userpojo looks like "+ System.lineSeparator() + userPojo.toString());
        System.out.println(nLin+nLin+"----------------- UserControllerTest.getUserByEmail.end ----------------------------"+nLin+nLin);
    }




}