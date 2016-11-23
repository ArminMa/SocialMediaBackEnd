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
import se.kth.awesome.model.mailMessage.MailMessageRepository;
import se.kth.awesome.model.modelConverter.ModelConverter;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.pojos.TokenPojo;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.pojos.UserRolePojo;
import se.kth.awesome.util.gson.GsonX;
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
public class UserAuthTest {

    @Autowired
    private MockMvc mockMvc;

    private List<UserPojo> userPojos = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailMessageRepository mailMessageRepo;

    private TokenPojo tokenPojo;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- UserAuthTest.setUp-start ----------------------------"+nLin+nLin);
		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
        userPojos.add(new UserPojo("testUser", "test@test.test", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
        userPojos.add(new UserPojo("testUser2", "test2@test2.test2", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
        userPojos.get(0).getRoles().add(new UserRolePojo(Role.MEMBER));
        List<UserEntity> userEntities = userRepository.save((Collection<UserEntity>) ModelConverter.convert(userPojos));
        userRepository.flush();
        userPojos.get(0).setPassword("PasswordHashed0");
        userPojos.get(0).setId(userEntities.get(0).getId());

        userPojos.get(1).setPassword("PasswordHashed0");
        userPojos.get(1).setId(userEntities.get(1).getId());

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
        System.out.println(nLin+nLin+"----------------- UserAuthTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- UserAuthTest.tearDown-start ----------------------------"+nLin+nLin);
        userRepository.deleteAll();
        userRepository.flush();
        System.out.println(nLin+nLin+"----------------- UserAuthTest.tearDown-end ----------------------------"+nLin+nLin);
    }

    @Test
    public void a_verifyUserToken() throws Exception {

//		X-Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdmxhZGFAZ21haWwuY29tIiwic2NvcGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1BSRU1JVU1fTUVNQkVSIl0sImlzcyI6Imh0dHA6Ly9zdmxhZGEuY29tIiwiaWF0IjoxNDcyMzkwMDY1LCJleHAiOjE0NzIzOTA5NjV9.Y9BR7q3f1npsSEYubz-u8tQ8dDOdBcVPFN7AIfWwO37KyhRugVzEbWVPO1obQlHNJWA0Nx1KrEqHqMEjuNWo5w
//		Cache-Control: no-cache
        assertThat(tokenPojo).isNotNull();
        System.out.println(nLin+nLin+"----------------- UserAuthTest.testGetUser.start ----------------------------"+nLin+nLin);

        System.out.println(nLin+ "token = " + tokenPojo.toString() + nLin);

        this.mockMvc.perform(get("/api/me").
                accept(MediaTypes.JsonUtf8)
                .header("X-Authorization", "Bearer " + tokenPojo.getToken())
                .header("Cache-Control", "no-cache"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.JsonUtf8))
                .andReturn().getResponse().getContentAsString();

        System.out.println(nLin+nLin+"----------------- UserAuthTest.testGetUser.end ----------------------------"+nLin+nLin);
    }

    @Test
    public void b_verifyUserRoleAuth() throws Exception {

        System.out.println(nLin+nLin+"----------------- UserAuthTest.verifyUserRoleAuth.start ----------------------------"+nLin+nLin);
        assertThat(tokenPojo).isNotNull();
//		//test role auth
//		System.out.println(nLin+nLin+"token valid. trying auth with role"+ nLin+ nLin);
//		String something = this.mockMvc.perform(get("/something").
//				accept(MediaTypes.JsonUtf8)
//				.header("X-Authorization", "Bearer " + tokenPojo.getToken())
//				.header("Cache-Control", "no-cache"))
//
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaTypes.JsonUtf8))
//				.andReturn().getResponse().getContentAsString();
//
//		assertThat(something).isNotNull();
//		assertThat(something).isEqualTo("{success: ok}");
        System.out.println(nLin+nLin+"----------------- UserAuthTest.verifyUserRoleAuth.start ----------------------------"+nLin+nLin);
    }


    /**
     * Assumes there is a user with a name containing e in the database
     * @throws Exception
     **/
    @SuppressWarnings("unchecked")
    @Test
    public void searchUsersByString() throws Exception {
        assertThat(tokenPojo).isNotNull();
        assertThat(userPojos).isNotNull();
        String userName =  userPojos.get(0).getUsername().substring(1,(userPojos.get(0).getUsername().length()-1));
        System.out.println(nLin+nLin+"----------------- UserControllerTest.searchUsersByString.start ----------------------------"+nLin+nLin);
        Collection<UserEntity> result = (Collection<UserEntity>) GsonX.gson.fromJson(
                this.mockMvc.perform(
                                get("/api/userSearch/"+userName)
                                .accept(MediaTypes.JsonUtf8)
                                .header("X-Authorization", "Bearer " +  tokenPojo.getToken())
                                .header("Cache-Control", "no-cache"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.JsonUtf8))
                        .andReturn().getResponse().getContentAsString()
                , Collection.class);
        assertThat(result).isNotNull();
        assertThat(!result.isEmpty());

        System.out.println(nLin+nLin+"----------------- UserControllerTest.searchUsersByString.end ----------------------------"+nLin+nLin);
    }




}
