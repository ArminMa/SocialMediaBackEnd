package se.kth.awesome.controller;

import com.google.gson.reflect.TypeToken;
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
import se.kth.awesome.model.User.UserEntity;
import se.kth.awesome.model.User.UserRepository;
import se.kth.awesome.model.mailMessage.MailMessage;
import se.kth.awesome.model.mailMessage.MailMessageRepository;
import se.kth.awesome.model.ModelConverter;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.mailMessage.MailMessagePojo;
import se.kth.awesome.model.TokenPojo;
import se.kth.awesome.model.User.UserPojo;
import se.kth.awesome.model.role.UserRoleEntity;
import se.kth.awesome.model.role.UserRoleRepository;
import se.kth.awesome.model.role.rolePojo.UserRolePojo;
import se.kth.awesome.util.gsonX.GsonX;
import se.kth.awesome.util.MediaTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class GetMailMessageTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepo;
    @Autowired
    private MailMessageRepository mailMessageRepository;

    private TokenPojo tokenPojo;
    private List<UserPojo> userPojos = new ArrayList<>();
    private List<MailMessage> mailMessages = new ArrayList<>();
    private List<UserEntity> userEntities = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetMailMessageTest.setUp-start ----------------------------"+nLin+nLin);

        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
        userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
        userRoleEntities = userRoleRepo.save(userRoleEntities);

		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
        userEntities.add(new UserEntity("getPersonalMails1", "getPersonalMails1@test1.test1", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
        userEntities.add(new UserEntity("getPersonalMails2", "getPersonalMails2@test2.test2", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));

        userEntities.get(0).getAuthorities().add(userRoleEntities.get(0));
        userEntities.get(1).getAuthorities().add(userRoleEntities.get(1));
        userEntities = userRepository.save(userEntities);
        userRepository.flush();
        assertThat(userEntities).isNotNull();
        assertThat(userEntities).isNotEmpty();
        userPojos = (List<UserPojo>) ModelConverter.convert(userEntities);
        userPojos.get(0).setPassword("PasswordHashed0");
        userPojos.get(1).setPassword("PasswordHashed0");

        assertThat(userPojos).isNotNull();
        assertThat(userPojos).isNotEmpty();
//String messageContent, String topic, UserEntity sender, UserEntity receiver)
        mailMessages.add(new MailMessage("P1", "topic1", userEntities.get(0),userEntities.get(1)));
        mailMessages.add(new MailMessage("P2", "topic2", userEntities.get(0),userEntities.get(0)));
        mailMessages.add(new MailMessage("P3", "topic3", userEntities.get(1),userEntities.get(0)));
        mailMessages = mailMessageRepository.save(mailMessages);
        mailMessageRepository.flush();

        assertThat(mailMessages).isNotNull();
        assertThat(mailMessages).isNotEmpty();
        assertThat(mailMessages.size()).isEqualTo(3);

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

        System.out.println(nLin+nLin+"----------------- GetMailMessageTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetMailMessageTest.tearDown-start ----------------------------"+nLin+nLin);
        mailMessageRepository.deleteAll();
        mailMessageRepository.flush();
        UserEntity userEntity = userRepository.findByEmail(userPojos.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo(userPojos.get(0).getEmail());
        userRepository.deleteAll();
        userRepository.flush();
        System.out.println(nLin+nLin+"----------------- GetMailMessageTest.tearDown-end ----------------------------"+nLin+nLin);
    }

    @Test
    public void getPersonalMails() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetMailMessageTest.getPersonalMessages.start ----------------------------"+nLin+nLin);
        assertThat(tokenPojo).isNotNull();
        assertThat(userPojos).isNotNull();


        String theResponse = this.mockMvc.perform(get("/api/getMyMails/"+userPojos.get(0).getUsername())
                .accept(MediaTypes.JsonUtf8)
                .header("X-Authorization", "Bearer " + tokenPojo.getToken())
                .header("Cache-Control", "no-cache"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.JsonUtf8))
                .andReturn().getResponse().getContentAsString();


        System.out.println(nLin+"  theResponse = "+theResponse+nLin);

        List<MailMessagePojo> mailMessageReturnFromResponse = GsonX.gson.fromJson(theResponse, new TypeToken<List<MailMessagePojo>>(){}.getType() );
        assertThat(mailMessageReturnFromResponse).isNotNull();
        assertThat(mailMessageReturnFromResponse).isNotEmpty();
        System.out.println(nLin+nLin+"----------------- GetMailMessageTest.getPersonalMessages.end ----------------------------"+nLin+nLin);
    }
}
