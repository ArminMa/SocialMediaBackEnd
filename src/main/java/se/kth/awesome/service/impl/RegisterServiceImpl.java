package se.kth.awesome.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

	@Override
	public ResponseEntity<?> registerNewUser(UserPojo userPojo)  {


		if(!startRegistration(userPojo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}


        UserEntity faceUserAvailability = userRepository.findOneUserByEmailOrUsername(userPojo.getEmail(), userPojo.getUserName());

        if( faceUserAvailability!= null){
            if(faceUserAvailability.getEmail().equals(userPojo.getEmail()) && faceUserAvailability.getUserName().equals(userPojo.getUserName()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{email: im_used, userName: im: im_used}");
            }
            else if(faceUserAvailability.getEmail().equals(userPojo.getEmail()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{ email: im_used }");
            }
            else if(  faceUserAvailability.getUserName().equals(userPojo.getUserName()) ){
                return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body("{ userName: im: im_used }");
            }

            else return ResponseEntity.status(HttpStatus.IM_USED)
                        .contentType(MediaTypes.JsonUtf8)
                        .body(userPojo); // this should never be returned
        }


		// TODO salt the password with servers secret key or some other salt method then save it in database
		String password = null;



		UserEntity userEntity =  userRepository.save( ModelConverter.convert(userPojo) );
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
		else if (userPojo.getUserName() == null || userPojo.getEmail() == null)
			return false;

		UserEntity userEntity = this.userRepository.findOneUserByEmailOrUsername(
				userPojo.getUserName(), userPojo.getEmail());
		return userEntity == null;

	}



}
