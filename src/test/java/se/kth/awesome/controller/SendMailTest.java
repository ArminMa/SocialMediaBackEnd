package se.kth.awesome.controller;

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
import se.kth.awesome.model.mailMessage.MailMessage;
import se.kth.awesome.model.mailMessage.MailMessageRepository;
import se.kth.awesome.model.ModelConverter;
import se.kth.awesome.model.post.Post;
import se.kth.awesome.model.post.PostPojo;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.mailMessage.MailMessagePojo;
import se.kth.awesome.model.TokenPojo;
import se.kth.awesome.model.User.UserPojo;
import se.kth.awesome.model.role.UserRoleEntity;
import se.kth.awesome.model.role.UserRoleRepository;
import se.kth.awesome.model.role.rolePojo.UserRolePojo;
import se.kth.awesome.util.gsonX.GsonX;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.awesome.util.Util.nLin;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SendMailTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MailMessageRepository mailMessageRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;
	@Autowired
	private UserRepository userRepo;

	private TokenPojo tokenPojo;
	private List<UserPojo> userPojos = new ArrayList<>();
	private List<UserEntity> userEntities = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		System.out.println(nLin+nLin+"----------------- GetPostTest.setUp-start ----------------------------"+nLin+nLin);

		List<UserRoleEntity> userRoleEntities = new ArrayList<>();
		userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
		userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
		userRoleEntities = userRoleRepo.save(userRoleEntities);

		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
		userEntities.add(new UserEntity("testUser", "test@test.test", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userEntities.add(new UserEntity("testUser2", "test2@test2.test2", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
		userEntities.get(0).getAuthorities().add(userRoleEntities.get(0));
		userEntities.get(1).getAuthorities().add(userRoleEntities.get(1));
		userEntities = userRepo.save(userEntities);
		userRepo.flush();

		userPojos = (List<UserPojo>) ModelConverter.convert(userEntities);
		userPojos.get(0).setPassword("PasswordHashed0");
		userPojos.get(1).setPassword("PasswordHashed0");
		assertThat(userEntities).isNotNull();
		assertThat(userEntities).isNotEmpty();



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
        System.out.println(nLin+nLin+"----------------- SendMailTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- SendMailTest.tearDown-start ----------------------------"+nLin+nLin);

	    Collection<MailMessage> mailMessages = mailMessageRepo.findAll();
	    assertThat(mailMessages).isNotNull();
	    assertThat(mailMessages).isNotEmpty();
	    mailMessageRepo.deleteAll();
	    mailMessageRepo.flush();
	    assertThat(userPojos).isNotNull();
	    //check that the user is not deleted
	    UserEntity userEntity = userRepo.findByEmail(userPojos.get(0).getEmail());
	    assertThat(userEntity).isNotNull();

	    userRepo.deleteAll();
	    userRepo.flush();
        System.out.println(nLin+nLin+"----------------- SendMailTest.tearDown-end ----------------------------"+nLin+nLin);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sendMessageToUser() throws Exception {
        System.out.println(nLin+nLin+"----------------- SendMailTest.sendMessageToUser.start ----------------------------"+nLin+nLin);
        MailMessagePojo mailMessagePojo = new MailMessagePojo("P1", "topic1", userPojos.get(0),userPojos.get(1));
        assertThat(tokenPojo).isNotNull();
        assertThat(userPojos).isNotNull();
        MockHttpServletResponse theResponse =  this.mockMvc.perform
                (
                        post("/api/sendMail")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content( GsonX.gson.toJson(mailMessagePojo) )
                                .header("X-Authorization", "Bearer " + tokenPojo.getToken())
                                .header("Cache-Control", "no-cache")
                )
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse();
        assertThat(theResponse).isNotNull();

//        System.out.println(nLin+"response message = " + theResponse.getContentAsString()+nLin);
//	    System.out.println(nLin+"status().is("+ HttpStatus.valueOf(theResponse.getStatus())+")"+nLin );

//        List<MailMessage> mailMessageReturnFromRepo = mailMessageRepo.findBySenderAndReceiver(userPojos.get(0).getId(), userPojos.get(1).getId());
//        assertThat(mailMessageReturnFromRepo).isNotNull();
//        assertThat(mailMessageReturnFromRepo).isNotEmpty();
        System.out.println(nLin+nLin+"----------------- SendMailTest.searchUsersByString.end ----------------------------"+nLin+nLin);
    }

}
