package se.kth.awesome.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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


		if(!startRegistration(userPojo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}


        UserEntity faceUserAvailability = userRepository.findOneUserByEmailOrUsername(userPojo.getEmail(), userPojo.getUsername());

        if( faceUserAvailability!= null){
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




		String password = passwordSaltUtil.encodePassword(userPojo.getPassword(), awesomeServerKeys.getSharedSecretKey());
		userPojo.setPassword(password);
		UserEntity userEntity =  ModelConverter.convert(userPojo);

		userEntity = userRepository.save(userEntity );
		userRepository.flush();

		UserRoleEntity userRoleEntity = new UserRoleEntity( Role.MEMBER );

		userRoleEntity = roleRepo.save(userRoleEntity);
		roleRepo.flush();

		userEntity.getAuthorities().add(userRoleEntity);

		userEntity = userRepository.save(userEntity );
		userRepository.flush();

        if(userEntity == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else{
            userPojo = ModelConverter.convert(userEntity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(userPojo);
        }

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
