package se.kth.awesome.controller;


import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.service.UserEntityService;
import se.kth.awesome.service.RegisterService;
import se.kth.awesome.util.MediaTypes;

@RestController
public class UserController {


    @Autowired
    private UserEntityService userService;


    @Autowired
    private RegisterService registerService;

    @RequestMapping(
            value = "/getEmail/{email:.+}",
            method = RequestMethod.GET)
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String userEmail) {

        return userService.findByEmail(userEmail);

    }

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<?> registerUser(
            @RequestBody UserPojo userPojo,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return registerService.registerNewUser(userPojo);
    }

    @RequestMapping(
            value = "/userSearch/{userName}",
            method = RequestMethod.GET)
    public ResponseEntity<?> userSearch(@PathVariable("userName") String userName) {
        Collection<UserPojo> matchingUsers = userService.searchUsersByName(userName);
        return  ResponseEntity.status(HttpStatus.OK).contentType(MediaTypes.JsonUtf8).body(matchingUsers);
    }

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody ResponseEntity<?> registerUser(
            @RequestBody UserPojo userPojo,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return registerService.registerNewUser(userPojo);
    }


}
