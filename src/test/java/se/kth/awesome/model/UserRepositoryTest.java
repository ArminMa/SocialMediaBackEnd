package se.kth.awesome.model;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryTest {
    private static String nLin = System.lineSeparator();
    @Test
    public void contextLoads() {
        System.out.println("hello test");
    }

    @Autowired
    private UserRepository userRepository;
    List<UserEntity> userEntities = new ArrayList<>();
    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- UserRepositoryTest.setUp-start ----------------------------"+nLin+nLin);

        userEntities.add(new UserEntity("testEmail0@gmail.com", "testUser0", "PasswordHashed0"));
        userEntities.add(new UserEntity("testEmail1@gmail.com", "testUser1", "PasswordHashed1"));
        userEntities.add(new UserEntity("testEmail2@gmail.com", "testUser2", "PasswordHashed2"));

        userEntities = userRepository.save(userEntities);
        userRepository.flush();

        Assertions.assertThat(userEntities).isNotNull();
        assertThat(userEntities.size()).isEqualTo(3);
        System.out.println(nLin+nLin+"----------------- UserRepositoryTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- UserRepositoryTest.tearDown-start ----------------------------"+nLin+nLin);
        Assertions.assertThat(userEntities).isNotNull();

        userRepository.delete(userEntities);
        userRepository.flush();
        System.out.println(nLin+nLin+"----------------- UserRepositoryTest.tearDown-end ----------------------------"+nLin+nLin);
    }

    @Test
    public void userRepositoryTest(){
        System.out.println(nLin+nLin+"----------------- UserRepositoryTest.userRepositoryTest-start ----------------------------"+nLin+nLin);
        UserEntity userEntity = null;

        userEntity = userRepository.findByEmail(userEntities.get(0).getEmail());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("testEmail0@gmail.com");

        userEntity = userRepository.findByUsername(userEntities.get(1).getUsername());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getUsername()).isEqualTo("testUser1");

        userEntity = userRepository.findByUsername(userEntities.get(2).getUsername());
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getId()).isEqualTo(userEntities.get(2).getId());

        System.out.println(nLin+nLin+"----------------- UserRepositoryTest.userRepositoryTest-end ----------------------------"+nLin+nLin);


    }

}