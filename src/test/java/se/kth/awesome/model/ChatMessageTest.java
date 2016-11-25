package se.kth.awesome.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import se.kth.awesome.model.chatMessage.ChatMessage;
import se.kth.awesome.model.chatMessage.ChatMessageRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChatMessageTest {
	private static String nLin = System.lineSeparator();
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ChatMessageRepository chatMessageRepo;
    private List<UserEntity> userEntities = new ArrayList<>();
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- ChatMessageTest.setUp-start ----------------------------"+nLin+nLin);
        userEntities.add(new UserEntity( "testChatUser0", "testChatEmail0@gmail.com","PasswordHashed0"));
        userEntities.add(new UserEntity( "testChatUser1", "testChatEmail1@gmail.com","PasswordHashed1"));
        userEntities.add(new UserEntity( "testChatUser2","testChatEmail2@gmail.com", "PasswordHashed2"));
        userEntities.add(new UserEntity( "testChatUser3","testChatEmail3@gmail.com", "PasswordHashed3"));
        userEntities.add(new UserEntity( "testChatUser4", "testChatEmail4@gmail.com", "PasswordHashed4"));
        userEntities.add(new UserEntity( "testChatUser5","testChatEmail5@gmail.com", "PasswordHashed5"));
        userEntities = userRepo.save(userEntities);
        userRepo.flush();
        assertThat(userEntities).isNotNull();
        assertThat(userEntities).isNotEmpty();

        chatMessages.add(new ChatMessage("P0", new Date(),userEntities.get(0),userEntities.get(1)));
        chatMessages.add(new ChatMessage("P1", new Date(),userEntities.get(0),userEntities.get(2)));
        chatMessages.add(new ChatMessage("P2", new Date(),userEntities.get(1),userEntities.get(2)));
        chatMessages = chatMessageRepo.save(chatMessages);
        chatMessageRepo.flush();
//        userEntities.get(0).getChatMessages().add(chatMessages.get(0));
//        userEntities.get(1).getChatMessages().add(chatMessages.get(0));
//        UserEntity userEntity0 = userRepo.save(userEntities.get(0));
//        userRepo.flush();
//        assertThat(userEntity0).isNotNull();
//        UserEntity userEntity1 = userRepo.save(userEntities.get(1));
//        userRepo.flush();
//        assertThat(userEntity1).isNotNull();
        System.out.println(nLin+nLin+"----------------- ChatMessageTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- ChatMessageTest.tearDown-start ----------------------------"+nLin+nLin);
        assertThat(chatMessages).isNotNull();
        chatMessageRepo.delete(chatMessages);
        chatMessageRepo.flush();
        assertThat(userEntities).isNotNull();
        //check that the user is not deleted
        UserEntity userEntity = userRepo.findByEmail(userEntities.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo(userEntities.get(0).getEmail());
        userRepo.delete(userEntities);
        userRepo.flush();
        System.out.println(nLin+nLin+"----------------- ChatMessageTest.tearDown-end ----------------------------"+nLin+nLin);
    }


    @Test
    public void A_chatMessageTest(){
        System.out.println(nLin+nLin+"----------------- ChatMessageTest.chatMessageTest-start ----------------------------"+nLin+nLin);
        UserEntity userEntity = null;
        userEntity = userRepo.findByEmail(userEntities.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo(userEntities.get(0).getEmail());
        List<ChatMessage> chatMessagesFromUser0ToUser1 =
                chatMessageRepo.findBySenderAndReceiver(userEntities.get(0).getId(), userEntities.get(1).getId());
        assertThat(chatMessagesFromUser0ToUser1).isNotNull();
        assertThat(chatMessagesFromUser0ToUser1).isNotEmpty();
        System.out.println(nLin+nLin+"----------------- ChatMessageTest.chatMessageTest-end ----------------------------"+nLin+nLin);
    }
}