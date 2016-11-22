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
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MailMessageTest {
    private static String nLin = System.lineSeparator();
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MailMessageRepository mailMessageRepository;
    private List<UserEntity> userEntities = new ArrayList<>();
    private List<MailMessage> mailMessages = new ArrayList<>();
    @Before
    public void setUp() throws Exception {
        System.out.println("\n\n----------------- MailMessageTest.setUp-start ----------------------------"+nLin+nLin);
        userEntities.add(new UserEntity("testMailEmail0@gmail.com", "testMailUser0", "PasswordHashed0"));
        userEntities.add(new UserEntity("testMailEmail1@gmail.com", "testMailUser2", "PasswordHashed1"));
        userEntities.add(new UserEntity("testMailEmail2@gmail.com", "testMailUser3", "PasswordHashed2"));
        userEntities.add(new UserEntity("testMailEmail3@gmail.com", "testMailUser4", "PasswordHashed3"));
        userEntities.add(new UserEntity("testMailEmail4@gmail.com", "testMailUser5", "PasswordHashed4"));
        userEntities.add(new UserEntity("testMailEmail5@gmail.com", "testMailUser6", "PasswordHashed5"));
        userEntities = userRepo.save(userEntities);
        userRepo.flush();
        assertThat(userEntities).isNotNull();
        assertThat(userEntities).isNotEmpty();
//String messageContent, String topic, UserEntity sender, UserEntity receiver)
        mailMessages.add(new MailMessage("P1", "topic1", userEntities.get(0),userEntities.get(1)));
        mailMessages.add(new MailMessage("P2", "topic2", userEntities.get(0),userEntities.get(0)));
        mailMessages.add(new MailMessage("P3", "topic3", userEntities.get(1),userEntities.get(0)));
        mailMessages = mailMessageRepository.save(mailMessages);
        mailMessageRepository.flush();

        System.out.println("\n\n----------------- MailMessageTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("\n\n----------------- MailMessageTest.tearDown-start ----------------------------"+nLin+nLin);
        assertThat(mailMessages).isNotNull();
        mailMessageRepository.delete(mailMessages);
        mailMessageRepository.flush();
        assertThat(userEntities).isNotNull();
        //check that the user is not deleted
        UserEntity userEntity = userRepo.findByEmail(userEntities.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("testMailEmail0@gmail.com");
        userRepo.delete(userEntities);
        userRepo.flush();
        System.out.println("\n\n----------------- MailMessageTest.tearDown-end ----------------------------"+nLin+nLin);
    }


    @Test
    public void A_mailMessageTest(){
        System.out.println("\n\n----------------- MailMessageTest.MailMessageTest-start ----------------------------"+nLin+nLin);
        UserEntity userEntity = null;
        userEntity = userRepo.findByEmail(userEntities.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("testMailEmail0@gmail.com");
        List<MailMessage> chatMessagesFromUser0ToUser1 =
                mailMessageRepository.findBySenderAndReceiver(userEntities.get(0).getId(), userEntities.get(1).getId());
        assertThat(chatMessagesFromUser0ToUser1).isNotNull();
        assertThat(chatMessagesFromUser0ToUser1).isNotEmpty();
        System.out.println("\n\n----------------- MailMessageTest.MailMessageTest-end ----------------------------"+nLin+nLin);
    }

}