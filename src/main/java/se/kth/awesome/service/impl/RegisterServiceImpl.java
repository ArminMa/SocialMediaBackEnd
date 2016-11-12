package se.kth.awesome.service.impl;


import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.UserRepository;
import se.kth.awesome.modelConverter.ModelConverter;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.service.RegisterService;
import se.kth.awesome.util.MediaTypes;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private UserRepository userRepository;

//	@Value("${rsa.key.public}")
//	public String serverPublicKey;


	@Override
	public ResponseEntity<?> registerNewUser(UserPojo userPojo, HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException {


		Assert.notNull(userPojo, "RegisterServiceImpl.registerNewUser the userPojo is null");


        UserEntity userAvailability = userRepository.findOneByUserNameOrEmail(userPojo.getEmail(), userPojo.getUserName());

        if( userAvailability!= null){
            if(userAvailability.getEmail().equals(userPojo.getEmail()) && userAvailability.getUserName().equals(userPojo.getUserName()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{email: im_used, username: im: im_used}");
            }
            else if(userAvailability.getEmail().equals(userPojo.getEmail()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{ email: im_used }");
            }
            else if(  userAvailability.getUserName().equals(userPojo.getUserName()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{ username: im: im_used }");
            }

            else return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body(userPojo); // this should never be returned
        }

		// TODO the password should be salted before saving it in the Data Base
		UserEntity userEntity =  userRepository.save( ModelConverter.convert(userPojo) );
		userRepository.flush();

		if(userEntity == null){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		else{
			userPojo = ModelConverter.convert(userEntity);
			return ResponseEntity.status(HttpStatus.CREATED)
					.contentType(MediaTypes.JsonUtf8)
					.body(userPojo);
		}



	}


}
