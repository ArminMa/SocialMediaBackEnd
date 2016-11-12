package se.kth.awesome.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.pojos.ping.PingPojo;
import se.kth.awesome.service.ApplicationUserService;
import se.kth.awesome.util.MediaTypes;

import java.nio.charset.Charset;

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
	public ResponseEntity<String> findByEmail(String email) {
		UserEntity appUser = userRepository.findByEmail(email);

		if(appUser == null) ResponseEntity.status(HttpStatus.NOT_FOUND).build();



		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaTypes.JsonUtf8)
				.body(appUser.toString());
		
	}


}
