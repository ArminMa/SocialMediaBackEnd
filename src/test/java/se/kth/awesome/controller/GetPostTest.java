package se.kth.awesome.controller;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.awesome.model.ModelConverter;
import se.kth.awesome.model.TokenPojo;
import se.kth.awesome.model.User.UserEntity;
import se.kth.awesome.model.User.UserPojo;
import se.kth.awesome.model.User.UserRepository;
import se.kth.awesome.model.post.Post;
import se.kth.awesome.model.post.PostPojo;
import se.kth.awesome.model.post.PostRepository;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.role.UserRoleEntity;
import se.kth.awesome.model.role.UserRoleRepository;
import se.kth.awesome.model.role.rolePojo.UserRolePojo;
import se.kth.awesome.util.MediaTypes;
import se.kth.awesome.util.gsonX.GsonX;

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
public class GetPostTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PostRepository postRepos;
	@Autowired
	private UserRoleRepository userRoleRepo;

    private TokenPojo tokenPojo;
    private List<UserPojo> userPojos = new ArrayList<>();
    private List<PostPojo> postPojos = new ArrayList<>();
	private List<Post> posts = new ArrayList<>();
	private List<UserEntity> userEntities = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetPostTest.setUp-start ----------------------------"+nLin+nLin);

        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
	    userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
	    userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
	    userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
	    userRoleEntities.add(new UserRoleEntity( Role.MEMBER));
	    userRoleEntities = userRoleRepo.save(userRoleEntities);

		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
	    userEntities.add(new UserEntity("getPostTest1", "getPostTest1@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
	    userEntities.add(new UserEntity("getPostTest2", "getPostTest2@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
	    userEntities.add(new UserEntity("getPostTest3", "getPostTest4@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
	    userEntities.add(new UserEntity("getPostTest4", "getPostTest5@gmail.com", "793148fd08f39ee62a84474fce8e0a544c5f1fc8"));
	    userEntities.get(0).getAuthorities().add(userRoleEntities.get(0));
	    userEntities.get(1).getAuthorities().add(userRoleEntities.get(1));
	    userEntities.get(2).getAuthorities().add(userRoleEntities.get(2));
	    userEntities.get(3).getAuthorities().add(userRoleEntities.get(3));
	    userEntities = userRepo.save(userEntities);
        userRepo.flush();
	    assertThat(userEntities).isNotNull();
	    assertThat(userEntities).isNotEmpty();
        userPojos = (List<UserPojo>) ModelConverter.convert(userEntities);
        userPojos.get(0).setPassword("PasswordHashed0");
        userPojos.get(1).setPassword("PasswordHashed0");
        userPojos.get(2).setPassword("PasswordHashed0");
        userPojos.get(3).setPassword("PasswordHashed0");

//String messageContent, String topic, UserEntity sender, UserEntity receiver)
        postPojos.add(new PostPojo("P1", userPojos.get(0),userPojos.get(0)));
        postPojos.add(new PostPojo("P2", userPojos.get(0),userPojos.get(1)));
        postPojos.add(new PostPojo("P3", userPojos.get(0),userPojos.get(2)));
        postPojos.add(new PostPojo("P4", userPojos.get(0),userPojos.get(3)));
        postPojos.add(new PostPojo("P5", userPojos.get(1),userPojos.get(0)));
        postPojos.add(new PostPojo("P6", userPojos.get(2),userPojos.get(3)));
        posts = postRepos.save((Collection<Post>) ModelConverter.convert(postPojos));
        postRepos.flush();
        assertThat(posts).isNotNull();
        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(postPojos.size());

        postPojos = (List<PostPojo>) ModelConverter.convert(posts);



        String tokenJson =  this.mockMvc.perform
                (
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(GsonX.gson.toJson(userPojos.get(0)))
                                .header("X-Requested-With","XMLHttpRequest")
                                .header("Cache-Control","no-cache")
                )
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();
        assertThat(tokenJson).isNotNull();
        tokenPojo = GsonX.gson.fromJson(tokenJson, TokenPojo.class);

        System.out.println(nLin+nLin+"----------------- GetPostTest.setUp-end ----------------------------"+nLin+nLin);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetPostTest.tearDown-start ----------------------------"+nLin+nLin);

        assertThat(posts).isNotNull();
	    assertThat(posts).isNotEmpty();
//        posts = postRepos.findAll();
//        assertThat(posts).isNotNull();
//        assertThat(posts).isNotEmpty();
//
//	    postRepos.deleteAll();
//	    postRepos.flush();
//        UserEntity userEntity = userRepo.findByEmail(userPojos.get(0).getEmail());
//        assertThat(userEntity).isNotNull();
//        assertThat(userEntity.getEmail()).isEqualTo(userPojos.get(0).getEmail());
//	    userRepo.deleteAll();
//	    userRepo.flush();
        System.out.println(nLin+nLin+"----------------- GetPostTest.tearDown-end ----------------------------"+nLin+nLin);
    }

    @Test
    public void getPosts() throws Exception {
        System.out.println(nLin+nLin+"----------------- GetPostTest.getPosts.start ----------------------------"+nLin+nLin);
        assertThat(tokenPojo).isNotNull();
        assertThat(userPojos).isNotNull();

	    // get post for the logged in member

        String theResponse = this.mockMvc.perform(get("/api/getPostsByUserName/"+userPojos.get(0).getUsername())
                .accept(MediaTypes.JsonUtf8)
                .header("X-Authorization", "Bearer " + tokenPojo.getToken())
                .header("Cache-Control", "no-cache"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.JsonUtf8))
                .andReturn().getResponse().getContentAsString();


        System.out.println(nLin+"  theResponse = "+theResponse+nLin);

        List<PostPojo> PostsReturnFromResponse = GsonX.gson.fromJson(theResponse, new TypeToken<List<PostPojo>>(){}.getType() );
        assertThat(PostsReturnFromResponse).isNotNull();
        assertThat(PostsReturnFromResponse).isNotEmpty();

        // get post for other other members posts

	    theResponse = this.mockMvc.perform(get("/api/getPostsByUserName/"+userPojos.get(3).getUsername())
			    .accept(MediaTypes.JsonUtf8)
			    .header("X-Authorization", "Bearer " + tokenPojo.getToken())
			    .header("Cache-Control", "no-cache"))

			    .andExpect(status().isOk())
			    .andExpect(content().contentType(MediaTypes.JsonUtf8))
			    .andReturn().getResponse().getContentAsString();


	    System.out.println(nLin+"  theResponse = "+theResponse+nLin);

	    PostsReturnFromResponse = GsonX.gson.fromJson(theResponse, new TypeToken<List<PostPojo>>(){}.getType() );
	    assertThat(PostsReturnFromResponse).isNotNull();
	    assertThat(PostsReturnFromResponse).isNotEmpty();






        System.out.println(nLin+nLin+"----------------- GetPostTest.getPosts.end ----------------------------"+nLin+nLin);
    }
}
