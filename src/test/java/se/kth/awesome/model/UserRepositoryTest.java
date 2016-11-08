package se.kth.awesome.model;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryTest {



    @Test
    public void contextLoads() {
        System.out.println("hello test");
    }


    @Autowired
    private UserRepository userRepository;



    List<UserEntity> userEntities = new ArrayList<>();





    @Before
    public void setUp() throws Exception {
        System.out.println("\n\n----------------- UserRepositoryTest.setUp-start ----------------------------\n\n");

        userEntities.add(new UserEntity("testEmail0@gmail.com", "testUser0", "PasswordHashed0"));
        userEntities.add(new UserEntity("testEmail1@gmail.com", "testUser1", "PasswordHashed1"));
        userEntities.add(new UserEntity("testEmail2@gmail.com", "testUser2", "PasswordHashed2"));

        userEntities = userRepository.save(userEntities);
        userRepository.flush();

        assertThat(userEntities).isNotNull();
        assertThat(userEntities.size()).isEqualTo(3);
        System.out.println("\n\n----------------- UserRepositoryTest.setUp-end ----------------------------\n\n");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("\n\n----------------- UserRepositoryTest.tearDown-start ----------------------------\n\n");
        assertThat(userEntities).isNotNull();

        userRepository.delete(userEntities);
        userRepository.flush();
        System.out.println("\n\n----------------- UserRepositoryTest.tearDown-end ----------------------------\n\n");
    }


    @Test
    public void userRepositoryTest(){
        System.out.println("\n\n----------------- UserRepositoryTest.userRepositoryTest-start ----------------------------\n\n");

        UserEntity userEntity = null;

        userEntity = userRepository.findByEmail(userEntities.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("testEmail0@gmail.com");


        userEntity = userRepository.findByUsername(userEntities.get(1).getUserName());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getUserName()).isEqualTo("testUser1");

        userEntity = userRepository.findByUsername(userEntities.get(2).getUserName());
        assertThat(userEntity).isNotNull();
        System.out.println("\nUserRepositoryTest.testKey.applicationUser2.id = " + userEntity.getId() + "\n");
        assertThat(userEntity.getId()).isEqualTo(userEntities.get(2).getId());

        System.out.println("\n\n----------------- UserRepositoryTest.userRepositoryTest-end ----------------------------\n\n");


    }

}