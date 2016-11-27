package se.kth.awesome.controller;

import java.util.ArrayList;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.user.UserRepository;
import se.kth.awesome.model.ModelConverter;
import se.kth.awesome.model.post.Post;
import se.kth.awesome.model.post.PostPojo;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.model.role.UserRoleEntity;
import se.kth.awesome.model.role.UserRoleRepository;
import se.kth.awesome.util.gsonX.GsonX;
import se.kth.awesome.util.MediaTypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.awesome.util.Util.nLin;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetUserByEmailTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepo;

    private List<UserPojo> userPojos = new ArrayList<>();
    private List<PostPojo> postPojos = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private List<UserEntity> userEntities = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetUserByEmailTest.setUp-start ----------------------------"+nLin+nLin);

        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
        userRoleEntities = userRoleRepo.save(userRoleEntities);

		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
        userEntities.add(new UserEntity("getUserByEmailTest", "getUserByEmailTest@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
        userEntities.get(0).getAuthorities().add(userRoleEntities.get(0));

        userEntities = userRepository.save(userEntities);
        userRepository.flush();
        userPojos = (List<UserPojo>) ModelConverter.convert(userEntities);
        userPojos.get(0).setPassword("PasswordHashed0");
        assertThat(userEntities).isNotNull();
        assertThat(userEntities).isNotEmpty();
        System.out.println(nLin+nLin+"----------------- GetUserByEmailTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetUserByEmailTest.tearDown-start ----------------------------"+nLin+nLin);
        assertThat(userEntities).isNotNull();
        assertThat(userEntities).isNotEmpty();
        userRepository.delete(userEntities);
        userRepository.flush();
        System.out.println(nLin+nLin+"----------------- GetUserByEmailTest.tearDown-end ----------------------------"+nLin+nLin);
    }

    @Test
    public void getUserByEmail() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetUserByEmailTest.getUserByEmail.start ----------------------------"+nLin+nLin);
        UserPojo userPojo = GsonX.gson.fromJson(
                this.mockMvc.perform(get("/social/getUserByEmail/"+userPojos.get(0).getEmail()).accept(MediaTypes.JsonUtf8))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.JsonUtf8))
                        .andReturn().getResponse().getContentAsString()
                , UserPojo.class);
        assertThat(userPojo).isNotNull();
        assertThat(userPojo.getEmail()).isEqualTo(userPojos.get(0).getEmail());
        System.out.println("this is how userpojo looks like "+ nLin + userPojo.toString());
        System.out.println(nLin+nLin+"----------------- GetUserByEmailTest.getUserByEmail.end ----------------------------"+nLin+nLin);
    }




}