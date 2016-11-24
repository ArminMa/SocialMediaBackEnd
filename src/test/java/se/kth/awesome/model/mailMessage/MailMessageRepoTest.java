package se.kth.awesome.model.mailMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.kth.awesome.model.User.UserEntity;
import se.kth.awesome.model.User.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MailMessageRepoTest {
    private static String nLin = System.lineSeparator();
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MailMessageRepository mailMessageRepo;
    private List<UserEntity> userEntities = new ArrayList<>();
    private List<MailMessage> mailMessages = new ArrayList<>();
    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- MailMessageRepoTest.setUp-start ----------------------------"+nLin+nLin);
        userEntities.add(new UserEntity("testMailUser0", "testMailEmail0@gmail.com" , "PasswordHashed0"));
        userEntities.add(new UserEntity("testMailUser1", "testMailEmail1@gmail.com" , "PasswordHashed0"));
        userEntities.add(new UserEntity("testMailUser2", "testMailUser2@gmail.com" , "PasswordHashed0"));
        userEntities.add(new UserEntity("testMailUser3", "testMailUser3@gmail.com" , "PasswordHashed0"));
        userEntities.add(new UserEntity("testMailUser4", "testMailUser4@gmail.com" , "PasswordHashed0"));
        userEntities = userRepo.save(userEntities);
        userRepo.flush();
        assertThat(userEntities).isNotNull();
        assertThat(userEntities).isNotEmpty();
//String messageContent, String topic, UserEntity sender, UserEntity receiver)
        mailMessages.add(new MailMessage("P1", "topic1", userEntities.get(0),userEntities.get(1)));
        mailMessages.add(new MailMessage("P2", "topic2", userEntities.get(0),userEntities.get(0)));
        mailMessages.add(new MailMessage("P3", "topic3", userEntities.get(1),userEntities.get(0)));
        mailMessages = mailMessageRepo.save(mailMessages);
        mailMessageRepo.flush();

        System.out.println(nLin+nLin+"----------------- MailMessageRepoTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- MailMessageRepoTest.tearDown-start ----------------------------"+nLin+nLin);
        assertThat(mailMessages).isNotNull();
        mailMessageRepo.delete(mailMessages);
        mailMessageRepo.flush();
        assertThat(userEntities).isNotNull();
        //check that the user is not deleted
        UserEntity userEntity = userRepo.findByEmail(userEntities.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo(userEntities.get(0).getEmail());
        userRepo.delete(userEntities);
        userRepo.flush();
        System.out.println(nLin+nLin+"----------------- MailMessageRepoTest.tearDown-end ----------------------------"+nLin+nLin);
    }


    @Test
    public void A_mailMessageTest(){
        System.out.println(nLin+nLin+"----------------- MailMessageRepoTest.MailMessageRepoTest-start ----------------------------"+nLin+nLin);
        UserEntity userEntity = null;
        userEntity = userRepo.findByEmail(userEntities.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo(userEntities.get(0).getEmail());
        List<MailMessage> chatMessagesFromUser0ToUser1 =
                mailMessageRepo.findBySenderAndReceiver(userEntities.get(0).getId(), userEntities.get(1).getId());
        assertThat(chatMessagesFromUser0ToUser1).isNotNull();
        assertThat(chatMessagesFromUser0ToUser1).isNotEmpty();
        System.out.println(nLin+nLin+"----------------- MailMessageRepoTest.MailMessageRepoTest-end ----------------------------"+nLin+nLin);
    }

}