package se.kth.awesome.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SortNatural;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import se.kth.awesome.model.security.UserRole;
import se.kth.awesome.util.GsonX;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

//import org.kth.HI1034.model.validators.ExtendedEmailValidator;
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "face_user", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "user_name")})
public class UserEntity implements Serializable{

	private Long id;
	private String userName;
	private String email;
	private String password;
    private byte[] picture; // Todo implement this Armin. low prio


	public UserEntity() {
	}

	/**
	 * the param order
	 *
	 * @param email       1
	 * @param username    2
	 * @param password    3
	 *
	 */
	public UserEntity(String email, String username, String password) {
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
	@Length(max = 255)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

//	@Lob
//	public byte[] getPicture() {
//		return picture;
//	}
//
//	public void setPicture(byte[] picture) {
//		this.picture = picture;
//	}

	/*
	 * User Mapping starts Here
	 */
//---------------------------- authority ------------------------

//	CascadeType.DETACH means that if i disappear if the rows where i i'm in "in between this and other table"
// should be remove. we don't want that

//	CascadeType.REMOVE wants to remove the other side Entity if this is removed, that is not good hear.

	private SortedSet<FriendRequest> friendRequests = new TreeSet<>();
	@ManyToMany(/*cascade = {CascadeType.PERSIST, CascadeType.MERGE},*/ fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
    public SortedSet<FriendRequest> getFriendRequests() {
        return friendRequests;
    }
    public void setFriendRequests(SortedSet<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

	// TODO change column name from app_user_id to Role_id
	private SortedSet<UserRole> roles = new TreeSet<>();
	@OneToMany(/*cascade = {CascadeType.PERSIST, CascadeType.MERGE},*/ fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
	@JoinColumn(name="app_user_id", referencedColumnName="id")
	public SortedSet<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(SortedSet<UserRole> roles) {
		this.roles = roles;
	}

//	private SortedSet<ChatMessage> chatMessages = new TreeSet<>();
//
//	@ManyToMany(
//			targetEntity=ChatMessage.class,
//			/*cascade={CascadeType.PERSIST, CascadeType.MERGE},*/
//					fetch = FetchType.EAGER
//	)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public SortedSet<ChatMessage> getChatMessages() {
//        return chatMessages;
//    }
//    public void setChatMessages(SortedSet<ChatMessage> receivedChatMessages) {
//        this.chatMessages = receivedChatMessages;
//    }
//
//	private SortedSet<MailMessage> mailMessages = new TreeSet<>();
//	@OneToMany(fetch = FetchType.EAGER)
//	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@SortNatural
//	public SortedSet<MailMessage> getMailMessages() {
//		return mailMessages;
//	}
//	public void setMailMessages(SortedSet<MailMessage> mailMessages) {
//		this.mailMessages = mailMessages;
//	}
//
//	private SortedSet<UserEntity> friends = new TreeSet<>();
//	@OneToOne( fetch = FetchType.LAZY)
////	@BatchSize(size=25)
////	@LazyCollection(LazyCollectionOption.FALSE)
//	@SortNatural
//	public SortedSet<UserEntity> getFriends() {
//		return friends;
//	}
//	public void setFriends(SortedSet<UserEntity> friends) {
//		this.friends = friends;
//	}
//
//	private SortedSet<Post> log = new TreeSet<>();
//	@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@SortNatural
//	public SortedSet<Post> getLog() {
//		return log;
//	}
//	public void setLog(SortedSet<Post> log) {
//		this.log = log;
//	}
//
//	private SortedSet<ChatMessage> receivedChatMessages = new TreeSet<>();
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public SortedSet<ChatMessage> getReceivedChatMessages() {
//        return receivedChatMessages;
//    }
//    public void setReceivedChatMessages(SortedSet<ChatMessage> receivedChatMessages) {
//        this.receivedChatMessages = receivedChatMessages;
//    }
//
//    private SortedSet<ChatMessage> sendentChatMessages = new TreeSet<>();
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public SortedSet<ChatMessage> getSendentChatMessages() {
//        return sendentChatMessages;
//    }
//
//    public void setSendentChatMessages(SortedSet<ChatMessage> sendentChatMessages) {
//        this.sendentChatMessages = sendentChatMessages;
//    }

	@Override
	public String toString() {
		if(this.friendRequests != null && this.friendRequests.isEmpty()){
			friendRequests = null;
		}
		if(this.roles != null && this.roles.isEmpty()){
			this.roles = null;
		}

//		if(this.mailMessages != null && this.mailMessages.isEmpty()){
//			this.mailMessages = null;
//		}
//		if(this.friends != null && this.friends.isEmpty()){
//			this.friends = null;
//		}
//		if(this.log != null && this.log.isEmpty()){
//			this.log = null;
//		}
//		if(this.receivedChatMessages != null && this.receivedChatMessages.isEmpty()){
//			this.receivedChatMessages = null;
//		}

		String thisJsonString = GsonX.gson.toJson(this);

		if(this.friendRequests == null){
			this.friendRequests = new TreeSet<>();
		}

		if(this.roles == null){
			this.roles = new TreeSet<>();
		}
//		if(this.mailMessages == null){
//			this.mailMessages = new TreeSet<>();
//		}
//		if(this.friends == null){
//			this.friends = new TreeSet<>();
//		}
//		if(this.log == null){
//			this.log = new TreeSet<>();
//		}
//		if(this.receivedChatMessages == null){
//			this.receivedChatMessages = new TreeSet<>();
//		}

		return thisJsonString;
	}

}