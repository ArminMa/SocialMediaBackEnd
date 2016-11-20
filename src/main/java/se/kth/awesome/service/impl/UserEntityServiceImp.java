package se.kth.awesome.service.impl;


import java.nio.charset.Charset;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.model.modelConverter.ModelConverter;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.service.UserEntityService;
import se.kth.awesome.util.MediaTypes;


@Service
public class UserEntityServiceImp implements UserEntityService {

	@Autowired
	private UserRepository userRepository;

	MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Override
	public UserPojo findByUsername(String userName) {
		UserEntity appUser = userRepository.findByUsername(userName);
		if(appUser == null){
			return null;
		}
		return ModelConverter.convert(appUser);
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
	public Collection<UserPojo> searchUsersResemblingByUsername(String name) {
		Collection<UserEntity> matchingUsers = userRepository.searchUsersResemblingByUsername(name);
		@SuppressWarnings("unchecked")
		Collection<UserPojo> users = (Collection<UserPojo>)ModelConverter.convert(matchingUsers);
		return users;
	}

}
