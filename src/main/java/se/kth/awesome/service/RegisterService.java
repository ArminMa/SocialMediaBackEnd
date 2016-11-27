package se.kth.awesome.service;


import org.springframework.http.ResponseEntity;
import se.kth.awesome.model.user.UserPojo;


public interface RegisterService {


	ResponseEntity<?> registerNewUser(UserPojo userPojo);



}
