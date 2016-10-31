package se.kth.awesome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.kth.awesome.service.ApplicationUserService;

@RestController
public class userController {


    @Autowired
    private ApplicationUserService userService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    public String getHelloWorld() {
        return "Hello  World";
    }

    @RequestMapping("/ping2")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello " + name;
    }

    @RequestMapping(
            value = "/getEmail/{email:.+}",
            method = RequestMethod.GET)
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String userEmail) {

        return userService.findByEmail(userEmail);

    }


}
