package se.kth.awesome.controller;


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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.service.RegisterService;
import se.kth.awesome.service.UserEntityService;
import se.kth.awesome.util.MediaTypes;


@RestController
@RequestMapping("/social")
public class UserController {

    @Autowired
    private UserEntityService userService;

    @Autowired
    private RegisterService registerService;

    @RequestMapping(
            value = "/getUserByEmail/{email:.+}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  @ResponseBody ResponseEntity<?> getUserByEmail(
    		@PathVariable("email") String userEmail,
		    HttpServletRequest request,
		    HttpServletResponse response) {

        return userService.findByEmail(userEmail);

    }

    @Deprecated
    @RequestMapping(
            value = "/login/{userName}/{password}",
            method = RequestMethod.GET)
    public ResponseEntity<?> login(@PathVariable("userName") String userName, @PathVariable("password") String password) {
        UserPojo userPojo = userService.findByUsername(userName);
        if(userPojo == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
//        if(userPojo == null || !userPojo.getUsername().equals(userName) || !userPojo.getPassword().equals(password)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaTypes.JsonUtf8).body("success");
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
