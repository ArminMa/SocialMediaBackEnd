package se.kth.awesome.security;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import se.kth.awesome.model.ModelConverter;
import se.kth.awesome.model.TokenPojo;
import se.kth.awesome.model.mailMessage.MailMessage;
import se.kth.awesome.model.mailMessage.MailMessageRepository;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.role.UserRoleEntity;
import se.kth.awesome.model.role.UserRoleRepository;
import se.kth.awesome.model.role.rolePojo.UserRolePojo;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.model.user.UserRepository;
import se.kth.awesome.security.auth.jwt.extractor.JwtHeaderTokenExtractor;
import se.kth.awesome.security.auth.jwt.model.token.JwtSettings;
import se.kth.awesome.security.auth.jwt.model.token.JwtTokenFactory;
import se.kth.awesome.security.util.KeyUtil;
import se.kth.awesome.security.util.PasswordSaltUtil;
import se.kth.awesome.util.gsonX.GsonX;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.awesome.util.Util.nLin;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenTest {

	@Autowired private JwtTokenFactory tokenFactory;

	@Autowired private JwtSettings jwtSettings;

	@Autowired private JwtHeaderTokenExtractor tokenExtractor;

	@Autowired private AwesomeServerKeys awesomeServerKeys;

	private List<TokenPojo>  tokenPojos = new ArrayList<>();
	private List<UserPojo> userPojos = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		System.out.println(nLin+nLin+"----------------- TokenTest.setUp-start ----------------------------"+nLin+nLin);



		userPojos.add(new UserPojo("tokenTest1", "tokenTest1@gmail.com", PasswordSaltUtil.encryptSalt("password1", awesomeServerKeys.getSharedSecretKey())));
		userPojos.add(new UserPojo("tokenTest2", "tokenTest2@gmail.com", PasswordSaltUtil.encryptSalt("password2", awesomeServerKeys.getSharedSecretKey())));
		userPojos.add(new UserPojo("tokenTest3", "tokenTest3@gmail.com", PasswordSaltUtil.encryptSalt("password3", awesomeServerKeys.getSharedSecretKey())));
		userPojos.add(new UserPojo("tokenTest4", "tokenTest4@gmail.com", PasswordSaltUtil.encryptSalt("password4", awesomeServerKeys.getSharedSecretKey())));
		/*"password:793148fd08f39ee62a84474fce8e0a544c5f1fc8," +*/ /*PasswordHashed0*/
		userPojos.get(0).getAuthorities().add(new UserRolePojo( Role.MEMBER));
		userPojos.get(1).getAuthorities().add(new UserRolePojo( Role.MEMBER));
		userPojos.get(2).getAuthorities().add(new UserRolePojo( Role.MEMBER));
		userPojos.get(3).getAuthorities().add(new UserRolePojo( Role.MEMBER));

		tokenPojos.add(new TokenPojo("Bearer "+ tokenFactory.createAccessJwtToken(userPojos.get(0)).getToken(), null ) );
		tokenPojos.add(new TokenPojo("Bearer "+ tokenFactory.createAccessJwtToken(userPojos.get(1)).getToken(), null ) );
		tokenPojos.add(new TokenPojo("Bearer "+ tokenFactory.createAccessJwtToken(userPojos.get(2)).getToken(), null ) );
		tokenPojos.add(new TokenPojo("Bearer "+ tokenFactory.createAccessJwtToken(userPojos.get(3)).getToken(), null ) );

		System.out.println(nLin+nLin+"----------------- TokenTest.setUp-end ----------------------------"+nLin+nLin);
	}



	@Test
	public void tokenTest(){
		System.out.println(nLin+nLin+"----------------- TokenTest.tokenTest-start ----------------------------"+nLin+nLin);

		assertThat(userPojos).isNotNull();
		assertThat(userPojos).isNotEmpty();
		assertThat(tokenPojos).isNotNull();
		assertThat(tokenPojos).isNotEmpty();


		String token1 = tokenExtractor.extract(tokenPojos.get(0).getToken());
		String token2 = tokenPojos.get(0).getToken().substring("Bearer ".length(), tokenPojos.get(0).getToken().length());
		assertThat(token1).isEqualTo(token2);

		UserPojo userPojo0 = GsonX.gson.fromJson( tokenFactory.getPayloadFromJwt(token1), UserPojo.class);

		String userName = tokenFactory.getSubject(token1);

		assertThat(userPojo0.getUsername()).isEqualTo(userName);

		System.out.println(nLin+"AuthEndpoint.sendMailMessage userPojo2 = " + userPojo0.toString() +nLin);

		System.out.println(nLin+nLin+"----------------- TokenTest.tokenTest-end ----------------------------"+nLin+nLin);


	}
}
