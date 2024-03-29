package se.kth.awesome.controller;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.kth.awesome.model.mailMessage.MailMessagePojo;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.model.post.PostPojo;
import se.kth.awesome.security.AwesomeServerKeys;
import se.kth.awesome.security.auth.JwtAuthenticationToken;
import se.kth.awesome.security.auth.jwt.extractor.JwtHeaderTokenExtractor;

import se.kth.awesome.security.auth.jwt.model.token.JwtTokenFactory;

import se.kth.awesome.security.util.CipherUtils;
import se.kth.awesome.security.util.KeyUtil;
import se.kth.awesome.service.UserEntityService;
import se.kth.awesome.util.MediaTypes;

import java.util.Collection;
import se.kth.awesome.util.gsonX.GsonX;

import static se.kth.awesome.util.Util.nLin;

/**
 * End-point for retrieving logged-in user details.
 * 
 * @author vladimir.stankovic
 *
 * Aug 4, 2016
 */
@RestController
public class AuthEndpoint {

    @Autowired private UserEntityService userService;

	@Autowired private JwtTokenFactory tokenFactory;

	@Autowired private AwesomeServerKeys awesomeServerKeys;

	@Autowired private JwtHeaderTokenExtractor tokenExtractor;

    @RequestMapping(value="/api/me", method=RequestMethod.GET)
    public @ResponseBody UserPojo get(JwtAuthenticationToken token) {
        return (UserPojo) token.getPrincipal();
    }


    @RequestMapping(
            value = "/api/userSearch/{userName}",
            method = RequestMethod.GET)
    public ResponseEntity<?> userSearch(@PathVariable("userName") String userName) {
        Collection<UserPojo> matchingUsers = userService.searchUsersResemblingByUsername(userName);
        return  ResponseEntity.status(HttpStatus.OK).contentType(MediaTypes.JsonUtf8).body(matchingUsers);
    }

    @RequestMapping(
            value = "/api/sendMail",
            method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> sendMailMessage(@RequestBody MailMessagePojo messagePojo,
                                             @RequestHeader(name = "X-Authorization", defaultValue = "") String jwt) {
	    UserPojo userPojo0 = getUserPojoFromToken(jwt);
	    messagePojo.setSender(userPojo0);
	    return userService.sendMailMessage(messagePojo);
    }

	@RequestMapping(
            value = "/api/getMyMails",
            method = RequestMethod.GET)
    public ResponseEntity<?> getMyMails(@RequestHeader(name = "X-Authorization", defaultValue = "") String jwt) {
		UserPojo user = getUserPojoFromToken(jwt);
        return userService.getMyMails(user);
    }

	@RequestMapping(
			value = "/api/sendPost",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> sendPost(@RequestBody PostPojo postPojo,
	                                  @RequestHeader(name = "X-Authorization", defaultValue = "") String jwt) {
		UserPojo user = getUserPojoFromToken(jwt);
		postPojo.setSender(user);
		return userService.senPostMessage(postPojo);
	}

    @RequestMapping(
            value = "/api/getPostsByUserName/{username}",
            method = RequestMethod.GET)
    public ResponseEntity<?> getPosts(@PathVariable("username") String username,
                                      @RequestHeader(name = "X-Authorization", defaultValue = "") String jwt) {
        return userService.getPosts(username);
    }


	@RequestMapping(value = "/api/deleteLogMessage",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ResponseEntity<?> deleteLogMessage(
			@RequestHeader(name = "X-Authorization", defaultValue = "") String jwt,
			@RequestBody PostPojo post, HttpServletRequest request, HttpServletResponse response){

    	UserPojo user = getUserPojoFromToken(jwt);
		post.setSender(user);
		return userService.deletePost(post);
	}

	private UserPojo getUserPojoFromToken(String jwt) {
		String token1 = jwt.substring("Bearer ".length(), jwt.length());
//	    String token2 = tokenExtractor.extract(jwt);

		SecretKey encryptDecryptTokenPayloadKey = KeyUtil.SymmetricKey.getSecretKeyFromString(awesomeServerKeys.getEncryptPayloadKey());
		String decryptedPayload = null;
		try {
			decryptedPayload = CipherUtils.decryptWithSymmetricKey(tokenFactory.getPayloadFromJwt(token1), encryptDecryptTokenPayloadKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserPojo userPojo0 = GsonX.gson.fromJson( decryptedPayload, UserPojo.class);
		String userName = tokenFactory.getSubject(token1); // username

		if(userName.equals(userPojo0.getUsername())){
			System.out.println(nLin+"\"/api/sendMail\".userToken verified = " + userPojo0.toString() +nLin);
		}else {
			return null;
		}
		return userPojo0;
	}

}
