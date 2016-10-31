package se.kth.awsome.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.kth.awsome.model.ApplicationUser;
import se.kth.awsome.model.UserRepository;
import se.kth.awsome.service.ApplicationUserService;

import java.nio.charset.Charset;
import java.util.List;

@Service
public class FaceUserServiceImp implements ApplicationUserService {

	@Autowired
	private UserRepository userRepository;

	MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Override
	public ResponseEntity<?> findByUsername(String userName) {
		return null;
	}

	@Override
	public ResponseEntity<?> findByUserID(Long idValue) {
		return null;
	}

	@Override
	public ResponseEntity<?> findByEmail(String email) {
		ApplicationUser appUser = userRepository.findByEmail(email);

		if(appUser == null) ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		return ResponseEntity.ok().build();
		
	}
}
