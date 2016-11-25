package se.kth.awesome.service;


import org.springframework.http.ResponseEntity;
import se.kth.awesome.model.User.UserPojo;


public interface RegisterService {


	ResponseEntity<?> registerNewUser(UserPojo userPojo);



}
