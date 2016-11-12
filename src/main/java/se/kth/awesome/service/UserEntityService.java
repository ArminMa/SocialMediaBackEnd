package se.kth.awesome.service;

import org.springframework.http.ResponseEntity;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.pojos.UserPojo;


public interface UserEntityService {


    UserPojo findByUsername(String userName);

	ResponseEntity<?> findByUserID(Long idValue);

    ResponseEntity<?> findByEmail(String email);

}