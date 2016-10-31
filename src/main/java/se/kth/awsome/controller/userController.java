package se.kth.awsome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.kth.awsome.service.ApplicationUserService;

@RestController
@RequestMapping("/user")
public class userController {


    @Autowired
    private ApplicationUserService userService;


    @RequestMapping(
            value = "/getEmail",
            method = RequestMethod.GET)
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String userEmail) {

        return userService.findByEmail(userEmail);


    }
}
