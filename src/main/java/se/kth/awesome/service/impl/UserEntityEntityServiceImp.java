package se.kth.awesome.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.service.UserEntityService;
import se.kth.awesome.util.MediaTypes;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserEntityEntityServiceImp implements UserEntityService {

	@Autowired
	private UserRepository userRepository;

	MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Override
	public UserPojo findByUsername(String userName) {
		UserEntity appUser = userRepository.findByUsername(userName);
		if(appUser == null){
			return null;
		}
		System.out.println("---------------------------------------appUserBeforeConvert--------------------------------------------------------");
		System.out.println(appUser.toString());
		UserPojo userPojo = userEntityToUserPojo(appUser);

		return userPojo;
	}

	@Override
	public ResponseEntity<?> findByUserID(Long idValue) {
		return null;
	}

	@Override
	public ResponseEntity<?> findByEmail(String email) {
		UserEntity appUser = userRepository.findByEmail(email);
		if(appUser == null) ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaTypes.JsonUtf8)
				.body(appUser);
	}

	@Override
	public Collection<UserPojo> searchUsersByName(String name) {
		Collection<UserEntity> matchingUsers = userRepository.searchUsersByName(name);
		Collection<UserPojo> users = userEntitiesToUserPojos(matchingUsers);
		return users;
	}

	private Collection<UserPojo>  userEntitiesToUserPojos(Collection<UserEntity> users){
		Collection<UserPojo> userPojos = new ArrayList<>();
		for (UserEntity user : users) {
			userPojos.add(userEntityToUserPojo(user));
		}
		return userPojos;
	}

	private UserPojo userEntityToUserPojo(UserEntity appUser) {
		UserPojo userPojo = new UserPojo();
		userPojo.setPassword(appUser.getPassword());
		userPojo.setEmail(appUser.getEmail());
		userPojo.setUserName(appUser.getUserName());
		userPojo.setId(appUser.getId());
		return userPojo;
	}
}
