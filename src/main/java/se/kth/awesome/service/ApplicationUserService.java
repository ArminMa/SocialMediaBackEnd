package se.kth.awesome.service;

import org.springframework.http.ResponseEntity;


public interface ApplicationUserService {


    ResponseEntity<?> findByUsername(String userName);

	ResponseEntity<?> findByUserID(Long idValue);

    ResponseEntity<?> findByEmail(String email);

}
