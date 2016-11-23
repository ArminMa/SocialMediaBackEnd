package se.kth.awesome.service;

import java.util.Collection;
import org.springframework.http.ResponseEntity;
import se.kth.awesome.model.mailMessage.MailMessagePojo;
import se.kth.awesome.model.User.UserPojo;


public interface UserEntityService {


    UserPojo findByUsername(String userName);

    ResponseEntity<?> findByUserID(Long idValue);

    ResponseEntity<?> findByEmail(String email);

    Collection<UserPojo> searchUsersResemblingByUsername(String name);

    ResponseEntity<?> sendMailMessage(MailMessagePojo messagePojo);

    ResponseEntity<?> getMyMails(String username);
}
