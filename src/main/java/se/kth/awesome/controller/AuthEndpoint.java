package se.kth.awesome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.kth.awesome.pojos.MailMessagePojo.MailMessagePojo;
import se.kth.awesome.pojos.TokenPojo;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.security.auth.JwtAuthenticationToken;
import se.kth.awesome.security.model.UserContext;
import se.kth.awesome.service.UserEntityService;
import se.kth.awesome.util.MediaTypes;

import java.util.Collection;

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

    @Autowired
    private UserEntityService userService;

    @RequestMapping(value="/api/me", method=RequestMethod.GET)
    public @ResponseBody UserContext get(JwtAuthenticationToken token) {
        return (UserContext) token.getPrincipal();
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
    public ResponseEntity<?> sendMailMessage(MailMessagePojo messagePojo ) {

         return userService.sendMailMessage(messagePojo);
    }

    @RequestMapping(
            value = "/api/getMyMails/{username}",
            method = RequestMethod.GET)
    public ResponseEntity<?> getMyMails(@PathVariable("username") String username,
                                   @RequestHeader(name = "X-Authorization", defaultValue = "") String jwt) {

        String token = jwt.substring(jwt.indexOf("Bearer "), jwt.length());
        System.out.println(nLin+"AuthEndpoint.getMyMails token = " + token +nLin);
        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setToken(token);




        return userService.getMyMails(username);
    }

}
