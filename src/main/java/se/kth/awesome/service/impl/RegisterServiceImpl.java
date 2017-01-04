package se.kth.awesome.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.user.UserRepository;
import se.kth.awesome.model.ModelConverter;

import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.model.role.UserRoleEntity;

import se.kth.awesome.model.role.UserRoleRepository;
import se.kth.awesome.security.AwesomeServerKeys;
import se.kth.awesome.security.util.PasswordSaltUtil;
import se.kth.awesome.service.RegisterService;
import se.kth.awesome.util.MediaTypes;
import se.kth.awesome.util.gsonX.GsonX;


@Service
public class RegisterServiceImpl implements RegisterService {

	public Logger logger2 = LoggerFactory.getLogger(getClass());
	private static final PasswordSaltUtil passwordSaltUtil = new PasswordSaltUtil();

	@Autowired private UserRepository userRepository;

	@Autowired private UserRoleRepository roleRepo;

	@Autowired private AwesomeServerKeys awesomeServerKeys;

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> registerNewUser(UserPojo userPojo)  {

		logger2.info("in register user");
		logger2.info("UserName = " + userPojo.getUsername() + ", password = "+ userPojo.getPassword() + ", email = " + userPojo.getEmail());
		if(!startRegistration(userPojo)) {
			logger2.info("failed start registration");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		logger2.info("before faceUserAvaiability");
        UserEntity faceUserAvailability = userRepository.findOneUserByEmailOrUsername(userPojo.getEmail(), userPojo.getUsername());
        if( faceUserAvailability!= null){
	        logger2.info("username taken");
            if(faceUserAvailability.getEmail().equals(userPojo.getEmail()) && faceUserAvailability.getUsername().equals(userPojo.getUsername()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{email: im_used, userName: im: im_used}");
            }
            else if(faceUserAvailability.getEmail().equals(userPojo.getEmail()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{ email: im_used }");
            }
            else if(  faceUserAvailability.getUsername().equals(userPojo.getUsername()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{ userName: im: im_used }");
            }

            else return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body(userPojo); // this should never be returned
        }

		logger2.info("username is ok");
        String unHachedPassword = userPojo.getPassword();

		String password = passwordSaltUtil.encodePassword(userPojo.getPassword(), awesomeServerKeys.getSharedSecretKey());
		userPojo.setPassword(password);
		UserEntity userEntity =  ModelConverter.convert(userPojo);
		logger2.info("converted userpojo into entity");
		userEntity = userRepository.save(userEntity );
		logger2.info("inserted user in database");
		userRepository.flush();

		//???? borde inte detta redan ligga inne i databasen??
		UserRoleEntity userRoleEntity = new UserRoleEntity( Role.MEMBER );
		logger2.info("add user role");
		userRoleEntity = roleRepo.save(userRoleEntity);
		roleRepo.flush();

		userEntity.getAuthorities().add(userRoleEntity);
		logger2.info("adds role to member");
		userEntity = userRepository.save(userEntity );
		userRepository.flush();

        if(userEntity == null){
	        logger2.info("something went very wrong. user not created");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
        else{

//	        private String username;
//	        private String email;
//	        private String password;

	        logger2.info("attempting to save in nodejs");
            userPojo = ModelConverter.convert(userEntity);
	        userPojo.setPassword(unHachedPassword);
	        userPojo.setAuthorities(null);
	        userPojo.setId(null);
	        userPojo.setToken(null);
	        String url =  "http://localhost:5500/register/user";
	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Content-Type", "application/json");

	        HttpEntity<UserPojo> entity = new HttpEntity<UserPojo>(userPojo, headers);
	        logger2.info("before post");
	        ResponseEntity<UserPojo> response = restTemplate.postForEntity(url, entity, UserPojo.class);
	        logger2.info("after post");
	        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
		        logger2.info("successfully added user to mongodb server");
		        UserPojo userPojo1 = (UserPojo) GsonX.gson.fromJson(response.getBody().toString(), UserPojo.class);
		        logger2.info("converted user into pojo");
		        return ResponseEntity.status(HttpStatus.CREATED)
				        .contentType(MediaType.APPLICATION_JSON_UTF8)
				        .body(userPojo);
	        }

        }

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}


	public Boolean startRegistration(UserPojo userPojo) {
		if( userPojo == null )
			return false ;
		else if (userPojo.getUsername() == null || userPojo.getEmail() == null)
			return false;

		UserEntity userEntity = this.userRepository.findOneUserByEmailOrUsername(
				userPojo.getUsername(), userPojo.getEmail());
		return userEntity == null;

	}



}
