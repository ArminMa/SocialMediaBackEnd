package se.kth.awesome.model;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.crypto.SecretKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.kth.awesome.model.ApplicationUser;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.service.ApplicationUserService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {



    @Test
    public void contextLoads() {
        System.out.println("hello test");
    }


    @Autowired
    private UserRepository userRepository;



    List<ApplicationUser> applicationUsers = new ArrayList<>();





    @Before
    public void setUp() throws Exception {


        applicationUsers.add(new ApplicationUser("testEmail0@gmail.com", "testUser0", "PasswordHashed0"));
        applicationUsers.add(new ApplicationUser("testEmail1@gmail.com", "testUser1", "PasswordHashed1"));
        applicationUsers.add(new ApplicationUser("testEmail2@gmail.com", "testUser2", "PasswordHashed2"));

        applicationUsers = userRepository.save(applicationUsers);
        userRepository.flush();

        assertThat(applicationUsers).isNotNull();
        assertThat(applicationUsers.size()).isEqualTo(3);

    }

    @After
    public void tearDown() throws Exception {

        assertThat(applicationUsers).isNotNull();

        userRepository.delete(applicationUsers);
        userRepository.flush();

    }


    @Test
    public void userRepositoryTest(){
        System.out.println("\n\n----------------- UserRepositoryTest.userRepositoryTest-start ----------------------------\n\n");

        ApplicationUser applicationUser = null;

        applicationUser = userRepository.findByEmail(applicationUsers.get(0).getEmail());
        assertThat(applicationUser).isNotNull();
        assertThat(applicationUser.getEmail()).isEqualTo("testEmail0@gmail.com");


        applicationUser = userRepository.findByUsername(applicationUsers.get(1).getUserName());
        assertThat(applicationUser).isNotNull();
        assertThat(applicationUser.getUserName()).isEqualTo("testUser1");

        applicationUser = userRepository.findByUsername(applicationUsers.get(2).getUserName());
        assertThat(applicationUser).isNotNull();
        System.out.println("\nUserRepositoryTest.testKey.applicationUser2.id = " + applicationUser.getId() + "\n");
        assertThat(applicationUser.getId()).isEqualTo(applicationUsers.get(2).getId());

        System.out.println("\n\n----------------- UserRepositoryTest.userRepositoryTest-end ----------------------------\n\n");


    }

}