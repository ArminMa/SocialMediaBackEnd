package se.kth.awesome.service;

import java.util.Collection;
import org.springframework.http.ResponseEntity;
import se.kth.awesome.pojos.MailMessagePojo.MailMessagePojo;
import se.kth.awesome.pojos.UserPojo;


public interface UserEntityService {


    UserPojo findByUsername(String userName);

    ResponseEntity<?> findByUserID(Long idValue);

    ResponseEntity<?> findByEmail(String email);

    Collection<UserPojo> searchUsersResemblingByUsername(String name);

    ResponseEntity<?> sendMailMessage(MailMessagePojo messagePojo);

    ResponseEntity<?> getMyMails(String username);
}
