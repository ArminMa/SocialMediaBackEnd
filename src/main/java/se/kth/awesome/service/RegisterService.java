package se.kth.awesome.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import se.kth.awesome.pojos.UserPojo;

public interface RegisterService {



    /**
     *
     * @param userPojo that is going to be registered
     * @param request it may be needed, probably not but maybe
     * @param response I do not know if we need this?
     * @return ResponseEntity<UserPojo>
     * @throws Exception if something went terribly wrong
     */
	ResponseEntity<?> registerNewUser(UserPojo userPojo, HttpServletRequest request, HttpServletResponse response) throws Exception;




}
