package se.kth.awsome.model;


import javassist.bytecode.ByteArray;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SortNatural;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

//import org.kth.HI1034.model.validators.ExtendedEmailValidator;

@Entity
@Table(name = "face_user", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "username")})
public class ApplicationUser implements Serializable{

	private Long id;
	private String userName;
	private String email;
	private String password;
    private byte[] picture; // Todo implement this Armin. low prio


	public ApplicationUser() {
	}

	/**
	 * the param order
	 *
	 * @param email       1
	 * @param username    2
	 * @param password    3
	 *
	 */
	public ApplicationUser(String email, String username, String password) {
		this.email = email;
		this.userName = username;
		this.password = password;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", insertable = false, updatable = false, unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

    @Column(name = "user_name")
//	@NotEmpty
    @Length(max = 100)
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

	//	@Basic
//	@ExtendedEmailValidator
	@NotEmpty
	@Length(max = 240)
	@Column(unique = true, nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Column
	@NotEmpty
	@Length(min = 255)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	/*
	 * User Mapping starts Here
	 */
//---------------------------- authority ------------------------

//	CascadeType.DETACH means that if i disappear if the rows where i i'm in "in between this and other table"
// should be remove. we don't want that

//	CascadeType.REMOVE wants to remove the other side Entity if this is removed, that is not good hear.

	private SortedSet<FriendRequest> friendRequests = new TreeSet<>();
    @OneToOne( orphanRemoval = true, fetch = FetchType.EAGER)
//	@BatchSize(size=100)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
    public SortedSet<FriendRequest> getFriendRequests() {
        return friendRequests;
    }
    public void setFriendRequests(SortedSet<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

	private SortedSet<MailMessage> mailMessages = new TreeSet<>();
	@OneToMany(fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
	public SortedSet<MailMessage> getMailMessages() {
		return mailMessages;
	}
	public void setMailMessages(SortedSet<MailMessage> mailMessages) {
		this.mailMessages = mailMessages;
	}

	private SortedSet<ApplicationUser> friends = new TreeSet<>();
	@OneToOne( fetch = FetchType.LAZY)
//	@BatchSize(size=25)
//	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
	public SortedSet<ApplicationUser> getFriends() {
		return friends;
	}
	public void setFriends(SortedSet<ApplicationUser> friends) {
		this.friends = friends;
	}

	private SortedSet<FriendRequest> sentFriendRequests = new TreeSet<>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@SortNatural
	public SortedSet<FriendRequest> getSentFriendRequests() {
		return sentFriendRequests;
	}
	public void setSentFriendRequests(SortedSet<FriendRequest> sentFriendRequests) {
		this.sentFriendRequests = sentFriendRequests;
	}

	private SortedSet<FriendRequest> receivedFriendRequests = new TreeSet<>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
	public SortedSet<FriendRequest> getReceivedFriendRequests() {
		return receivedFriendRequests;
	}
	public void setReceivedFriendRequests(SortedSet<FriendRequest> receivedFriendRequests) {
		this.receivedFriendRequests = receivedFriendRequests;
	}

	private SortedSet<Post> log = new TreeSet<>();
	@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
	public SortedSet<Post> getLog() {
		return log;
	}
	public void setLog(SortedSet<Post> log) {
		this.log = log;
	}

	private SortedSet<ChatMessage> chatMessages = new TreeSet<>();
	@ManyToMany(fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
	public SortedSet<ChatMessage> getChatMessages() {
		return chatMessages;
	}
	public void setChatMessages( SortedSet<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}

}