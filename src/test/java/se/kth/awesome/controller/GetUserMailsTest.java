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
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.model.mailMessage.MailMessage;
import se.kth.awesome.model.mailMessage.MailMessageRepository;
import se.kth.awesome.model.modelConverter.ModelConverter;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.pojos.MailMessagePojo.MailMessagePojo;
import se.kth.awesome.pojos.TokenPojo;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.pojos.UserRolePojo;
import se.kth.awesome.util.gson.GsonX;
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
public class GetUserMailsTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailMessageRepository mailMessageRepository;

    private TokenPojo tokenPojo;
    private List<UserPojo> userPojos = new ArrayList<>();
    private List<MailMessage> mailMessages = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetUserMailsTest.setUp-start ----------------------------"+nLin+nLin);
		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
        userPojos.add(new UserPojo("testUser", "test@test.test", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
        userPojos.add(new UserPojo("testUser2", "test2@test2.test2", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
        userPojos.get(0).getRoles().add(new UserRolePojo(Role.MEMBER));
        List<UserEntity> userEntities = userRepository.save((Collection<UserEntity>) ModelConverter.convert(userPojos));
        userRepository.flush();
        userPojos.get(0).setPassword("PasswordHashed0");
        userPojos.get(0).setId(userEntities.get(0).getId());
        userPojos.get(1).setId(userEntities.get(1).getId());


        assertThat(userEntities).isNotNull();
        assertThat(userEntities).isNotEmpty();
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

        System.out.println(nLin+nLin+"----------------- GetUserMailsTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetUserMailsTest.tearDown-start ----------------------------"+nLin+nLin);
        mailMessageRepository.deleteAll();
        mailMessageRepository.flush();
        UserEntity userEntity = userRepository.findByEmail(userPojos.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("test@test.test");
        userRepository.deleteAll();
        userRepository.flush();
        System.out.println(nLin+nLin+"----------------- GetUserMailsTest.tearDown-end ----------------------------"+nLin+nLin);
    }

    @Test
    public void getPersonalMails() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetUserMailsTest.getPersonalMessages.start ----------------------------"+nLin+nLin);
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
        System.out.println(nLin+nLin+"----------------- GetUserMailsTest.getPersonalMessages.end ----------------------------"+nLin+nLin);
    }
}
