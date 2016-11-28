package se.kth.awesome.service.impl;


import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.user.UserRepository;
import se.kth.awesome.model.mailMessage.MailMessage;
import se.kth.awesome.model.mailMessage.MailMessageRepository;
import se.kth.awesome.model.ModelConverter;
import se.kth.awesome.model.mailMessage.MailMessagePojo;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.model.post.Post;
import se.kth.awesome.model.post.PostPojo;
import se.kth.awesome.model.post.PostRepository;
import se.kth.awesome.service.UserEntityService;
import se.kth.awesome.util.MediaTypes;

import static se.kth.awesome.util.Util.nLin;


@Service
public class UserEntityServiceImp implements UserEntityService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MailMessageRepository mailMessageRepository;

//	private final Logger logger1 = LoggerFactory.getLogger(getClass());

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

	@Override
	public ResponseEntity<?> sendMailMessage(MailMessagePojo messagePojo) {
        if(messagePojo == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("messagePojo is null");
        if(messagePojo.getPk() == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("messagePojo.getPk() is null");
        if(messagePojo.getSender() == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("messagePojo.getSender() is null");
        if(messagePojo.getReceiver() == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("messagePojo.getReceiver() is null");
        if(messagePojo.getTopic() == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("messagePojo.getTopic() is null");

		System.out.println(nLin+"1.UserEntityServiceImp.sendMailMessage");
        MailMessage mailMessage = ModelConverter.convert(messagePojo);
		System.out.println(nLin+"2.UserEntityServiceImp.sendMailMessage");
        mailMessageRepository.save(mailMessage);
        mailMessageRepository.flush();
        return ResponseEntity.status(HttpStatus.CREATED).build();
	}

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<?> getMyMails(String username) {
        if(username == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Collection<MailMessage> mailMessages = mailMessageRepository.getAllSentAndReceivedMailByUserName(username);

        if(mailMessages == null ||  mailMessages.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        System.out.println(nLin+"3.UserEntityServiceImp.getMyMails");
		Collection<MailMessagePojo> mailMessagePojos = (Collection<MailMessagePojo>) ModelConverter.convert(mailMessages);
        System.out.println(nLin+"4.UserEntityServiceImp.getMyMails");
        if(mailMessagePojos == null ||  mailMessagePojos.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        System.out.println(nLin+"5.UserEntityServiceImp.getMyMails");

		Collection<MailMessagePojo> messagePojos = (Collection<MailMessagePojo>) ModelConverter.convert(mailMessages);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaTypes.JsonUtf8)
                .body(messagePojos);
    }

	@Override
	public ResponseEntity<?> getPosts(String username) {
		if(username == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		Collection<Post> posts = postRepository.getAllReceivedPostsByUserName(username);
		if(posts == null ||  posts.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		Collection<PostPojo> postPojos = (Collection<PostPojo>) ModelConverter.convert(posts);
		if(postPojos == null ||  postPojos.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaTypes.JsonUtf8)
				.body(postPojos);

	}

	@Override
	public ResponseEntity<?> senPostMessage(PostPojo postPojo) {
		if(postPojo == null) {
//			logger1.error("postpojo = null");
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		if(postPojo.getPk() == null) {
//			logger1.error("pk = null");
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		if(postPojo.getSender() == null) {
//			logger1.error("sender = null");
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		if(postPojo.getReceiver() == null){
			postPojo.setReceiver(postPojo.getSender());
		}
		postPojo.setPostedDate(new Date());

		Post post = ModelConverter.convert(postPojo);

		postRepository.save(post);
		postRepository.flush();
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Override
	public ResponseEntity<?> deletePost(PostPojo post) {
		if(post == null) {
//			logger1.error("postpojo = null");
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		if(post.getPk() == null) {
//			logger1.error("pk = null");
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		if(post.getSender() == null) {
//			logger1.error("sender = null");
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		if(post.getReceiver() == null){
			post.setReceiver(post.getSender());
		}
		postRepository.deletePostByID(post.getId());

		Post deletePost = postRepository.getPost(post.getId());
		if(deletePost != null){
			return ResponseEntity.status(HttpStatus.OK).build();

		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}


}
